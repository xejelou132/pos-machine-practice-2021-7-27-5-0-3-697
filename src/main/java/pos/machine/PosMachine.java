package pos.machine;

import java.util.List;

import static pos.machine.ItemDataLoader.loadAllItemInfos;

public class PosMachine {
    public final char space = '\n';
    public int finalTotal = 0;
    public String itemDetail = "";

    public String printReceipt(List<String> barcodes) {
        List<ItemInfo> itemInfos = loadAllItemInfos();
        calculateReceipt(barcodes, itemInfos);
        return generateReceipt(itemDetail, finalTotal);
    }

    private void calculateReceipt(List<String> barcodes, List<ItemInfo> itemInfos) {
        int barcodeCounter = 0;
        int receiptCounter = 0;
        ItemInfo item = null;
        int total = 0;

        for (String barcode : barcodes) {

            if (isfDifferentBarcode(item, barcode) || isSingleEqualBarcode(item, barcode, barcodes, receiptCounter)) {

                int quantity = barcodeCounter;
                total = getTotal(quantity, item.getPrice());
                barcodeCounter = 0;

                if(isLastBarcode(receiptCounter, barcodes.size())){
                    quantity++;
                    total = getTotal(quantity, item.getPrice());
                }

                itemDetail += getItemDetails(item, quantity, total);
            }

            item = convertToItems(itemInfos, barcode);

            barcodeCounter++;
            receiptCounter++;

            finalTotal += total;
            total = 0;
        }
    }


    private String generateReceipt(String receipt, int finaltotal) {
        return "***<store earning no money>Receipt***" + space + receipt + "----------------------" + space +
                "Total: " + finaltotal + " (yuan)" + space + "**********************";
    }

    private ItemInfo convertToItems(List<ItemInfo> itemInfos, String barcode) {
        return itemInfos.stream()
                .filter(itemInfo -> itemInfo.getBarcode().equals(barcode))
                .findFirst()
                .get();
    }

    private String getItemDetails(ItemInfo item, int quantity, int total) {
        return "Name: " + item.getName() + "," + " Quantity: " + quantity + ", Unit price: " + item.getPrice() + " (yuan), Subtotal: " + total + " (yuan)" + space;
    }

    private boolean isLastBarcode(int receiptCounter, int size) {
        return receiptCounter+1 == size;
    }

    private int getTotal(int quantity, int price) {
        return quantity*price;
    }

    private boolean isSingleEqualBarcode(ItemInfo item, String barcode, List<String> barcodes, int receiptCounter) {
        return item != null && barcode.equals(barcodes.get(barcodes.size()-1)) && receiptCounter+1 == barcodes.size();
    }

    private boolean isfDifferentBarcode(ItemInfo item, String barcode) {
        return item != null && !barcode.equals(item.getBarcode());
    }

}
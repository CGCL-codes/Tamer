package com.vlee.util;

import java.math.BigDecimal;
import java.util.ArrayList;

public class InvoicePrintableLine {

    private int number = 0;

    private String description = "";

    private String serial = "";

    private String remarks = "";

    private String quantity = "";

    private String unitPrice = "";

    private String amount = "";

    private String eanCode = "";

    private String itemCode = "";

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumber() {
        return this.number;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public String getDescription() {
        return this.description;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getSerial() {
        return this.serial;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public void setEanCode(String ean) {
        this.eanCode = ean;
    }

    public String getEanCode() {
        return this.eanCode;
    }

    public void setItemCode(String item) {
        this.itemCode = item;
    }

    public String getItemCode() {
        return this.itemCode;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getQuantity() {
        return this.quantity;
    }

    public void setUnitPrice(String price) {
        this.unitPrice = price;
    }

    public String getUnitPrice() {
        return this.unitPrice;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmount() {
        return this.amount;
    }

    public static ArrayList splitStringByWord(String longString, int maxLength) {
        ArrayList split = new ArrayList();
        int from = 0;
        int to = 0;
        longString = longString.trim();
        if (longString.length() - 1 < maxLength) {
            split.add(longString);
        } else {
            to = maxLength;
            while (to <= longString.length() - 1) {
                if (longString.substring(from, to).charAt(0) == ' ') {
                    String current = longString.substring(from, to);
                    boolean stop = false;
                    for (int si = 0; si < current.length(); si++) {
                        if (current.charAt(si) == ' ') {
                            from++;
                            to++;
                        } else {
                            stop = true;
                        }
                        if (stop) break;
                    }
                }
                if (longString.substring(from, to).charAt(to - from - 1) != ' ') {
                    int lastSpace = longString.substring(from, to).lastIndexOf(' ');
                    if (lastSpace + from < to + maxLength && lastSpace != -1) {
                        to = longString.substring(from, to).lastIndexOf(' ') + from;
                    }
                }
                split.add(longString.substring(from, to));
                if (to + maxLength < longString.length() - 1) {
                    from = to;
                    to += maxLength;
                } else if (to + maxLength >= longString.length() - 1) {
                    from = to;
                    to = longString.length();
                    if (longString.substring(from, to).length() > 0 && longString.substring(from, to).charAt(0) == ' ') {
                        System.out.println("before current");
                        String current = longString.substring(from, to);
                        boolean stop = false;
                        for (int si = 0; si < current.length(); si++) {
                            System.out.println("after current in loop");
                            if (current.charAt(si) == ' ') {
                                System.out.println("inside loop!!!");
                                from++;
                            } else {
                                System.out.println("stopping now :D");
                                stop = true;
                            }
                            if (stop) break;
                        }
                    }
                    split.add(longString.substring(from, to));
                    to = longString.length() + 1;
                }
            }
        }
        return split;
    }

    public static ArrayList splitStringByMaxLength(String longString, int maxLength) {
        ArrayList split = new ArrayList();
        int from = 0;
        int to = 0;
        longString = longString.trim();
        Log.printDebug("String: " + longString + " EOS");
        if (longString.length() < maxLength) {
            split.add(longString);
        } else {
            to = maxLength;
            while (to <= longString.length() - 1) {
                split.add(longString.substring(from, to));
                if (to + maxLength < longString.length() - 1) {
                    from = to;
                    to += maxLength;
                } else if (to + maxLength >= longString.length() - 1) {
                    from = to;
                    to = longString.length() - 1;
                    split.add(longString.substring(from, to));
                    to = longString.length() + 1;
                }
            }
        }
        return split;
    }
}

package com.ravana.invoice;

import com.ravana.pos.DocumentWrapper;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 *
 * @author Manjuka Soysa
 */
public class InvoiceDocumentWrapper implements DocumentWrapper {

    private Invoice order;

    private List<InvoiceItem> items;

    public InvoiceDocumentWrapper(Invoice o) {
        order = o;
        items = o.getItems();
    }

    public String getTitle() {
        if (order.getReferenceDocType() == InvoiceDocType.PO) return "PURCHASE INVOICE";
        if (order.getReferenceDocType() == InvoiceDocType.PT) return "TRANSFER IN INVOICE";
        if (order.getReferenceDocType() == InvoiceDocType.ST) return "TRANSFER OUT INVOICE";
        return "SALES INVIOCE";
    }

    public String getFrom() {
        if (order.getReferenceDocType() == InvoiceDocType.PO) return "FROM SUPPLIER : " + order.getExternalPartyName();
        if (order.getReferenceDocType() == InvoiceDocType.PT) return "FROM BRANCH : " + order.getExternalPartyName();
        if (order.getReferenceDocType() == InvoiceDocType.ST) return "TO BRANCH : " + order.getExternalPartyName();
        return "TO : " + order.getExternalPartyName();
    }

    public String getTo() {
        return "";
    }

    public String getDate() {
        return "DATE: " + SimpleDateFormat.getDateInstance().format(order.getCreationTime());
    }

    public String getReference() {
        return "REF#: " + order.getInvoiceId();
    }

    public String getDescription(int line) {
        InvoiceItem oi = items.get(line - 1);
        return oi.getDescription();
    }

    public String getQuantity(int line) {
        InvoiceItem oi = items.get(line - 1);
        return "" + oi.getQuantity();
    }

    public String getUnitPrice(int line) {
        InvoiceItem oi = items.get(line - 1);
        return DecimalFormat.getCurrencyInstance().format(oi.getUnitPrice());
    }

    public String getSubTotal(int line) {
        InvoiceItem oi = items.get(line - 1);
        return DecimalFormat.getCurrencyInstance().format(oi.getSubTotal());
    }

    public String getGst(int line) {
        InvoiceItem oi = items.get(line - 1);
        return DecimalFormat.getCurrencyInstance().format(oi.getSubTotalTax());
    }

    public String getTotal() {
        return DecimalFormat.getCurrencyInstance().format(order.getTotalValue());
    }

    public String getTotalGst() {
        return DecimalFormat.getCurrencyInstance().format(order.getTotalTax());
    }
}

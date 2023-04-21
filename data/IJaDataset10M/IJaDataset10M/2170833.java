package org.noranj.formak.shared.dto;

import java.io.Serializable;

/**
 * 
 * 
 * This module, both source code and documentation, is in the Public Domain, and comes with NO WARRANTY.
 * See http://www.noranj.org for further information.
 *
 * @author
 */
public class PurchaseOrderItemDTO implements Serializable {

    /**
   * 
   */
    private static final long serialVersionUID = -2658109658962923203L;

    private int sequenceHolder;

    /** this is the identifier (or PK) of the item in this system. this is not displayed on the screen. */
    private String id;

    private long itemID;

    private String gtin;

    private String buyerItemID;

    private String description;

    private String qtyOrdered;

    private String uom;

    /** Unit price for the item.
   * all monetary are stored as long with 3 digits decimal point. */
    private long price;

    private int quantity;

    public int getSequenceHolder() {
        return sequenceHolder;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQtyOrdered() {
        return qtyOrdered;
    }

    public void setQtyOrdered(String qtyOrdered) {
        this.qtyOrdered = qtyOrdered;
    }

    public String getUom() {
        return uom;
    }

    public void setUOM(String uom) {
        this.uom = uom;
    }

    public String getBuyerItemID() {
        return buyerItemID;
    }

    public void setBuyerItemID(String buyerItemID) {
        this.buyerItemID = buyerItemID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGTIN() {
        return gtin;
    }

    public void setGTIN(String gtin) {
        this.gtin = gtin;
    }

    public void setSequenceHolder(int sequenceHolder) {
        this.sequenceHolder = sequenceHolder;
    }

    public long getItemID() {
        return itemID;
    }

    public void setItemID(long itemID) {
        this.itemID = itemID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public static PurchaseOrderItemDTO addNewRecord() {
        PurchaseOrderItemDTO purchaseOrderItemDTO = new PurchaseOrderItemDTO();
        purchaseOrderItemDTO.setBuyerItemID("");
        purchaseOrderItemDTO.setDescription("");
        purchaseOrderItemDTO.setGTIN("");
        purchaseOrderItemDTO.setItemID(0);
        purchaseOrderItemDTO.setPrice(0);
        purchaseOrderItemDTO.setQtyOrdered("");
        purchaseOrderItemDTO.setQuantity(0);
        purchaseOrderItemDTO.setSequenceHolder(0);
        purchaseOrderItemDTO.setUOM("");
        purchaseOrderItemDTO.setId("0");
        return purchaseOrderItemDTO;
    }

    public String toString() {
        StringBuilder strb = new StringBuilder();
        strb.append("ID[");
        strb.append(id);
        strb.append("] sequence[");
        strb.append(sequenceHolder);
        strb.append("] GITN[");
        strb.append(gtin);
        strb.append("] itemID[");
        strb.append(itemID);
        strb.append("] buyerItemID[");
        strb.append(buyerItemID);
        strb.append("] description[");
        strb.append(description);
        strb.append("] uom[");
        strb.append(uom);
        strb.append("] price[");
        strb.append(price);
        strb.append("] qty[");
        strb.append(quantity);
        strb.append("]");
        return strb.toString();
    }
}

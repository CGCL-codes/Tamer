package com.debitors.vo;

import java.math.BigDecimal;

public class CustomerItemDiscountVO {

    private long id;

    private long contactID;

    private long itemID;

    private double discountPercent;

    private java.math.BigDecimal discountAmount;

    private java.sql.Timestamp modified;

    private java.sql.Date validFrom;

    private java.sql.Date validTo;

    public void setId(long id) {
        this.id = id;
    }

    public void setContactID(long contactID) {
        this.contactID = contactID;
    }

    public void setItemID(long itemID) {
        this.itemID = itemID;
    }

    public void setDiscountPercent(double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public void setDiscountAmount(java.math.BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public void setModified(java.sql.Timestamp modified) {
        this.modified = modified;
    }

    public void setValidFrom(java.sql.Date validFrom) {
        this.validFrom = validFrom;
    }

    public void setValidTo(java.sql.Date validTo) {
        this.validTo = validTo;
    }

    public long getId() {
        return id;
    }

    public long getContactID() {
        return contactID;
    }

    public long getItemID() {
        return itemID;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public java.math.BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public java.sql.Timestamp getModified() {
        return modified;
    }

    public java.sql.Date getValidFrom() {
        return validFrom;
    }

    public java.sql.Date getValidTo() {
        return validTo;
    }

    public String toString() {
        return id + ";" + contactID + ";" + itemID + ";" + discountPercent + ";" + discountAmount + ";" + modified + ";" + validFrom + ";" + validTo;
    }

    public void init() {
        id = 0;
        contactID = 0;
        itemID = 0;
        discountPercent = 0.0;
        discountAmount = new BigDecimal(0.0);
        validFrom = new java.sql.Date(System.currentTimeMillis());
        validTo = new java.sql.Date(System.currentTimeMillis());
    }
}

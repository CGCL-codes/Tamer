package com.eshop.vo;

public class ItemViewVO {

    private long id;

    private String codeAn;

    private long itemNumber;

    private long supplierID;

    private String name;

    private long typeNum;

    private String type;

    private long guarantee;

    private String availability;

    private long count;

    private String description;

    private String remarks;

    private java.math.BigDecimal price;

    private java.math.BigDecimal discount;

    private double discountPercent;

    private long vatID;

    private String pictureUrl;

    private java.sql.Timestamp modified;

    private java.sql.Date validFrom;

    private java.sql.Date validTo;

    public void setId(long id) {
        this.id = id;
    }

    public void setCodeAn(String codeAn) {
        this.codeAn = codeAn;
    }

    public void setItemNumber(long itemNumber) {
        this.itemNumber = itemNumber;
    }

    public void setSupplierID(long supplierID) {
        this.supplierID = supplierID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTypeNum(long typeNum) {
        this.typeNum = typeNum;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setGuarantee(long guarantee) {
        this.guarantee = guarantee;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void setPrice(java.math.BigDecimal price) {
        this.price = price;
    }

    public void setDiscount(java.math.BigDecimal discount) {
        this.discount = discount;
    }

    public void setDiscountPercent(double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public void setVatID(long vatID) {
        this.vatID = vatID;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
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

    public String getCodeAn() {
        return codeAn;
    }

    public long getItemNumber() {
        return itemNumber;
    }

    public long getSupplierID() {
        return supplierID;
    }

    public String getName() {
        return name;
    }

    public long getTypeNum() {
        return typeNum;
    }

    public String getType() {
        return type;
    }

    public long getGuarantee() {
        return guarantee;
    }

    public String getAvailability() {
        return availability;
    }

    public long getCount() {
        return count;
    }

    public String getDescription() {
        return description;
    }

    public String getRemarks() {
        return remarks;
    }

    public java.math.BigDecimal getPrice() {
        return price;
    }

    public java.math.BigDecimal getDiscount() {
        return discount;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public long getVatID() {
        return vatID;
    }

    public String getPictureUrl() {
        return pictureUrl;
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
        return id + ";" + codeAn + ";" + itemNumber + ";" + supplierID + ";" + name + ";" + typeNum + ";" + type + ";" + guarantee + ";" + availability + ";" + count + ";" + description + ";" + remarks + ";" + price + ";" + discount + ";" + discountPercent + ";" + vatID + ";" + pictureUrl + ";" + modified + ";" + validFrom + ";" + validTo;
    }

    public void init() {
        id = 0;
        codeAn = "";
        itemNumber = 0;
        supplierID = 0;
        name = "";
        typeNum = 0;
        type = "";
        guarantee = 0;
        availability = "";
        count = 0;
        description = "";
        remarks = "";
        discountPercent = 0.0;
        vatID = 0;
        pictureUrl = "";
        validFrom = new java.sql.Date(System.currentTimeMillis());
        validTo = new java.sql.Date(System.currentTimeMillis());
    }
}

package com.debitors.vo;

public class PFT4VO {

    private long id;

    private String type;

    private String ta;

    private String origin;

    private String deliveryKind;

    private String customerNr;

    private String referenceNr;

    private String currencyCode;

    private java.math.BigDecimal amount;

    private String referenceFi;

    private java.sql.Date orderDate;

    private java.sql.Date processingDate;

    private java.sql.Date creditDate;

    private String rejectCode;

    private String currencyCode2;

    private java.math.BigDecimal cost;

    public void setId(long id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTa(String ta) {
        this.ta = ta;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setDeliveryKind(String deliveryKind) {
        this.deliveryKind = deliveryKind;
    }

    public void setCustomerNr(String customerNr) {
        this.customerNr = customerNr;
    }

    public void setReferenceNr(String referenceNr) {
        this.referenceNr = referenceNr;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public void setAmount(java.math.BigDecimal amount) {
        this.amount = amount;
    }

    public void setReferenceFi(String referenceFi) {
        this.referenceFi = referenceFi;
    }

    public void setOrderDate(java.sql.Date orderDate) {
        this.orderDate = orderDate;
    }

    public void setProcessingDate(java.sql.Date processingDate) {
        this.processingDate = processingDate;
    }

    public void setCreditDate(java.sql.Date creditDate) {
        this.creditDate = creditDate;
    }

    public void setRejectCode(String rejectCode) {
        this.rejectCode = rejectCode;
    }

    public void setCurrencyCode2(String currencyCode2) {
        this.currencyCode2 = currencyCode2;
    }

    public void setCost(java.math.BigDecimal cost) {
        this.cost = cost;
    }

    public long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getTa() {
        return ta;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDeliveryKind() {
        return deliveryKind;
    }

    public String getCustomerNr() {
        return customerNr;
    }

    public String getReferenceNr() {
        return referenceNr;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public java.math.BigDecimal getAmount() {
        return amount;
    }

    public String getReferenceFi() {
        return referenceFi;
    }

    public java.sql.Date getOrderDate() {
        return orderDate;
    }

    public java.sql.Date getProcessingDate() {
        return processingDate;
    }

    public java.sql.Date getCreditDate() {
        return creditDate;
    }

    public String getRejectCode() {
        return rejectCode;
    }

    public String getCurrencyCode2() {
        return currencyCode2;
    }

    public java.math.BigDecimal getCost() {
        return cost;
    }

    public String toString() {
        return id + ";" + type + ";" + ta + ";" + origin + ";" + deliveryKind + ";" + customerNr + ";" + referenceNr + ";" + currencyCode + ";" + amount + ";" + referenceFi + ";" + orderDate + ";" + processingDate + ";" + creditDate + ";" + rejectCode + ";" + currencyCode2 + ";" + cost;
    }

    public void init() {
        id = 0;
        type = "";
        ta = "";
        origin = "";
        deliveryKind = "";
        customerNr = "";
        referenceNr = "";
        currencyCode = "";
        referenceFi = "";
        orderDate = new java.sql.Date(System.currentTimeMillis());
        processingDate = new java.sql.Date(System.currentTimeMillis());
        creditDate = new java.sql.Date(System.currentTimeMillis());
        rejectCode = "";
        currencyCode2 = "";
    }
}

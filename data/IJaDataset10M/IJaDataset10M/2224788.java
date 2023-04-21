package com.creditors.vo;

public class PF32VO {

    private long id;

    private long type;

    private long principalID;

    private short currencyBookID;

    private java.math.BigDecimal amount;

    private short currencyPayID;

    private long countryID;

    private java.sql.Date creationDate;

    private java.sql.Date dueDate;

    private long postAccountID;

    private long recipientID;

    private long beneficiaryID;

    private String message4x35;

    private String taxCode;

    private String flagUrgent;

    private long ordererID;

    public void setId(long id) {
        this.id = id;
    }

    public void setType(long type) {
        this.type = type;
    }

    public void setPrincipalID(long principalID) {
        this.principalID = principalID;
    }

    public void setCurrencyBookID(short currencyBookID) {
        this.currencyBookID = currencyBookID;
    }

    public void setAmount(java.math.BigDecimal amount) {
        this.amount = amount;
    }

    public void setCurrencyPayID(short currencyPayID) {
        this.currencyPayID = currencyPayID;
    }

    public void setCountryID(long countryID) {
        this.countryID = countryID;
    }

    public void setCreationDate(java.sql.Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setDueDate(java.sql.Date dueDate) {
        this.dueDate = dueDate;
    }

    public void setPostAccountID(long postAccountID) {
        this.postAccountID = postAccountID;
    }

    public void setRecipientID(long recipientID) {
        this.recipientID = recipientID;
    }

    public void setBeneficiaryID(long beneficiaryID) {
        this.beneficiaryID = beneficiaryID;
    }

    public void setMessage4x35(String message4x35) {
        this.message4x35 = message4x35;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public void setFlagUrgent(String flagUrgent) {
        this.flagUrgent = flagUrgent;
    }

    public void setOrdererID(long ordererID) {
        this.ordererID = ordererID;
    }

    public long getId() {
        return id;
    }

    public long getType() {
        return type;
    }

    public long getPrincipalID() {
        return principalID;
    }

    public short getCurrencyBookID() {
        return currencyBookID;
    }

    public java.math.BigDecimal getAmount() {
        return amount;
    }

    public short getCurrencyPayID() {
        return currencyPayID;
    }

    public long getCountryID() {
        return countryID;
    }

    public java.sql.Date getCreationDate() {
        return creationDate;
    }

    public java.sql.Date getDueDate() {
        return dueDate;
    }

    public long getPostAccountID() {
        return postAccountID;
    }

    public long getRecipientID() {
        return recipientID;
    }

    public long getBeneficiaryID() {
        return beneficiaryID;
    }

    public String getMessage4x35() {
        return message4x35;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public String getFlagUrgent() {
        return flagUrgent;
    }

    public long getOrdererID() {
        return ordererID;
    }

    public String toString() {
        return id + ";" + type + ";" + principalID + ";" + currencyBookID + ";" + amount + ";" + currencyPayID + ";" + countryID + ";" + creationDate + ";" + dueDate + ";" + postAccountID + ";" + recipientID + ";" + beneficiaryID + ";" + message4x35 + ";" + taxCode + ";" + flagUrgent + ";" + ordererID;
    }

    public void init() {
        id = 0;
        type = 0;
        principalID = 0;
        currencyBookID = 0;
        currencyPayID = 0;
        countryID = 0;
        creationDate = new java.sql.Date(System.currentTimeMillis());
        dueDate = new java.sql.Date(System.currentTimeMillis());
        postAccountID = 0;
        recipientID = 0;
        beneficiaryID = 0;
        message4x35 = "";
        taxCode = "";
        flagUrgent = "";
        ordererID = 0;
    }
}

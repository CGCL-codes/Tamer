package com.pr.vo;

import com.be.bo.GlobalParameter;

public class WageVO {

    private long id;

    private long employeeID;

    private String bookText;

    private long categoryID;

    private double percentage;

    private double quantity;

    private java.math.BigDecimal rate;

    private java.math.BigDecimal amount;

    private short currencyID;

    private java.sql.Timestamp modified;

    private java.sql.Date validFrom;

    private java.sql.Date validTo;

    public void setId(long id) {
        this.id = id;
    }

    public void setEmployeeID(long employeeID) {
        this.employeeID = employeeID;
    }

    public void setBookText(String bookText) {
        this.bookText = bookText;
    }

    public void setCategoryID(long categoryID) {
        this.categoryID = categoryID;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public void setRate(java.math.BigDecimal rate) {
        this.rate = rate;
    }

    public void setAmount(java.math.BigDecimal amount) {
        this.amount = amount;
    }

    public void setCurrencyID(short currencyID) {
        this.currencyID = currencyID;
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

    public long getEmployeeID() {
        return employeeID;
    }

    public String getBookText() {
        return bookText;
    }

    public long getCategoryID() {
        return categoryID;
    }

    public double getPercentage() {
        return percentage;
    }

    public double getQuantity() {
        return quantity;
    }

    public java.math.BigDecimal getRate() {
        return rate;
    }

    public java.math.BigDecimal getAmount() {
        return amount;
    }

    public short getCurrencyID() {
        return currencyID;
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
        return id + ";" + employeeID + ";" + bookText + ";" + categoryID + ";" + percentage + ";" + quantity + ";" + rate + ";" + amount + ";" + currencyID + ";" + modified + ";" + validFrom + ";" + validTo;
    }

    public void init() {
        id = 0;
        employeeID = 0;
        bookText = "";
        categoryID = 0;
        percentage = 0.0;
        quantity = 0.0;
        currencyID = 0;
        rate = new java.math.BigDecimal(0.0);
        amount = new java.math.BigDecimal(0.0);
        validFrom = new java.sql.Date(System.currentTimeMillis());
        validTo = new java.sql.Date(GlobalParameter.defaultValidTo);
    }
}

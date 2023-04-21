package com.pr.vo;

import com.be.bo.GlobalParameter;

public class WageRecordVO {

    private long id;

    private long companyID;

    private String name;

    private long type;

    private double percentage;

    private java.math.BigDecimal amount;

    private java.sql.Date validFrom;

    private java.sql.Date validTo;

    public void setId(long id) {
        this.id = id;
    }

    public void setCompanyID(long companyID) {
        this.companyID = companyID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(long type) {
        this.type = type;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public void setAmount(java.math.BigDecimal amount) {
        this.amount = amount;
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

    public long getCompanyID() {
        return companyID;
    }

    public String getName() {
        return name;
    }

    public long getType() {
        return type;
    }

    public double getPercentage() {
        return percentage;
    }

    public java.math.BigDecimal getAmount() {
        return amount;
    }

    public java.sql.Date getValidFrom() {
        return validFrom;
    }

    public java.sql.Date getValidTo() {
        return validTo;
    }

    public String toString() {
        return id + ";" + companyID + ";" + name + ";" + type + ";" + percentage + ";" + amount + ";" + validFrom + ";" + validTo;
    }

    public void init() {
        id = 0;
        companyID = 0;
        name = "";
        type = 0;
        percentage = 0.0;
        amount = new java.math.BigDecimal(0.0);
        validFrom = new java.sql.Date(System.currentTimeMillis());
        validTo = new java.sql.Date(GlobalParameter.defaultValidTo);
    }
}

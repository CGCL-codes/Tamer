package com.be.vo;

public class LedgerHistVO {

    private long id;

    private java.sql.Timestamp tstamp;

    private long dmType;

    private long dmSubType;

    private java.sql.Date evalDate;

    private long ledgerID;

    private String accountID;

    private String ledgerName;

    private String ledgerType;

    private java.math.BigDecimal amount;

    private short currencyID;

    private double exchangeRate;

    private java.math.BigDecimal amountRef;

    public void setId(long id) {
        this.id = id;
    }

    public void setTstamp(java.sql.Timestamp tstamp) {
        this.tstamp = tstamp;
    }

    public void setDmType(long dmType) {
        this.dmType = dmType;
    }

    public void setDmSubType(long dmSubType) {
        this.dmSubType = dmSubType;
    }

    public void setEvalDate(java.sql.Date evalDate) {
        this.evalDate = evalDate;
    }

    public void setLedgerID(long ledgerID) {
        this.ledgerID = ledgerID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public void setLedgerName(String ledgerName) {
        this.ledgerName = ledgerName;
    }

    public void setLedgerType(String ledgerType) {
        this.ledgerType = ledgerType;
    }

    public void setAmount(java.math.BigDecimal amount) {
        this.amount = amount;
    }

    public void setCurrencyID(short currencyID) {
        this.currencyID = currencyID;
    }

    public void setExchangeRate(double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public void setAmountRef(java.math.BigDecimal amountRef) {
        this.amountRef = amountRef;
    }

    public long getId() {
        return id;
    }

    public java.sql.Timestamp getTstamp() {
        return tstamp;
    }

    public long getDmType() {
        return dmType;
    }

    public long getDmSubType() {
        return dmSubType;
    }

    public java.sql.Date getEvalDate() {
        return evalDate;
    }

    public long getLedgerID() {
        return ledgerID;
    }

    public String getAccountID() {
        return accountID;
    }

    public String getLedgerName() {
        return ledgerName;
    }

    public String getLedgerType() {
        return ledgerType;
    }

    public java.math.BigDecimal getAmount() {
        return amount;
    }

    public short getCurrencyID() {
        return currencyID;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }

    public java.math.BigDecimal getAmountRef() {
        return amountRef;
    }

    public String toString() {
        return id + ";" + tstamp + ";" + dmType + ";" + dmSubType + ";" + evalDate + ";" + ledgerID + ";" + accountID + ";" + ledgerName + ";" + ledgerType + ";" + amount + ";" + currencyID + ";" + exchangeRate + ";" + amountRef;
    }

    public void init() {
        id = 0;
        dmType = 0;
        dmSubType = 0;
        evalDate = new java.sql.Date(System.currentTimeMillis());
        ledgerID = 0;
        accountID = "";
        ledgerName = "";
        ledgerType = "";
        currencyID = 0;
        exchangeRate = 0.0;
    }
}

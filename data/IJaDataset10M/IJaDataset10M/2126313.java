package com.be.vo;

import java.math.BigDecimal;

public class JournalVO {

    private long id;

    private long orderID;

    private long journalID;

    private java.sql.Date bookDate;

    private java.sql.Date valueDate;

    private long ledgerDebit;

    private java.math.BigDecimal amountDebit;

    private java.math.BigDecimal amountDebitReal;

    private short currencyDebitID;

    private double exchangeRateDebit;

    private long ledgerCredit;

    private java.math.BigDecimal amountCredit;

    private java.math.BigDecimal amountCreditReal;

    private short currencyCreditID;

    private double exchangeRateCredit;

    public void setId(long id) {
        this.id = id;
    }

    public void setOrderID(long orderID) {
        this.orderID = orderID;
    }

    public void setJournalID(long journalID) {
        this.journalID = journalID;
    }

    public void setBookDate(java.sql.Date bookDate) {
        this.bookDate = bookDate;
    }

    public void setValueDate(java.sql.Date valueDate) {
        this.valueDate = valueDate;
    }

    public void setLedgerDebit(long ledgerDebit) {
        this.ledgerDebit = ledgerDebit;
    }

    public void setAmountDebit(java.math.BigDecimal amountDebit) {
        this.amountDebit = amountDebit;
    }

    public void setCurrencyDebitID(short currencyDebitID) {
        this.currencyDebitID = currencyDebitID;
    }

    public void setLedgerCredit(long ledgerCredit) {
        this.ledgerCredit = ledgerCredit;
    }

    public void setAmountCredit(java.math.BigDecimal amountCredit) {
        this.amountCredit = amountCredit;
    }

    public void setCurrencyCreditID(short currencyCreditID) {
        this.currencyCreditID = currencyCreditID;
    }

    public long getId() {
        return id;
    }

    public long getOrderID() {
        return orderID;
    }

    public long getJournalID() {
        return journalID;
    }

    public java.sql.Date getBookDate() {
        return bookDate;
    }

    public java.sql.Date getValueDate() {
        return valueDate;
    }

    public long getLedgerDebit() {
        return ledgerDebit;
    }

    public java.math.BigDecimal getAmountDebit() {
        return amountDebit;
    }

    public short getCurrencyDebitID() {
        return currencyDebitID;
    }

    public long getLedgerCredit() {
        return ledgerCredit;
    }

    public java.math.BigDecimal getAmountCredit() {
        return amountCredit;
    }

    public short getCurrencyCreditID() {
        return currencyCreditID;
    }

    public String toString() {
        return id + ";" + orderID + ";" + journalID + ";" + bookDate + ";" + valueDate + ";" + ledgerDebit + ";" + amountDebit + ";" + currencyDebitID + ";" + ledgerCredit + ";" + amountCredit + ";" + currencyCreditID + ";" + exchangeRateCredit;
    }

    public void init() {
        id = 0;
        orderID = 0;
        journalID = 0;
        bookDate = new java.sql.Date(System.currentTimeMillis());
        valueDate = new java.sql.Date(System.currentTimeMillis());
        ledgerDebit = 0;
        currencyDebitID = 0;
        exchangeRateDebit = 0.0;
        ledgerCredit = 0;
        currencyCreditID = 0;
        exchangeRateCredit = 0.0;
        amountDebit = new BigDecimal(0);
        amountCredit = new BigDecimal(0);
        amountDebitReal = new BigDecimal(0);
        amountCreditReal = new BigDecimal(0);
    }

    public java.math.BigDecimal getAmountCreditReal() {
        return amountCreditReal;
    }

    public void setAmountCreditReal(java.math.BigDecimal amountCreditReal) {
        this.amountCreditReal = amountCreditReal;
    }

    public java.math.BigDecimal getAmountDebitReal() {
        return amountDebitReal;
    }

    public void setAmountDebitReal(java.math.BigDecimal amountDebitReal) {
        this.amountDebitReal = amountDebitReal;
    }

    public double getExchangeRateCredit() {
        return exchangeRateCredit;
    }

    public void setExchangeRateCredit(double exchangeRateCredit) {
        this.exchangeRateCredit = exchangeRateCredit;
    }

    public double getExchangeRateDebit() {
        return exchangeRateDebit;
    }

    public void setExchangeRateDebit(double exchangeRateDebit) {
        this.exchangeRateDebit = exchangeRateDebit;
    }
}

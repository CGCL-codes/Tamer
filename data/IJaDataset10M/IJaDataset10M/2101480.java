package org.posterita.beans;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class BankStatementLineBean {

    private Timestamp dateTrx;

    private String documentNo;

    private int paymentId;

    private int currencyId;

    private String isoCode;

    private BigDecimal payAmt;

    private BigDecimal currencyConvert;

    private String bpName;

    public String getBpName() {
        return bpName;
    }

    public void setBpName(String bpName) {
        this.bpName = bpName;
    }

    public BigDecimal getCurrencyConvert() {
        return currencyConvert;
    }

    public void setCurrencyConvert(BigDecimal currencyConvert) {
        this.currencyConvert = currencyConvert;
    }

    public int getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(int currencyId) {
        this.currencyId = currencyId;
    }

    public Timestamp getDateTrx() {
        return dateTrx;
    }

    public void setDateTrx(Timestamp dateTrx) {
        this.dateTrx = dateTrx;
    }

    public String getDocumentNo() {
        return documentNo;
    }

    public void setDocumentNo(String documentNo) {
        this.documentNo = documentNo;
    }

    public String getIsoCode() {
        return isoCode;
    }

    public void setIsoCode(String isoCode) {
        this.isoCode = isoCode;
    }

    public BigDecimal getPayAmt() {
        return payAmt;
    }

    public void setPayAmt(BigDecimal payAmt) {
        this.payAmt = payAmt;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }
}

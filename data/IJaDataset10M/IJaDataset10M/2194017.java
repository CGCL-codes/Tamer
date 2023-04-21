package org.posterita.beans;

import java.io.Serializable;
import java.math.BigDecimal;

public class CloseTillBean implements Serializable {

    private String currency;

    private String tillNo;

    private String tillName;

    private BigDecimal beginningBalance;

    private BigDecimal netCashTrx;

    private BigDecimal balanceEntered;

    private BigDecimal difference;

    private BigDecimal endingBalance;

    private BigDecimal cashTotal;

    private BigDecimal cardTotal;

    private BigDecimal chequeTotal;

    private BigDecimal grandTotal;

    private BigDecimal cardAmtEntered;

    private BigDecimal cardDifference;

    private BigDecimal chequeAmtEntered;

    private BigDecimal chequeDifference;

    public String getTillNo() {
        return tillNo;
    }

    public void setTillNo(String tillNo) {
        this.tillNo = tillNo;
    }

    public String getTillName() {
        return tillName;
    }

    public void setTillName(String tillName) {
        this.tillName = tillName;
    }

    public BigDecimal getBeginningBalance() {
        return beginningBalance;
    }

    public void setBeginningBalance(BigDecimal beginningBalance) {
        this.beginningBalance = beginningBalance;
    }

    public BigDecimal getNetCashTrx() {
        return netCashTrx;
    }

    public void setNetCashTrx(BigDecimal netCashTrx) {
        this.netCashTrx = netCashTrx;
    }

    public BigDecimal getBalanceEntered() {
        return balanceEntered;
    }

    public void setBalanceEntered(BigDecimal balanceEntered) {
        this.balanceEntered = balanceEntered;
    }

    public BigDecimal getDifference() {
        return difference;
    }

    public void setDifference(BigDecimal difference) {
        this.difference = difference;
    }

    public BigDecimal getEndingBalance() {
        return endingBalance;
    }

    public void setEndingBalance(BigDecimal endingBalance) {
        this.endingBalance = endingBalance;
    }

    public BigDecimal getCashTotal() {
        return cashTotal;
    }

    public void setCashTotal(BigDecimal cashTotal) {
        this.cashTotal = cashTotal;
    }

    public BigDecimal getCardTotal() {
        return cardTotal;
    }

    public void setCardTotal(BigDecimal cardTotal) {
        this.cardTotal = cardTotal;
    }

    public BigDecimal getChequeTotal() {
        return chequeTotal;
    }

    public void setChequeTotal(BigDecimal chequeTotal) {
        this.chequeTotal = chequeTotal;
    }

    public BigDecimal getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(BigDecimal grandTotal) {
        this.grandTotal = grandTotal;
    }

    public BigDecimal getCardAmtEntered() {
        return cardAmtEntered;
    }

    public void setCardAmtEntered(BigDecimal cardAmtEntered) {
        this.cardAmtEntered = cardAmtEntered;
    }

    public BigDecimal getCardDifference() {
        return cardDifference;
    }

    public void setCardDifference(BigDecimal cardDifference) {
        this.cardDifference = cardDifference;
    }

    public BigDecimal getChequeAmtEntered() {
        return chequeAmtEntered;
    }

    public void setChequeAmtEntered(BigDecimal chequeAmtEntered) {
        this.chequeAmtEntered = chequeAmtEntered;
    }

    public BigDecimal getChequeDifference() {
        return chequeDifference;
    }

    public void setChequeDifference(BigDecimal chequeDifference) {
        this.chequeDifference = chequeDifference;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}

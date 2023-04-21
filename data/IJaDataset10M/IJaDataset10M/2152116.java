package com.stockapp.hibernate;

import java.util.Date;

public class Transaction {

    private Integer id;

    private String email;

    private String stockCode;

    private Date transactionDate;

    private double buyPrice, sellPrice;

    private int quantity;

    public Transaction() {
    }

    public Transaction(String email) {
        this.email = email;
        this.stockCode = null;
        this.transactionDate = null;
        this.buyPrice = this.sellPrice = 0.0;
        this.quantity = 0;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String emailId) {
        this.email = emailId;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(stockCode + "," + buyPrice);
        return sb.toString();
    }
}

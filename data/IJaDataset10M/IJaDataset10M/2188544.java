package com.core.pay.post.esr;

import java.util.Date;

public class TR4 {

    private String transactionCode = "  ";

    private int transactionType;

    private String origin = "  ";

    private int deliveryType;

    private String customerNumber = "         ";

    private String sortKey = "                           ";

    private String ISOCode = "CHF";

    private double amount = 0.0;

    private int transactionCount = 0;

    private Date creationDateMedia = null;

    private double paymentFee = 0.0;

    private double postProcessingFee = 0.0;

    private String reserve = "         ";

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getPaymentFee() {
        return paymentFee;
    }

    public void setPaymentFee(double paymentFee) {
        this.paymentFee = paymentFee;
    }

    public Date getCreationDateMedia() {
        return creationDateMedia;
    }

    public void setCreationDateMedia(Date creationDateMedia) {
        this.creationDateMedia = creationDateMedia;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getISOCode() {
        return ISOCode;
    }

    public void setISOCode(String code) {
        ISOCode = code;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public double getPostProcessingFee() {
        return postProcessingFee;
    }

    public void setPostProcessingFee(double postProcessingFee) {
        this.postProcessingFee = postProcessingFee;
    }

    public String getSortKey() {
        return sortKey;
    }

    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }

    public int getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(int transactionCount) {
        this.transactionCount = transactionCount;
    }

    public String getReserve() {
        return reserve;
    }

    public void setReserve(String reserve) {
        this.reserve = reserve;
    }

    public int getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(int deliveryType) {
        this.deliveryType = deliveryType;
    }

    public int getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(int transactionType) {
        this.transactionType = transactionType;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }
}

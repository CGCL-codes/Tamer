package com.creditors.param;

public class TA830Parameter {

    private String id;

    private String type;

    private String salaryPayment;

    private String referenceNumber;

    private String debitAccountID;

    private String principalID;

    private String currencyID;

    private String amount;

    private String exchangeRate;

    private String creationDate;

    private String dueDate;

    private String bankAccountID;

    private String recipientID;

    private String beneficiaryID;

    private String message4x30;

    private String instructions4x30;

    private String ordererID;

    private String searchString;

    public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSalaryPayment(String salaryPayment) {
        this.salaryPayment = salaryPayment;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public void setDebitAccountID(String debitAccountID) {
        this.debitAccountID = debitAccountID;
    }

    public void setPrincipalID(String principalID) {
        this.principalID = principalID;
    }

    public void setCurrencyID(String currencyID) {
        this.currencyID = currencyID;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setExchangeRate(String exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setBankAccountID(String bankAccountID) {
        this.bankAccountID = bankAccountID;
    }

    public void setRecipientID(String recipientID) {
        this.recipientID = recipientID;
    }

    public void setBeneficiaryID(String beneficiaryID) {
        this.beneficiaryID = beneficiaryID;
    }

    public void setMessage4x30(String message4x30) {
        this.message4x30 = message4x30;
    }

    public void setInstructions4x30(String instructions4x30) {
        this.instructions4x30 = instructions4x30;
    }

    public void setOrdererID(String ordererID) {
        this.ordererID = ordererID;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getSalaryPayment() {
        return salaryPayment;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public String getDebitAccountID() {
        return debitAccountID;
    }

    public String getPrincipalID() {
        return principalID;
    }

    public String getCurrencyID() {
        return currencyID;
    }

    public String getAmount() {
        return amount;
    }

    public String getExchangeRate() {
        return exchangeRate;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getBankAccountID() {
        return bankAccountID;
    }

    public String getRecipientID() {
        return recipientID;
    }

    public String getBeneficiaryID() {
        return beneficiaryID;
    }

    public String getMessage4x30() {
        return message4x30;
    }

    public String getInstructions4x30() {
        return instructions4x30;
    }

    public String getOrdererID() {
        return ordererID;
    }

    public String getSearchString() {
        return searchString;
    }

    public String toString() {
        return id + ";" + type + ";" + salaryPayment + ";" + referenceNumber + ";" + debitAccountID + ";" + principalID + ";" + currencyID + ";" + amount + ";" + exchangeRate + ";" + creationDate + ";" + dueDate + ";" + bankAccountID + ";" + recipientID + ";" + beneficiaryID + ";" + message4x30 + ";" + instructions4x30 + ";" + ordererID + ";" + searchString;
    }

    public void init() {
    }
}

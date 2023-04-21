package com.creditors.param;

public class TAViewParameter {

    private String id;

    private String type;

    private String salaryPayment;

    private String referenceNumber;

    private String debitAccountID;

    private String principalID;

    private String currencyID;

    private String amountFrom;

    private String amountTo;

    private String creationDateFrom;

    private String creationDateTo;

    private String dueDateFrom;

    private String dueDateTo;

    private String bankAccountID;

    private String postAccountID;

    private String recipientID;

    private String beneficiaryID;

    private String message;

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

    public void setBankAccountID(String bankAccountID) {
        this.bankAccountID = bankAccountID;
    }

    public void setPostAccountID(String postAccountID) {
        this.postAccountID = postAccountID;
    }

    public void setRecipientID(String recipientID) {
        this.recipientID = recipientID;
    }

    public void setBeneficiaryID(String beneficiaryID) {
        this.beneficiaryID = beneficiaryID;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public String getBankAccountID() {
        return bankAccountID;
    }

    public String getPostAccountID() {
        return postAccountID;
    }

    public String getRecipientID() {
        return recipientID;
    }

    public String getBeneficiaryID() {
        return beneficiaryID;
    }

    public String getMessage() {
        return message;
    }

    public String getOrdererID() {
        return ordererID;
    }

    public String getSearchString() {
        return searchString;
    }

    public String toString() {
        return id + ";" + type + ";" + salaryPayment + ";" + referenceNumber + ";" + debitAccountID + ";" + principalID + ";" + currencyID + ";" + amountFrom + ";" + amountTo + ";" + creationDateFrom + ";" + creationDateTo + ";" + dueDateFrom + ";" + dueDateTo + ";" + bankAccountID + ";" + postAccountID + ";" + recipientID + ";" + beneficiaryID + ";" + message + ";" + ordererID + ";" + searchString;
    }

    public void init() {
    }

    public String getAmountFrom() {
        return amountFrom;
    }

    public void setAmountFrom(String amountFrom) {
        this.amountFrom = amountFrom;
    }

    public String getAmountTo() {
        return amountTo;
    }

    public void setAmountTo(String amountTo) {
        this.amountTo = amountTo;
    }

    public String getCreationDateFrom() {
        return creationDateFrom;
    }

    public void setCreationDateFrom(String creationDateFrom) {
        this.creationDateFrom = creationDateFrom;
    }

    public String getCreationDateTo() {
        return creationDateTo;
    }

    public void setCreationDateTo(String creationDateTo) {
        this.creationDateTo = creationDateTo;
    }

    public String getDueDateFrom() {
        return dueDateFrom;
    }

    public void setDueDateFrom(String dueDateFrom) {
        this.dueDateFrom = dueDateFrom;
    }

    public String getDueDateTo() {
        return dueDateTo;
    }

    public void setDueDateTo(String dueDateTo) {
        this.dueDateTo = dueDateTo;
    }
}

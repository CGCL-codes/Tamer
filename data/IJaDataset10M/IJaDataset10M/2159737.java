package com.creditors.http.forms;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

public class PaymentViewForm extends ValidatorForm {

    private static final long serialVersionUID = 6755061828713001430L;

    private String action;

    private long id;

    private long type;

    private String principal;

    private String currencyBook;

    private String amount;

    private String currencyPay;

    private String country;

    private String creationDate;

    private String dueDate;

    private String postAccount;

    private String bankAccount;

    private String recipient;

    private String beneficiary;

    private String message4x35;

    private String orderer;

    public void setAction(String action) {
        this.action = action;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setType(long type) {
        this.type = type;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public void setCurrencyBook(String currencyBook) {
        this.currencyBook = currencyBook;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setCurrencyPay(String currencyPay) {
        this.currencyPay = currencyPay;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setPostAccount(String postAccount) {
        this.postAccount = postAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public void setBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
    }

    public void setMessage4x35(String message4x35) {
        this.message4x35 = message4x35;
    }

    public void setOrderer(String orderer) {
        this.orderer = orderer;
    }

    public String getAction() {
        return action;
    }

    public long getId() {
        return id;
    }

    public long getType() {
        return type;
    }

    public String getPrincipal() {
        return principal;
    }

    public String getCurrencyBook() {
        return currencyBook;
    }

    public String getAmount() {
        return amount;
    }

    public String getCurrencyPay() {
        return currencyPay;
    }

    public String getCountry() {
        return country;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getPostAccount() {
        return postAccount;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getBeneficiary() {
        return beneficiary;
    }

    public String getMessage4x35() {
        return message4x35;
    }

    public String getOrderer() {
        return orderer;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        action = "";
        id = 0;
        type = 0;
        principal = "";
        currencyBook = "";
        amount = "";
        currencyPay = "";
        country = "";
        creationDate = "";
        dueDate = "";
        postAccount = "";
        bankAccount = "";
        recipient = "";
        beneficiary = "";
        message4x35 = "";
        orderer = "";
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = super.validate(mapping, request);
        return errors;
    }

    public String toString() {
        return action + ";" + id + ";" + type + ";" + principal + ";" + currencyBook + ";" + amount + ";" + currencyPay + ";" + country + ";" + creationDate + ";" + dueDate + ";" + postAccount + ";" + bankAccount + ";" + recipient + ";" + beneficiary + ";" + message4x35 + ";" + orderer;
    }
}

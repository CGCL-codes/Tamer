package com.creditors.http.forms;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

public class PF22ParameterForm extends ValidatorForm {

    private static final long serialVersionUID = -5680268184373385888L;

    private String action;

    private String id;

    private String type;

    private String principalID;

    private String currencyBookID;

    private String amount;

    private String currencyPayID;

    private String countryID;

    private String creationDate;

    private String dueDate;

    private String postAccountID;

    private String bankAccountID;

    private String recipientID;

    private String beneficiaryID;

    private String message4x35;

    private String ordererID;

    private String searchString;

    public void setAction(String action) {
        this.action = action;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPrincipalID(String principalID) {
        this.principalID = principalID;
    }

    public void setCurrencyBookID(String currencyBookID) {
        this.currencyBookID = currencyBookID;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setCurrencyPayID(String currencyPayID) {
        this.currencyPayID = currencyPayID;
    }

    public void setCountryID(String countryID) {
        this.countryID = countryID;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setPostAccountID(String postAccountID) {
        this.postAccountID = postAccountID;
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

    public void setMessage4x35(String message4x35) {
        this.message4x35 = message4x35;
    }

    public void setOrdererID(String ordererID) {
        this.ordererID = ordererID;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public String getAction() {
        return action;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getPrincipalID() {
        return principalID;
    }

    public String getCurrencyBookID() {
        return currencyBookID;
    }

    public String getAmount() {
        return amount;
    }

    public String getCurrencyPayID() {
        return currencyPayID;
    }

    public String getCountryID() {
        return countryID;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getPostAccountID() {
        return postAccountID;
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

    public String getMessage4x35() {
        return message4x35;
    }

    public String getOrdererID() {
        return ordererID;
    }

    public String getSearchString() {
        return searchString;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        action = "";
        id = "";
        type = "";
        principalID = "";
        currencyBookID = "";
        amount = "";
        currencyPayID = "";
        countryID = "";
        creationDate = "";
        dueDate = "";
        postAccountID = "";
        bankAccountID = "";
        recipientID = "";
        beneficiaryID = "";
        message4x35 = "";
        ordererID = "";
        searchString = "";
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = super.validate(mapping, request);
        return errors;
    }

    public String toString() {
        return action + ";" + id + ";" + type + ";" + principalID + ";" + currencyBookID + ";" + amount + ";" + currencyPayID + ";" + countryID + ";" + creationDate + ";" + dueDate + ";" + postAccountID + ";" + bankAccountID + ";" + recipientID + ";" + beneficiaryID + ";" + message4x35 + ";" + ordererID + ";" + searchString;
    }
}

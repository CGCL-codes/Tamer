package com.debitors.http.forms;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

public class OrderItemParameterForm extends ValidatorForm {

    private static final long serialVersionUID = 3129050954529625131L;

    private String action;

    private String id;

    private String orderID;

    private String itemID;

    private String itemName;

    private String itemDescription;

    private String itemSerialNr;

    private String itemCount;

    private String shippingDate;

    private String price;

    private String currencyID;

    private String discount;

    private String discountPercent;

    private String vatID;

    private String vat;

    private String searchString;

    public void setAction(String action) {
        this.action = action;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public void setItemSerialNr(String itemSerialNr) {
        this.itemSerialNr = itemSerialNr;
    }

    public void setItemCount(String itemCount) {
        this.itemCount = itemCount;
    }

    public void setShippingDate(String shippingDate) {
        this.shippingDate = shippingDate;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setCurrencyID(String currencyID) {
        this.currencyID = currencyID;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public void setDiscountPercent(String discountPercent) {
        this.discountPercent = discountPercent;
    }

    public void setVatID(String vatID) {
        this.vatID = vatID;
    }

    public void setVat(String vat) {
        this.vat = vat;
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

    public String getOrderID() {
        return orderID;
    }

    public String getItemID() {
        return itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public String getItemSerialNr() {
        return itemSerialNr;
    }

    public String getItemCount() {
        return itemCount;
    }

    public String getShippingDate() {
        return shippingDate;
    }

    public String getPrice() {
        return price;
    }

    public String getCurrencyID() {
        return currencyID;
    }

    public String getDiscount() {
        return discount;
    }

    public String getDiscountPercent() {
        return discountPercent;
    }

    public String getVatID() {
        return vatID;
    }

    public String getVat() {
        return vat;
    }

    public String getSearchString() {
        return searchString;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        action = "";
        id = "";
        orderID = "";
        itemID = "";
        itemName = "";
        itemDescription = "";
        itemSerialNr = "";
        itemCount = "";
        shippingDate = "";
        price = "";
        currencyID = "";
        discount = "";
        discountPercent = "";
        vatID = "";
        vat = "";
        searchString = "";
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = super.validate(mapping, request);
        return errors;
    }

    public String toString() {
        return action + ";" + id + ";" + orderID + ";" + itemID + ";" + itemName + ";" + itemDescription + ";" + itemSerialNr + ";" + itemCount + ";" + shippingDate + ";" + price + ";" + currencyID + ";" + discount + ";" + discountPercent + ";" + vatID + ";" + vat + ";" + searchString;
    }
}

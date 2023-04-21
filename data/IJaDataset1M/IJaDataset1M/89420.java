package com.sun.j2ee.blueprints.consumerwebsite.actions;

public class PurchaseOrder {

    protected com.sun.j2ee.blueprints.consumerwebsite.actions.Activity[] activities;

    protected com.sun.j2ee.blueprints.consumerwebsite.actions.ContactInfo billingInfo;

    protected com.sun.j2ee.blueprints.consumerwebsite.actions.CreditCard creditCard;

    protected java.lang.String departureCity;

    protected com.sun.j2ee.blueprints.consumerwebsite.actions.Transportation departureFlightInfo;

    protected java.lang.String emailId;

    protected java.util.Calendar endDate;

    protected int headCount;

    protected java.lang.String locale;

    protected com.sun.j2ee.blueprints.consumerwebsite.actions.Lodging lodging;

    protected java.util.Calendar orderDate;

    protected java.lang.String poId;

    protected com.sun.j2ee.blueprints.consumerwebsite.actions.Transportation returnFlightInfo;

    protected com.sun.j2ee.blueprints.consumerwebsite.actions.ContactInfo shippingInfo;

    protected java.util.Calendar startDate;

    protected float totalPrice;

    protected java.lang.String userId;

    public PurchaseOrder() {
    }

    public PurchaseOrder(com.sun.j2ee.blueprints.consumerwebsite.actions.Activity[] activities, com.sun.j2ee.blueprints.consumerwebsite.actions.ContactInfo billingInfo, com.sun.j2ee.blueprints.consumerwebsite.actions.CreditCard creditCard, java.lang.String departureCity, com.sun.j2ee.blueprints.consumerwebsite.actions.Transportation departureFlightInfo, java.lang.String emailId, java.util.Calendar endDate, int headCount, java.lang.String locale, com.sun.j2ee.blueprints.consumerwebsite.actions.Lodging lodging, java.util.Calendar orderDate, java.lang.String poId, com.sun.j2ee.blueprints.consumerwebsite.actions.Transportation returnFlightInfo, com.sun.j2ee.blueprints.consumerwebsite.actions.ContactInfo shippingInfo, java.util.Calendar startDate, float totalPrice, java.lang.String userId) {
        this.activities = activities;
        this.billingInfo = billingInfo;
        this.creditCard = creditCard;
        this.departureCity = departureCity;
        this.departureFlightInfo = departureFlightInfo;
        this.emailId = emailId;
        this.endDate = endDate;
        this.headCount = headCount;
        this.locale = locale;
        this.lodging = lodging;
        this.orderDate = orderDate;
        this.poId = poId;
        this.returnFlightInfo = returnFlightInfo;
        this.shippingInfo = shippingInfo;
        this.startDate = startDate;
        this.totalPrice = totalPrice;
        this.userId = userId;
    }

    public com.sun.j2ee.blueprints.consumerwebsite.actions.Activity[] getActivities() {
        return activities;
    }

    public void setActivities(com.sun.j2ee.blueprints.consumerwebsite.actions.Activity[] activities) {
        this.activities = activities;
    }

    public com.sun.j2ee.blueprints.consumerwebsite.actions.ContactInfo getBillingInfo() {
        return billingInfo;
    }

    public void setBillingInfo(com.sun.j2ee.blueprints.consumerwebsite.actions.ContactInfo billingInfo) {
        this.billingInfo = billingInfo;
    }

    public com.sun.j2ee.blueprints.consumerwebsite.actions.CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(com.sun.j2ee.blueprints.consumerwebsite.actions.CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    public java.lang.String getDepartureCity() {
        return departureCity;
    }

    public void setDepartureCity(java.lang.String departureCity) {
        this.departureCity = departureCity;
    }

    public com.sun.j2ee.blueprints.consumerwebsite.actions.Transportation getDepartureFlightInfo() {
        return departureFlightInfo;
    }

    public void setDepartureFlightInfo(com.sun.j2ee.blueprints.consumerwebsite.actions.Transportation departureFlightInfo) {
        this.departureFlightInfo = departureFlightInfo;
    }

    public java.lang.String getEmailId() {
        return emailId;
    }

    public void setEmailId(java.lang.String emailId) {
        this.emailId = emailId;
    }

    public java.util.Calendar getEndDate() {
        return endDate;
    }

    public void setEndDate(java.util.Calendar endDate) {
        this.endDate = endDate;
    }

    public int getHeadCount() {
        return headCount;
    }

    public void setHeadCount(int headCount) {
        this.headCount = headCount;
    }

    public java.lang.String getLocale() {
        return locale;
    }

    public void setLocale(java.lang.String locale) {
        this.locale = locale;
    }

    public com.sun.j2ee.blueprints.consumerwebsite.actions.Lodging getLodging() {
        return lodging;
    }

    public void setLodging(com.sun.j2ee.blueprints.consumerwebsite.actions.Lodging lodging) {
        this.lodging = lodging;
    }

    public java.util.Calendar getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(java.util.Calendar orderDate) {
        this.orderDate = orderDate;
    }

    public java.lang.String getPoId() {
        return poId;
    }

    public void setPoId(java.lang.String poId) {
        this.poId = poId;
    }

    public com.sun.j2ee.blueprints.consumerwebsite.actions.Transportation getReturnFlightInfo() {
        return returnFlightInfo;
    }

    public void setReturnFlightInfo(com.sun.j2ee.blueprints.consumerwebsite.actions.Transportation returnFlightInfo) {
        this.returnFlightInfo = returnFlightInfo;
    }

    public com.sun.j2ee.blueprints.consumerwebsite.actions.ContactInfo getShippingInfo() {
        return shippingInfo;
    }

    public void setShippingInfo(com.sun.j2ee.blueprints.consumerwebsite.actions.ContactInfo shippingInfo) {
        this.shippingInfo = shippingInfo;
    }

    public java.util.Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(java.util.Calendar startDate) {
        this.startDate = startDate;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public java.lang.String getUserId() {
        return userId;
    }

    public void setUserId(java.lang.String userId) {
        this.userId = userId;
    }
}

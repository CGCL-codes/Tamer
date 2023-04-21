package com.debitors.param;

public class PFT3Parameter {

    private String id;

    private String type;

    private String esrAccountNr;

    private String referenceNr;

    private String amount;

    private String orderReference;

    private String orderDate;

    private String processingDate;

    private String creditDate;

    private String microFilmNr;

    private String rejectCode;

    private String cost;

    private String searchString;

    public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setEsrAccountNr(String esrAccountNr) {
        this.esrAccountNr = esrAccountNr;
    }

    public void setReferenceNr(String referenceNr) {
        this.referenceNr = referenceNr;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setOrderReference(String orderReference) {
        this.orderReference = orderReference;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public void setProcessingDate(String processingDate) {
        this.processingDate = processingDate;
    }

    public void setCreditDate(String creditDate) {
        this.creditDate = creditDate;
    }

    public void setMicroFilmNr(String microFilmNr) {
        this.microFilmNr = microFilmNr;
    }

    public void setRejectCode(String rejectCode) {
        this.rejectCode = rejectCode;
    }

    public void setCost(String cost) {
        this.cost = cost;
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

    public String getEsrAccountNr() {
        return esrAccountNr;
    }

    public String getReferenceNr() {
        return referenceNr;
    }

    public String getAmount() {
        return amount;
    }

    public String getOrderReference() {
        return orderReference;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getProcessingDate() {
        return processingDate;
    }

    public String getCreditDate() {
        return creditDate;
    }

    public String getMicroFilmNr() {
        return microFilmNr;
    }

    public String getRejectCode() {
        return rejectCode;
    }

    public String getCost() {
        return cost;
    }

    public String getSearchString() {
        return searchString;
    }

    public String toString() {
        return id + ";" + type + ";" + esrAccountNr + ";" + referenceNr + ";" + amount + ";" + orderReference + ";" + orderDate + ";" + processingDate + ";" + creditDate + ";" + microFilmNr + ";" + rejectCode + ";" + cost + ";" + searchString;
    }

    public void init() {
    }
}

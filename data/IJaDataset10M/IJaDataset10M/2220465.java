package com.amazon.webservices.awsecommerceservice.x20090201;

public class Review implements java.io.Serializable {

    private java.lang.String aSIN;

    public java.lang.String getASIN() {
        return this.aSIN;
    }

    public void setASIN(java.lang.String aSIN) {
        this.aSIN = aSIN;
    }

    private java.math.BigDecimal rating;

    public java.math.BigDecimal getRating() {
        return this.rating;
    }

    public void setRating(java.math.BigDecimal rating) {
        this.rating = rating;
    }

    private java.math.BigInteger helpfulVotes;

    public java.math.BigInteger getHelpfulVotes() {
        return this.helpfulVotes;
    }

    public void setHelpfulVotes(java.math.BigInteger helpfulVotes) {
        this.helpfulVotes = helpfulVotes;
    }

    private java.lang.String customerId;

    public java.lang.String getCustomerId() {
        return this.customerId;
    }

    public void setCustomerId(java.lang.String customerId) {
        this.customerId = customerId;
    }

    private com.amazon.webservices.awsecommerceservice.x20090201.Reviewer reviewer;

    public com.amazon.webservices.awsecommerceservice.x20090201.Reviewer getReviewer() {
        return this.reviewer;
    }

    public void setReviewer(com.amazon.webservices.awsecommerceservice.x20090201.Reviewer reviewer) {
        this.reviewer = reviewer;
    }

    private java.math.BigInteger totalVotes;

    public java.math.BigInteger getTotalVotes() {
        return this.totalVotes;
    }

    public void setTotalVotes(java.math.BigInteger totalVotes) {
        this.totalVotes = totalVotes;
    }

    private java.lang.String date;

    public java.lang.String getDate() {
        return this.date;
    }

    public void setDate(java.lang.String date) {
        this.date = date;
    }

    private java.lang.String summary;

    public java.lang.String getSummary() {
        return this.summary;
    }

    public void setSummary(java.lang.String summary) {
        this.summary = summary;
    }

    private java.lang.String content;

    public java.lang.String getContent() {
        return this.content;
    }

    public void setContent(java.lang.String content) {
        this.content = content;
    }
}

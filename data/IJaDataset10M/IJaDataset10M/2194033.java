package com.amazon.webservices.awsecommerceservice.x20090201;

public class ShippingCharge implements java.io.Serializable {

    private java.lang.String shippingType;

    public java.lang.String getShippingType() {
        return this.shippingType;
    }

    public void setShippingType(java.lang.String shippingType) {
        this.shippingType = shippingType;
    }

    private boolean isRateTaxInclusive;

    public boolean getIsRateTaxInclusive() {
        return this.isRateTaxInclusive;
    }

    public void setIsRateTaxInclusive(boolean isRateTaxInclusive) {
        this.isRateTaxInclusive = isRateTaxInclusive;
    }

    private com.amazon.webservices.awsecommerceservice.x20090201.Price shippingPrice;

    public com.amazon.webservices.awsecommerceservice.x20090201.Price getShippingPrice() {
        return this.shippingPrice;
    }

    public void setShippingPrice(com.amazon.webservices.awsecommerceservice.x20090201.Price shippingPrice) {
        this.shippingPrice = shippingPrice;
    }
}

package com.spicesoft.clientobjects.samples.form;

public class Address {

    public static final String ADDRESS_LINE_1 = "addressLine1";

    public static final String ADDRESS_LINE_2 = "addressLine2";

    public static final String CITY = "city";

    public static final String PROVINCE = "province";

    public static final String COUNTRY = "country";

    public static final String POSTAL_CODE = "postalCode";

    private String addressLine1;

    private String addressLine2;

    private String city;

    private String province;

    private String country;

    private String postalCode;

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    @Override
    public String toString() {
        return "Address [addressLine1=" + addressLine1 + ", addressLine2=" + addressLine2 + ", city=" + city + ", country=" + country + ", postalCode=" + postalCode + ", province=" + province + "]";
    }
}

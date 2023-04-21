package org.safu.bean;

import org.safu.enums.FraudTendency;

public class Address implements FraudEntity, java.io.Serializable {

    private String street1;

    private String street2;

    private String zip;

    private String state;

    private String country;

    private FraudTendency fraudTendency = null;

    public String getStreet1() {
        return street1;
    }

    public void setStreet1(String street1) {
        this.street1 = street1;
    }

    public String getStreet2() {
        return street2;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getKey() {
        return zip;
    }

    public String toString() {
        return "{" + street1 + ((street2 != null) ? ", " + street2 : "") + ", " + state + "  " + zip + "}";
    }
}

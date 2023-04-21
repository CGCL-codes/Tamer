package org.json.annotation.test.person;

import org.json.annotation.XQueryField;

/**
 * @Author : <a href="mailto:Juanyong.zhang@gmail.com">Juanyong.zhang</a>
 * @Date : Aug 31, 2011
 */
public class Address {

    @XQueryField(query = "streetAddress")
    private String streetAddress;

    @XQueryField(query = "city")
    private String city;

    @XQueryField(query = "state")
    private String state;

    @XQueryField(query = "postalCode")
    private String postalCode;

    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getState() {
        return state;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }
}

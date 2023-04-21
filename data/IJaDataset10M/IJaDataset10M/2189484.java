package com.avatal.vo.user;

import java.io.Serializable;
import com.avatal.vo.base.AbstractIdentifyingNumberObject;

/**
 * Created May 2003
 */
public class Address extends AbstractIdentifyingNumberObject implements Serializable {

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    private String street;

    private String zipCode;

    private String city;

    /**
     * @supplierCardinality 1
     * @clientCardinality 0..* 
     */
    private Country country;
}

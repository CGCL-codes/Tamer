package com.codename1.contacts;

/**
 * This class represents a Contact Address
 * 
 * @author Chen
 */
public class Address {

    private String streetAddress;

    private String locality;

    private String region;

    private String postalCode;

    private String country;

    /**
     * Empty Constructor
     */
    public Address() {
    }

    /**
     * Gets Address Country
     * @return 
     */
    public String getCountry() {
        return country;
    }

    /**
     * Gets Address Locality
     * @return 
     */
    public String getLocality() {
        return locality;
    }

    /**
     * Gets Address Postal Code
     * @return 
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Gets Address Region
     * @return 
     */
    public String getRegion() {
        return region;
    }

    /**
     * Gets Address Street
     * @return 
     */
    public String getStreetAddress() {
        return streetAddress;
    }

    /**
     * Sets Address Country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Sets Address Locality
     */
    public void setLocality(String locality) {
        this.locality = locality;
    }

    /**
     * Sets Address Postal Code
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * Sets Address Region
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * Sets Address street
     */
    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }
}

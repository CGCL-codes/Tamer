package de.buelowssiege.jayaddress.misc;

import de.buelowssiege.jayaddress.bin.jayAddress;

/**
 * Represents one postal address
 * 
 * @author Maximilian Schwerin
 * @created 13. Juli 2002
 */
public class PostalAddress {

    public static final int UNKNOWN = 0;

    public static final int HOME = 1;

    public static final int OFFICE = 2;

    public static final int PARTNER = 3;

    public static final int PARENTS = 4;

    public static final int COUNTRY = 5;

    public static String[] TYPES = { jayAddress.getString("postaladdress.type.unknown"), jayAddress.getString("postaladdress.type.home"), jayAddress.getString("postaladdress.type.office"), jayAddress.getString("postaladdress.type.partner"), jayAddress.getString("postaladdress.type.parents"), jayAddress.getString("postaladdress.type.country") };

    private String street = "";

    private String city = "";

    private String region = "";

    private String zipcode = "";

    private String country = "";

    private int type = UNKNOWN;

    /**
     * This is the constructor for the <code>PostalAddress</code>
     */
    public PostalAddress() {
    }

    /**
     * This is the constructor for the <code>PostalAddress</code>
     * 
     * @param street
     *            Description of the Parameter
     * @param zipcode
     *            Description of the Parameter
     * @param city
     *            Description of the Parameter
     * @param region
     *            Description of the Parameter
     * @param country
     *            Description of the Parameter
     */
    public PostalAddress(String street, String zipcode, String city, String region, String country) {
        setStreet(street);
        setCity(city);
        setRegion(region);
        setZipCode(zipcode);
        setCountry(country);
    }

    /**
     * Sets the street attribute of the PostalAddress object
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * Sets the city attribute of the PostalAddress object
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Sets the region attribute of the PostalAddress object
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * Sets the zipCode attribute of the PostalAddress object
     */
    public void setZipCode(String zipcode) {
        this.zipcode = zipcode;
    }

    /**
     * Sets the country attribute of the PostalAddress object
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Sets the type attribute of the PostalAddress object
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * Gets the street attribute of the PostalAddress object
     */
    public String getStreet() {
        return (street);
    }

    /**
     * Gets the city attribute of the PostalAddress object
     */
    public String getCity() {
        return (city);
    }

    /**
     * Gets the region attribute of the PostalAddress object
     */
    public String getRegion() {
        return (region);
    }

    /**
     * Gets the zipCode attribute of the PostalAddress object
     */
    public String getZipCode() {
        return (zipcode);
    }

    /**
     * Gets the country attribute of the PostalAddress object
     */
    public String getCountry() {
        return (country);
    }

    /**
     * Gets the type attribute of the PostalAddress object
     */
    public int getType() {
        return (type);
    }

    /**
     * Its selfexplaining isnt it! :-)
     */
    public boolean isEqual(PostalAddress postalAddress) {
        boolean equals = getStreet().equals(postalAddress.getStreet());
        equals = equals && getCity().equals(postalAddress.getCity());
        equals = equals && getRegion().equals(postalAddress.getRegion());
        equals = equals && getZipCode().equals(postalAddress.getZipCode());
        equals = equals && getCountry().equals(postalAddress.getCountry());
        equals = equals && getType() == postalAddress.getType();
        return (equals);
    }

    /**
     * Its selfexplaining isnt it! :-)
     */
    public boolean equals(Object other) {
        return (other != null && other instanceof PostalAddress && isEqual((PostalAddress) other));
    }
}

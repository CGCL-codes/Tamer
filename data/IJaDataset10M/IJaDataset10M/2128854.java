package be.vds.jtbdive.model;

/**
 * This is a general representation of an address.
 * 
 * @author vanderslyen.g
 * 
 */
public class Address {

    private String countryCode;

    private String street;

    private String number;

    private String postalCode;

    private String box;

    private String city;

    private String region;

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setBox(String box) {
        this.box = box;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getStreet() {
        return street;
    }

    public String getNumber() {
        return number;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getBox() {
        return box;
    }

    public String getCity() {
        return city;
    }

    public String getRegion() {
        return region;
    }
}

package at.rc.tacos.model;

import at.rc.tacos.common.AbstractMessage;

/**
 * Stores information about a specifiy address
 * 
 * @author Michael
 */
public class Address extends AbstractMessage {

    public static final String ID = "address";

    private int addressId;

    private int zip;

    private String city;

    private String street;

    private String streetNumber;

    /**
	 * Default class constructor
	 */
    public Address() {
        super(ID);
        city = "";
        street = "";
        streetNumber = "-";
    }

    /**
	 * Default class constructor for a complete address object.
	 * 
	 * @param zip
	 *            the zip code
	 * @param city
	 *            the name of the city
	 * @param street
	 *            the name of the street
	 */
    public Address(int zip, String city, String street) {
        this();
        this.zip = zip;
        this.city = city;
        this.street = street;
        streetNumber = "-";
    }

    /**
	 * Returns the string based description
	 * 
	 * @return the human readable version
	 */
    @Override
    public String toString() {
        return "zip: " + zip + ";" + "city: " + city + ";" + "street: " + street;
    }

    /**
	 * Returns the calculated hash code based on the complete address<br>
	 * Two addresses have the same hash code if all fields are the same.
	 * 
	 * @return the calculated hash code
	 */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((city == null) ? 0 : city.hashCode());
        result = prime * result + ((street == null) ? 0 : street.hashCode());
        result = prime * result + zip;
        return result;
    }

    /**
	 * Returns whether the objects are equal or not.<br>
	 * Two addresses are equal if all propertie fields are the same.
	 * 
	 * @return true if all fields are the same otherwise false.
	 */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Address other = (Address) obj;
        if (city == null) {
            if (other.city != null) return false;
        } else if (!city.equals(other.city)) return false;
        if (street == null) {
            if (other.street != null) return false;
        } else if (!street.equals(other.street)) return false;
        if (zip != other.zip) return false;
        return true;
    }

    /**
	 * Returns the zip code of this address record
	 * 
	 * @return the zip code
	 */
    public int getZip() {
        return zip;
    }

    /**
	 * Returns the name of the city.
	 * 
	 * @return the name of the city
	 */
    public String getCity() {
        return city;
    }

    /**
	 * Returns the name of the street
	 * 
	 * @return the streetname
	 */
    public String getStreet() {
        return street;
    }

    /**
	 * Sets the zip code.
	 * 
	 * @param zip
	 *            the zip code as number
	 */
    public void setZip(int zip) {
        this.zip = zip;
    }

    /**
	 * Sets the name of the city
	 * 
	 * @param city
	 *            the cityname
	 */
    public void setCity(String city) {
        this.city = city;
    }

    /**
	 * Sets the name of the street
	 * 
	 * @param street
	 *            the streetname
	 */
    public void setStreet(String street) {
        this.street = street;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }
}

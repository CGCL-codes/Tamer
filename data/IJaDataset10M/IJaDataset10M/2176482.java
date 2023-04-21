package models;

import siena.Generator;
import siena.Id;
import siena.Model;
import siena.Table;

/**
 * Represents a street address.
 * 
 * @author SLever
 */
@Table("adresses")
public class Address extends Model {

    @Id(Generator.AUTO_INCREMENT)
    public Long id;

    public String street;

    public String city;

    public String countryCode;

    public long zipCode;

    /**
	 * Address Constructor
	 * 
	 * @param street
	 * @param town
	 * @param zip
	 * @param countryCode two-letter ISO-3166 code
	 */
    public Address(String street, String town, long zip, String countryCode) {
        this.street = street;
        this.city = town;
        this.countryCode = countryCode.toUpperCase();
        this.zipCode = zip;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.street).append("\n").append(city).append(", ").append(" ").append(zipCode).append(", ").append(this.countryCode);
        return sb.toString();
    }
}

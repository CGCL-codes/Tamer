package org.uxd.domain;

/** */
public class Country {

    /** Represent the unique identifier for a country
	 */
    private String id = null;

    /** Represent the country name
	 */
    private String name;

    /** Represent an optional abbreviation for the country name
	 */
    private String abbreviation = null;

    /** Represent the international country code used to identify telephone service location
	 */
    private int countryCode;

    /** Empty constructor for this object
	 */
    public Country() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public int getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(int countryCode) {
        this.countryCode = countryCode;
    }
}

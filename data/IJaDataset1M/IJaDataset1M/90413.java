package at.ac.tuwien.vitalab.hrcrm.bo;

public class Address {

    private Long id;

    private String street;

    private String country;

    private String town;

    /**
	 * Read access to the private field.
	 * @return The id.
	 */
    public Long getId() {
        return this.id;
    }

    /**
	 * Write access to private field.
	 * @param id
	 *            The id to set.
	 */
    public void setId(Long id) {
        this.id = id;
    }

    /**
	 * Read access to the private field.
	 * @return The street.
	 */
    public String getStreet() {
        return this.street;
    }

    /**
	 * Write access to private field.
	 * @param street
	 *            The street to set.
	 */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
	 * Read access to the private field.
	 * @return The country.
	 */
    public String getCountry() {
        return this.country;
    }

    /**
	 * Write access to private field.
	 * @param country
	 *            The country to set.
	 */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
	 * Read access to the private field.
	 * @return The town.
	 */
    public String getTown() {
        return this.town;
    }

    /**
	 * Write access to private field.
	 * @param town
	 *            The town to set.
	 */
    public void setTown(String town) {
        this.town = town;
    }
}

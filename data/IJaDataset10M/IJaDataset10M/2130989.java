package net.sf.josas.om;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import net.sf.josas.persistence.dao.DataObjectAccessor;

/**
 * This class represents zip records. As a zip code is unique for a given city
 * in a given country, this class allow addresses to share common information.
 * In addition, this class will allow users to just enter a zip code and
 * automatically get the corresponding city and country as far as the
 * corresponding zip record has been recorded. The list of zip records is
 * progressively built as the application is used.
 *
 * Because the zip code is assumed to be unique for a given city and a given
 * country, indexing of the corresponding table in the databases relies on the
 * zip code itself.
 *
 *
 * @author frederic
 *
 */
@Entity
public class ZipRecord implements Serializable {

    /** Default serial ID. */
    private static final long serialVersionUID = 1L;

    /** Size of the city text. */
    public static final int CITY_SIZE = 30;

    /** Id. */
    private long id;

    /** Version. */
    private int version;

    /** Zip code. */
    private String zipCode;

    /** City. */
    private String city;

    /** Country. */
    private Country country;

    /** Addresses. */
    private Collection<Address> addresses = new HashSet<Address>();

    /** Persons. */
    private Collection<Person> persons = new HashSet<Person>();

    /**
    * Constructor with arguments.
    *
    * @param code
    *            zip code
    * @param aCity
    *            city
    */
    public ZipRecord(final String code, final String aCity) {
        this(code, aCity, null);
    }

    /**
    * Default constructor.
    */
    public ZipRecord() {
    }

    /**
    * @param code
    *            zip code
    * @param name
    *            city name
    * @param aCountry
    *            country
    */
    public ZipRecord(final String code, final String name, final Country aCountry) {
        this.zipCode = code;
        this.city = name;
        this.country = aCountry;
    }

    /**
    * Get the unique Id.
    *
    * @return the unique Id
    */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CUST_SEQ")
    public final Long getId() {
        return id;
    }

    /**
    * Set the unique Id.
    *
    * @param anId
    *            unique Id to set
    */
    public final void setId(final Long anId) {
        this.id = anId;
    }

    /**
    * Get zipCode.
    *
    * @return the zipCode
    */
    @Column(name = "CODE", nullable = false)
    public final String getZipCode() {
        return zipCode;
    }

    /**
    * Set zipCode.
    *
    * @param aCode
    *            the zipCode to set
    */
    public final void setZipCode(final String aCode) {
        this.zipCode = aCode;
    }

    /**
    * @return the version
    */
    @Version
    public final int getVersion() {
        return version;
    }

    /**
    * @param aVersion
    *            the version to set
    */
    public final void setVersion(final int aVersion) {
        this.version = aVersion;
    }

    /**
    * Get city.
    *
    * @return the city
    */
    @Column(name = "CITY", nullable = false)
    public final String getCity() {
        return city;
    }

    /**
    * Set city.
    *
    * @param aCity
    *            the city to set
    */
    public final void setCity(final String aCity) {
        this.city = aCity;
    }

    /**
    * Get the list of all recorded zip records.
    *
    * @return the requested list.
    */
    public static List<ZipRecord> getList() {
        return DataObjectAccessor.getInstance().getZipRecords();
    }

    /**
    * Register a zip record. Generate an EntityExistsException if a zip record
    * has been previously registered with the same zip code.
    *
    * @param zipRecord
    *            record to register
    */
    public static void register(final ZipRecord zipRecord) {
        DataObjectAccessor.getInstance().record(zipRecord);
    }

    /**
    * @return country
    */
    @ManyToOne(cascade = CascadeType.PERSIST)
    public final Country getCountry() {
        return country;
    }

    /**
    * Set country.
    *
    * @param aCountry
    *            the value to set
    */
    public final void setCountry(final Country aCountry) {
        this.country = aCountry;
    }

    /**
    * Get the associated addresses.
    *
    * @return the associated addresses.
    */
    @OneToMany(mappedBy = "zipRecord")
    public final Collection<Address> getAddresses() {
        return addresses;
    }

    /**
    * Set the associated addresses.
    *
    * @param addressColl
    *            addresses to set
    */
    public final void setAddresses(final Collection<Address> addressColl) {
        this.addresses = addressColl;
    }

    /**
    * Add a new address in the list of addresses.
    *
    * @param address
    *            value to add
    */
    public final void addAddress(final Address address) {
        this.addresses.add(address);
    }

    /**
     * Update the record in the database.
     */
    public final void update() {
        DataObjectAccessor.getInstance().update(this);
    }

    /**
      * Find a zip record using its Id.
      *
      * @param id
      *            Id
      * @return Found zip or null
      */
    public static ZipRecord findZipRecord(final long id) {
        return DataObjectAccessor.getInstance().getZipRecord(id);
    }

    /**
       * Remove a zip from the database.
       *
       * @param zip
       *            zip to remove.
       */
    public static void remove(final ZipRecord zip) {
        DataObjectAccessor.getInstance().remove(zip);
    }

    /**
     * Get a record using its id.
     * @param id identifier
     * @return record found
     */
    public static final ZipRecord getZipRecord(final long id) {
        return DataObjectAccessor.getInstance().getZipRecord(id);
    }

    /**
     * Get the associated persons.
     *
     * @return the associated persons.
     */
    @OneToMany(mappedBy = "birthPlace")
    public final Collection<Person> getPersons() {
        return persons;
    }

    /**
     * Set the associated persons.
     *
     * @param personColl
     *            persons to set
     */
    public final void setPersons(final Collection<Person> personColl) {
        this.persons = personColl;
    }

    /**
     * Add a new person in the list of persons.
     *
     * @param person
     *            value to add
     */
    public final void addPerson(final Person person) {
        this.persons.add(person);
    }
}

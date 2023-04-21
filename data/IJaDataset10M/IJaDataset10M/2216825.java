package sample.core;

import com.xavax.xstore.PObject;
import java.sql.Timestamp;

/**
 * AddressImpl is the implementation class for a persistent Address.
 */
public class AddressImpl implements Address, PObject {

    /**
   * Construct an AddressImpl.
   */
    public AddressImpl() {
        suite = blank;
        street1 = blank;
        street2 = blank;
        city = blank;
        state = blank;
        zipCode = blank;
        timestamp = getTime();
    }

    /**
   * Construct an AddressImpl with initial values.
   *
   * @param suite    the suite number.
   * @param street1  line 1 of the street address.
   * @param street2  line 2 of the street address.
   * @param city     the city.
   * @param state    the state (or province).
   * @param zip      the ZIP code.
   */
    public AddressImpl(String suite, String street1, String street2, String city, String state, String zip) {
        this.suite = suite;
        this.street1 = street1;
        this.street2 = street2;
        this.city = city;
        this.state = state;
        this.zipCode = zip;
        timestamp = getTime();
    }

    public String getSuite() {
        return this.suite;
    }

    public void setSuite(String s) {
        this.suite = s;
        update();
    }

    public String getStreet1() {
        return this.street1;
    }

    public void setStreet1(String s) {
        this.street1 = s;
        update();
    }

    public String getStreet2() {
        return this.street2;
    }

    public void setStreet2(String s) {
        this.street2 = s;
        update();
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String s) {
        this.city = s;
        update();
    }

    public String getState() {
        return this.state;
    }

    public void setState(String s) {
        this.state = s;
        update();
    }

    public String getZipCode() {
        return this.zipCode;
    }

    public void setZipCode(String s) {
        this.zipCode = s;
        update();
    }

    /**
   * Returns the time this object was last modified.
   *
   * @return the time this object was last modified.
   */
    public Timestamp getUpdateTime() {
        return this.timestamp;
    }

    /**
   * Returns a string representation of this object.
   *
   * @return a string representation of this object.
   */
    public String toString() {
        StringBuffer s = new StringBuffer();
        s.append(street1).append(", ");
        if (suite != null && suite != blank && !suite.equals("")) {
            s.append(suite).append(", ");
        }
        if (street2 != null && street2 != blank && !street2.equals("")) {
            s.append(street2).append(", ");
        }
        s.append(city).append(", ");
        s.append(state).append(" ").append(zipCode);
        s.append(" [").append(timestamp.toString()).append("]");
        return s.toString();
    }

    /**
   * Returns a new timestamp with the current time.
   *
   * @return a new timestamp with the current time.
   */
    private Timestamp getTime() {
        long time = System.currentTimeMillis();
        Timestamp ts = new Timestamp(time);
        return ts;
    }

    /**
   * Set the timestamp to the current time.
   */
    private void setTime() {
        long time = System.currentTimeMillis();
        timestamp.setTime(time);
    }

    /**
   * Update the timestamp on this object and set the dirty bit so
   * the persistence framework knows the object was modified.
   */
    private void update() {
        setTime();
        setDirty();
    }

    public boolean isDirty() {
        return dirty;
    }

    public void clearDirty() {
        dirty = false;
    }

    public void setDirty() {
        dirty = true;
    }

    public void post$load() {
    }

    public transient boolean dirty;

    private String suite;

    private String street1;

    private String street2;

    private String city;

    private String state;

    private String zipCode;

    private Timestamp timestamp;

    protected static final String blank = "";
}

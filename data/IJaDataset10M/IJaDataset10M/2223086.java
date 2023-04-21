package net.wimpi.pim.contact.model;

import java.io.Serializable;
import net.wimpi.pim.util.Identifiable;

/**
 * An interface modeling an address based on the
 * types and information of the vCard Mime directory
 * profile standard specification.
 * <p>
 * For reference see RFC 2426:<br>
 * 3.2.1 ADR Type Definition<br>
 * 3.2.2 LABEL Type Definition<br>
 * <br>
 * The ADR type is based on semantics of the
 * X.520 geographical and postal addressing attributes.
 * <br>
 * Note that for a standard conformant implementation
 * you have to observe the following:
 * <ul>
 * <li>the default set of flags is
 * international, postal, parcel and work</li>
 * <li>flags should not be dependent on each other
 * (e.g. a residence can be a work place at the same time).</li>
 * </ul>
 *
 * @author Dieter Wimberger
 * @version @version@ (@date@)
 */
public interface Address extends Identifiable, Serializable {

    /**
   * Returns the post office box of this <tt>Address</tt>.
   *
   * @return the post office box as <tt>String</tt>.
   */
    public String getPostBox();

    /**
   * Sets the post office box of this <tt>Address</tt>.
   *
   * @param pobox the post office box as <tt>String</tt>.
   */
    public void setPostBox(String pobox);

    /**
   * Returns the extended part of this <tt>Address</tt>.
   *
   * @return the extended part as <tt>String</tt>.
   */
    public String getExtended();

    /**
   * Sets the extended part of this <tt>Address</tt>.
   *
   * @param extended the extended part as <tt>String</tt>.
   */
    public void setExtended(String extended);

    /**
   * Returns the street of this <tt>Address</tt>.
   *
   * @return the street as <tt>String</tt>.
   */
    public String getStreet();

    /**
   * Sets the street of this <tt>Address</tt>.
   *
   * @param street the street as <tt>String</tt>.
   */
    public void setStreet(String street);

    /**
   * Returns the city of this <tt>Address</tt>.
   *
   * @return the city as <tt>String</tt>.
   */
    public String getCity();

    /**
   * Sets the city of this <tt>Address</tt>.
   *
   * @param city the city as <tt>String</tt>.
   */
    public void setCity(String city);

    /**
   * Returns the region of this <tt>Address</tt>.
   *
   * @return the region as <tt>String</tt>.
   */
    public String getRegion();

    /**
   * Sets the region of this <tt>Address</tt>.
   *
   * @param region the region as <tt>String</tt>.
   */
    public void setRegion(String region);

    /**
   * Returns the postal code of this <tt>Address</tt>.
   *
   * @return the postal code as <tt>String</tt>.
   */
    public String getPostalCode();

    /**
   * Sets the postal code of this <tt>Address</tt>.
   *
   * @param postalcode the postal code as <tt>String</tt>.
   */
    public void setPostalCode(String postalcode);

    /**
   * Returns the country of this <tt>Address</tt>.
   *
   * @return the country as <tt>String</tt>.
   */
    public String getCountry();

    /**
   * Sets the country of this <tt>Address</tt>.
   *
   * @param country as <tt>String</tt>.
   */
    public void setCountry(String country);

    /**
   * Returns the label of this <tt>Address</tt>.
   * The label refers to a formatted text representation
   * of this <tt>Address</tt>.
   *
   * @return the label as <tt>String</tt>.
   */
    public String getLabel();

    /**
   * Sets the label of this <tt>Address</tt>.
   *
   * @param label the label as <tt>String</tt>.
   */
    public void setLabel(String label);

    /**
   * Tests if this <tt>Address</tt> is a domestic
   * delivery address.
   *
   * @return true if for domestic delivery,
   *         false otherwise.
   */
    public boolean isDomestic();

    /**
   * Sets or resets the domestic flag of
   * this <tt>Address</tt>.
   *
   * @param b true if for domestic delivery,
   *        false otherwise.
   */
    public void setDomestic(boolean b);

    /**
   * Tests if this <tt>Address</tt> is international
   * delivery address.
   *
   * @return true if for international delivery,
   *         false otherwise.
   */
    public boolean isInternational();

    /**
   * Sets or resets the international flag of
   * this <tt>Address</tt>.
   *
   * @param b true if for international delivery,
   *        false otherwise.
   */
    public void setInternational(boolean b);

    /**
   * Tests if this <tt>Address</tt> is a parcel
   * delivery address.
   *
   * @return true if for parcel delivery,
   *         false otherwise.
   */
    public boolean isParcel();

    /**
   * Sets or resets the parcel flag of
   * this <tt>Address</tt>.
   *
   * @param b true if for parcel delivery,
   *         false otherwise.
   */
    public void setParcel(boolean b);

    /**
   * Tests if this <tt>Address</tt> is a postal
   * delivery address.
   *
   * @return true if for postal delivery,
   *         false otherwise.
   */
    public boolean isPostal();

    /**
   * Sets or resets the postal flag of
   * this <tt>Address</tt>.
   *
   * @param b true if for postal delivery,
   *         false otherwise.
   */
    public void setPostal(boolean b);

    /**
   * Tests if this <tt>Address</tt> represents a
   * residence.
   *
   * @return true if representing a residence,
   *         false otherwise.
   */
    public boolean isHome();

    /**
   * Sets or resets the home flag of
   * this <tt>Address</tt>.
   *
   * @param b true if representing a residence,
   *         false otherwise.
   */
    public void setHome(boolean b);

    /**
   * Tests if this <tt>Address</tt> represents a
   * place of work.
   *
   * @return true if representing a place of work,
   *         false otherwise.
   */
    public boolean isWork();

    /**
   * Sets or resets the work flag of
   * this <tt>Address</tt>.
   *
   * @param b true if representing a place of work,
   *         false otherwise.
   */
    public void setWork(boolean b);

    /**
   * Tests if this <tt>Address</tt> is
   * of a specific type.
   * Types are defined as constants of this
   * interface.
   *
   * @param TYPE a type as <tt>int</tt>.
   */
    public boolean isType(int TYPE);

    /**
   * Type that indicates an address associated
   * with a work place.
   */
    public static final int TYPE_WORK = 100;

    /**
   * Type that indicates an address associated
   * with a residence.
   */
    public static final int TYPE_HOME = 101;

    /**
   * Type that indicates a domestic delivery address.
   */
    public static final int TYPE_DOMESTIC = 102;

    /**
   * Type that indicates an international delivery address.
   */
    public static final int TYPE_INTERNATIONAL = 103;

    /**
   * Type that indicates a postal delivery address.
   */
    public static final int TYPE_POSTAL = 104;

    /**
   * Type that indicates a parcel delivery address.
   */
    public static final int TYPE_PARCEL = 105;
}

package org.ujac.util.xml;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;
import org.ujac.util.BeanUtils;

/**
 * Name: Person<br>
 * Description: Defines persons.
 * 
 * @author lauerc
 */
public class Person implements Serializable {

    /** The serial version UID. */
    private static final long serialVersionUID = -1741893033237030367L;

    /** The last name. */
    private String name = null;

    /** The fist name. */
    private String firstName = null;

    /** The sex name. */
    private String sex = null;

    /** The date of birth name. */
    private Date dateOfBirth = null;

    /** The place of birth name. */
    private String placeOfBirth = null;

    /** The main address. */
    private Address mainAddress = null;

    /** The address list. */
    private List addresses = null;

    /**
   * Getter method for the the property addresses.
   * @return The current value of property addresses.
   */
    public List getAddresses() {
        return addresses;
    }

    /**
   * Setter method for the the property addresses.
   * @param addresses The value to set for the property addresses.
   */
    public void setAddresses(List addresses) {
        this.addresses = addresses;
    }

    /**
   * Getter method for the the property dateOfBirth.
   * @return The current value of property dateOfBirth.
   */
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    /**
   * Setter method for the the property dateOfBirth.
   * @param dateOfBirth The value to set for the property dateOfBirth.
   */
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
   * Getter method for the the property firstName.
   * @return The current value of property firstName.
   */
    public String getFirstName() {
        return firstName;
    }

    /**
   * Setter method for the the property firstName.
   * @param firstName The value to set for the property firstName.
   */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
   * Getter method for the the property mainAddress.
   * @return The current value of property mainAddress.
   */
    public Address getMainAddress() {
        return mainAddress;
    }

    /**
   * Setter method for the the property mainAddress.
   * @param mainAddress The value to set for the property mainAddress.
   */
    public void setMainAddress(Address mainAddress) {
        this.mainAddress = mainAddress;
    }

    /**
   * Getter method for the the property name.
   * @return The current value of property name.
   */
    public String getName() {
        return name;
    }

    /**
   * Setter method for the the property name.
   * @param name The value to set for the property name.
   */
    public void setName(String name) {
        this.name = name;
    }

    /**
   * Getter method for the the property placeOfBirth.
   * @return The current value of property placeOfBirth.
   */
    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    /**
   * Setter method for the the property placeOfBirth.
   * @param placeOfBirth The value to set for the property placeOfBirth.
   */
    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    /**
   * Getter method for the the property sex.
   * @return The current value of property sex.
   */
    public String getSex() {
        return sex;
    }

    /**
   * Setter method for the the property sex.
   * @param sex The value to set for the property sex.
   */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
    public boolean equals(Object obj) {
        if (!(obj instanceof Person)) {
            return false;
        }
        Person other = (Person) obj;
        return BeanUtils.equals(name, other.name) && BeanUtils.equals(firstName, other.firstName) && BeanUtils.equals(sex, other.sex) && BeanUtils.equals(dateOfBirth, other.dateOfBirth) && BeanUtils.equals(placeOfBirth, other.placeOfBirth) && BeanUtils.equals(mainAddress, other.mainAddress) && BeanUtils.equals(addresses, other.addresses);
    }

    /**
   * @see java.lang.Object#toString()
   */
    public String toString() {
        return "{name='" + name + "', firstName='" + firstName + "', sex='" + sex + "', dateOfBirth='" + dateOfBirth + "', placeOfBirth='" + placeOfBirth + "', mainAddress=" + mainAddress + ", addresses=" + addresses + "}";
    }
}

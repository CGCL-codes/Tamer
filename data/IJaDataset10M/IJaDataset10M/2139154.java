package org.ufind.person;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import org.ufind.util.StringUtil;
import org.ufind.contact.Address;
import org.ufind.contact.EmailAddress;
import org.ufind.contact.PhoneNumber;
import org.ufind.gwt.client.beans.IdentifiableBean;
import org.ufind.gwt.client.beans.OrderedBean;
import org.ufind.gwt.client.person.AccountStatus;

/**
 * The Person class holds the fields linked to a searcher/volunteer.
 * @author Bryce Cottam
 *
 */
public class Person extends IdentifiableBean {

    /**
     * Some unique name for this person, this is usually the (MD5) hash of the full license number.
     */
    private String username = null;

    /**
     * The (MD5) hash of the users password (usually the 2-char state code + full license number):
     */
    private String passwordHash = null;

    /**
     * The LAST 4 digits of the drivers license (for display purposes)
     */
    private String licenseDisplay = null;

    /**
     * The 2 character abbreviation of the state this users drivers license was issued in.
     */
    private String licenseState = null;

    private String firstName = null;

    private String middleName = null;

    private String lastName = null;

    private Date birthDate = null;

    private Date createDate = new Date();

    private SortedSet<Address> addresses = new TreeSet<Address>();

    private SortedSet<EmailAddress> emailAddresses = new TreeSet<EmailAddress>();

    private SortedSet<PhoneNumber> phoneNumbers = new TreeSet<PhoneNumber>();

    private AccountStatus accountStatus = null;

    public Person() {
    }

    public Person(Long id) {
        setId(id);
    }

    /**
     * Returns the value of username.  NOTE: this value is usually a non-displayable (MD5) hash code.
     * @return
     */
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        if (StringUtil.isEmpty(username)) {
            throw new IllegalArgumentException("username cannot be empty.");
        }
        this.username = username;
    }

    /**
     * Returns the (MD5) hash of the users full drivers license number.
     * @return
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Sets this users (MD5) hash of the drivers license.
     * This method is intentionally private as it is only to be called
     * by an ORM solution.
     * @param licenseMD5
     */
    private void setPasswordHash(String passwordHash) {
        if (StringUtil.isEmpty(passwordHash)) {
            throw new IllegalArgumentException("passwordHash cannot be empty");
        }
        this.passwordHash = passwordHash;
    }

    /**
     * Returns the 2 character abbreviation of the state the drivers license was issued in.
     * @return
     */
    public String getLicenseState() {
        return licenseState;
    }

    /**
     * Sest the 2 character abbreviation of the state the drivers license was issued in.
     * @param licenseState
     */
    public void setLicenseState(String licenseState) {
        if (StringUtil.isEmpty(licenseState)) {
            throw new IllegalArgumentException("licenseState cannot be empty.");
        }
        this.licenseState = licenseState;
    }

    /**
     * Returns the last 4 digits of the users drivers license number.
     * @return
     */
    public String getLicenseDisplay() {
        return licenseDisplay;
    }

    /**
     * 
     * @param licenseNumber
     */
    protected void setLicenseDisplay(String licenseDisplay) {
        if (StringUtil.isEmpty(licenseDisplay)) {
            throw new IllegalArgumentException("licenseDisplay cannot be empty.");
        }
        this.licenseDisplay = licenseDisplay;
    }

    /**
     * Sets the full license number for this person.  This value is not stored on this person object,
     * instead the hash is computed and setLicenseMD5 is called.
     * @param licenseNumber
     */
    public void setFullLicenseNumber(String licenseNumber) {
        if (StringUtil.isEmpty(licenseNumber)) {
            throw new IllegalArgumentException("fullLicenseNumber cannot be empty");
        }
        if (licenseNumber.length() < 4) {
            throw new IllegalArgumentException("fullLicenseNumber must be at least 4 characters long");
        }
        setPasswordHash(StringUtil.computeHash(licenseNumber));
        setLicenseDisplay(licenseNumber.substring(licenseNumber.length() - 4));
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        if (StringUtil.isEmpty(firstName)) {
            throw new IllegalArgumentException("firstName cannot be empty.");
        }
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        if (StringUtil.isEmpty(lastName)) {
            throw new IllegalArgumentException("lastName cannot be empty.");
        }
        this.lastName = lastName;
    }

    /**
     * Returns the birthDate for this Person.
     * @return
     */
    public Date getBirthDate() {
        return birthDate;
    }

    /**
     * Sets the birthDate for this Person.  Note, if a null value is supplied an IllegalArgumentException is raised.
     * @param birthDate
     */
    public void setBirthDate(Date birthDate) {
        if (birthDate == null) {
            throw new IllegalArgumentException("birthDate cannot be null.");
        }
        this.birthDate = new Date(birthDate.getTime());
    }

    /**
     * Returns the Date this Person object was created.
     * @return
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * Sets the createDate for this Person.  This is private because it is intended to only be used by the ORM solution.
     * @param createDate
     */
    private void setCreateDate(Date createDate) {
        if (createDate == null) {
            throw new IllegalArgumentException("createDate cannot be null.");
        }
        this.createDate = new Date(createDate.getTime());
    }

    /**
     * Adds the supplied Address to the end of the set of Addresses managed by this Person.
     * @param address
     */
    public void addEmailAddress(Address address) {
        int displayOrder = OrderedBean.getMaxDisplayOrder(addresses);
        address.setDisplayOrder(displayOrder);
        addresses.add(address);
    }

    /**
     * Returns true if the internal addresses collection contains the supplied Address
     * and removal of that address was a success.
     * @param address
     * @return
     */
    public boolean removeAddress(Address address) {
        return addresses.remove(address);
    }

    /**
     *  TODO: implement something like this for each of the collections: Phone, Email, Address.
     *  and do it more elegantly than this  :)
     */
    public Address getPrimaryAddress() {
        SortedSet<Address> set = getAddresses();
        return set.isEmpty() ? null : set.first();
    }

    /**
     * Returns all the users addresses.
     * NOTE: this method should *not* be used to add elements to the underlying collection.
     * @return
     */
    public SortedSet<Address> getAddresses() {
        return addresses;
    }

    /**
     * Sets the address for this Person.  Note: we may want to make this a collection as well.
     * @param address
     */
    private void setAddresses(SortedSet<Address> addresses) {
        if (addresses == null) {
            throw new IllegalArgumentException("addresses cannot be null.");
        }
        this.addresses = addresses;
    }

    /**
     * Adds the supplied EmailAddress to the end of the set of EmailAddresses managed by this Person.
     * @param email
     */
    public void addEmailAddress(EmailAddress email) {
        int displayOrder = OrderedBean.getMaxDisplayOrder(emailAddresses);
        email.setDisplayOrder(displayOrder);
        emailAddresses.add(email);
    }

    /**
     * Returns true if the internal emailAddresses collection contains the supplied EmailAddress
     * and removal of that email was a success.
     * @param email
     * @return
     */
    public boolean removeEmailAddress(EmailAddress email) {
        return emailAddresses.remove(email);
    }

    /**
     * Returns the first EmailAddress in the emailAddresses collection
     * or null if no EmailAddresses have been set on this Person.
     * @return
     */
    public EmailAddress getPrimaryEmailAddress() {
        SortedSet<EmailAddress> set = getEmailAddresses();
        return set.isEmpty() ? null : set.first();
    }

    /**
     * Returns the list of EmailAddresses applied to this person.
     * NOTE: this method should *not* be used to add elements to the underlying collection.
     * @return
     */
    public SortedSet<EmailAddress> getEmailAddresses() {
        return emailAddresses;
    }

    /**
     * Sets the list of EmailAddress objects for this Person.  If a null List is supplied,
     * an IllegalArgumentException is thrown.
     * @param phoneNumbers
     */
    private void setEmailAddresses(SortedSet<EmailAddress> emailAddresses) {
        if (emailAddresses == null) {
            throw new IllegalArgumentException("emailAddresses cannot be null.");
        }
        this.emailAddresses = emailAddresses;
    }

    /**
     * Returns the first PhoneNumber in the phoneNumbers collection
     * or null if no PhoneNumbers have been set on this Person.
     * @return
     */
    public PhoneNumber getPrimaryPhoneNumber() {
        SortedSet<PhoneNumber> set = getPhoneNumbers();
        return set.isEmpty() ? null : set.first();
    }

    /**
     * Returns the list of PhoneNumbers applied to this person.
     * NOTE: this method should *not* be used to add elements to the underlying collection.
     * @return
     */
    public SortedSet<PhoneNumber> getPhoneNumbers() {
        return phoneNumbers;
    }

    /**
     * Sets the list of PhoneNumber objects for this Person.  If a null List is supplied,
     * an IllegalArgumentException is thrown.
     * @param phoneNumbers
     */
    private void setPhoneNumbers(SortedSet<PhoneNumber> phoneNumbers) {
        if (phoneNumbers == null) {
            throw new IllegalArgumentException("phoneNumbers cannot be null.");
        }
        this.phoneNumbers = phoneNumbers;
    }

    /**
     * Adds the supplied PhoneNumber to this Persons internal collection of PhoneNumbers.
     * @param number
     */
    public void addPhoneNumber(PhoneNumber number) {
        number.setDisplayOrder(OrderedBean.getMaxDisplayOrder(phoneNumbers) + 1);
        phoneNumbers.add(number);
    }

    /**
     * Returns true if the internal phoneNumbers collection contains the supplied PhoneNumber
     * and removal of that number was a success.
     * @param number
     * @return
     */
    public boolean removePhoneNumber(PhoneNumber number) {
        return phoneNumbers.remove(number);
    }

    /**
     * Returns the current AccountStatus applied to this person.
     * @return
     */
    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    /**
     * Sets the accountStatus for this Person object.  If null is supplied an IllegalArgumentException is thrown.
     * @param accountStatus
     */
    public void setAccountStatus(AccountStatus accountStatus) {
        if (accountStatus == null) {
            throw new IllegalArgumentException("accountStatus cannot be null.");
        }
        this.accountStatus = accountStatus;
    }

    private Long getAccountStatusId() {
        return accountStatus.getId();
    }

    private void setAccountStatusId(Long id) {
        setAccountStatus(AccountStatus.getById(id));
    }
}

package com.cti.product;

import java.util.*;
import org.jlf.dataMap.*;

/**
 * This class holds instances of customers from the CTI
 * Product database.
 *
 * @version: $Revision: 1.2 $
 * @author:  Todd Lauinger
 *
 * @see Order
 */
public class Customer extends DataMappedObject {

    public static final String ID_ATTRIBUTE = "customerId";

    public static final String NAME_ATTRIBUTE = "name";

    public static final String PHONE_ATTRIBUTE = "phone";

    public static final String ADDRESS_LINE_1_ATTRIBUTE = "addressLine1";

    public static final String ADDRESS_LINE_2_ATTRIBUTE = "addressLine2";

    public static final String CITY_ATTRIBUTE = "city";

    public static final String STATE_ATTRIBUTE = "state";

    public static final String ZIP_CODE_ATTRIBUTE = "zipCode";

    public static final String EMAIL_ADDRESS_ATTRIBUTE = "emailAddress";

    public static final String CREDIT_CARD_NUMBER_ATTRIBUTE = "creditCardNumber";

    public static final String CREDIT_CARD_TYPE_ATTRIBUTE = "creditCardType";

    public static final String CREDIT_CARD_EXPIRATION_ATTRIBUTE = "creditCardExpiration";

    public static final String LOCKING_TIMESTAMP_ATTRIBUTE = "lockingTimestamp";

    public static final String ORDERS_RELATIONSHIP = "orders";

    /**
     * The default constructor is needed by the framework,
     * however, new Customers should NOT use this, please use
     * a parameterized constructor!
     */
    public Customer() {
    }

    /**
     * Tries to find a customer by its primary key, its id.
     * Returns the customer if found in the database, null if not.
     */
    public static Customer findById(long id) {
        Customer customer = new Customer();
        customer.setId(id);
        return (Customer) customer.findByPrimaryKey();
    }

    /**
     * Creates a new Customer given that customer's name.
     */
    public Customer(String name) {
        this();
        setName(name);
    }

    /**
     * Sets up a table of attribute descriptors for the Customer
     * object.
     */
    protected Hashtable basicGetAttributeDescriptors() {
        Hashtable descriptors = super.basicGetAttributeDescriptors();
        DataAttributeDescriptor descriptor;
        descriptor = new DataAttributeDescriptor(ID_ATTRIBUTE, LongAttribute.class, true);
        descriptor.setIsKeyField(true);
        descriptors.put(ID_ATTRIBUTE, descriptor);
        descriptor = new DataAttributeDescriptor(NAME_ATTRIBUTE, StringAttribute.class, false);
        descriptor.setMaximumLength(50);
        descriptors.put(NAME_ATTRIBUTE, descriptor);
        descriptor = new DataAttributeDescriptor(PHONE_ATTRIBUTE, StringAttribute.class, true);
        descriptor.setMaximumLength(20);
        descriptors.put(PHONE_ATTRIBUTE, descriptor);
        descriptor = new DataAttributeDescriptor(ADDRESS_LINE_1_ATTRIBUTE, StringAttribute.class, true);
        descriptor.setMaximumLength(100);
        descriptors.put(ADDRESS_LINE_1_ATTRIBUTE, descriptor);
        descriptor = new DataAttributeDescriptor(ADDRESS_LINE_2_ATTRIBUTE, StringAttribute.class, true);
        descriptor.setMaximumLength(100);
        descriptors.put(ADDRESS_LINE_2_ATTRIBUTE, descriptor);
        descriptor = new DataAttributeDescriptor(CITY_ATTRIBUTE, StringAttribute.class, true);
        descriptor.setMaximumLength(50);
        descriptors.put(CITY_ATTRIBUTE, descriptor);
        descriptor = new DataAttributeDescriptor(STATE_ATTRIBUTE, StringAttribute.class, true);
        descriptor.setMaximumLength(2);
        descriptors.put(STATE_ATTRIBUTE, descriptor);
        descriptor = new DataAttributeDescriptor(ZIP_CODE_ATTRIBUTE, StringAttribute.class, true);
        descriptor.setMaximumLength(10);
        descriptors.put(ZIP_CODE_ATTRIBUTE, descriptor);
        descriptor = new DataAttributeDescriptor(EMAIL_ADDRESS_ATTRIBUTE, StringAttribute.class, true);
        descriptor.setMaximumLength(100);
        descriptors.put(EMAIL_ADDRESS_ATTRIBUTE, descriptor);
        descriptor = new DataAttributeDescriptor(CREDIT_CARD_NUMBER_ATTRIBUTE, StringAttribute.class, true);
        descriptor.setMaximumLength(20);
        descriptors.put(CREDIT_CARD_NUMBER_ATTRIBUTE, descriptor);
        descriptor = new DataAttributeDescriptor(CREDIT_CARD_TYPE_ATTRIBUTE, StringAttribute.class, true);
        descriptor.setMaximumLength(20);
        descriptors.put(CREDIT_CARD_TYPE_ATTRIBUTE, descriptor);
        descriptor = new DataAttributeDescriptor(CREDIT_CARD_EXPIRATION_ATTRIBUTE, StringAttribute.class, true);
        descriptor.setMaximumLength(20);
        descriptors.put(CREDIT_CARD_EXPIRATION_ATTRIBUTE, descriptor);
        descriptor = new DataAttributeDescriptor(LOCKING_TIMESTAMP_ATTRIBUTE, DateAttribute.class, true);
        descriptors.put(LOCKING_TIMESTAMP_ATTRIBUTE, descriptor);
        return descriptors;
    }

    /**
     * Sets up the relationship from a customer to any orders it has
     */
    protected Hashtable basicGetRelationshipDescriptors() {
        Hashtable descriptors = super.basicGetRelationshipDescriptors();
        descriptors.put(ORDERS_RELATIONSHIP, new RelationshipDescriptor(ORDERS_RELATIONSHIP, Order.class));
        return descriptors;
    }

    /**
     * Retrieves the Customer's name.
     */
    public long getId() {
        Long id = (Long) getAttributeValue(ID_ATTRIBUTE);
        if (id == null) return 0; else return id.longValue();
    }

    /**
     * Sets the customer's id.
     */
    protected void setId(long id) {
        setLongAttribute(ID_ATTRIBUTE, id);
    }

    /**
     * Retrieves the Customer's name.
     */
    public String getName() {
        return getStringAttribute(NAME_ATTRIBUTE);
    }

    /**
     * Sets the Customer's name.
     */
    public void setName(String name) {
        setStringAttribute(NAME_ATTRIBUTE, name);
    }

    /**
     * Retrieves the Customer's phone.
     */
    public String getPhone() {
        return getStringAttribute(PHONE_ATTRIBUTE);
    }

    /**
     * Sets the Customer's phone.
     */
    public void setPhone(String phone) {
        setStringAttribute(PHONE_ATTRIBUTE, phone);
    }

    /**
     * Retrieves the Customer's address line 1.
     */
    public String getAddressLine1() {
        return getStringAttribute(ADDRESS_LINE_1_ATTRIBUTE);
    }

    /**
     * Sets the Customer's address.
     */
    public void setAddressLine1(String address) {
        setStringAttribute(ADDRESS_LINE_1_ATTRIBUTE, address);
    }

    /**
     * Retrieves the Customer's address line 2.
     */
    public String getAddressLine2() {
        return getStringAttribute(ADDRESS_LINE_2_ATTRIBUTE);
    }

    /**
     * Sets the Customer's address.
     */
    public void setAddressLine2(String address) {
        setStringAttribute(ADDRESS_LINE_2_ATTRIBUTE, address);
    }

    /**
     * Retrieves the Customer's city.
     */
    public String getCity() {
        return getStringAttribute(CITY_ATTRIBUTE);
    }

    /**
     * Sets the Customer's city.
     */
    public void setCity(String city) {
        setStringAttribute(CITY_ATTRIBUTE, city);
    }

    /**
     * Retrieves the Customer's state.
     */
    public String getState() {
        return getStringAttribute(STATE_ATTRIBUTE);
    }

    /**
     * Sets the Customer's state.
     */
    public void setState(String state) {
        setStringAttribute(STATE_ATTRIBUTE, state);
    }

    /**
     * Retrieves the Customer's zip code.
     */
    public String getZipCode() {
        return getStringAttribute(ZIP_CODE_ATTRIBUTE);
    }

    /**
     * Sets the Customer's zip code.
     */
    public void setZipCode(String zipCode) {
        setStringAttribute(ZIP_CODE_ATTRIBUTE, zipCode);
    }

    /**
     * Retrieves the Customer's email address.
     */
    public String getEmailAddress() {
        return getStringAttribute(EMAIL_ADDRESS_ATTRIBUTE);
    }

    /**
     * Sets the Customer's email address.
     */
    public void setEmailAddress(String emailAddress) {
        setStringAttribute(EMAIL_ADDRESS_ATTRIBUTE, emailAddress);
    }

    /**
     * Retrieves the Customer's name.
     */
    public String getCreditCardNumber() {
        return getStringAttribute(CREDIT_CARD_NUMBER_ATTRIBUTE);
    }

    /**
     * Sets the Customer's name.
     */
    public void setCreditCardNumber(String creditCardNumber) {
        setStringAttribute(CREDIT_CARD_NUMBER_ATTRIBUTE, creditCardNumber);
    }

    /**
     * Retrieves the Customer's credit card type.
     */
    public String getCreditCardType() {
        return getStringAttribute(CREDIT_CARD_TYPE_ATTRIBUTE);
    }

    /**
     * Sets the Customer's name.
     */
    public void setCreditCardType(String type) {
        setStringAttribute(CREDIT_CARD_TYPE_ATTRIBUTE, type);
    }

    /**
     * Retrieves the Customer's credit card expiration.
     */
    public String getCreditCardExpiration() {
        return getStringAttribute(CREDIT_CARD_EXPIRATION_ATTRIBUTE);
    }

    /**
     * Sets the Customer's name.
     */
    public void setCreditCardExpiration(String expiration) {
        setStringAttribute(CREDIT_CARD_EXPIRATION_ATTRIBUTE, expiration);
    }

    /**
     * Retrieves any orders for this customer.
     */
    public Vector getOrders() {
        return getRelatedObjects(ORDERS_RELATIONSHIP);
    }

    /**
     * Prints out a information about the Customer.
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Customer Info:\n");
        if (getAttributeValue(ID_ATTRIBUTE) != null) {
            sb.append("  ID: " + getId() + "\n");
        }
        if (getName() != null) {
            sb.append("  Name: " + getName() + "\n");
        }
        sb.append("  Address: ");
        if (getAddressLine1() != null) {
            sb.append(getAddressLine1());
            sb.append("\n           ");
        }
        if (getAddressLine2() != null) {
            sb.append(getAddressLine2());
        }
        sb.append("\n           ");
        if (getCity() != null) {
            sb.append(getCity() + ", ");
        }
        if (getState() != null) {
            sb.append(getState() + "  ");
        }
        if (getZipCode() != null) {
            sb.append(getZipCode());
        }
        return sb.toString();
    }
}

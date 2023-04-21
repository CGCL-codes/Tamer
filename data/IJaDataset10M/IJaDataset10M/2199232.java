package org.agilercp.demo.contacts;

/**
 * @author Heiko Seeberger
 */
public class Person {

    private Address address = new Address();

    private ContactInfo contactInfo = new ContactInfo();

    private String firstName = "";

    private String lastName = "";

    public Person() {
    }

    public Person(final String firstName, final String lastName) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Person(final String firstName, final String lastName, final Address address, final ContactInfo contactInfo) {
        super();
        setFirstName(firstName);
        setLastName(lastName);
        setAddress(address);
        setContactInfo(contactInfo);
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object arg0) {
        if (arg0 instanceof Person) {
            final Person other = (Person) arg0;
            return firstName.equals(other.getFirstName()) && lastName.equals(other.getLastName());
        }
        return super.equals(arg0);
    }

    public Address getAddress() {
        return address;
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (firstName + lastName).hashCode();
    }

    public void setAddress(final Address address) {
        if (address == null) {
            throw new IllegalArgumentException("The given address must not be null!");
        }
        this.address = address;
    }

    public void setContactInfo(final ContactInfo contactInfo) {
        if (contactInfo == null) {
            throw new IllegalArgumentException("The given contact information must not be null!");
        }
        this.contactInfo = contactInfo;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }
}

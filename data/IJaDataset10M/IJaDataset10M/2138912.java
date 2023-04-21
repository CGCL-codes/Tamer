package be.hoornaert.tom.blackbelt.jpa.ebay.entities;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import be.hoornaert.tom.blackbelt.jpa.ebay.enums.Gender;
import be.hoornaert.tom.blackbelt.jpa.ebay.enums.Salutation;

/**
 * @author tom.hoornaert
 * 
 */
@Entity
public class Contact {

    @Id
    @GeneratedValue
    private Long id;

    private Gender gender;

    private Salutation salutation;

    @Column(unique = true, nullable = false)
    private String userId;

    private String firstName;

    private String lastName;

    private String email;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "SHIPPING_ADDRESS_ID")
    private Address shippingAddress;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "BILLING_ADDRESS_ID")
    private Address billingAddress;

    @ManyToMany
    @JoinTable(name = "METHOD_CONTACT", joinColumns = { @JoinColumn(name = "CONTACT_ID") }, inverseJoinColumns = { @JoinColumn(name = "METHOD_ID") })
    private Set<PaymentMethod> paymentMethods = new HashSet<PaymentMethod>();

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private Set<EBayItem> items = new HashSet<EBayItem>();

    /**
     * Default constructor.
     * 
     * @author tom.hoornaert
     */
    private Contact() {
        super();
    }

    public Contact(String userId) {
        super();
        this.userId = userId;
    }

    /**
     * Default constructor.
     * 
     * @param userId
     * @param gender
     * @param salutation
     * @param firstName
     * @param lastName
     * @param email
     * @author tom.hoornaert
     */
    public Contact(String userId, Gender gender, Salutation salutation, String firstName, String lastName, String email) {
        super();
        this.gender = gender;
        this.salutation = salutation;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    /**
     * @param methods
     * @author tom.hoornaert
     */
    public void addPaymentMethods(PaymentMethod... methods) {
        for (PaymentMethod paymentMethod : methods) {
            paymentMethods.add(paymentMethod);
        }
    }

    /**
     * getter for the {@link #id}.
     * 
     * @return the current value of the field.
     * @author tom.hoornaert
     */
    public Long getId() {
        return id;
    }

    /**
     * setter for the {@link #id}.
     * 
     * @param id
     *            the new value to set.
     * @author tom.hoornaert
     */
    private void setId(Long id) {
        this.id = id;
    }

    /**
     * getter for the {@link #gender}.
     * 
     * @return the current value of the field.
     * @author tom.hoornaert
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * setter for the {@link #gender}.
     * 
     * @param gender
     *            the new value to set.
     * @author tom.hoornaert
     */
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    /**
     * getter for the {@link #salutation}.
     * 
     * @return the current value of the field.
     * @author tom.hoornaert
     */
    public Salutation getSalutation() {
        return salutation;
    }

    /**
     * setter for the {@link #salutation}.
     * 
     * @param salutation
     *            the new value to set.
     * @author tom.hoornaert
     */
    public void setSalutation(Salutation salutation) {
        this.salutation = salutation;
    }

    /**
     * getter for the {@link #userId}.
     * 
     * @return the current value of the field.
     * @author tom.hoornaert
     */
    public String getUserId() {
        return userId;
    }

    /**
     * setter for the {@link #userId}.
     * 
     * @param userId
     *            the new value to set.
     * @author tom.hoornaert
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * getter for the {@link #firstName}.
     * 
     * @return the current value of the field.
     * @author tom.hoornaert
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * setter for the {@link #firstName}.
     * 
     * @param firstName
     *            the new value to set.
     * @author tom.hoornaert
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * getter for the {@link #lastName}.
     * 
     * @return the current value of the field.
     * @author tom.hoornaert
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * setter for the {@link #lastName}.
     * 
     * @param lastName
     *            the new value to set.
     * @author tom.hoornaert
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * getter for the {@link #email}.
     * 
     * @return the current value of the field.
     * @author tom.hoornaert
     */
    public String getEmail() {
        return email;
    }

    /**
     * setter for the {@link #email}.
     * 
     * @param email
     *            the new value to set.
     * @author tom.hoornaert
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * getter for the {@link #shippingAddress}.
     * 
     * @return the current value of the field.
     * @author tom.hoornaert
     */
    public Address getShippingAddress() {
        return shippingAddress;
    }

    /**
     * setter for the {@link #shippingAddress}.
     * 
     * @param shippingAddress
     *            the new value to set.
     * @author tom.hoornaert
     */
    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    /**
     * getter for the {@link #billingAddress}.
     * 
     * @return the current value of the field.
     * @author tom.hoornaert
     */
    public Address getBillingAddress() {
        return billingAddress;
    }

    /**
     * setter for the {@link #billingAddress}.
     * 
     * @param billingAddress
     *            the new value to set.
     * @author tom.hoornaert
     */
    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

    /**
     * getter for the {@link #paymentMethods}.
     * 
     * @return the current value of the field.
     * @author tom.hoornaert
     */
    public Set<PaymentMethod> getPaymentMethods() {
        return paymentMethods;
    }

    /**
     * setter for the {@link #paymentMethods}.
     * 
     * @param paymentMethods
     *            the new value to set.
     * @author tom.hoornaert
     */
    public void setPaymentMethods(Set<PaymentMethod> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    /**
     * getter for the {@link #items}.
     * 
     * @return the current value of the field.
     * @author tom.hoornaert
     */
    public Set<EBayItem> getItems() {
        return items;
    }

    /**
     * setter for the {@link #items}.
     * 
     * @param items
     *            the new value to set.
     * @author tom.hoornaert
     */
    private void setItems(Set<EBayItem> items) {
        this.items = items;
    }

    /**
     * {@inheritDoc}
     * 
     * @author tom.hoornaert
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @author tom.hoornaert
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Contact other = (Contact) obj;
        if (userId == null) {
            if (other.userId != null) return false;
        } else if (!userId.equals(other.userId)) return false;
        return true;
    }
}

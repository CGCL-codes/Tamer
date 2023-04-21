package domfarr.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@Entity
@Table(name = "ADDRESSES")
public class Address {

    private static final long serialVersionUID = 1L;

    private String addressLine1;

    private String addressLine2;

    private String city;

    private String country;

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    private String state;

    private AddressType type;

    private String zipCode;

    public Address() {
    }

    public Address(AddressType type, String addressLine1, String addressLine2, String city, String country, String state, String zipCode) {
        this.type = type;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.country = country;
        this.state = state;
        this.zipCode = zipCode;
    }

    @Override
    public boolean equals(Object rhs) {
        return EqualsBuilder.reflectionEquals(this, rhs);
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public Long getId() {
        return id;
    }

    public String getPostalCode() {
        return zipCode;
    }

    public String getState() {
        return state;
    }

    public AddressType getType() {
        return type;
    }

    public String getZipCode() {
        return zipCode;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPostalCode(String postalCode) {
        this.zipCode = postalCode;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setType(AddressType type) {
        this.type = type;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}

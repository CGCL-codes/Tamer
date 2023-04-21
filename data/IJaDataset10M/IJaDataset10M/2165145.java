package com.spring.hibernate.addressbook.model.obj.addressbook;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/** 
 * Object mapping for hibernate-handled table: addresses
 * author: auto-generated
 */
@Entity
@Table(name = "addresses", catalog = "addressbook")
public class Addresses implements Cloneable, Serializable {

    /** Serial Version UID */
    private static final long serialVersionUID = -559039810L;

    /** Field mapping */
    private String addressComment;

    /** Field mapping */
    private Integer id;

    /** Field mapping */
    private String street;

    /** Field mapping */
    private String streetnumber;

    /** Field mapping */
    private Zipcodes zipcode;

    /**
	 * Default constructor, mainly for hibernate use
	 */
    public Addresses() {
    }

    /** Constructor taking a given ID
	 * @param id to set
	 */
    public Addresses(Integer id) {
        this.id = id;
    }

    /**
     * Return the value associated with the column: addressComment
	 * @return A String object (this.addressComment)
	 */
    @Column(name = "address_comment", length = 100)
    public String getAddressComment() {
        return this.addressComment;
    }

    /**  
     * Set the value related to the column: addressComment 
	 * @param addressComment the addressComment value you wish to set
	 */
    public void setAddressComment(final String addressComment) {
        this.addressComment = addressComment;
    }

    /**
     * Return the value associated with the column: id
	 * @return A Integer object (this.id)
	 */
    @Id
    @Column(name = "id", nullable = false)
    public Integer getId() {
        return this.id;
    }

    /**  
     * Set the value related to the column: id 
	 * @param id the id value you wish to set
	 */
    public void setId(final Integer id) {
        this.id = id;
    }

    /**
     * Return the value associated with the column: street
	 * @return A String object (this.street)
	 */
    @Column(length = 64)
    public String getStreet() {
        return this.street;
    }

    /**  
     * Set the value related to the column: street 
	 * @param street the street value you wish to set
	 */
    public void setStreet(final String street) {
        this.street = street;
    }

    /**
     * Return the value associated with the column: streetnumber
	 * @return A String object (this.streetnumber)
	 */
    @Column(length = 5)
    public String getStreetnumber() {
        return this.streetnumber;
    }

    /**  
     * Set the value related to the column: streetnumber 
	 * @param streetnumber the streetnumber value you wish to set
	 */
    public void setStreetnumber(final String streetnumber) {
        this.streetnumber = streetnumber;
    }

    /**
     * Return the value associated with the column: zipcode
	 * @return A Zipcodes object (this.zipcode)
	 */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "zipcode_id", nullable = true)
    public Zipcodes getZipcode() {
        return this.zipcode;
    }

    /**  
     * Set the value related to the column: zipcode 
	 * @param zipcode the zipcode value you wish to set
	 */
    public void setZipcode(final Zipcodes zipcode) {
        this.zipcode = zipcode;
    }

    /**
     * Deep copy
	 * @throws CloneNotSupportedException
     */
    @Override
    public Addresses clone() throws CloneNotSupportedException {
        super.clone();
        Addresses copy = new Addresses();
        copy.setAddressComment(this.getAddressComment());
        copy.setId(this.getId());
        copy.setStreet(this.getStreet());
        copy.setStreetnumber(this.getStreetnumber());
        copy.setZipcode(this.getZipcode());
        return copy;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("addressComment: " + this.addressComment + ", ");
        sb.append("id: " + this.id + ", ");
        sb.append("street: " + this.street + ", ");
        sb.append("streetnumber: " + this.streetnumber + ", ");
        return sb.toString();
    }

    @Override
    public boolean equals(Object aThat) {
        if (this == aThat) return true;
        if ((aThat == null) || (!(aThat.getClass().equals(Addresses.class)))) return false;
        Addresses that = (Addresses) aThat;
        return (((this.addressComment == null) && (that.addressComment == null)) || (this.addressComment != null && this.addressComment.equals(that.addressComment))) && (((this.id == null) && (that.id == null)) || (this.id != null && this.id.equals(that.id))) && (((this.street == null) && (that.street == null)) || (this.street != null && this.street.equals(that.street))) && (((this.streetnumber == null) && (that.streetnumber == null)) || (this.streetnumber != null && this.streetnumber.equals(that.streetnumber))) && (((this.zipcode == null) && (that.zipcode == null)) || (this.zipcode != null && this.zipcode.equals(that.zipcode)));
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 1000003 * result + (this.addressComment == null ? 0 : this.addressComment.hashCode());
        result = 1000003 * result + (this.id == null ? 0 : this.id.hashCode());
        result = 1000003 * result + (this.street == null ? 0 : this.street.hashCode());
        result = 1000003 * result + (this.streetnumber == null ? 0 : this.streetnumber.hashCode());
        result = 1000003 * result + (this.zipcode == null ? 0 : this.zipcode.hashCode());
        return result;
    }
}

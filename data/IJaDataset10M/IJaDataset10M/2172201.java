package com.spring.hibernate.Seminars.model.obj.addressbook;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
    private Integer zipcodeId;

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
     * Return the value associated with the column: zipcodeId
	 * @return A Integer object (this.zipcodeId)
	 */
    @Column(name = "zipcode_id")
    public Integer getZipcodeId() {
        return this.zipcodeId;
    }

    /**  
     * Set the value related to the column: zipcodeId 
	 * @param zipcodeId the zipcodeId value you wish to set
	 */
    public void setZipcodeId(final Integer zipcodeId) {
        this.zipcodeId = zipcodeId;
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
        copy.setZipcodeId(this.getZipcodeId());
        return copy;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("addressComment: " + this.addressComment + ", ");
        sb.append("id: " + this.id + ", ");
        sb.append("street: " + this.street + ", ");
        sb.append("streetnumber: " + this.streetnumber + ", ");
        sb.append("zipcodeId: " + this.zipcodeId);
        return sb.toString();
    }

    @Override
    public boolean equals(Object aThat) {
        if (this == aThat) return true;
        if ((aThat == null) || (!(aThat.getClass().equals(Addresses.class)))) return false;
        Addresses that = (Addresses) aThat;
        return (((this.addressComment == null) && (that.addressComment == null)) || (this.addressComment != null && this.addressComment.equals(that.addressComment))) && (((this.id == null) && (that.id == null)) || (this.id != null && this.id.equals(that.id))) && (((this.street == null) && (that.street == null)) || (this.street != null && this.street.equals(that.street))) && (((this.streetnumber == null) && (that.streetnumber == null)) || (this.streetnumber != null && this.streetnumber.equals(that.streetnumber))) && (((this.zipcodeId == null) && (that.zipcodeId == null)) || (this.zipcodeId != null && this.zipcodeId.equals(that.zipcodeId)));
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 1000003 * result + (this.addressComment == null ? 0 : this.addressComment.hashCode());
        result = 1000003 * result + (this.id == null ? 0 : this.id.hashCode());
        result = 1000003 * result + (this.street == null ? 0 : this.street.hashCode());
        result = 1000003 * result + (this.streetnumber == null ? 0 : this.streetnumber.hashCode());
        result = 1000003 * result + (this.zipcodeId == null ? 0 : this.zipcodeId.hashCode());
        return result;
    }
}

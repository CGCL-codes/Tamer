package com.spring.hibernate.addressbook.model.obj.addressbook;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/** 
 * Object mapping for hibernate-handled table: contact_methods
 * author: auto-generated
 */
@Entity
@Table(name = "contact_methods", catalog = "addressbook")
public class ContactMethods implements Cloneable, Serializable {

    /** Serial Version UID */
    private static final long serialVersionUID = -559039808L;

    /** Field mapping */
    private Integer id;

    /** Field mapping */
    private String method;

    /**
	 * Default constructor, mainly for hibernate use
	 */
    public ContactMethods() {
    }

    /** Constructor taking a given ID
	 * @param id to set
	 */
    public ContactMethods(Integer id) {
        this.id = id;
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
     * Return the value associated with the column: method
	 * @return A String object (this.method)
	 */
    @Column(length = 40)
    public String getMethod() {
        return this.method;
    }

    /**  
     * Set the value related to the column: method 
	 * @param method the method value you wish to set
	 */
    public void setMethod(final String method) {
        this.method = method;
    }

    /**
     * Deep copy
	 * @throws CloneNotSupportedException
     */
    @Override
    public ContactMethods clone() throws CloneNotSupportedException {
        super.clone();
        ContactMethods copy = new ContactMethods();
        copy.setId(this.getId());
        copy.setMethod(this.getMethod());
        return copy;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("id: " + this.id + ", ");
        sb.append("method: " + this.method);
        return sb.toString();
    }

    @Override
    public boolean equals(Object aThat) {
        if (this == aThat) return true;
        if ((aThat == null) || (!(aThat.getClass().equals(ContactMethods.class)))) return false;
        ContactMethods that = (ContactMethods) aThat;
        return (((this.id == null) && (that.id == null)) || (this.id != null && this.id.equals(that.id))) && (((this.method == null) && (that.method == null)) || (this.method != null && this.method.equals(that.method)));
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 1000003 * result + (this.id == null ? 0 : this.id.hashCode());
        result = 1000003 * result + (this.method == null ? 0 : this.method.hashCode());
        return result;
    }
}

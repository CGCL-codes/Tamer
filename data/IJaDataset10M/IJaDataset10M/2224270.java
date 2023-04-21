package com.useme.model.base;

import java.io.Serializable;

/**
 * This is an object that contains data related to the customer table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="customer"
 */
public abstract class BaseCustomer implements Serializable {

    public static String REF = "Customer";

    public static String PROP_NAME = "Name";

    public static String PROP_ID = "Id";

    public BaseCustomer() {
        initialize();
    }

    /**
	 * Constructor for primary key
	 */
    public BaseCustomer(java.lang.Integer id) {
        this.setId(id);
        initialize();
    }

    protected void initialize() {
    }

    private int hashCode = Integer.MIN_VALUE;

    private java.lang.Integer id;

    private java.lang.String name;

    /**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="sequence"
     *  column="id"
     */
    public java.lang.Integer getId() {
        return id;
    }

    /**
	 * Set the unique identifier of this class
	 * @param id the new ID
	 */
    public void setId(java.lang.Integer id) {
        this.id = id;
        this.hashCode = Integer.MIN_VALUE;
    }

    /**
	 * Return the value associated with the column: name
	 */
    public java.lang.String getName() {
        return name;
    }

    /**
	 * Set the value related to the column: name
	 * @param name the name value
	 */
    public void setName(java.lang.String name) {
        this.name = name;
    }

    public boolean equals(Object obj) {
        if (null == obj) return false;
        if (!(obj instanceof com.useme.model.Customer)) return false; else {
            com.useme.model.Customer customer = (com.useme.model.Customer) obj;
            if (null == this.getId() || null == customer.getId()) return false; else return (this.getId().equals(customer.getId()));
        }
    }

    public int hashCode() {
        if (Integer.MIN_VALUE == this.hashCode) {
            if (null == this.getId()) return super.hashCode(); else {
                String hashStr = this.getClass().getName() + ":" + this.getId().hashCode();
                this.hashCode = hashStr.hashCode();
            }
        }
        return this.hashCode;
    }

    public String toString() {
        return super.toString();
    }
}

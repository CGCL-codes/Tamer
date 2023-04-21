package com.googlecode.beauti4j.gwt.databinding.client;

import java.io.Serializable;

/**
 * DataStore is a interface class and help DataBroker and DataDriver to be
 * agnostic of the underlying data structure by wrapping the domain object in
 * its interface.
 * 
 * @param <PK>
 *            primary key
 * @author Hang Yuan (anthony.yuan@gmail.com)
 */
public interface DataStore<PK extends Serializable> {

    /**
     * @return true if the content holding by the DataStore has been changed;
     *         false otherwise
     */
    boolean isDirty();

    /**
     * Set to true if the content holding by the DataStore has been changed.
     * 
     * @param dirty
     *            assigned value of dirty property
     */
    void setDirty(boolean dirty);

    /**
     * @return ID of object
     */
    PK getId();

    /**
     * @param id
     *            object ID to set
     */
    void setId(PK id);

    /**
     * @return type of object
     */
    String getType();

    /**
     * @return name of object
     */
    String getName();

    /**
     * @param propertyName
     *            Property name
     * @return the value of the specified property.
     */
    Object getProperty(String propertyName);

    /**
     * Modify the value of the given property.
     * 
     * @param propertyName
     *            the property name
     * @param value
     *            new value
     */
    void setProperty(String propertyName, Object value);
}

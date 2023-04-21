package org.datashare;

import java.util.Hashtable;

/**
 * Defines the object that will be put into our Queue and extracted by the PersistDataQueueConsumer
 * class.  This object fully describes the EJB that is to be created by the
 * PersistDataQueueConsumer.
 *
 * @date March 01, 2001
 * @author Charles Wood
 * @version 1.0
 */
public class PersistDataQueueObject {

    /**
    * the name/type of the EJB to be created, should be the 'home' EJB class
    *
    */
    String tableName = "";

    /**
    * describes the attributes of the EJB, determined by the EJB class that is to be created
    * Contains attributes of the data, the keys are strings that correspond to fields,
    * the values are the field values.
    *
    */
    Hashtable props = new Hashtable();

    /**
    * should be set to the class that wishes to receive the ADSKey that corresponds
    * to the EJB that is to be created, should be set to null if no callback with the
    * ADSKey is desired
    *
    */
    PersistDataCallbackInterface callback = null;

    /**
    * class constructor, must be created with first two parameters non-null
    *
    * @param beanName the name of the bean, used in the EjbUtil.create call
    * @param props the property values that describe this bean, used in the
    *     call to EjbUtil.create
    * @param callback provides the method that will let us return the ADSkey
    *     that corresponds to this EJB, set to null if no callback is desired
    */
    PersistDataQueueObject(String tableName, Hashtable props, PersistDataCallbackInterface callback) {
        this.tableName = tableName;
        this.props = props;
        this.callback = callback;
    }

    /**
    * accessor for beanName of this instance
    *
    * @return the beanName that was provided to the class constructor
    */
    public String getTableName() {
        return tableName;
    }

    /**
    * accessor for the ADSProperties of this instance
    *
    * @return the ADSProperties that was provided to the class constructor
    */
    public Hashtable getProperties() {
        return props;
    }

    /**
    * returns the instance that was supplied to the consructor
    *
    * @return the instance supplied by the constructor parameter
    */
    public PersistDataCallbackInterface getCallback() {
        return callback;
    }
}

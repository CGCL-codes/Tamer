package com.gwtaf.ext.core.client.store;

import com.gwtaf.ext.core.client.field.ComboBoxAdapter;
import com.gwtext.client.data.Store;

/**
 * Store Adapter.<br>
 * Used by combo boxes.
 * 
 * @author Matthias Huebner
 * 
 * @param <T> Object type
 * @see com.gwtaf.ext.core.client.store.GenericStoreAdapter
 * @see ComboBoxAdapter
 */
public interface IStoreAdapter<T> {

    Store getStore();

    /**
   * Get object of a record.
   * 
   * @param id id of a record
   * @return Related object
   */
    T getData(String id);

    /**
   * Find the related id. If the storew does not contains the object, this methode has to add it and return
   * the related id.
   * 
   * @param obj Find record id of this object
   * @return id (null if object is null)
   */
    String getID(T obj);

    /**
   * Get the name of the value field.
   * 
   * @return Name of value record field
   */
    String getValueField();

    /**
   * Get the name of the display field.
   * 
   * @return Name of the display record field.
   */
    String getDisplayField();
}

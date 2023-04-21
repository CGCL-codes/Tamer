package com.organic.maynard.outliner.model.propertycontainer;

import java.io.Serializable;
import java.util.Iterator;

/**
 * A container for property values. Manages both a current value and an optional
 * default value for each named property contained within the PropertyContainer.
 * 
 * Allows for PropertyFilterChains to be assigned to each named property. The
 * PropertyFilterChain will be executed whenever the property value is set.
 */
public interface PropertyContainer extends Cloneable, Serializable {

    /**
	 * Removes all properties and their default values and PropertyFilterChains.
	 */
    public void removeAllProperties();

    /**
	 * Removes the property and it's default value and PropertyFilterChain indicated 
	 * by the provided key.
	 */
    public void removeProperty(String key);

    /**
	 * Resets all properties to thier default values.
	 */
    public void resetAllProperties();

    /**
	 * Resets the property indicated by the provided key to it's default value.
	 */
    public void resetProperty(String key);

    /**
	 * Sets the current value of the property indicated by the provided key to 
	 * the provided value.
	 */
    public void setProperty(String key, Object value);

    /**
	 * Sets the default value of the property indicated by the provided key to 
	 * the provided value.
	 */
    public void setPropertyDefault(String key, Object default_value);

    /**
	 * Sets a PropertyFilterChain for the indicated key.
	 */
    public void setPropertyFilterChain(String key, PropertyFilterChain filter_chain);

    /**
	 * Gets the PropertyFilterChain associated with the provided key if it exists.
	 */
    public PropertyFilterChain getPropertyFilterChain(String key);

    /**
	 * Removes the PropertyFilterChain associated with the provided key if it exists.
	 */
    public PropertyFilterChain removePropertyFilterChain(String key, int index);

    /**
	 * A convience method that adds a PropertyFilter to the PropertyFilterChain
	 * associated with the provided key and instantiates a new PropertyFilterChain
	 * if none exists yet.
	 */
    public void addPropertyFilter(String key, PropertyFilter filter);

    /**
	 * Gets the property indicated by the provided key. If no current value exists
	 * then the default value is returned. If no default value exists then null is
	 * returned.
	 */
    public Object getProperty(String key);

    /**
	 * Gets the property indicated by the provided key. If no current value exists
	 * then the provided backup value is returned.
	 */
    public Object getProperty(String key, Object backup_value);

    /**
	 * Gets the default value of the property indicated by the provided key.
	 */
    public Object getPropertyDefault(String key);

    /**
	 * Tests if the property indicated by the provided key is equal to the
	 * provided test value. Note: a property that does not exists will always return
	 * false.
	 */
    public boolean propertyEquals(String key, Object test_value);

    /**
	 * Tests if a current value exists for the property indicated by the provided
	 * key. Note: a property having a null value still indicates that it exists.
	 */
    public boolean propertyExists(String key);

    /**
	 * Tests if a default value exists for the property indicated by the provided
	 * key. Note: a property having a null value still indicates that it exists.
	 */
    public boolean propertyDefaultExists(String key);

    /**
	 * Tests if the current value of the property indicated by the provided key
	 * is equal to the default value of the property. If either the current value
	 * or the default value do not exist then this returns false.
	 */
    public boolean propertyIsDefault(String key);

    /**
	 * Gets an Iterator for all the existing property keys.
	 */
    public Iterator getKeys();
}

package com.tecacet.util.introspection;

/**
 * Abstraction for getting and setting object properties
 * 
 * @author Dimitri Papaioannou
 * 
 * @param <T>
 */
public interface PropertyAccessor<T> {

    /**
     * Get a named property value from a bean Return NULL if any object is NULL
     * when trying to get the specified property
     * 
     * @param bean
     * @param propertyName
     */
    Object getProperty(T bean, String propertyName) throws BeanIntrospectorException;

    /**
     * Set a named property value
     * 
     * @param bean
     * @param propertyName
     * @param value
     * @throws BeanIntrospectorException
     */
    void setProperty(T bean, String propertyName, Object value) throws BeanIntrospectorException;
}

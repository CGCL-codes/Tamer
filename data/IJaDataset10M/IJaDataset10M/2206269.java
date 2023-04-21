package org.caleigo.core;

public interface IPropertyProvider {

    /** Access method that returns the named property object or null if the
     * property name could not be identified.
     */
    public Object getProperty(String propertyName);

    /** Access method that returns the named property object or the provided 
     * default value object if the property name could not be identified.
     */
    public Object getProperty(String propertyName, Object defaultValue);

    /** Mutation methods that sets the named property to the provided property
     * object. If the property value is null then the property may be deleted.
     */
    public void setProperty(String propertyName, Object propertyValue);
}

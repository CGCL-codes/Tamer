package org.skycastle.util.propertyaccess;

import java.util.Collection;

/**
 * Any source of properties that a PropertyAccessor implementation can use.
 * <p/>
 * Only implements the minimal number of necessary methods, a get method, a has method, and a getAvailableProperties method.
 * <p/>
 * The template type H can represent a host object that properties are stored in.
 * <p/>
 * NOTE: We probably need change listener support here also.
 *
 * @author Hans H�ggstr�m
 */
public interface MinimalPropertyAccessor<H> {

    /**
     * Called by instance nodes to get property values.  Should not be called from client code.
     *
     * @param name         the name of the property to get.
     * @param defaultValue default value to return if there was no such property.
     * @param hostObject   the host object object to get the property value for.
     *                     Can be used to calculate a host specific value on the fly.
     *
     * @return the value of the specified property, or the specified defaultValue if it doesn't exist.
     */
    Object getPropertyValue(String name, Object defaultValue, H hostObject);

    /**
     * @return a read only collection with the names of the properties currently
     *         available from this MinimalPropertyAccessor.
     */
    Collection<String> getAvailableProperties();

    /**
     * @return true if a property with the specified name exists in these properties, false if not.
     */
    boolean hasProperty(String name);
}

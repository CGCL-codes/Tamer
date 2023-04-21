package org.skycastle.util.propertyaccess;

import org.skycastle.util.propertyaccess.metadata.PropertyMetadatas;
import java.util.List;

/**
 * An property accessor with convenience method for retrieving properties of various types.
 *
 * @author Hans H�ggstr�m
 */
public interface PropertyAccessor<H> extends MinimalPropertyAccessor<H> {

    /**
     * @return metadatas for this set of properties.
     */
    PropertyMetadatas getMetadatas();

    /**
     * @return the value of the specified property, or the default value specified in the metadata,
     *         or null if no default value is available.
     */
    Object get(String name);

    /**
     * @return the value of the specified property as a boolean, or the specified defaultValue if it doesn't exist.
     *         Throws an exception if the current property value can not be converted to a boolean.
     */
    boolean getBoolean(String name, boolean defaultValue);

    /**
     * @return the value of the specified property as an int, or the specified defaultValue if it doesn't exist.
     *         Throws an exception if the current property value can not be converted to an integer.
     */
    int getInt(String name, int defaultValue);

    /**
     * @return the value of the specified property as a long, or the specified defaultValue if it doesn't exist.
     *         Throws an exception if the current property value can not be converted to a long.
     */
    long getLong(String name, long defaultValue);

    /**
     * @return the value of the specified property as a float, or the specified defaultValue if it doesn't exist.
     *         Throws an exception if the current property value can not be converted to a float.
     */
    double getFloat(String name, float defaultValue);

    /**
     * @return the value of the specified property as a double, or the specified defaultValue if it doesn't exist.
     *         Throws an exception if the current property value can not be converted to a double.
     */
    double getDouble(String name, double defaultValue);

    /**
     * @return the value of the specified property as a String, or the specified defaultValue if it doesn't exist.
     */
    String getString(String name, String defaultValue);

    /**
     * @param name the name of the list property to get.
     *
     * @return a read only reference to the specified list property, or an empty list if not found.
     */
    List<Object> getList(String name);

    /**
     * Gets a list of a specified type.
     *
     * @param name the name of the list property to get.
     * @param type the type of the list to get.
     *
     * @return a read only reference to the specified list property of the specified type, or an empty list if not found.
     */
    <L> List<L> getList(String name, Class<L> type);

    /**
     * @return the host object assoiated with these properties, or null if there is none.
     */
    H getHostObject();
}

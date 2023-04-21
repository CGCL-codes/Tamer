package org.vizzini.util;

import java.util.Collection;
import java.util.Map;

/**
 * Provides utility methods to determine is an object is null or empty.
 *
 * @author   Jeffrey M. Thompson
 * @version  v0.4
 * @see      <a
 *           href="http://java.dzone.com/tips/consistent-way-doing-null-0?utm_source=feedburner&utm_medium=feed&utm_campaign=Feed:+javalobby/frontpage+(Javalobby+/+Java+Zone)">
 *           A Consistent Way of Doing Null Checks and Empty Checks on Objects</a>
 * @since    v0.4
 */
public class EmptyUtilities {

    /**
     * This method returns true if the collection is null or is empty.
     *
     * @param   collection  Collection to test.
     *
     * @return  true if the parameter is null or empty.
     *
     * @since   v0.4
     */
    public boolean isNullOrEmpty(Collection<?> collection) {
        return ((collection == null) || collection.isEmpty());
    }

    /**
     * This method returns true of the map is null or is empty.
     *
     * @param   map  Map to test.
     *
     * @return  true if the parameter is null or empty.
     *
     * @since   v0.4
     */
    public boolean isNullOrEmpty(Map<?, ?> map) {
        return ((map == null) || map.isEmpty());
    }

    /**
     * This method returns true if the object is null.
     *
     * @param   object  Object to test.
     *
     * @return  true if the parameter is null.
     *
     * @since   v0.4
     */
    public boolean isNullOrEmpty(Object object) {
        return (object == null);
    }

    /**
     * This method returns true if the input array is null or its length is
     * zero.
     *
     * @param   array  Array to test.
     *
     * @return  true if the parameter is null or empty.
     *
     * @since   v0.4
     */
    public boolean isNullOrEmpty(Object[] array) {
        return ((array == null) || (array.length == 0));
    }

    /**
     * This method returns true if the input string is null or its length is
     * zero.
     *
     * @param   string  String to test.
     *
     * @return  true if the parameter is null or empty.
     *
     * @since   v0.4
     */
    public boolean isNullOrEmpty(String string) {
        return ((string == null) || (string.trim().length() == 0));
    }
}

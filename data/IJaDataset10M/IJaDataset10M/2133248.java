package org.mc4j.ems.connection.bean;

import java.util.Map;

/**
 * An MBean name.
 */
public interface EmsBeanName extends Comparable {

    /**
     * Returns the domain of this name.
     *
     * @return the domain of this name
     */
    String getDomain();

    /**
     * Returns the canonical form of the name as defined by {@link ObjectName#getCanonicalName()}.
     *
     * @return the canonical form of the name as defined by {@link ObjectName#getCanonicalName()}
     */
    String getCanonicalName();

    /**
     * Returns a Map of this name's key properties. The keys of the Map are the key property names, and the values are
     * the values of the corresponding key properties.
     *
     * @return a Map of this name's key properties
     */
    Map<String, String> getKeyProperties();

    /**
    * Returns the value for the key property with the specified name.
    *
    * @param name the key property name
    *
    * @return the value of the key property, or null if there is no such
    *         key property in this DBeanName.
    *
    * @throws NullPointerException If <code>name</code> is null
    */
    String getKeyProperty(String key);

    /**
     * Tests whether this name, which may be a pattern,
     * matches another MBean name. If <code>name</code> is a pattern,
     * the result is false.  If this EmsBeanName is a pattern, the
     * result is true if and only if <code>name</code> matches the
     * pattern.  If neither this EmsBeanName nor <code>name</code> is
     * a pattern, the result is true if and only if the two
     * EmsBeanNames have canonical String forms that are equal.
     *
     * @param name the MBean name to compare to
     *
     * @return true if <code>name</code> matches this EmsBeanName
     *
     * @throws NullPointerException if <code>name</code> is null
     */
    boolean apply(String name);
}

package org.dwgsoftware.raistlin.meta.info;

import java.io.Serializable;
import java.util.Properties;

/**
 * This is the Abstract class for all feature feature descriptors.
 *
 * @author <a href="mailto:dev@avalon.apache.org">Avalon Development Team</a>
 * @version $Revision: 1.1 $ $Date: 2005/09/06 00:58:25 $
 */
public abstract class Descriptor implements Serializable {

    private static final String[] EMPTY_SET = new String[0];

    /**
     * The arbitrary set of attributes associated with Component.
     */
    private final Properties m_attributes;

    /**
     * Creation of an abstract descriptor.
     * @param attributes the set of attributes to assign to the descriptor
     */
    protected Descriptor(final Properties attributes) {
        m_attributes = attributes;
    }

    /**
     * Return the attribute for specified key.
     *
     * @param key the attribute key to resolve
     * @return the attribute for specified key.
     */
    public String getAttribute(final String key) {
        if (null == m_attributes) {
            return null;
        } else {
            return m_attributes.getProperty(key);
        }
    }

    /**
     * Return the attribute for specified key.
     *
     * @param key the attribute key to resolve
     * @param defaultValue the default value to use if the value is not defined
     * @return the attribute for specified key.
     */
    public String getAttribute(final String key, final String defaultValue) {
        if (null == m_attributes) {
            return defaultValue;
        } else {
            return m_attributes.getProperty(key, defaultValue);
        }
    }

    /**
     * Returns the set of attribute names available under this descriptor.
     *
     * @return an array of the properties names held by the descriptor.
     */
    public String[] getAttributeNames() {
        if (null == m_attributes) {
            return EMPTY_SET;
        } else {
            return (String[]) m_attributes.keySet().toArray(EMPTY_SET);
        }
    }

    /**
     * Compare this object with another for equality.
     * @param other the object to compare this object with
     * @return TRUE if the supplied object equivalent
     */
    public boolean equals(Object other) {
        if (other instanceof Descriptor) {
            Descriptor descriptor = (Descriptor) other;
            if (null == m_attributes) return null == descriptor.m_attributes;
            return m_attributes.equals(descriptor.m_attributes);
        }
        return false;
    }

    /**
    * Return the hashcode for the object.
    * @return the hashcode value
    */
    public int hashCode() {
        if (m_attributes != null) {
            return m_attributes.hashCode();
        } else {
            return 1;
        }
    }

    /**
     * Returns the property set.
     * TODO: check necessity for this operationi and if really needed return 
     * a cloned equivalent (i.e. disable modification)
     * 
     * @return the property set.
     */
    protected Properties getProperties() {
        return m_attributes;
    }
}

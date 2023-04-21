package org.joda.beans.impl.direct;

import org.joda.beans.MetaProperty;
import org.joda.beans.impl.BasicMetaBean;

/**
 * A meta-bean implementation designed for use by {@code DirectBean}.
 * <p>
 * This implementation uses direct access via {@link #metaPropertyGet(String)} to avoid reflection.
 * 
 * @author Stephen Colebourne
 */
public abstract class DirectMetaBean extends BasicMetaBean {

    /**
     * Gets the meta-property by name.
     * <p>
     * This implementation returns null, and must be overridden in subclasses.
     * 
     * @param propertyName  the property name, not null
     * @return the meta-property, null if not found
     */
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
        return null;
    }
}

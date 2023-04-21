package org.dyndns.doujindb.conf.impl;

import java.awt.Color;
import java.awt.Font;
import java.io.Serializable;
import org.dyndns.doujindb.conf.Property;
import org.dyndns.doujindb.conf.PropertyException;

/**  
* PropertyImpl.java - A property.
* @author  nozomu
* @version 1.0
*/
final class PropertyImpl implements Property, Serializable {

    private static final long serialVersionUID = 0x001L;

    private Object value;

    private transient String description;

    PropertyImpl() {
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public void setValue(Object value) throws PropertyException {
        this.value = value;
    }

    @Override
    public Boolean asBoolean() throws PropertyException {
        try {
            return (Boolean) value;
        } catch (ClassCastException cce) {
            throw new PropertyException(cce);
        }
    }

    @Override
    public String asString() throws PropertyException {
        try {
            return (String) value;
        } catch (ClassCastException cce) {
            throw new PropertyException(cce);
        }
    }

    @Override
    public Integer asNumber() throws PropertyException {
        try {
            return (Integer) value;
        } catch (ClassCastException cce) {
            throw new PropertyException(cce);
        }
    }

    @Override
    public Font asFont() throws PropertyException {
        try {
            return (Font) value;
        } catch (ClassCastException cce) {
            throw new PropertyException(cce);
        }
    }

    @Override
    public Color asColor() throws PropertyException {
        try {
            return (Color) value;
        } catch (ClassCastException cce) {
            throw new PropertyException(cce);
        }
    }
}

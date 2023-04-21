package org.simulare;

import java.util.*;

/**
 * Represents an attribute of a simulation item.
 */
public class Attribute extends Named {

    private Class type;

    private Object value;

    private transient Vector attributeListeners;

    public Attribute(String name, Class type, Object value) {
        super(name);
        setType(type);
        setValue(value);
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        AttributeEvent e = new AttributeEvent(this);
        e.oldClass = this.type;
        this.type = type;
        fireTypeChanged(e);
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        AttributeEvent e = new AttributeEvent(this);
        e.oldValue = this.value;
        this.value = value;
        fireValueChanged(e);
    }

    public boolean getBoolean() {
        return ((Boolean) value).booleanValue();
    }

    public char getChar() {
        return ((Character) value).charValue();
    }

    public byte getByte() {
        return ((Number) value).byteValue();
    }

    public short getShort() {
        return ((Number) value).shortValue();
    }

    public int getInt() {
        return ((Number) value).intValue();
    }

    public long getLong() {
        return ((Number) value).longValue();
    }

    public float getFloat() {
        return ((Number) value).floatValue();
    }

    public double getDouble() {
        return ((Number) value).doubleValue();
    }

    public String getString() {
        return (String) value;
    }

    public Attribute getCopy() {
        return new Attribute(getName(), getType(), getValue());
    }

    public synchronized void removeAttributeListener(AttributeListener l) {
        if (attributeListeners != null && attributeListeners.contains(l)) {
            Vector v = (Vector) attributeListeners.clone();
            v.removeElement(l);
            attributeListeners = v;
        }
    }

    public synchronized void addAttributeListener(AttributeListener l) {
        Vector v = attributeListeners == null ? new Vector(2) : (Vector) attributeListeners.clone();
        if (!v.contains(l)) {
            v.addElement(l);
            attributeListeners = v;
        }
    }

    protected void fireTypeChanged(AttributeEvent e) {
        if (attributeListeners != null) {
            Vector listeners = attributeListeners;
            int count = listeners.size();
            for (int i = 0; i < count; i++) {
                ((AttributeListener) listeners.elementAt(i)).typeChanged(e);
            }
        }
    }

    protected void fireValueChanged(AttributeEvent e) {
        if (attributeListeners != null) {
            Vector listeners = attributeListeners;
            int count = listeners.size();
            for (int i = 0; i < count; i++) {
                ((AttributeListener) listeners.elementAt(i)).valueChanged(e);
            }
        }
    }
}

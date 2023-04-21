package org.openwar.victory.network.pack;

import java.util.Arrays;

/**
 * A single property.
 * @author Bart van Heukelom
 */
public class Property implements Cloneable, Comparable<Property> {

    private String key;

    private Object value;

    private int keyHash;

    /**
     * Create a property.
     * @param k The key
     * @param v The value
     */
    public Property(final String k, final Object v) {
        setKey(k);
        value = v;
    }

    /**
     * @return The key.
     */
    public String getKey() {
        return key;
    }

    /**
     * @return The hashCode of the key.
     */
    public int getKeyHash() {
        return keyHash;
    }

    /**
     * Set the key.
     * @param k The key
     */
    public void setKey(final String k) {
        if (k == null) {
            throw new NullPointerException("Key is NULL");
        }
        if (k.length() == 0) {
            throw new IllegalArgumentException("Key is 0 length");
        }
        key = k;
        keyHash = key.hashCode();
    }

    /**
     * @return The value.
     */
    public Object getValue() {
        return value;
    }

    /**
     * Set the value.
     * @param v The value
     */
    public void setValue(final Object v) {
        this.value = v;
    }

    /**
     * @return The type of the value as String, or <code>null</code> if the value is <code>null</code>
     */
    public String getTypeString() {
        if (value == null) {
            return null;
        } else {
            return value.getClass().getSimpleName();
        }
    }

    /**
     * @return The type of the value as <code>Class</code>, or <code>null</code> if the value is <code>null</code>.
     * Shorthand for getValue().getClass();
     */
    public Class<?> getTypeClass() {
        if (value == null) {
            return null;
        } else {
            return value.getClass();
        }
    }

    /**
     * @return A string representation of this propery, of the format:<br/>
     * <code>key - value (type)</code>
     */
    public String toString() {
        String echoValue;
        echoValue = String.valueOf(value);
        return key + " = " + echoValue + " (" + getTypeString() + ")";
    }

    /**
     * @return An exact copy of this property.
     */
    public Property clone() {
        return new Property(key, value);
    }

    /**
     * Validate whether this property is equal in key, value and type to another.
     * @param other The other property.
     * @return Whether it is equal.
     */
    public boolean equals(final Object other) {
        if (other == null || !(other instanceof Property)) {
            return false;
        }
        if (other == this) {
            return true;
        }
        final Property otherProp = (Property) other;
        if (!key.equals(otherProp.key)) {
            return false;
        }
        if (value == null) {
            return otherProp.value == null;
        }
        if (value instanceof byte[]) {
            if (!(otherProp.value instanceof byte[])) {
                return false;
            }
            return Arrays.equals((byte[]) value, (byte[]) otherProp.value);
        } else {
            return value.equals(otherProp.value);
        }
    }

    /**
     * @return The hash code for this property.
     */
    public int hashCode() {
        return getKeyHash() * value.hashCode();
    }

    /**
     * Compare this property by key to another property.
     * @param o The other property.
     * @return The comparison between the two keys.
     */
    public int compareTo(final Property o) {
        return key.compareTo(o.key);
    }
}

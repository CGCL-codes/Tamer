package de.ios.framework.db2;

import de.ios.framework.basic.*;

/**
 * This class represents a Time-value that can be
 * used in DBObject-inheritances.
 */
public class DBTimeAttr extends DBAttribute implements DBStringTypeAttribute {

    /**
   * Constructor
   */
    public DBTimeAttr() {
    }

    /**
   * Constructor. Attribute is not marked as modified.
   */
    public DBTimeAttr(IoSTime time) {
        set(time);
    }

    /**
   * Get value.
   */
    public IoSTime get() {
        return isNull() ? null : ((IoSTime) value.clone());
    }

    /**
   * Set value.
   */
    public void set(IoSTime v) {
        if (v != null) {
            setModifiedIf(isNull() ? true : (!value.equals(v)));
            value = (IoSTime) v.clone();
        } else {
            setModifiedIf(!isNull());
            value = null;
        }
        defineNullValue(value);
    }

    /**
   * Sets the value.
   */
    public void setAsString(String v) {
        set((v == null) ? null : IoSTime.fastCreate(v));
    }

    /**
   * Copy of contents.
   */
    public void copy(DBAttribute other) {
        super.copy(other);
        value = ((DBTimeAttr) other).value;
        if (value != null) value = (IoSTime) value.clone();
    }

    /**
   * Convert to String
   */
    public String toString() {
        return (isNull() ? "<null>" : value.getTime());
    }

    /**
   * Get value as SQL-String.
   */
    public String SQLValue() {
        return SQLStringValue(isNull() ? null : value.getCompressedTime());
    }

    protected IoSTime value = null;
}

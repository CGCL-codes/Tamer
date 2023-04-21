package de.ios.framework.db2;

import java.io.*;

/**
 * This class represents columns of type FLOAT.
 */
public class DBFloatAttr extends DBAttribute {

    /**
   * Constructor.
   */
    public DBFloatAttr() {
        super();
    }

    /**
   * Get value.
   */
    public float get() {
        return value;
    }

    /**
   * Get Boolean-Value.
   */
    public Float getFloat() {
        return isNull() ? null : (new Float(value));
    }

    /**
   * Set value.
   */
    public void set(float v) {
        setModifiedIf((v != value) || isNull());
        value = v;
        setNotNull();
    }

    /**
   * Set value.
   */
    public void set(Float v) {
        if (v != null) set(v.floatValue()); else {
            setModifiedIf(!isNull());
            value = -1;
            setNull();
        }
    }

    /**
   * Copy of contents.
   */
    public void copy(DBAttribute other) {
        super.copy(other);
        value = ((DBFloatAttr) other).value;
    }

    /**
   * Get value as SQL-string.
   */
    public String SQLValue() {
        return isNull() ? "null" : String.valueOf(value);
    }

    /**
   * Convert to String
   */
    public String toString() {
        return (isNull() ? "<null>" : String.valueOf(value));
    }

    protected float value = (float) 0.0;
}

;

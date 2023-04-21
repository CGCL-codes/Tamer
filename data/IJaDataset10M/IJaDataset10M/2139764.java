package org.openxava.test.model;

import java.util.*;
import org.openxava.util.*;

/**
 * Primary key for Driver.
 */
public class DriverKey extends java.lang.Object implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    public java.lang.Integer _Number;

    public DriverKey() {
    }

    public DriverKey(java.lang.Integer _Number) {
        this._Number = _Number;
    }

    public java.lang.Integer get_Number() {
        return _Number;
    }

    public void set_Number(java.lang.Integer _Number) {
        this._Number = _Number;
    }

    public int hashCode() {
        int _hashCode = 0;
        if (this._Number != null) _hashCode += this._Number.hashCode();
        return _hashCode;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof org.openxava.test.model.DriverKey)) return false;
        org.openxava.test.model.DriverKey pk = (org.openxava.test.model.DriverKey) obj;
        boolean eq = true;
        if (obj == null) {
            eq = false;
        } else {
            if (this._Number != null) {
                eq = eq && this._Number.equals(pk.get_Number());
            } else {
                eq = eq && (pk.get_Number() == null);
            }
        }
        return eq;
    }

    /**
    * Create from a string with the format of toString() method
    */
    public static DriverKey createFromString(String string) throws IllegalArgumentException, IllegalAccessException {
        StringTokenizer st = new StringTokenizer(string, "[.]");
        DriverKey key = new DriverKey();
        java.lang.reflect.Field[] fields = key.getClass().getFields();
        Arrays.sort(fields, FieldComparator.getInstance());
        for (int i = 0; i < fields.length; i++) {
            String v = st.nextToken();
            Class type = fields[i].getType();
            Object value = null;
            if (!type.equals(String.class)) {
                value = Strings.toObject(type, v);
            } else {
                value = string;
            }
            fields[i].set(key, value);
        }
        return key;
    }

    /** @return String representation of this pk in the form of [.field1.field2.field3]. */
    public String toString() {
        StringBuffer toStringValue = new StringBuffer("[.");
        java.lang.reflect.Field[] fields = getClass().getFields();
        Arrays.sort(fields, FieldComparator.getInstance());
        for (int i = 0; i < fields.length; i++) {
            try {
                toStringValue.append(fields[i].get(this)).append('.');
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
                toStringValue.append(" ").append('.');
            }
        }
        toStringValue.append(']');
        return toStringValue.toString();
    }
}

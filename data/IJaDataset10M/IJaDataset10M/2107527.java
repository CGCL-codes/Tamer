package org.openxava.test.model;

import java.util.*;
import org.openxava.util.*;

/**
 * Primary key for Year.
 */
public class YearKey extends java.lang.Object implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    public YearKey() {
    }

    public int hashCode() {
        int _hashCode = 0;
        return _hashCode;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof org.openxava.test.model.YearKey)) return false;
        org.openxava.test.model.YearKey pk = (org.openxava.test.model.YearKey) obj;
        boolean eq = true;
        if (obj == null) {
            eq = false;
        } else {
        }
        return eq;
    }

    /**
    * Create from a string with the format of toString() method
    */
    public static YearKey createFromString(String string) throws IllegalArgumentException, IllegalAccessException {
        StringTokenizer st = new StringTokenizer(string, "[.]");
        YearKey key = new YearKey();
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

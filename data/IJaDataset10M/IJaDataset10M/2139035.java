package org.openxava.school.model;

import java.util.*;
import org.openxava.util.*;

/**
 * Primary key for Teacher.
 */
public class TeacherKey extends java.lang.Object implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    public java.lang.String id;

    public TeacherKey() {
    }

    public TeacherKey(java.lang.String id) {
        this.id = id;
    }

    public java.lang.String getId() {
        return id;
    }

    public void setId(java.lang.String id) {
        this.id = id;
    }

    public int hashCode() {
        int _hashCode = 0;
        if (this.id != null) _hashCode += this.id.hashCode();
        return _hashCode;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof org.openxava.school.model.TeacherKey)) return false;
        org.openxava.school.model.TeacherKey pk = (org.openxava.school.model.TeacherKey) obj;
        boolean eq = true;
        if (obj == null) {
            eq = false;
        } else {
            if (this.id != null) {
                eq = eq && this.id.equals(pk.getId());
            } else {
                eq = eq && (pk.getId() == null);
            }
        }
        return eq;
    }

    /**
    * Create from a string with the format of toString() method
    */
    public static TeacherKey createFromString(String string) throws IllegalArgumentException, IllegalAccessException {
        StringTokenizer st = new StringTokenizer(string, "[.]");
        TeacherKey key = new TeacherKey();
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

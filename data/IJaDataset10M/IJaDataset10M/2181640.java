package org.openxava.test.model;

import java.util.*;
import org.openxava.util.*;

/**
 * Primary key for OrderDetail.
 */
public class OrderDetailKey extends java.lang.Object implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    public java.lang.String oid;

    public OrderDetailKey() {
    }

    public OrderDetailKey(java.lang.String oid) {
        this.oid = oid;
    }

    public java.lang.String getOid() {
        return oid;
    }

    public void setOid(java.lang.String oid) {
        this.oid = oid;
    }

    public int hashCode() {
        int _hashCode = 0;
        if (this.oid != null) _hashCode += this.oid.hashCode();
        return _hashCode;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof org.openxava.test.model.OrderDetailKey)) return false;
        org.openxava.test.model.OrderDetailKey pk = (org.openxava.test.model.OrderDetailKey) obj;
        boolean eq = true;
        if (obj == null) {
            eq = false;
        } else {
            if (this.oid != null) {
                eq = eq && this.oid.equals(pk.getOid());
            } else {
                eq = eq && (pk.getOid() == null);
            }
        }
        return eq;
    }

    /**
    * Create from a string with the format of toString() method
    */
    public static OrderDetailKey createFromString(String string) throws IllegalArgumentException, IllegalAccessException {
        StringTokenizer st = new StringTokenizer(string, "[.]");
        OrderDetailKey key = new OrderDetailKey();
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

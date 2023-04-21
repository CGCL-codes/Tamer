package org.openxava.test.ejb;

/**
 * Primary key for AdditionalDetail.
 */
public class AdditionalDetailKey extends java.lang.Object implements java.io.Serializable {

    public int counter;

    public int service_number;

    public AdditionalDetailKey() {
    }

    public AdditionalDetailKey(int counter, int service_number) {
        this.counter = counter;
        this.service_number = service_number;
    }

    public int getCounter() {
        return counter;
    }

    public int getService_number() {
        return service_number;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public void setService_number(int service_number) {
        this.service_number = service_number;
    }

    public int hashCode() {
        int _hashCode = 0;
        _hashCode += (int) this.counter;
        _hashCode += (int) this.service_number;
        return _hashCode;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof org.openxava.test.ejb.AdditionalDetailKey)) return false;
        org.openxava.test.ejb.AdditionalDetailKey pk = (org.openxava.test.ejb.AdditionalDetailKey) obj;
        boolean eq = true;
        if (obj == null) {
            eq = false;
        } else {
            eq = eq && this.counter == pk.counter;
            eq = eq && this.service_number == pk.service_number;
        }
        return eq;
    }

    /** @return String representation of this pk in the form of [.field1.field2.field3]. */
    public String toString() {
        StringBuffer toStringValue = new StringBuffer("[.");
        java.lang.reflect.Field[] fields = getClass().getFields();
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

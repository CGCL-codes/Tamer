package org.jaffa.soa.rules;

/**
 *
 * @author paule
 */
public class FieldChanged {

    private String object = "";

    private String field = "";

    private Object oldValue;

    private Object key1;

    private Object key2;

    private Object key3;

    /**
     * Getter for property object.
     * @return Value of property object.
     */
    public String getObject() {
        return this.object;
    }

    /**
     * Setter for property object.
     * @param object New value of property object.
     */
    public void setObject(String object) {
        this.object = object;
    }

    /**
     * Getter for property field.
     * @return Value of property field.
     */
    public String getField() {
        return this.field;
    }

    /**
     * Setter for property field.
     * @param field New value of property field.
     */
    public void setField(String field) {
        this.field = field;
    }

    /**
     * Getter for property oldValue.
     * @return Value of property oldValue.
     */
    public Object getOldValue() {
        return this.oldValue;
    }

    /**
     * Setter for property oldValue.
     * @param oldValue New value of property oldValue.
     */
    public void setOldValue(Object oldValue) {
        this.oldValue = oldValue;
    }

    /**
     * Getter for property key1.
     * @return Value of property key1.
     */
    public Object getKey1() {
        return this.key1;
    }

    /**
     * Setter for property key1.
     * @param key1 New value of property key1.
     */
    public void setKey1(Object key1) {
        this.key1 = key1;
    }

    /**
     * Getter for property key2.
     * @return Value of property key2.
     */
    public Object getKey2() {
        return key2;
    }

    /**
     * Setter for property key2.
     * @param key2 New value of property key2.
     */
    public void setKey2(Object key2) {
        this.key2 = key2;
    }

    /**
     * Getter for property key3.
     * @return Value of property key3.
     */
    public Object getKey3() {
        return key3;
    }

    /**
     * Setter for property key3.
     * @param key3 New value of property key3.
     */
    public void setKey3(Object key3) {
        this.key3 = key3;
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof FieldChanged)) return false;
        FieldChanged fc = (FieldChanged) obj;
        return (object != null ? object.equals(fc.object) : fc.object == null) && (field != null ? field.equals(fc.field) : fc.field == null) && (key1 != null ? key1.equals(fc.key1) : fc.key1 == null) && (key2 != null ? key2.equals(fc.key2) : fc.key2 == null) && (key3 != null ? key3.equals(fc.key3) : fc.key3 == null);
    }

    public String toString() {
        StringBuilder buf = new StringBuilder(object);
        buf.append('[').append(key1);
        if (key2 != null) buf.append(", ").append(key2);
        if (key3 != null) buf.append(", ").append(key3);
        buf.append("].").append(field).append('=').append(oldValue);
        return buf.toString();
    }
}

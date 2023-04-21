package it.cefriel.glue2.util;

public class KeyValuePair {

    private String key = null;

    private Object value = null;

    public KeyValuePair(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public KeyValuePair(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public KeyValuePair() {
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }

    public String getString() {
        return value.toString();
    }

    public void setKey(String key) {
        if ((key != null) && (key != "")) this.key = key;
    }

    public void setValue(Object value) {
        if ((value != null) && (value != "")) this.value = value;
    }

    public String toString() {
        return ("Key: " + key + " Value: " + value.toString());
    }
}

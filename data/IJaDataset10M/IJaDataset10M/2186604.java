package net.sf.crispy.extension.hivemind;

public class KeyValue {

    private String key = null;

    private String value = null;

    public KeyValue() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String pvKey) {
        key = pvKey;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String pvValue) {
        value = pvValue;
    }

    public String toString() {
        return getKey() + "=" + getValue() + "  -->" + super.toString();
    }
}

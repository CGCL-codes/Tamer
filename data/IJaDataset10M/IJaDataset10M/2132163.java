package hu.sztaki.lpds.pgportal.services.xml.beans;

/**
 * @author  Krisztian Karóczkai
 */
public class beanAttribute {

    private String key;

    private Object value;

    private String type;

    public beanAttribute(String pKey, Object pValue) {
        key = pKey;
        value = pValue;
        type = null;
    }

    public beanAttribute(String pKey, Object pValue, String pType) {
        key = pKey;
        value = pValue;
        this.type = pType;
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

    public boolean isKey(String pKey) {
        return pKey.equals(key);
    }

    public boolean isType(String pType) {
        return pType.equals(type);
    }
}

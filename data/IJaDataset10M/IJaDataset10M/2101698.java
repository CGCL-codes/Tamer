package com.amazon.webservices.awsecommerceservice.x20090201;

import java.util.HashMap;
import java.util.Map;
import com.bea.xbean.util.XsTypeConverter;

public class DeliveryMethod implements java.io.Serializable {

    private java.lang.String value;

    private static Map map = new HashMap();

    protected DeliveryMethod(java.lang.String value) {
        this.value = value;
    }

    public static final java.lang.String _ship = "Ship";

    public static final java.lang.String _ispu = "ISPU";

    public static final DeliveryMethod ship = new DeliveryMethod(_ship);

    public static final DeliveryMethod ispu = new DeliveryMethod(_ispu);

    public java.lang.String getValue() {
        return this.value;
    }

    public static DeliveryMethod fromValue(java.lang.String value) {
        if (map.containsKey(value)) return (DeliveryMethod) map.get(value); else throw new IllegalArgumentException();
    }

    public static DeliveryMethod fromString(String value) {
        return fromValue(value);
    }

    public String toXML() {
        return XsTypeConverter.printString(this.value).toString();
    }

    public String toString() {
        return String.valueOf(value);
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof DeliveryMethod)) return false;
        final DeliveryMethod x = (DeliveryMethod) obj;
        if (x.value.equals(value)) return true;
        return false;
    }

    public int hashCode() {
        return value.hashCode();
    }

    static {
        map.put(_ship, ship);
        map.put(_ispu, ispu);
    }
}

package com.amazon.webservices.awsecommerceservice.x20090201;

import java.util.HashMap;
import java.util.Map;
import com.bea.xbean.util.XsTypeConverter;

public class AudienceRating implements java.io.Serializable {

    private java.lang.String value;

    private static Map map = new HashMap();

    protected AudienceRating(java.lang.String value) {
        this.value = value;
    }

    public static final java.lang.String _value1 = "G";

    public static final java.lang.String _value2 = "PG";

    public static final java.lang.String _value3 = "PG-13";

    public static final java.lang.String _value4 = "R";

    public static final java.lang.String _value5 = "NC-17";

    public static final java.lang.String _value6 = "NR";

    public static final java.lang.String _value7 = "Unrated";

    public static final java.lang.String _value8 = "6";

    public static final java.lang.String _value9 = "12";

    public static final java.lang.String _value10 = "16";

    public static final java.lang.String _value11 = "18";

    public static final java.lang.String _value12 = "FamilyViewing";

    public static final AudienceRating value1 = new AudienceRating(_value1);

    public static final AudienceRating value2 = new AudienceRating(_value2);

    public static final AudienceRating value3 = new AudienceRating(_value3);

    public static final AudienceRating value4 = new AudienceRating(_value4);

    public static final AudienceRating value5 = new AudienceRating(_value5);

    public static final AudienceRating value6 = new AudienceRating(_value6);

    public static final AudienceRating value7 = new AudienceRating(_value7);

    public static final AudienceRating value8 = new AudienceRating(_value8);

    public static final AudienceRating value9 = new AudienceRating(_value9);

    public static final AudienceRating value10 = new AudienceRating(_value10);

    public static final AudienceRating value11 = new AudienceRating(_value11);

    public static final AudienceRating value12 = new AudienceRating(_value12);

    public java.lang.String getValue() {
        return this.value;
    }

    public static AudienceRating fromValue(java.lang.String value) {
        if (map.containsKey(value)) return (AudienceRating) map.get(value); else throw new IllegalArgumentException();
    }

    public static AudienceRating fromString(String value) {
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
        if (!(obj instanceof AudienceRating)) return false;
        final AudienceRating x = (AudienceRating) obj;
        if (x.value.equals(value)) return true;
        return false;
    }

    public int hashCode() {
        return value.hashCode();
    }

    static {
        map.put(_value1, value1);
        map.put(_value2, value2);
        map.put(_value3, value3);
        map.put(_value4, value4);
        map.put(_value5, value5);
        map.put(_value6, value6);
        map.put(_value7, value7);
        map.put(_value8, value8);
        map.put(_value9, value9);
        map.put(_value10, value10);
        map.put(_value11, value11);
        map.put(_value12, value12);
    }
}

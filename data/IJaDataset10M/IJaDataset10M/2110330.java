package com.google.api.ads.dfp.v201203;

public class DayOfWeek implements java.io.Serializable {

    private java.lang.String _value_;

    private static java.util.HashMap _table_ = new java.util.HashMap();

    protected DayOfWeek(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_, this);
    }

    public static final java.lang.String _MONDAY = "MONDAY";

    public static final java.lang.String _TUESDAY = "TUESDAY";

    public static final java.lang.String _WEDNESDAY = "WEDNESDAY";

    public static final java.lang.String _THURSDAY = "THURSDAY";

    public static final java.lang.String _FRIDAY = "FRIDAY";

    public static final java.lang.String _SATURDAY = "SATURDAY";

    public static final java.lang.String _SUNDAY = "SUNDAY";

    public static final DayOfWeek MONDAY = new DayOfWeek(_MONDAY);

    public static final DayOfWeek TUESDAY = new DayOfWeek(_TUESDAY);

    public static final DayOfWeek WEDNESDAY = new DayOfWeek(_WEDNESDAY);

    public static final DayOfWeek THURSDAY = new DayOfWeek(_THURSDAY);

    public static final DayOfWeek FRIDAY = new DayOfWeek(_FRIDAY);

    public static final DayOfWeek SATURDAY = new DayOfWeek(_SATURDAY);

    public static final DayOfWeek SUNDAY = new DayOfWeek(_SUNDAY);

    public java.lang.String getValue() {
        return _value_;
    }

    public static DayOfWeek fromValue(java.lang.String value) throws java.lang.IllegalArgumentException {
        DayOfWeek enumeration = (DayOfWeek) _table_.get(value);
        if (enumeration == null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }

    public static DayOfWeek fromString(java.lang.String value) throws java.lang.IllegalArgumentException {
        return fromValue(value);
    }

    public boolean equals(java.lang.Object obj) {
        return (obj == this);
    }

    public int hashCode() {
        return toString().hashCode();
    }

    public java.lang.String toString() {
        return _value_;
    }

    public java.lang.Object readResolve() throws java.io.ObjectStreamException {
        return fromValue(_value_);
    }

    public static org.apache.axis.encoding.Serializer getSerializer(java.lang.String mechType, java.lang.Class _javaType, javax.xml.namespace.QName _xmlType) {
        return new org.apache.axis.encoding.ser.EnumSerializer(_javaType, _xmlType);
    }

    public static org.apache.axis.encoding.Deserializer getDeserializer(java.lang.String mechType, java.lang.Class _javaType, javax.xml.namespace.QName _xmlType) {
        return new org.apache.axis.encoding.ser.EnumDeserializer(_javaType, _xmlType);
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(DayOfWeek.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "DayOfWeek"));
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }
}

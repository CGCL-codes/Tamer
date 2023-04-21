package com.google.api.ads.dfp.v201108;

public class RequiredSizeErrorReason implements java.io.Serializable {

    private java.lang.String _value_;

    private static java.util.HashMap _table_ = new java.util.HashMap();

    protected RequiredSizeErrorReason(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_, this);
    }

    public static final java.lang.String _REQUIRED = "REQUIRED";

    public static final java.lang.String _NOT_ALLOWED = "NOT_ALLOWED";

    public static final RequiredSizeErrorReason REQUIRED = new RequiredSizeErrorReason(_REQUIRED);

    public static final RequiredSizeErrorReason NOT_ALLOWED = new RequiredSizeErrorReason(_NOT_ALLOWED);

    public java.lang.String getValue() {
        return _value_;
    }

    public static RequiredSizeErrorReason fromValue(java.lang.String value) throws java.lang.IllegalArgumentException {
        RequiredSizeErrorReason enumeration = (RequiredSizeErrorReason) _table_.get(value);
        if (enumeration == null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }

    public static RequiredSizeErrorReason fromString(java.lang.String value) throws java.lang.IllegalArgumentException {
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

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(RequiredSizeErrorReason.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201108", "RequiredSizeError.Reason"));
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }
}

package com.google.api.adwords.v201109.cm;

public class EntityAccessDeniedReason implements java.io.Serializable {

    private java.lang.String _value_;

    private static java.util.HashMap _table_ = new java.util.HashMap();

    protected EntityAccessDeniedReason(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_, this);
    }

    public static final java.lang.String _READ_ACCESS_DENIED = "READ_ACCESS_DENIED";

    public static final java.lang.String _WRITE_ACCESS_DENIED = "WRITE_ACCESS_DENIED";

    public static final EntityAccessDeniedReason READ_ACCESS_DENIED = new EntityAccessDeniedReason(_READ_ACCESS_DENIED);

    public static final EntityAccessDeniedReason WRITE_ACCESS_DENIED = new EntityAccessDeniedReason(_WRITE_ACCESS_DENIED);

    public java.lang.String getValue() {
        return _value_;
    }

    public static EntityAccessDeniedReason fromValue(java.lang.String value) throws java.lang.IllegalArgumentException {
        EntityAccessDeniedReason enumeration = (EntityAccessDeniedReason) _table_.get(value);
        if (enumeration == null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }

    public static EntityAccessDeniedReason fromString(java.lang.String value) throws java.lang.IllegalArgumentException {
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

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(EntityAccessDeniedReason.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "EntityAccessDenied.Reason"));
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }
}

package com.google.api.adwords.v201008.cm;

public class AdExtensionOverrideStatus implements java.io.Serializable {

    private java.lang.String _value_;

    private static java.util.HashMap _table_ = new java.util.HashMap();

    protected AdExtensionOverrideStatus(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_, this);
    }

    public static final java.lang.String _ACTIVE = "ACTIVE";

    public static final java.lang.String _DELETED = "DELETED";

    public static final AdExtensionOverrideStatus ACTIVE = new AdExtensionOverrideStatus(_ACTIVE);

    public static final AdExtensionOverrideStatus DELETED = new AdExtensionOverrideStatus(_DELETED);

    public java.lang.String getValue() {
        return _value_;
    }

    public static AdExtensionOverrideStatus fromValue(java.lang.String value) throws java.lang.IllegalArgumentException {
        AdExtensionOverrideStatus enumeration = (AdExtensionOverrideStatus) _table_.get(value);
        if (enumeration == null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }

    public static AdExtensionOverrideStatus fromString(java.lang.String value) throws java.lang.IllegalArgumentException {
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

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(AdExtensionOverrideStatus.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201008", "AdExtensionOverride.Status"));
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }
}

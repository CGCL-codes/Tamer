package com.google.api.adwords.v201101.ch;

public class ChangeStatus implements java.io.Serializable {

    private java.lang.String _value_;

    private static java.util.HashMap _table_ = new java.util.HashMap();

    protected ChangeStatus(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_, this);
    }

    public static final java.lang.String _FIELDS_UNCHANGED = "FIELDS_UNCHANGED";

    public static final java.lang.String _FIELDS_CHANGED = "FIELDS_CHANGED";

    public static final java.lang.String _NEW = "NEW";

    public static final ChangeStatus FIELDS_UNCHANGED = new ChangeStatus(_FIELDS_UNCHANGED);

    public static final ChangeStatus FIELDS_CHANGED = new ChangeStatus(_FIELDS_CHANGED);

    public static final ChangeStatus NEW = new ChangeStatus(_NEW);

    public java.lang.String getValue() {
        return _value_;
    }

    public static ChangeStatus fromValue(java.lang.String value) throws java.lang.IllegalArgumentException {
        ChangeStatus enumeration = (ChangeStatus) _table_.get(value);
        if (enumeration == null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }

    public static ChangeStatus fromString(java.lang.String value) throws java.lang.IllegalArgumentException {
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

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(ChangeStatus.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/ch/v201101", "ChangeStatus"));
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }
}

package com.google.api.ads.dfp.v201201;

public class InventoryStatus implements java.io.Serializable {

    private java.lang.String _value_;

    private static java.util.HashMap _table_ = new java.util.HashMap();

    protected InventoryStatus(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_, this);
    }

    public static final java.lang.String _ACTIVE = "ACTIVE";

    public static final java.lang.String _INACTIVE = "INACTIVE";

    public static final java.lang.String _ARCHIVED = "ARCHIVED";

    public static final InventoryStatus ACTIVE = new InventoryStatus(_ACTIVE);

    public static final InventoryStatus INACTIVE = new InventoryStatus(_INACTIVE);

    public static final InventoryStatus ARCHIVED = new InventoryStatus(_ARCHIVED);

    public java.lang.String getValue() {
        return _value_;
    }

    public static InventoryStatus fromValue(java.lang.String value) throws java.lang.IllegalArgumentException {
        InventoryStatus enumeration = (InventoryStatus) _table_.get(value);
        if (enumeration == null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }

    public static InventoryStatus fromString(java.lang.String value) throws java.lang.IllegalArgumentException {
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

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(InventoryStatus.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201201", "InventoryStatus"));
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }
}

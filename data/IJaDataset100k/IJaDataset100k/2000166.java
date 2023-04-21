package net.myphpshop.www.myPHPShopAdmin;

public class DataType implements java.io.Serializable {

    private java.lang.String _value_;

    private static java.util.HashMap _table_ = new java.util.HashMap();

    protected DataType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_, this);
    }

    public static final java.lang.String _Double = "Double";

    public static final java.lang.String _Integer = "Integer";

    public static final java.lang.String _Enum = "Enum";

    public static final java.lang.String _String = "String";

    public static final DataType Double = new DataType(_Double);

    public static final DataType Integer = new DataType(_Integer);

    public static final DataType Enum = new DataType(_Enum);

    public static final DataType String = new DataType(_String);

    public java.lang.String getValue() {
        return _value_;
    }

    public static DataType fromValue(java.lang.String value) throws java.lang.IllegalArgumentException {
        DataType enumeration = (DataType) _table_.get(value);
        if (enumeration == null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }

    public static DataType fromString(java.lang.String value) throws java.lang.IllegalArgumentException {
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

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(DataType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.myphpshop.net/myPHPShopAdmin", "DataType"));
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }
}

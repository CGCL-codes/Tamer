package org.icestandard.ICE.V20.message;

public class SenderTypeRole implements java.io.Serializable {

    private org.apache.axis.types.NMTokens _value_;

    private static java.util.HashMap _table_ = new java.util.HashMap();

    protected SenderTypeRole(org.apache.axis.types.NMTokens value) {
        _value_ = value;
        _table_.put(_value_, this);
    }

    public static final org.apache.axis.types.NMTokens _subscriber = new org.apache.axis.types.NMTokens("subscriber");

    public static final org.apache.axis.types.NMTokens _syndicator = new org.apache.axis.types.NMTokens("syndicator");

    public static final SenderTypeRole subscriber = new SenderTypeRole(_subscriber);

    public static final SenderTypeRole syndicator = new SenderTypeRole(_syndicator);

    public org.apache.axis.types.NMTokens getValue() {
        return _value_;
    }

    public static SenderTypeRole fromValue(org.apache.axis.types.NMTokens value) throws java.lang.IllegalArgumentException {
        SenderTypeRole enumeration = (SenderTypeRole) _table_.get(value);
        if (enumeration == null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }

    public static SenderTypeRole fromString(java.lang.String value) throws java.lang.IllegalArgumentException {
        try {
            return fromValue(new org.apache.axis.types.NMTokens(value));
        } catch (Exception e) {
            throw new java.lang.IllegalArgumentException();
        }
    }

    public boolean equals(java.lang.Object obj) {
        return (obj == this);
    }

    public int hashCode() {
        return toString().hashCode();
    }

    public java.lang.String toString() {
        return _value_.toString();
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

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(SenderTypeRole.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://icestandard.org/ICE/V20/message", ">senderType>role"));
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }
}

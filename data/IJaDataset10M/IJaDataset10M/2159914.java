package api.server.jUDDI.api_v2_uddi;

public class URLType implements java.io.Serializable {

    private org.apache.axis.types.NMToken _value_;

    private static java.util.HashMap _table_ = new java.util.HashMap();

    protected URLType(org.apache.axis.types.NMToken value) {
        _value_ = value;
        _table_.put(_value_, this);
    }

    public static final org.apache.axis.types.NMToken _mailto = new org.apache.axis.types.NMToken("mailto");

    public static final org.apache.axis.types.NMToken _http = new org.apache.axis.types.NMToken("http");

    public static final org.apache.axis.types.NMToken _https = new org.apache.axis.types.NMToken("https");

    public static final org.apache.axis.types.NMToken _ftp = new org.apache.axis.types.NMToken("ftp");

    public static final org.apache.axis.types.NMToken _fax = new org.apache.axis.types.NMToken("fax");

    public static final org.apache.axis.types.NMToken _phone = new org.apache.axis.types.NMToken("phone");

    public static final org.apache.axis.types.NMToken _other = new org.apache.axis.types.NMToken("other");

    public static final URLType mailto = new URLType(_mailto);

    public static final URLType http = new URLType(_http);

    public static final URLType https = new URLType(_https);

    public static final URLType ftp = new URLType(_ftp);

    public static final URLType fax = new URLType(_fax);

    public static final URLType phone = new URLType(_phone);

    public static final URLType other = new URLType(_other);

    public org.apache.axis.types.NMToken getValue() {
        return _value_;
    }

    public static URLType fromValue(org.apache.axis.types.NMToken value) throws java.lang.IllegalArgumentException {
        URLType enumeration = (URLType) _table_.get(value);
        if (enumeration == null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }

    public static URLType fromString(java.lang.String value) throws java.lang.IllegalArgumentException {
        try {
            return fromValue(new org.apache.axis.types.NMToken(value));
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

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(URLType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:uddi-org:api_v2", "URLType"));
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }
}

package ototype;

public class MsgSearchReq implements java.io.Serializable {

    private java.lang.String msgType;

    private java.lang.String version;

    private ototype.AuthInfo authInfo;

    private java.lang.String SQL;

    public MsgSearchReq() {
    }

    public MsgSearchReq(java.lang.String msgType, java.lang.String version, ototype.AuthInfo authInfo, java.lang.String SQL) {
        this.msgType = msgType;
        this.version = version;
        this.authInfo = authInfo;
        this.SQL = SQL;
    }

    /**
     * Gets the msgType value for this MsgSearchReq.
     * 
     * @return msgType
     */
    public java.lang.String getMsgType() {
        return msgType;
    }

    /**
     * Sets the msgType value for this MsgSearchReq.
     * 
     * @param msgType
     */
    public void setMsgType(java.lang.String msgType) {
        this.msgType = msgType;
    }

    /**
     * Gets the version value for this MsgSearchReq.
     * 
     * @return version
     */
    public java.lang.String getVersion() {
        return version;
    }

    /**
     * Sets the version value for this MsgSearchReq.
     * 
     * @param version
     */
    public void setVersion(java.lang.String version) {
        this.version = version;
    }

    /**
     * Gets the authInfo value for this MsgSearchReq.
     * 
     * @return authInfo
     */
    public ototype.AuthInfo getAuthInfo() {
        return authInfo;
    }

    /**
     * Sets the authInfo value for this MsgSearchReq.
     * 
     * @param authInfo
     */
    public void setAuthInfo(ototype.AuthInfo authInfo) {
        this.authInfo = authInfo;
    }

    /**
     * Gets the SQL value for this MsgSearchReq.
     * 
     * @return SQL
     */
    public java.lang.String getSQL() {
        return SQL;
    }

    /**
     * Sets the SQL value for this MsgSearchReq.
     * 
     * @param SQL
     */
    public void setSQL(java.lang.String SQL) {
        this.SQL = SQL;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MsgSearchReq)) return false;
        MsgSearchReq other = (MsgSearchReq) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.msgType == null && other.getMsgType() == null) || (this.msgType != null && this.msgType.equals(other.getMsgType()))) && ((this.version == null && other.getVersion() == null) || (this.version != null && this.version.equals(other.getVersion()))) && ((this.authInfo == null && other.getAuthInfo() == null) || (this.authInfo != null && this.authInfo.equals(other.getAuthInfo()))) && ((this.SQL == null && other.getSQL() == null) || (this.SQL != null && this.SQL.equals(other.getSQL())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;

    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getMsgType() != null) {
            _hashCode += getMsgType().hashCode();
        }
        if (getVersion() != null) {
            _hashCode += getVersion().hashCode();
        }
        if (getAuthInfo() != null) {
            _hashCode += getAuthInfo().hashCode();
        }
        if (getSQL() != null) {
            _hashCode += getSQL().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(MsgSearchReq.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:ototype", "MsgSearchReq"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("msgType");
        elemField.setXmlName(new javax.xml.namespace.QName("", "MsgType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("version");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Version"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("authInfo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "AuthInfo"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:ototype", "AuthInfo"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("SQL");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SQL"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(java.lang.String mechType, java.lang.Class _javaType, javax.xml.namespace.QName _xmlType) {
        return new org.apache.axis.encoding.ser.BeanSerializer(_javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(java.lang.String mechType, java.lang.Class _javaType, javax.xml.namespace.QName _xmlType) {
        return new org.apache.axis.encoding.ser.BeanDeserializer(_javaType, _xmlType, typeDesc);
    }
}

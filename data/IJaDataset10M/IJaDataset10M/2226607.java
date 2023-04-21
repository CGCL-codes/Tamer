package ototype;

public class MsgSendStatReq implements java.io.Serializable {

    private java.lang.String msgType;

    private java.lang.String version;

    private java.lang.String batchID;

    private ototype.AuthInfo authInfo;

    public MsgSendStatReq() {
    }

    public MsgSendStatReq(java.lang.String msgType, java.lang.String version, java.lang.String batchID, ototype.AuthInfo authInfo) {
        this.msgType = msgType;
        this.version = version;
        this.batchID = batchID;
        this.authInfo = authInfo;
    }

    /**
     * Gets the msgType value for this MsgSendStatReq.
     * 
     * @return msgType
     */
    public java.lang.String getMsgType() {
        return msgType;
    }

    /**
     * Sets the msgType value for this MsgSendStatReq.
     * 
     * @param msgType
     */
    public void setMsgType(java.lang.String msgType) {
        this.msgType = msgType;
    }

    /**
     * Gets the version value for this MsgSendStatReq.
     * 
     * @return version
     */
    public java.lang.String getVersion() {
        return version;
    }

    /**
     * Sets the version value for this MsgSendStatReq.
     * 
     * @param version
     */
    public void setVersion(java.lang.String version) {
        this.version = version;
    }

    /**
     * Gets the batchID value for this MsgSendStatReq.
     * 
     * @return batchID
     */
    public java.lang.String getBatchID() {
        return batchID;
    }

    /**
     * Sets the batchID value for this MsgSendStatReq.
     * 
     * @param batchID
     */
    public void setBatchID(java.lang.String batchID) {
        this.batchID = batchID;
    }

    /**
     * Gets the authInfo value for this MsgSendStatReq.
     * 
     * @return authInfo
     */
    public ototype.AuthInfo getAuthInfo() {
        return authInfo;
    }

    /**
     * Sets the authInfo value for this MsgSendStatReq.
     * 
     * @param authInfo
     */
    public void setAuthInfo(ototype.AuthInfo authInfo) {
        this.authInfo = authInfo;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MsgSendStatReq)) return false;
        MsgSendStatReq other = (MsgSendStatReq) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.msgType == null && other.getMsgType() == null) || (this.msgType != null && this.msgType.equals(other.getMsgType()))) && ((this.version == null && other.getVersion() == null) || (this.version != null && this.version.equals(other.getVersion()))) && ((this.batchID == null && other.getBatchID() == null) || (this.batchID != null && this.batchID.equals(other.getBatchID()))) && ((this.authInfo == null && other.getAuthInfo() == null) || (this.authInfo != null && this.authInfo.equals(other.getAuthInfo())));
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
        if (getBatchID() != null) {
            _hashCode += getBatchID().hashCode();
        }
        if (getAuthInfo() != null) {
            _hashCode += getAuthInfo().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(MsgSendStatReq.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:ototype", "MsgSendStatReq"));
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
        elemField.setFieldName("batchID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "BatchID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("authInfo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "AuthInfo"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:ototype", "AuthInfo"));
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

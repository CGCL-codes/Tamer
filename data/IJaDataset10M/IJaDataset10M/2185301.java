package cn.myapps.webservice.gmcczj.tempurl;

public class SendResult implements java.io.Serializable {

    private boolean success;

    private java.lang.String sendMsgID;

    private java.lang.String message;

    public SendResult() {
    }

    public SendResult(boolean success, java.lang.String sendMsgID, java.lang.String message) {
        this.success = success;
        this.sendMsgID = sendMsgID;
        this.message = message;
    }

    /**
     * Gets the success value for this SendResult.
     * 
     * @return success
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Sets the success value for this SendResult.
     * 
     * @param success
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * Gets the sendMsgID value for this SendResult.
     * 
     * @return sendMsgID
     */
    public java.lang.String getSendMsgID() {
        return sendMsgID;
    }

    /**
     * Sets the sendMsgID value for this SendResult.
     * 
     * @param sendMsgID
     */
    public void setSendMsgID(java.lang.String sendMsgID) {
        this.sendMsgID = sendMsgID;
    }

    /**
     * Gets the message value for this SendResult.
     * 
     * @return message
     */
    public java.lang.String getMessage() {
        return message;
    }

    /**
     * Sets the message value for this SendResult.
     * 
     * @param message
     */
    public void setMessage(java.lang.String message) {
        this.message = message;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SendResult)) return false;
        SendResult other = (SendResult) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && this.success == other.isSuccess() && ((this.sendMsgID == null && other.getSendMsgID() == null) || (this.sendMsgID != null && this.sendMsgID.equals(other.getSendMsgID()))) && ((this.message == null && other.getMessage() == null) || (this.message != null && this.message.equals(other.getMessage())));
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
        _hashCode += (isSuccess() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getSendMsgID() != null) {
            _hashCode += getSendMsgID().hashCode();
        }
        if (getMessage() != null) {
            _hashCode += getMessage().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(SendResult.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", "SendResult"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("success");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "Success"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sendMsgID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "SendMsgID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("message");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "Message"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
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

package archsw0904.controller.shopping.order;

public class HttpException extends org.apache.axis.AxisFault implements java.io.Serializable {

    private java.lang.String message1;

    private java.lang.String reason;

    private int reasonCode;

    public HttpException() {
    }

    public HttpException(java.lang.String message1, java.lang.String reason, int reasonCode) {
        this.message1 = message1;
        this.reason = reason;
        this.reasonCode = reasonCode;
    }

    /**
     * Gets the message1 value for this HttpException.
     * 
     * @return message1
     */
    public java.lang.String getMessage1() {
        return message1;
    }

    /**
     * Sets the message1 value for this HttpException.
     * 
     * @param message1
     */
    public void setMessage1(java.lang.String message1) {
        this.message1 = message1;
    }

    /**
     * Gets the reason value for this HttpException.
     * 
     * @return reason
     */
    public java.lang.String getReason() {
        return reason;
    }

    /**
     * Sets the reason value for this HttpException.
     * 
     * @param reason
     */
    public void setReason(java.lang.String reason) {
        this.reason = reason;
    }

    /**
     * Gets the reasonCode value for this HttpException.
     * 
     * @return reasonCode
     */
    public int getReasonCode() {
        return reasonCode;
    }

    /**
     * Sets the reasonCode value for this HttpException.
     * 
     * @param reasonCode
     */
    public void setReasonCode(int reasonCode) {
        this.reasonCode = reasonCode;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof HttpException)) return false;
        HttpException other = (HttpException) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.message1 == null && other.getMessage1() == null) || (this.message1 != null && this.message1.equals(other.getMessage1()))) && ((this.reason == null && other.getReason() == null) || (this.reason != null && this.reason.equals(other.getReason()))) && this.reasonCode == other.getReasonCode();
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
        if (getMessage1() != null) {
            _hashCode += getMessage1().hashCode();
        }
        if (getReason() != null) {
            _hashCode += getReason().hashCode();
        }
        _hashCode += getReasonCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(HttpException.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://order.shopping.controller.archsw0904/", "HttpException"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("message1");
        elemField.setXmlName(new javax.xml.namespace.QName("", "message"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reason");
        elemField.setXmlName(new javax.xml.namespace.QName("", "reason"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reasonCode");
        elemField.setXmlName(new javax.xml.namespace.QName("", "reasonCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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

    /**
     * Writes the exception data to the faultDetails
     */
    public void writeDetails(javax.xml.namespace.QName qname, org.apache.axis.encoding.SerializationContext context) throws java.io.IOException {
        context.serialize(qname, null, this);
    }
}

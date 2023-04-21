package com.google.api.adwords.v201008.cm;

/**
 * Represents a success result of processing a mutate operation that
 * returned
 *             a value.
 */
public class ReturnValueResult extends com.google.api.adwords.v201008.cm.OperationResult implements java.io.Serializable {

    private com.google.api.adwords.v201008.cm.Operand returnValue;

    public ReturnValueResult() {
    }

    public ReturnValueResult(java.lang.String operationResultType, com.google.api.adwords.v201008.cm.Operand returnValue) {
        super(operationResultType);
        this.returnValue = returnValue;
    }

    /**
     * Gets the returnValue value for this ReturnValueResult.
     * 
     * @return returnValue   * The value returned from successfully processing a mutate operation.
     */
    public com.google.api.adwords.v201008.cm.Operand getReturnValue() {
        return returnValue;
    }

    /**
     * Sets the returnValue value for this ReturnValueResult.
     * 
     * @param returnValue   * The value returned from successfully processing a mutate operation.
     */
    public void setReturnValue(com.google.api.adwords.v201008.cm.Operand returnValue) {
        this.returnValue = returnValue;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ReturnValueResult)) return false;
        ReturnValueResult other = (ReturnValueResult) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && ((this.returnValue == null && other.getReturnValue() == null) || (this.returnValue != null && this.returnValue.equals(other.getReturnValue())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;

    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (getReturnValue() != null) {
            _hashCode += getReturnValue().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(ReturnValueResult.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201008", "ReturnValueResult"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("returnValue");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201008", "returnValue"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201008", "Operand"));
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

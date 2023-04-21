package com.google.api.adwords.v201109.cm;

/**
 * Represents an operation on an {@link AdParam}. The supported operators
 * are {@code SET} and {@code REMOVE}.
 */
public class AdParamOperation extends com.google.api.adwords.v201109.cm.Operation implements java.io.Serializable {

    private com.google.api.adwords.v201109.cm.AdParam operand;

    public AdParamOperation() {
    }

    public AdParamOperation(com.google.api.adwords.v201109.cm.Operator operator, java.lang.String operationType, com.google.api.adwords.v201109.cm.AdParam operand) {
        super(operator, operationType);
        this.operand = operand;
    }

    /**
     * Gets the operand value for this AdParamOperation.
     * 
     * @return operand   * The ad parameter to operate on.
     *                     <span class="constraint Required">This field is
     * required and should not be {@code null}.</span>
     */
    public com.google.api.adwords.v201109.cm.AdParam getOperand() {
        return operand;
    }

    /**
     * Sets the operand value for this AdParamOperation.
     * 
     * @param operand   * The ad parameter to operate on.
     *                     <span class="constraint Required">This field is
     * required and should not be {@code null}.</span>
     */
    public void setOperand(com.google.api.adwords.v201109.cm.AdParam operand) {
        this.operand = operand;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AdParamOperation)) return false;
        AdParamOperation other = (AdParamOperation) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && ((this.operand == null && other.getOperand() == null) || (this.operand != null && this.operand.equals(other.getOperand())));
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
        if (getOperand() != null) {
            _hashCode += getOperand().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(AdParamOperation.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "AdParamOperation"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("operand");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "operand"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "AdParam"));
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

package org.isurf.cpfr;

public class ReceivePurchaseConditionsMessage implements java.io.Serializable {

    private PurchaseConditions_2.xsd.schema.ubl.specification.names.oasis.PurchaseConditionsType arg0;

    public ReceivePurchaseConditionsMessage() {
    }

    public ReceivePurchaseConditionsMessage(PurchaseConditions_2.xsd.schema.ubl.specification.names.oasis.PurchaseConditionsType arg0) {
        this.arg0 = arg0;
    }

    /**
     * Gets the arg0 value for this ReceivePurchaseConditionsMessage.
     * 
     * @return arg0
     */
    public PurchaseConditions_2.xsd.schema.ubl.specification.names.oasis.PurchaseConditionsType getArg0() {
        return arg0;
    }

    /**
     * Sets the arg0 value for this ReceivePurchaseConditionsMessage.
     * 
     * @param arg0
     */
    public void setArg0(PurchaseConditions_2.xsd.schema.ubl.specification.names.oasis.PurchaseConditionsType arg0) {
        this.arg0 = arg0;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ReceivePurchaseConditionsMessage)) return false;
        ReceivePurchaseConditionsMessage other = (ReceivePurchaseConditionsMessage) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.arg0 == null && other.getArg0() == null) || (this.arg0 != null && this.arg0.equals(other.getArg0())));
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
        if (getArg0() != null) {
            _hashCode += getArg0().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(ReceivePurchaseConditionsMessage.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://cpfr.isurf.org/", "receivePurchaseConditionsMessage"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("arg0");
        elemField.setXmlName(new javax.xml.namespace.QName("", "arg0"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:PurchaseConditions-2", "PurchaseConditionsType"));
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

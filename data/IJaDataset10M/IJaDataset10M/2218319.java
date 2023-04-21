package com.google.api.adwords.v200909.cm;

/**
 * A container for return values from the AdGroupAdService.
 */
public class AdGroupAdReturnValue extends com.google.api.adwords.v200909.cm.ListReturnValue implements java.io.Serializable {

    private com.google.api.adwords.v200909.cm.AdGroupAd[] value;

    public AdGroupAdReturnValue() {
    }

    public AdGroupAdReturnValue(java.lang.String listReturnValueType, com.google.api.adwords.v200909.cm.AdGroupAd[] value) {
        super(listReturnValueType);
        this.value = value;
    }

    /**
     * Gets the value value for this AdGroupAdReturnValue.
     * 
     * @return value
     */
    public com.google.api.adwords.v200909.cm.AdGroupAd[] getValue() {
        return value;
    }

    /**
     * Sets the value value for this AdGroupAdReturnValue.
     * 
     * @param value
     */
    public void setValue(com.google.api.adwords.v200909.cm.AdGroupAd[] value) {
        this.value = value;
    }

    public com.google.api.adwords.v200909.cm.AdGroupAd getValue(int i) {
        return this.value[i];
    }

    public void setValue(int i, com.google.api.adwords.v200909.cm.AdGroupAd _value) {
        this.value[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AdGroupAdReturnValue)) return false;
        AdGroupAdReturnValue other = (AdGroupAdReturnValue) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && ((this.value == null && other.getValue() == null) || (this.value != null && java.util.Arrays.equals(this.value, other.getValue())));
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
        if (getValue() != null) {
            for (int i = 0; i < java.lang.reflect.Array.getLength(getValue()); i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getValue(), i);
                if (obj != null && !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(AdGroupAdReturnValue.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "AdGroupAdReturnValue"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("value");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "value"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "AdGroupAd"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
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

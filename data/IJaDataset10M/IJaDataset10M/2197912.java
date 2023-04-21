package com.google.api.adwords.v201109.o;

/**
 * Represents a {@link TargetingIdea} returned by search criteria
 * specified in
 *             the {@link TargetingIdeaSelector}. Targeting ideas are
 * keywords or placements
 *             that are similar to those the user inputs.
 */
public class TargetingIdea implements java.io.Serializable {

    private com.google.api.adwords.v201109.o.Type_AttributeMapEntry[] data;

    public TargetingIdea() {
    }

    public TargetingIdea(com.google.api.adwords.v201109.o.Type_AttributeMapEntry[] data) {
        this.data = data;
    }

    /**
     * Gets the data value for this TargetingIdea.
     * 
     * @return data   * Map of
     *                 {@link com.google.ads.api.services.targetingideas.external.AttributeType}
     * to {@link Attribute}. Stores all data retrieved for each key {@code
     * AttributeType}.
     *                 <span class="constraint Required">This field is required
     * and should not be {@code null}.</span>
     */
    public com.google.api.adwords.v201109.o.Type_AttributeMapEntry[] getData() {
        return data;
    }

    /**
     * Sets the data value for this TargetingIdea.
     * 
     * @param data   * Map of
     *                 {@link com.google.ads.api.services.targetingideas.external.AttributeType}
     * to {@link Attribute}. Stores all data retrieved for each key {@code
     * AttributeType}.
     *                 <span class="constraint Required">This field is required
     * and should not be {@code null}.</span>
     */
    public void setData(com.google.api.adwords.v201109.o.Type_AttributeMapEntry[] data) {
        this.data = data;
    }

    public com.google.api.adwords.v201109.o.Type_AttributeMapEntry getData(int i) {
        return this.data[i];
    }

    public void setData(int i, com.google.api.adwords.v201109.o.Type_AttributeMapEntry _value) {
        this.data[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TargetingIdea)) return false;
        TargetingIdea other = (TargetingIdea) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.data == null && other.getData() == null) || (this.data != null && java.util.Arrays.equals(this.data, other.getData())));
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
        if (getData() != null) {
            for (int i = 0; i < java.lang.reflect.Array.getLength(getData()); i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getData(), i);
                if (obj != null && !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(TargetingIdea.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/o/v201109", "TargetingIdea"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("data");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/o/v201109", "data"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/o/v201109", "Type_AttributeMapEntry"));
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

package com.google.api.adwords.v201109.o;

/**
 * {@link Attribute} type that contains an {@link IdeaType} value.
 * For example,
 *             if a {@link com.google.ads.api.services.targetingideas.TargetingIdea}
 * represents a keyword idea, its {@link IdeaTypeAttribute} would contain
 * a
 *             {@code KEYWORD} {@link IdeaType}.
 */
public class IdeaTypeAttribute extends com.google.api.adwords.v201109.o.Attribute implements java.io.Serializable {

    private com.google.api.adwords.v201109.o.IdeaType value;

    public IdeaTypeAttribute() {
    }

    public IdeaTypeAttribute(java.lang.String attributeType, com.google.api.adwords.v201109.o.IdeaType value) {
        super(attributeType);
        this.value = value;
    }

    /**
     * Gets the value value for this IdeaTypeAttribute.
     * 
     * @return value   * {@link IdeaType} value contained by this {@link Attribute}.
     * <span class="constraint Required">This field is required and should
     * not be {@code null}.</span>
     */
    public com.google.api.adwords.v201109.o.IdeaType getValue() {
        return value;
    }

    /**
     * Sets the value value for this IdeaTypeAttribute.
     * 
     * @param value   * {@link IdeaType} value contained by this {@link Attribute}.
     * <span class="constraint Required">This field is required and should
     * not be {@code null}.</span>
     */
    public void setValue(com.google.api.adwords.v201109.o.IdeaType value) {
        this.value = value;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof IdeaTypeAttribute)) return false;
        IdeaTypeAttribute other = (IdeaTypeAttribute) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && ((this.value == null && other.getValue() == null) || (this.value != null && this.value.equals(other.getValue())));
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
            _hashCode += getValue().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(IdeaTypeAttribute.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/o/v201109", "IdeaTypeAttribute"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("value");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/o/v201109", "value"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/o/v201109", "IdeaType"));
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

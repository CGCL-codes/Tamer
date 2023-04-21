package com.google.api.adwords.v200909.cm;

/**
 * Content Label for category exclusion.
 */
public class ContentLabel extends com.google.api.adwords.v200909.cm.Criterion implements java.io.Serializable {

    private com.google.api.adwords.v200909.cm.ContentLabelType contentLabelType;

    public ContentLabel() {
    }

    public ContentLabel(java.lang.Long id, java.lang.String criterionType, com.google.api.adwords.v200909.cm.ContentLabelType contentLabelType) {
        super(id, criterionType);
        this.contentLabelType = contentLabelType;
    }

    /**
     * Gets the contentLabelType value for this ContentLabel.
     * 
     * @return contentLabelType   * Content label type
     *                     <span class="constraint Required">This field is
     * required and should not be {@code null}.</span>
     */
    public com.google.api.adwords.v200909.cm.ContentLabelType getContentLabelType() {
        return contentLabelType;
    }

    /**
     * Sets the contentLabelType value for this ContentLabel.
     * 
     * @param contentLabelType   * Content label type
     *                     <span class="constraint Required">This field is
     * required and should not be {@code null}.</span>
     */
    public void setContentLabelType(com.google.api.adwords.v200909.cm.ContentLabelType contentLabelType) {
        this.contentLabelType = contentLabelType;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ContentLabel)) return false;
        ContentLabel other = (ContentLabel) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && ((this.contentLabelType == null && other.getContentLabelType() == null) || (this.contentLabelType != null && this.contentLabelType.equals(other.getContentLabelType())));
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
        if (getContentLabelType() != null) {
            _hashCode += getContentLabelType().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(ContentLabel.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "ContentLabel"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("contentLabelType");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "contentLabelType"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "ContentLabelType"));
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

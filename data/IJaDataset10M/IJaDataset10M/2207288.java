package com.google.api.adwords.v200909.cm;

/**
 * Base class for AdExtension objects. An AdExtension is an extension
 * to
 *             an existing ad or metadata that will process into an extension.
 * The class is concrete, so ad extensions can be added/removed to campaigns
 * by referring to the id.
 */
public class AdExtension implements java.io.Serializable {

    private java.lang.Long id;

    private java.lang.String adExtensionType;

    public AdExtension() {
    }

    public AdExtension(java.lang.Long id, java.lang.String adExtensionType) {
        this.id = id;
        this.adExtensionType = adExtensionType;
    }

    /**
     * Gets the id value for this AdExtension.
     * 
     * @return id   * Id of ad extension
     */
    public java.lang.Long getId() {
        return id;
    }

    /**
     * Sets the id value for this AdExtension.
     * 
     * @param id   * Id of ad extension
     */
    public void setId(java.lang.Long id) {
        this.id = id;
    }

    /**
     * Gets the adExtensionType value for this AdExtension.
     * 
     * @return adExtensionType   * This field indicates the subtype of AdExtension of this instance.
     * It is ignored
     *                 on input, and instead xsi:type should be specified.
     */
    public java.lang.String getAdExtensionType() {
        return adExtensionType;
    }

    /**
     * Sets the adExtensionType value for this AdExtension.
     * 
     * @param adExtensionType   * This field indicates the subtype of AdExtension of this instance.
     * It is ignored
     *                 on input, and instead xsi:type should be specified.
     */
    public void setAdExtensionType(java.lang.String adExtensionType) {
        this.adExtensionType = adExtensionType;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AdExtension)) return false;
        AdExtension other = (AdExtension) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.id == null && other.getId() == null) || (this.id != null && this.id.equals(other.getId()))) && ((this.adExtensionType == null && other.getAdExtensionType() == null) || (this.adExtensionType != null && this.adExtensionType.equals(other.getAdExtensionType())));
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
        if (getId() != null) {
            _hashCode += getId().hashCode();
        }
        if (getAdExtensionType() != null) {
            _hashCode += getAdExtensionType().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(AdExtension.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "AdExtension"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("adExtensionType");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "AdExtension.Type"));
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

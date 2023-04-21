package com.google.api.ads.dfp.v201111;

/**
 * {@code Stats} contains trafficking statistics for {@link LineItem}
 * and
 *             {@link LineItemCreativeAssociation} objects
 */
public class Stats implements java.io.Serializable {

    private java.lang.Long impressionsDelivered;

    private java.lang.Long clicksDelivered;

    public Stats() {
    }

    public Stats(java.lang.Long impressionsDelivered, java.lang.Long clicksDelivered) {
        this.impressionsDelivered = impressionsDelivered;
        this.clicksDelivered = clicksDelivered;
    }

    /**
     * Gets the impressionsDelivered value for this Stats.
     * 
     * @return impressionsDelivered   * The number of impressions delivered.
     */
    public java.lang.Long getImpressionsDelivered() {
        return impressionsDelivered;
    }

    /**
     * Sets the impressionsDelivered value for this Stats.
     * 
     * @param impressionsDelivered   * The number of impressions delivered.
     */
    public void setImpressionsDelivered(java.lang.Long impressionsDelivered) {
        this.impressionsDelivered = impressionsDelivered;
    }

    /**
     * Gets the clicksDelivered value for this Stats.
     * 
     * @return clicksDelivered   * The number of clicks delivered.
     */
    public java.lang.Long getClicksDelivered() {
        return clicksDelivered;
    }

    /**
     * Sets the clicksDelivered value for this Stats.
     * 
     * @param clicksDelivered   * The number of clicks delivered.
     */
    public void setClicksDelivered(java.lang.Long clicksDelivered) {
        this.clicksDelivered = clicksDelivered;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Stats)) return false;
        Stats other = (Stats) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.impressionsDelivered == null && other.getImpressionsDelivered() == null) || (this.impressionsDelivered != null && this.impressionsDelivered.equals(other.getImpressionsDelivered()))) && ((this.clicksDelivered == null && other.getClicksDelivered() == null) || (this.clicksDelivered != null && this.clicksDelivered.equals(other.getClicksDelivered())));
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
        if (getImpressionsDelivered() != null) {
            _hashCode += getImpressionsDelivered().hashCode();
        }
        if (getClicksDelivered() != null) {
            _hashCode += getClicksDelivered().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(Stats.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201111", "Stats"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("impressionsDelivered");
        elemField.setXmlName(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201111", "impressionsDelivered"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("clicksDelivered");
        elemField.setXmlName(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201111", "clicksDelivered"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
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

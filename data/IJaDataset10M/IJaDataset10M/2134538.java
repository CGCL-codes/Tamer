package com.google.api.ads.dfp.v201201;

/**
 * A creative that is used for tracking clicks on ads that are served
 * directly
 *             from the customers' web servers or media servers.
 *             NOTE: The size attribute is not used for click tracking
 * creative and it will
 *             not be persisted upon save.
 */
public class ClickTrackingCreative extends com.google.api.ads.dfp.v201201.Creative implements java.io.Serializable {

    private java.lang.String clickTrackingUrl;

    public ClickTrackingCreative() {
    }

    public ClickTrackingCreative(java.lang.Long advertiserId, java.lang.Long id, java.lang.String name, com.google.api.ads.dfp.v201201.Size size, java.lang.String previewUrl, com.google.api.ads.dfp.v201201.AppliedLabel[] appliedLabels, com.google.api.ads.dfp.v201201.DateTime lastModifiedDateTime, java.lang.String creativeType, java.lang.String clickTrackingUrl) {
        super(advertiserId, id, name, size, previewUrl, appliedLabels, lastModifiedDateTime, creativeType);
        this.clickTrackingUrl = clickTrackingUrl;
    }

    /**
     * Gets the clickTrackingUrl value for this ClickTrackingCreative.
     * 
     * @return clickTrackingUrl   * The click tracking URL. This attribute is required.
     */
    public java.lang.String getClickTrackingUrl() {
        return clickTrackingUrl;
    }

    /**
     * Sets the clickTrackingUrl value for this ClickTrackingCreative.
     * 
     * @param clickTrackingUrl   * The click tracking URL. This attribute is required.
     */
    public void setClickTrackingUrl(java.lang.String clickTrackingUrl) {
        this.clickTrackingUrl = clickTrackingUrl;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ClickTrackingCreative)) return false;
        ClickTrackingCreative other = (ClickTrackingCreative) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && ((this.clickTrackingUrl == null && other.getClickTrackingUrl() == null) || (this.clickTrackingUrl != null && this.clickTrackingUrl.equals(other.getClickTrackingUrl())));
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
        if (getClickTrackingUrl() != null) {
            _hashCode += getClickTrackingUrl().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(ClickTrackingCreative.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201201", "ClickTrackingCreative"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("clickTrackingUrl");
        elemField.setXmlName(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201201", "clickTrackingUrl"));
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

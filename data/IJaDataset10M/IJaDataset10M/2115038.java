package com.google.api.ads.dfp.v201108;

/**
 * Represents browser languages that are being targeted or excluded
 * by the
 *             {@link LineItem}.
 */
public class BrowserLanguageTargeting implements java.io.Serializable {

    private java.lang.Boolean isTargeted;

    private com.google.api.ads.dfp.v201108.Technology[] browserLanguages;

    public BrowserLanguageTargeting() {
    }

    public BrowserLanguageTargeting(java.lang.Boolean isTargeted, com.google.api.ads.dfp.v201108.Technology[] browserLanguages) {
        this.isTargeted = isTargeted;
        this.browserLanguages = browserLanguages;
    }

    /**
     * Gets the isTargeted value for this BrowserLanguageTargeting.
     * 
     * @return isTargeted   * Indicates whether browsers languages should be targeted or
     * excluded. This
     *                 attribute is optional and defaults to {@code true}.
     */
    public java.lang.Boolean getIsTargeted() {
        return isTargeted;
    }

    /**
     * Sets the isTargeted value for this BrowserLanguageTargeting.
     * 
     * @param isTargeted   * Indicates whether browsers languages should be targeted or
     * excluded. This
     *                 attribute is optional and defaults to {@code true}.
     */
    public void setIsTargeted(java.lang.Boolean isTargeted) {
        this.isTargeted = isTargeted;
    }

    /**
     * Gets the browserLanguages value for this BrowserLanguageTargeting.
     * 
     * @return browserLanguages   * Browser languages that are being targeted or excluded by the
     * {@link LineItem}.
     */
    public com.google.api.ads.dfp.v201108.Technology[] getBrowserLanguages() {
        return browserLanguages;
    }

    /**
     * Sets the browserLanguages value for this BrowserLanguageTargeting.
     * 
     * @param browserLanguages   * Browser languages that are being targeted or excluded by the
     * {@link LineItem}.
     */
    public void setBrowserLanguages(com.google.api.ads.dfp.v201108.Technology[] browserLanguages) {
        this.browserLanguages = browserLanguages;
    }

    public com.google.api.ads.dfp.v201108.Technology getBrowserLanguages(int i) {
        return this.browserLanguages[i];
    }

    public void setBrowserLanguages(int i, com.google.api.ads.dfp.v201108.Technology _value) {
        this.browserLanguages[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof BrowserLanguageTargeting)) return false;
        BrowserLanguageTargeting other = (BrowserLanguageTargeting) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.isTargeted == null && other.getIsTargeted() == null) || (this.isTargeted != null && this.isTargeted.equals(other.getIsTargeted()))) && ((this.browserLanguages == null && other.getBrowserLanguages() == null) || (this.browserLanguages != null && java.util.Arrays.equals(this.browserLanguages, other.getBrowserLanguages())));
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
        if (getIsTargeted() != null) {
            _hashCode += getIsTargeted().hashCode();
        }
        if (getBrowserLanguages() != null) {
            for (int i = 0; i < java.lang.reflect.Array.getLength(getBrowserLanguages()); i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getBrowserLanguages(), i);
                if (obj != null && !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(BrowserLanguageTargeting.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201108", "BrowserLanguageTargeting"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("isTargeted");
        elemField.setXmlName(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201108", "isTargeted"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("browserLanguages");
        elemField.setXmlName(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201108", "browserLanguages"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201108", "Technology"));
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

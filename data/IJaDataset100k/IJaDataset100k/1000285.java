package com.google.api.adwords.v201101.cm;

/**
 * Represents bids at the adgroup level, which could be of different
 * subtypes based on
 *             campaign's bidding strategy.
 */
public abstract class AdGroupBids implements java.io.Serializable {

    private java.lang.String adGroupBidsType;

    public AdGroupBids() {
    }

    public AdGroupBids(java.lang.String adGroupBidsType) {
        this.adGroupBidsType = adGroupBidsType;
    }

    /**
     * Gets the adGroupBidsType value for this AdGroupBids.
     * 
     * @return adGroupBidsType   * This field indicates the subtype of AdGroupBids of this instance.
     * It is ignored
     *                 on input, and instead xsi:type should be specified.
     */
    public java.lang.String getAdGroupBidsType() {
        return adGroupBidsType;
    }

    /**
     * Sets the adGroupBidsType value for this AdGroupBids.
     * 
     * @param adGroupBidsType   * This field indicates the subtype of AdGroupBids of this instance.
     * It is ignored
     *                 on input, and instead xsi:type should be specified.
     */
    public void setAdGroupBidsType(java.lang.String adGroupBidsType) {
        this.adGroupBidsType = adGroupBidsType;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AdGroupBids)) return false;
        AdGroupBids other = (AdGroupBids) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.adGroupBidsType == null && other.getAdGroupBidsType() == null) || (this.adGroupBidsType != null && this.adGroupBidsType.equals(other.getAdGroupBidsType())));
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
        if (getAdGroupBidsType() != null) {
            _hashCode += getAdGroupBidsType().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(AdGroupBids.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201101", "AdGroupBids"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("adGroupBidsType");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201101", "AdGroupBids.Type"));
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

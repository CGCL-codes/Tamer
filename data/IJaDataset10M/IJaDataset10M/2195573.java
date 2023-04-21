package com.google.api.adwords.v200909.cm;

/**
 * Adgroup level bids used in budget optimizer bidding strategy.
 */
public class BudgetOptimizerAdGroupBids extends com.google.api.adwords.v200909.cm.AdGroupBids implements java.io.Serializable {

    private com.google.api.adwords.v200909.cm.Bid proxyKeywordMaxCpc;

    private com.google.api.adwords.v200909.cm.Bid proxySiteMaxCpc;

    public BudgetOptimizerAdGroupBids() {
    }

    public BudgetOptimizerAdGroupBids(java.lang.String adGroupBidsType, com.google.api.adwords.v200909.cm.Bid proxyKeywordMaxCpc, com.google.api.adwords.v200909.cm.Bid proxySiteMaxCpc) {
        super(adGroupBidsType);
        this.proxyKeywordMaxCpc = proxyKeywordMaxCpc;
        this.proxySiteMaxCpc = proxySiteMaxCpc;
    }

    /**
     * Gets the proxyKeywordMaxCpc value for this BudgetOptimizerAdGroupBids.
     * 
     * @return proxyKeywordMaxCpc   * Proxy bid set by budget optimizer:
     *                     <span class="constraint ReadOnly">This field is
     * read only and should not be set.  If this field is sent to the API,
     * it will be ignored.</span>
     */
    public com.google.api.adwords.v200909.cm.Bid getProxyKeywordMaxCpc() {
        return proxyKeywordMaxCpc;
    }

    /**
     * Sets the proxyKeywordMaxCpc value for this BudgetOptimizerAdGroupBids.
     * 
     * @param proxyKeywordMaxCpc   * Proxy bid set by budget optimizer:
     *                     <span class="constraint ReadOnly">This field is
     * read only and should not be set.  If this field is sent to the API,
     * it will be ignored.</span>
     */
    public void setProxyKeywordMaxCpc(com.google.api.adwords.v200909.cm.Bid proxyKeywordMaxCpc) {
        this.proxyKeywordMaxCpc = proxyKeywordMaxCpc;
    }

    /**
     * Gets the proxySiteMaxCpc value for this BudgetOptimizerAdGroupBids.
     * 
     * @return proxySiteMaxCpc   * Proxy bid set by budget optimizer:
     *                     <span class="constraint ReadOnly">This field is
     * read only and should not be set.  If this field is sent to the API,
     * it will be ignored.</span>
     */
    public com.google.api.adwords.v200909.cm.Bid getProxySiteMaxCpc() {
        return proxySiteMaxCpc;
    }

    /**
     * Sets the proxySiteMaxCpc value for this BudgetOptimizerAdGroupBids.
     * 
     * @param proxySiteMaxCpc   * Proxy bid set by budget optimizer:
     *                     <span class="constraint ReadOnly">This field is
     * read only and should not be set.  If this field is sent to the API,
     * it will be ignored.</span>
     */
    public void setProxySiteMaxCpc(com.google.api.adwords.v200909.cm.Bid proxySiteMaxCpc) {
        this.proxySiteMaxCpc = proxySiteMaxCpc;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof BudgetOptimizerAdGroupBids)) return false;
        BudgetOptimizerAdGroupBids other = (BudgetOptimizerAdGroupBids) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && ((this.proxyKeywordMaxCpc == null && other.getProxyKeywordMaxCpc() == null) || (this.proxyKeywordMaxCpc != null && this.proxyKeywordMaxCpc.equals(other.getProxyKeywordMaxCpc()))) && ((this.proxySiteMaxCpc == null && other.getProxySiteMaxCpc() == null) || (this.proxySiteMaxCpc != null && this.proxySiteMaxCpc.equals(other.getProxySiteMaxCpc())));
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
        if (getProxyKeywordMaxCpc() != null) {
            _hashCode += getProxyKeywordMaxCpc().hashCode();
        }
        if (getProxySiteMaxCpc() != null) {
            _hashCode += getProxySiteMaxCpc().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(BudgetOptimizerAdGroupBids.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "BudgetOptimizerAdGroupBids"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("proxyKeywordMaxCpc");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "proxyKeywordMaxCpc"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "Bid"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("proxySiteMaxCpc");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "proxySiteMaxCpc"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "Bid"));
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

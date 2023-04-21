package com.google.api.adwords.v201101.cm;

/**
 * Used to switch a campaign's bidding strategy to conversion optimizer.
 */
public class ConversionOptimizerBiddingTransition extends com.google.api.adwords.v201101.cm.BiddingTransition implements java.io.Serializable {

    private java.lang.Boolean useSavedBids;

    public ConversionOptimizerBiddingTransition() {
    }

    public ConversionOptimizerBiddingTransition(com.google.api.adwords.v201101.cm.BiddingStrategy targetBiddingStrategy, com.google.api.adwords.v201101.cm.AdGroupBids explicitAdGroupBids, java.lang.String biddingTransitionType, java.lang.Boolean useSavedBids) {
        super(targetBiddingStrategy, explicitAdGroupBids, biddingTransitionType);
        this.useSavedBids = useSavedBids;
    }

    /**
     * Gets the useSavedBids value for this ConversionOptimizerBiddingTransition.
     * 
     * @return useSavedBids   * Indicate to use the existing bids, if there are previously
     * saved CPA bids
     *                     for the AdGroup during transition of campaign
     * from
     *                     {@link ManualCPC} to {@link ConversionOptimizer}.
     */
    public java.lang.Boolean getUseSavedBids() {
        return useSavedBids;
    }

    /**
     * Sets the useSavedBids value for this ConversionOptimizerBiddingTransition.
     * 
     * @param useSavedBids   * Indicate to use the existing bids, if there are previously
     * saved CPA bids
     *                     for the AdGroup during transition of campaign
     * from
     *                     {@link ManualCPC} to {@link ConversionOptimizer}.
     */
    public void setUseSavedBids(java.lang.Boolean useSavedBids) {
        this.useSavedBids = useSavedBids;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ConversionOptimizerBiddingTransition)) return false;
        ConversionOptimizerBiddingTransition other = (ConversionOptimizerBiddingTransition) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && ((this.useSavedBids == null && other.getUseSavedBids() == null) || (this.useSavedBids != null && this.useSavedBids.equals(other.getUseSavedBids())));
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
        if (getUseSavedBids() != null) {
            _hashCode += getUseSavedBids().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(ConversionOptimizerBiddingTransition.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201101", "ConversionOptimizerBiddingTransition"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("useSavedBids");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201101", "useSavedBids"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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

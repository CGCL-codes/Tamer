package com.google.api.adwords.v201003.cm;

/**
 * A selector used to specify which adgroups should be returned.
 */
public class AdGroupSelector implements java.io.Serializable {

    private long[] campaignIds;

    private long[] adGroupIds;

    private com.google.api.adwords.v201003.cm.StatsSelector statsSelector;

    private com.google.api.adwords.v201003.cm.Paging paging;

    public AdGroupSelector() {
    }

    public AdGroupSelector(long[] campaignIds, long[] adGroupIds, com.google.api.adwords.v201003.cm.StatsSelector statsSelector, com.google.api.adwords.v201003.cm.Paging paging) {
        this.campaignIds = campaignIds;
        this.adGroupIds = adGroupIds;
        this.statsSelector = statsSelector;
        this.paging = paging;
    }

    /**
     * Gets the campaignIds value for this AdGroupSelector.
     * 
     * @return campaignIds   * The list of possible campaigns to be selected.
     *                 An empty list indicates all account campaigns as possibly
     * being selected.
     *                 <span class="constraint ContentsDistinct">This field
     * must contain distinct elements.</span>
     *                 <span class="constraint ContentsNotNull">This field
     * must not contain {@code null} elements.</span>
     */
    public long[] getCampaignIds() {
        return campaignIds;
    }

    /**
     * Sets the campaignIds value for this AdGroupSelector.
     * 
     * @param campaignIds   * The list of possible campaigns to be selected.
     *                 An empty list indicates all account campaigns as possibly
     * being selected.
     *                 <span class="constraint ContentsDistinct">This field
     * must contain distinct elements.</span>
     *                 <span class="constraint ContentsNotNull">This field
     * must not contain {@code null} elements.</span>
     */
    public void setCampaignIds(long[] campaignIds) {
        this.campaignIds = campaignIds;
    }

    public long getCampaignIds(int i) {
        return this.campaignIds[i];
    }

    public void setCampaignIds(int i, long _value) {
        this.campaignIds[i] = _value;
    }

    /**
     * Gets the adGroupIds value for this AdGroupSelector.
     * 
     * @return adGroupIds   * List of adgroup ids to select.
     *                 <span class="constraint ContentsDistinct">This field
     * must contain distinct elements.</span>
     *                 <span class="constraint ContentsNotNull">This field
     * must not contain {@code null} elements.</span>
     */
    public long[] getAdGroupIds() {
        return adGroupIds;
    }

    /**
     * Sets the adGroupIds value for this AdGroupSelector.
     * 
     * @param adGroupIds   * List of adgroup ids to select.
     *                 <span class="constraint ContentsDistinct">This field
     * must contain distinct elements.</span>
     *                 <span class="constraint ContentsNotNull">This field
     * must not contain {@code null} elements.</span>
     */
    public void setAdGroupIds(long[] adGroupIds) {
        this.adGroupIds = adGroupIds;
    }

    public long getAdGroupIds(int i) {
        return this.adGroupIds[i];
    }

    public void setAdGroupIds(int i, long _value) {
        this.adGroupIds[i] = _value;
    }

    /**
     * Gets the statsSelector value for this AdGroupSelector.
     * 
     * @return statsSelector   * Selects stats that the returned adgroups will include.
     */
    public com.google.api.adwords.v201003.cm.StatsSelector getStatsSelector() {
        return statsSelector;
    }

    /**
     * Sets the statsSelector value for this AdGroupSelector.
     * 
     * @param statsSelector   * Selects stats that the returned adgroups will include.
     */
    public void setStatsSelector(com.google.api.adwords.v201003.cm.StatsSelector statsSelector) {
        this.statsSelector = statsSelector;
    }

    /**
     * Gets the paging value for this AdGroupSelector.
     * 
     * @return paging
     */
    public com.google.api.adwords.v201003.cm.Paging getPaging() {
        return paging;
    }

    /**
     * Sets the paging value for this AdGroupSelector.
     * 
     * @param paging
     */
    public void setPaging(com.google.api.adwords.v201003.cm.Paging paging) {
        this.paging = paging;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AdGroupSelector)) return false;
        AdGroupSelector other = (AdGroupSelector) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.campaignIds == null && other.getCampaignIds() == null) || (this.campaignIds != null && java.util.Arrays.equals(this.campaignIds, other.getCampaignIds()))) && ((this.adGroupIds == null && other.getAdGroupIds() == null) || (this.adGroupIds != null && java.util.Arrays.equals(this.adGroupIds, other.getAdGroupIds()))) && ((this.statsSelector == null && other.getStatsSelector() == null) || (this.statsSelector != null && this.statsSelector.equals(other.getStatsSelector()))) && ((this.paging == null && other.getPaging() == null) || (this.paging != null && this.paging.equals(other.getPaging())));
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
        if (getCampaignIds() != null) {
            for (int i = 0; i < java.lang.reflect.Array.getLength(getCampaignIds()); i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getCampaignIds(), i);
                if (obj != null && !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getAdGroupIds() != null) {
            for (int i = 0; i < java.lang.reflect.Array.getLength(getAdGroupIds()); i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAdGroupIds(), i);
                if (obj != null && !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getStatsSelector() != null) {
            _hashCode += getStatsSelector().hashCode();
        }
        if (getPaging() != null) {
            _hashCode += getPaging().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(AdGroupSelector.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "AdGroupSelector"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("campaignIds");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "campaignIds"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("adGroupIds");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "adGroupIds"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("statsSelector");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "statsSelector"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "StatsSelector"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("paging");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "paging"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "Paging"));
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

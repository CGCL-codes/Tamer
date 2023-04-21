package com.google.api.adwords.v201101.cm;

/**
 * Represents stats specific to Campaigns.
 */
public class CampaignStats extends com.google.api.adwords.v201101.cm.Stats implements java.io.Serializable {

    public CampaignStats() {
    }

    public CampaignStats(java.lang.String startDate, java.lang.String endDate, com.google.api.adwords.v201101.cm.StatsNetwork network, java.lang.Long clicks, java.lang.Long impressions, com.google.api.adwords.v201101.cm.Money cost, java.lang.Double averagePosition, com.google.api.adwords.v201101.cm.Money averageCpc, com.google.api.adwords.v201101.cm.Money averageCpm, java.lang.Double ctr, java.lang.Long conversions, java.lang.Double conversionRate, com.google.api.adwords.v201101.cm.Money costPerConversion, java.lang.Long conversionsManyPerClick, java.lang.Double conversionRateManyPerClick, com.google.api.adwords.v201101.cm.Money costPerConversionManyPerClick, java.lang.Long viewThroughConversions, java.lang.Long totalConvValue, java.lang.Double valuePerConv, java.lang.Double valuePerConvManyPerClick, java.lang.Long invalidClicks, java.lang.Double invalidClickRate, java.lang.String statsType) {
        super(startDate, endDate, network, clicks, impressions, cost, averagePosition, averageCpc, averageCpm, ctr, conversions, conversionRate, costPerConversion, conversionsManyPerClick, conversionRateManyPerClick, costPerConversionManyPerClick, viewThroughConversions, totalConvValue, valuePerConv, valuePerConvManyPerClick, invalidClicks, invalidClickRate, statsType);
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CampaignStats)) return false;
        CampaignStats other = (CampaignStats) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj);
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
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(CampaignStats.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201101", "CampaignStats"));
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

package com.google.api.adwords.v201003.cm;

/**
 * Statistics about an ad or criterion within an adgroup or campaign.
 */
public class Stats implements java.io.Serializable {

    private java.lang.String startDate;

    private java.lang.String endDate;

    private com.google.api.adwords.v201003.cm.StatsNetwork network;

    private java.lang.Long clicks;

    private java.lang.Long impressions;

    private com.google.api.adwords.v201003.cm.Money cost;

    private java.lang.Double averagePosition;

    private com.google.api.adwords.v201003.cm.Money averageCpc;

    private com.google.api.adwords.v201003.cm.Money averageCpm;

    private java.lang.Double ctr;

    private java.lang.Long conversions;

    private java.lang.Double conversionRate;

    private com.google.api.adwords.v201003.cm.Money costPerConversion;

    private java.lang.Long conversionsManyPerClick;

    private java.lang.Double conversionRateManyPerClick;

    private com.google.api.adwords.v201003.cm.Money costPerConversionManyPerClick;

    private java.lang.String statsType;

    public Stats() {
    }

    public Stats(java.lang.String startDate, java.lang.String endDate, com.google.api.adwords.v201003.cm.StatsNetwork network, java.lang.Long clicks, java.lang.Long impressions, com.google.api.adwords.v201003.cm.Money cost, java.lang.Double averagePosition, com.google.api.adwords.v201003.cm.Money averageCpc, com.google.api.adwords.v201003.cm.Money averageCpm, java.lang.Double ctr, java.lang.Long conversions, java.lang.Double conversionRate, com.google.api.adwords.v201003.cm.Money costPerConversion, java.lang.Long conversionsManyPerClick, java.lang.Double conversionRateManyPerClick, com.google.api.adwords.v201003.cm.Money costPerConversionManyPerClick, java.lang.String statsType) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.network = network;
        this.clicks = clicks;
        this.impressions = impressions;
        this.cost = cost;
        this.averagePosition = averagePosition;
        this.averageCpc = averageCpc;
        this.averageCpm = averageCpm;
        this.ctr = ctr;
        this.conversions = conversions;
        this.conversionRate = conversionRate;
        this.costPerConversion = costPerConversion;
        this.conversionsManyPerClick = conversionsManyPerClick;
        this.conversionRateManyPerClick = conversionRateManyPerClick;
        this.costPerConversionManyPerClick = costPerConversionManyPerClick;
        this.statsType = statsType;
    }

    /**
     * Gets the startDate value for this Stats.
     * 
     * @return startDate   * Starting date (inclusive) of the statistics.
     */
    public java.lang.String getStartDate() {
        return startDate;
    }

    /**
     * Sets the startDate value for this Stats.
     * 
     * @param startDate   * Starting date (inclusive) of the statistics.
     */
    public void setStartDate(java.lang.String startDate) {
        this.startDate = startDate;
    }

    /**
     * Gets the endDate value for this Stats.
     * 
     * @return endDate   * Ending date (inclusive) of the statistics.
     */
    public java.lang.String getEndDate() {
        return endDate;
    }

    /**
     * Sets the endDate value for this Stats.
     * 
     * @param endDate   * Ending date (inclusive) of the statistics.
     */
    public void setEndDate(java.lang.String endDate) {
        this.endDate = endDate;
    }

    /**
     * Gets the network value for this Stats.
     * 
     * @return network   * The ad network which the stats are for.
     */
    public com.google.api.adwords.v201003.cm.StatsNetwork getNetwork() {
        return network;
    }

    /**
     * Sets the network value for this Stats.
     * 
     * @param network   * The ad network which the stats are for.
     */
    public void setNetwork(com.google.api.adwords.v201003.cm.StatsNetwork network) {
        this.network = network;
    }

    /**
     * Gets the clicks value for this Stats.
     * 
     * @return clicks   * Number of clicks.
     */
    public java.lang.Long getClicks() {
        return clicks;
    }

    /**
     * Sets the clicks value for this Stats.
     * 
     * @param clicks   * Number of clicks.
     */
    public void setClicks(java.lang.Long clicks) {
        this.clicks = clicks;
    }

    /**
     * Gets the impressions value for this Stats.
     * 
     * @return impressions   * Number of impressions.
     */
    public java.lang.Long getImpressions() {
        return impressions;
    }

    /**
     * Sets the impressions value for this Stats.
     * 
     * @param impressions   * Number of impressions.
     */
    public void setImpressions(java.lang.Long impressions) {
        this.impressions = impressions;
    }

    /**
     * Gets the cost value for this Stats.
     * 
     * @return cost   * Cost.
     */
    public com.google.api.adwords.v201003.cm.Money getCost() {
        return cost;
    }

    /**
     * Sets the cost value for this Stats.
     * 
     * @param cost   * Cost.
     */
    public void setCost(com.google.api.adwords.v201003.cm.Money cost) {
        this.cost = cost;
    }

    /**
     * Gets the averagePosition value for this Stats.
     * 
     * @return averagePosition   * Average position of ads.
     */
    public java.lang.Double getAveragePosition() {
        return averagePosition;
    }

    /**
     * Sets the averagePosition value for this Stats.
     * 
     * @param averagePosition   * Average position of ads.
     */
    public void setAveragePosition(java.lang.Double averagePosition) {
        this.averagePosition = averagePosition;
    }

    /**
     * Gets the averageCpc value for this Stats.
     * 
     * @return averageCpc   * Average cost per click.
     */
    public com.google.api.adwords.v201003.cm.Money getAverageCpc() {
        return averageCpc;
    }

    /**
     * Sets the averageCpc value for this Stats.
     * 
     * @param averageCpc   * Average cost per click.
     */
    public void setAverageCpc(com.google.api.adwords.v201003.cm.Money averageCpc) {
        this.averageCpc = averageCpc;
    }

    /**
     * Gets the averageCpm value for this Stats.
     * 
     * @return averageCpm   * Average Cost Per Impression.
     */
    public com.google.api.adwords.v201003.cm.Money getAverageCpm() {
        return averageCpm;
    }

    /**
     * Sets the averageCpm value for this Stats.
     * 
     * @param averageCpm   * Average Cost Per Impression.
     */
    public void setAverageCpm(com.google.api.adwords.v201003.cm.Money averageCpm) {
        this.averageCpm = averageCpm;
    }

    /**
     * Gets the ctr value for this Stats.
     * 
     * @return ctr   * Click Through Rate.
     */
    public java.lang.Double getCtr() {
        return ctr;
    }

    /**
     * Sets the ctr value for this Stats.
     * 
     * @param ctr   * Click Through Rate.
     */
    public void setCtr(java.lang.Double ctr) {
        this.ctr = ctr;
    }

    /**
     * Gets the conversions value for this Stats.
     * 
     * @return conversions   * Number of conversions.
     */
    public java.lang.Long getConversions() {
        return conversions;
    }

    /**
     * Sets the conversions value for this Stats.
     * 
     * @param conversions   * Number of conversions.
     */
    public void setConversions(java.lang.Long conversions) {
        this.conversions = conversions;
    }

    /**
     * Gets the conversionRate value for this Stats.
     * 
     * @return conversionRate   * Conversion Rate.
     */
    public java.lang.Double getConversionRate() {
        return conversionRate;
    }

    /**
     * Sets the conversionRate value for this Stats.
     * 
     * @param conversionRate   * Conversion Rate.
     */
    public void setConversionRate(java.lang.Double conversionRate) {
        this.conversionRate = conversionRate;
    }

    /**
     * Gets the costPerConversion value for this Stats.
     * 
     * @return costPerConversion   * Cost per Conversion.
     */
    public com.google.api.adwords.v201003.cm.Money getCostPerConversion() {
        return costPerConversion;
    }

    /**
     * Sets the costPerConversion value for this Stats.
     * 
     * @param costPerConversion   * Cost per Conversion.
     */
    public void setCostPerConversion(com.google.api.adwords.v201003.cm.Money costPerConversion) {
        this.costPerConversion = costPerConversion;
    }

    /**
     * Gets the conversionsManyPerClick value for this Stats.
     * 
     * @return conversionsManyPerClick   * Number of conversions (many-per-click).
     */
    public java.lang.Long getConversionsManyPerClick() {
        return conversionsManyPerClick;
    }

    /**
     * Sets the conversionsManyPerClick value for this Stats.
     * 
     * @param conversionsManyPerClick   * Number of conversions (many-per-click).
     */
    public void setConversionsManyPerClick(java.lang.Long conversionsManyPerClick) {
        this.conversionsManyPerClick = conversionsManyPerClick;
    }

    /**
     * Gets the conversionRateManyPerClick value for this Stats.
     * 
     * @return conversionRateManyPerClick   * Conversion Rate (many-per-click).
     */
    public java.lang.Double getConversionRateManyPerClick() {
        return conversionRateManyPerClick;
    }

    /**
     * Sets the conversionRateManyPerClick value for this Stats.
     * 
     * @param conversionRateManyPerClick   * Conversion Rate (many-per-click).
     */
    public void setConversionRateManyPerClick(java.lang.Double conversionRateManyPerClick) {
        this.conversionRateManyPerClick = conversionRateManyPerClick;
    }

    /**
     * Gets the costPerConversionManyPerClick value for this Stats.
     * 
     * @return costPerConversionManyPerClick   * Cost per Conversion (many-per-click).
     */
    public com.google.api.adwords.v201003.cm.Money getCostPerConversionManyPerClick() {
        return costPerConversionManyPerClick;
    }

    /**
     * Sets the costPerConversionManyPerClick value for this Stats.
     * 
     * @param costPerConversionManyPerClick   * Cost per Conversion (many-per-click).
     */
    public void setCostPerConversionManyPerClick(com.google.api.adwords.v201003.cm.Money costPerConversionManyPerClick) {
        this.costPerConversionManyPerClick = costPerConversionManyPerClick;
    }

    /**
     * Gets the statsType value for this Stats.
     * 
     * @return statsType   * This field indicates the subtype of Stats of this instance.
     * It is ignored
     *                 on input, and instead xsi:type should be specified.
     */
    public java.lang.String getStatsType() {
        return statsType;
    }

    /**
     * Sets the statsType value for this Stats.
     * 
     * @param statsType   * This field indicates the subtype of Stats of this instance.
     * It is ignored
     *                 on input, and instead xsi:type should be specified.
     */
    public void setStatsType(java.lang.String statsType) {
        this.statsType = statsType;
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
        _equals = true && ((this.startDate == null && other.getStartDate() == null) || (this.startDate != null && this.startDate.equals(other.getStartDate()))) && ((this.endDate == null && other.getEndDate() == null) || (this.endDate != null && this.endDate.equals(other.getEndDate()))) && ((this.network == null && other.getNetwork() == null) || (this.network != null && this.network.equals(other.getNetwork()))) && ((this.clicks == null && other.getClicks() == null) || (this.clicks != null && this.clicks.equals(other.getClicks()))) && ((this.impressions == null && other.getImpressions() == null) || (this.impressions != null && this.impressions.equals(other.getImpressions()))) && ((this.cost == null && other.getCost() == null) || (this.cost != null && this.cost.equals(other.getCost()))) && ((this.averagePosition == null && other.getAveragePosition() == null) || (this.averagePosition != null && this.averagePosition.equals(other.getAveragePosition()))) && ((this.averageCpc == null && other.getAverageCpc() == null) || (this.averageCpc != null && this.averageCpc.equals(other.getAverageCpc()))) && ((this.averageCpm == null && other.getAverageCpm() == null) || (this.averageCpm != null && this.averageCpm.equals(other.getAverageCpm()))) && ((this.ctr == null && other.getCtr() == null) || (this.ctr != null && this.ctr.equals(other.getCtr()))) && ((this.conversions == null && other.getConversions() == null) || (this.conversions != null && this.conversions.equals(other.getConversions()))) && ((this.conversionRate == null && other.getConversionRate() == null) || (this.conversionRate != null && this.conversionRate.equals(other.getConversionRate()))) && ((this.costPerConversion == null && other.getCostPerConversion() == null) || (this.costPerConversion != null && this.costPerConversion.equals(other.getCostPerConversion()))) && ((this.conversionsManyPerClick == null && other.getConversionsManyPerClick() == null) || (this.conversionsManyPerClick != null && this.conversionsManyPerClick.equals(other.getConversionsManyPerClick()))) && ((this.conversionRateManyPerClick == null && other.getConversionRateManyPerClick() == null) || (this.conversionRateManyPerClick != null && this.conversionRateManyPerClick.equals(other.getConversionRateManyPerClick()))) && ((this.costPerConversionManyPerClick == null && other.getCostPerConversionManyPerClick() == null) || (this.costPerConversionManyPerClick != null && this.costPerConversionManyPerClick.equals(other.getCostPerConversionManyPerClick()))) && ((this.statsType == null && other.getStatsType() == null) || (this.statsType != null && this.statsType.equals(other.getStatsType())));
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
        if (getStartDate() != null) {
            _hashCode += getStartDate().hashCode();
        }
        if (getEndDate() != null) {
            _hashCode += getEndDate().hashCode();
        }
        if (getNetwork() != null) {
            _hashCode += getNetwork().hashCode();
        }
        if (getClicks() != null) {
            _hashCode += getClicks().hashCode();
        }
        if (getImpressions() != null) {
            _hashCode += getImpressions().hashCode();
        }
        if (getCost() != null) {
            _hashCode += getCost().hashCode();
        }
        if (getAveragePosition() != null) {
            _hashCode += getAveragePosition().hashCode();
        }
        if (getAverageCpc() != null) {
            _hashCode += getAverageCpc().hashCode();
        }
        if (getAverageCpm() != null) {
            _hashCode += getAverageCpm().hashCode();
        }
        if (getCtr() != null) {
            _hashCode += getCtr().hashCode();
        }
        if (getConversions() != null) {
            _hashCode += getConversions().hashCode();
        }
        if (getConversionRate() != null) {
            _hashCode += getConversionRate().hashCode();
        }
        if (getCostPerConversion() != null) {
            _hashCode += getCostPerConversion().hashCode();
        }
        if (getConversionsManyPerClick() != null) {
            _hashCode += getConversionsManyPerClick().hashCode();
        }
        if (getConversionRateManyPerClick() != null) {
            _hashCode += getConversionRateManyPerClick().hashCode();
        }
        if (getCostPerConversionManyPerClick() != null) {
            _hashCode += getCostPerConversionManyPerClick().hashCode();
        }
        if (getStatsType() != null) {
            _hashCode += getStatsType().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(Stats.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "Stats"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("startDate");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "startDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("endDate");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "endDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("network");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "network"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "Stats.Network"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("clicks");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "clicks"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("impressions");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "impressions"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cost");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "cost"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "Money"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("averagePosition");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "averagePosition"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("averageCpc");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "averageCpc"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "Money"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("averageCpm");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "averageCpm"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "Money"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ctr");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "ctr"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("conversions");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "conversions"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("conversionRate");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "conversionRate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("costPerConversion");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "costPerConversion"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "Money"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("conversionsManyPerClick");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "conversionsManyPerClick"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("conversionRateManyPerClick");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "conversionRateManyPerClick"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("costPerConversionManyPerClick");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "costPerConversionManyPerClick"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "Money"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("statsType");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "Stats.Type"));
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

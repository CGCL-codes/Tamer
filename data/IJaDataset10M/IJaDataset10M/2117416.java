package com.google.api.adwords.v201003.cm;

/**
 * A frequency cap is the maximum number of times an ad (or some set
 * of ads) can
 *             be shown to a user over a particular time period.
 */
public class FrequencyCap implements java.io.Serializable {

    private java.lang.Long impressions;

    private com.google.api.adwords.v201003.cm.TimeUnit timeUnit;

    private com.google.api.adwords.v201003.cm.Level level;

    public FrequencyCap() {
    }

    public FrequencyCap(java.lang.Long impressions, com.google.api.adwords.v201003.cm.TimeUnit timeUnit, com.google.api.adwords.v201003.cm.Level level) {
        this.impressions = impressions;
        this.timeUnit = timeUnit;
        this.level = level;
    }

    /**
     * Gets the impressions value for this FrequencyCap.
     * 
     * @return impressions   * Maximum number of impressions allowed during the time range
     * by this cap.
     */
    public java.lang.Long getImpressions() {
        return impressions;
    }

    /**
     * Sets the impressions value for this FrequencyCap.
     * 
     * @param impressions   * Maximum number of impressions allowed during the time range
     * by this cap.
     */
    public void setImpressions(java.lang.Long impressions) {
        this.impressions = impressions;
    }

    /**
     * Gets the timeUnit value for this FrequencyCap.
     * 
     * @return timeUnit   * Unit of time the cap is defined at.
     *                 Only the Day, Week and Month time units are supported.
     */
    public com.google.api.adwords.v201003.cm.TimeUnit getTimeUnit() {
        return timeUnit;
    }

    /**
     * Sets the timeUnit value for this FrequencyCap.
     * 
     * @param timeUnit   * Unit of time the cap is defined at.
     *                 Only the Day, Week and Month time units are supported.
     */
    public void setTimeUnit(com.google.api.adwords.v201003.cm.TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    /**
     * Gets the level value for this FrequencyCap.
     * 
     * @return level   * The level on which the cap is to be applied (creative/adgroup).
     * Cap is applied to all the entities of this level in the campaign.
     */
    public com.google.api.adwords.v201003.cm.Level getLevel() {
        return level;
    }

    /**
     * Sets the level value for this FrequencyCap.
     * 
     * @param level   * The level on which the cap is to be applied (creative/adgroup).
     * Cap is applied to all the entities of this level in the campaign.
     */
    public void setLevel(com.google.api.adwords.v201003.cm.Level level) {
        this.level = level;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof FrequencyCap)) return false;
        FrequencyCap other = (FrequencyCap) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.impressions == null && other.getImpressions() == null) || (this.impressions != null && this.impressions.equals(other.getImpressions()))) && ((this.timeUnit == null && other.getTimeUnit() == null) || (this.timeUnit != null && this.timeUnit.equals(other.getTimeUnit()))) && ((this.level == null && other.getLevel() == null) || (this.level != null && this.level.equals(other.getLevel())));
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
        if (getImpressions() != null) {
            _hashCode += getImpressions().hashCode();
        }
        if (getTimeUnit() != null) {
            _hashCode += getTimeUnit().hashCode();
        }
        if (getLevel() != null) {
            _hashCode += getLevel().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(FrequencyCap.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "FrequencyCap"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("impressions");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "impressions"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("timeUnit");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "timeUnit"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "TimeUnit"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("level");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "level"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "Level"));
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

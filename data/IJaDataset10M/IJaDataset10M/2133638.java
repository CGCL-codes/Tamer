package com.google.api.ads.dfp.v201108;

/**
 * Modify the delivery times of line items for particular days of
 * the week. By
 *             default, line items are served at all days and times.
 */
public class DayPartTargeting implements java.io.Serializable {

    private com.google.api.ads.dfp.v201108.DayPart[] dayParts;

    private com.google.api.ads.dfp.v201108.DeliveryTimeZone timeZone;

    public DayPartTargeting() {
    }

    public DayPartTargeting(com.google.api.ads.dfp.v201108.DayPart[] dayParts, com.google.api.ads.dfp.v201108.DeliveryTimeZone timeZone) {
        this.dayParts = dayParts;
        this.timeZone = timeZone;
    }

    /**
     * Gets the dayParts value for this DayPartTargeting.
     * 
     * @return dayParts   * Specifies days of the week and times at which a {@code LineItem}
     * will be
     *                 delivered.
     *                 <p>
     *                 If targeting all days and times, this value will be
     * ignored.
     */
    public com.google.api.ads.dfp.v201108.DayPart[] getDayParts() {
        return dayParts;
    }

    /**
     * Sets the dayParts value for this DayPartTargeting.
     * 
     * @param dayParts   * Specifies days of the week and times at which a {@code LineItem}
     * will be
     *                 delivered.
     *                 <p>
     *                 If targeting all days and times, this value will be
     * ignored.
     */
    public void setDayParts(com.google.api.ads.dfp.v201108.DayPart[] dayParts) {
        this.dayParts = dayParts;
    }

    public com.google.api.ads.dfp.v201108.DayPart getDayParts(int i) {
        return this.dayParts[i];
    }

    public void setDayParts(int i, com.google.api.ads.dfp.v201108.DayPart _value) {
        this.dayParts[i] = _value;
    }

    /**
     * Gets the timeZone value for this DayPartTargeting.
     * 
     * @return timeZone   * Specifies the time zone to be used for delivering {@link LineItem}
     * objects.
     *                 This attribute is optional and defaults to
     *                 {@link DeliveryTimeZone#BROWSER}.
     *                 <p>
     *                 Setting this has no effect if targeting all days and
     * times.
     */
    public com.google.api.ads.dfp.v201108.DeliveryTimeZone getTimeZone() {
        return timeZone;
    }

    /**
     * Sets the timeZone value for this DayPartTargeting.
     * 
     * @param timeZone   * Specifies the time zone to be used for delivering {@link LineItem}
     * objects.
     *                 This attribute is optional and defaults to
     *                 {@link DeliveryTimeZone#BROWSER}.
     *                 <p>
     *                 Setting this has no effect if targeting all days and
     * times.
     */
    public void setTimeZone(com.google.api.ads.dfp.v201108.DeliveryTimeZone timeZone) {
        this.timeZone = timeZone;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DayPartTargeting)) return false;
        DayPartTargeting other = (DayPartTargeting) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.dayParts == null && other.getDayParts() == null) || (this.dayParts != null && java.util.Arrays.equals(this.dayParts, other.getDayParts()))) && ((this.timeZone == null && other.getTimeZone() == null) || (this.timeZone != null && this.timeZone.equals(other.getTimeZone())));
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
        if (getDayParts() != null) {
            for (int i = 0; i < java.lang.reflect.Array.getLength(getDayParts()); i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDayParts(), i);
                if (obj != null && !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getTimeZone() != null) {
            _hashCode += getTimeZone().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(DayPartTargeting.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201108", "DayPartTargeting"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dayParts");
        elemField.setXmlName(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201108", "dayParts"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201108", "DayPart"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("timeZone");
        elemField.setXmlName(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201108", "timeZone"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201108", "DeliveryTimeZone"));
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

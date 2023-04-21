package com.google.api.adwords.v201109.cm;

/**
 * Represents a date.
 */
public class Date implements java.io.Serializable {

    private java.lang.Integer year;

    private java.lang.Integer month;

    private java.lang.Integer day;

    public Date() {
    }

    public Date(java.lang.Integer year, java.lang.Integer month, java.lang.Integer day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    /**
     * Gets the year value for this Date.
     * 
     * @return year   * Year (e.g., 2009)
     */
    public java.lang.Integer getYear() {
        return year;
    }

    /**
     * Sets the year value for this Date.
     * 
     * @param year   * Year (e.g., 2009)
     */
    public void setYear(java.lang.Integer year) {
        this.year = year;
    }

    /**
     * Gets the month value for this Date.
     * 
     * @return month   * Month (1..12)
     */
    public java.lang.Integer getMonth() {
        return month;
    }

    /**
     * Sets the month value for this Date.
     * 
     * @param month   * Month (1..12)
     */
    public void setMonth(java.lang.Integer month) {
        this.month = month;
    }

    /**
     * Gets the day value for this Date.
     * 
     * @return day   * Day (1..31)
     */
    public java.lang.Integer getDay() {
        return day;
    }

    /**
     * Sets the day value for this Date.
     * 
     * @param day   * Day (1..31)
     */
    public void setDay(java.lang.Integer day) {
        this.day = day;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Date)) return false;
        Date other = (Date) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.year == null && other.getYear() == null) || (this.year != null && this.year.equals(other.getYear()))) && ((this.month == null && other.getMonth() == null) || (this.month != null && this.month.equals(other.getMonth()))) && ((this.day == null && other.getDay() == null) || (this.day != null && this.day.equals(other.getDay())));
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
        if (getYear() != null) {
            _hashCode += getYear().hashCode();
        }
        if (getMonth() != null) {
            _hashCode += getMonth().hashCode();
        }
        if (getDay() != null) {
            _hashCode += getDay().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(Date.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "Date"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("year");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "year"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("month");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "month"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("day");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "day"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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

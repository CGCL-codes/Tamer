package com.google.api.adwords.v201101.cm;

/**
 * An event defined by a status change of a job.
 */
public class JobEvent implements java.io.Serializable {

    private java.lang.String dateTime;

    private java.lang.String jobEventType;

    public JobEvent() {
    }

    public JobEvent(java.lang.String dateTime, java.lang.String jobEventType) {
        this.dateTime = dateTime;
        this.jobEventType = jobEventType;
    }

    /**
     * Gets the dateTime value for this JobEvent.
     * 
     * @return dateTime   * Time when the job status changed.
     */
    public java.lang.String getDateTime() {
        return dateTime;
    }

    /**
     * Sets the dateTime value for this JobEvent.
     * 
     * @param dateTime   * Time when the job status changed.
     */
    public void setDateTime(java.lang.String dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * Gets the jobEventType value for this JobEvent.
     * 
     * @return jobEventType   * This field indicates the subtype of JobEvent of this instance.
     * It is ignored
     *                 on input, and instead xsi:type should be specified.
     */
    public java.lang.String getJobEventType() {
        return jobEventType;
    }

    /**
     * Sets the jobEventType value for this JobEvent.
     * 
     * @param jobEventType   * This field indicates the subtype of JobEvent of this instance.
     * It is ignored
     *                 on input, and instead xsi:type should be specified.
     */
    public void setJobEventType(java.lang.String jobEventType) {
        this.jobEventType = jobEventType;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof JobEvent)) return false;
        JobEvent other = (JobEvent) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.dateTime == null && other.getDateTime() == null) || (this.dateTime != null && this.dateTime.equals(other.getDateTime()))) && ((this.jobEventType == null && other.getJobEventType() == null) || (this.jobEventType != null && this.jobEventType.equals(other.getJobEventType())));
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
        if (getDateTime() != null) {
            _hashCode += getDateTime().hashCode();
        }
        if (getJobEventType() != null) {
            _hashCode += getJobEventType().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(JobEvent.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201101", "JobEvent"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dateTime");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201101", "dateTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("jobEventType");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201101", "JobEvent.Type"));
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

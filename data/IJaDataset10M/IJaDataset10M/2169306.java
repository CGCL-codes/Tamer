package com.google.api.adwords.v201003.cm;

/**
 * Represents the request and processing context information of a
 * job.
 */
public class JobContext implements java.io.Serializable {

    private java.lang.String authenticatedUserEmail;

    private java.lang.Long effectiveCustomerId;

    public JobContext() {
    }

    public JobContext(java.lang.String authenticatedUserEmail, java.lang.Long effectiveCustomerId) {
        this.authenticatedUserEmail = authenticatedUserEmail;
        this.effectiveCustomerId = effectiveCustomerId;
    }

    /**
     * Gets the authenticatedUserEmail value for this JobContext.
     * 
     * @return authenticatedUserEmail   * The login email of the authenticated user who submitted the
     * job request.
     */
    public java.lang.String getAuthenticatedUserEmail() {
        return authenticatedUserEmail;
    }

    /**
     * Sets the authenticatedUserEmail value for this JobContext.
     * 
     * @param authenticatedUserEmail   * The login email of the authenticated user who submitted the
     * job request.
     */
    public void setAuthenticatedUserEmail(java.lang.String authenticatedUserEmail) {
        this.authenticatedUserEmail = authenticatedUserEmail;
    }

    /**
     * Gets the effectiveCustomerId value for this JobContext.
     * 
     * @return effectiveCustomerId   * The id of the customer being operated upon in the job request.
     */
    public java.lang.Long getEffectiveCustomerId() {
        return effectiveCustomerId;
    }

    /**
     * Sets the effectiveCustomerId value for this JobContext.
     * 
     * @param effectiveCustomerId   * The id of the customer being operated upon in the job request.
     */
    public void setEffectiveCustomerId(java.lang.Long effectiveCustomerId) {
        this.effectiveCustomerId = effectiveCustomerId;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof JobContext)) return false;
        JobContext other = (JobContext) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.authenticatedUserEmail == null && other.getAuthenticatedUserEmail() == null) || (this.authenticatedUserEmail != null && this.authenticatedUserEmail.equals(other.getAuthenticatedUserEmail()))) && ((this.effectiveCustomerId == null && other.getEffectiveCustomerId() == null) || (this.effectiveCustomerId != null && this.effectiveCustomerId.equals(other.getEffectiveCustomerId())));
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
        if (getAuthenticatedUserEmail() != null) {
            _hashCode += getAuthenticatedUserEmail().hashCode();
        }
        if (getEffectiveCustomerId() != null) {
            _hashCode += getEffectiveCustomerId().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(JobContext.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "JobContext"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("authenticatedUserEmail");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "authenticatedUserEmail"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("effectiveCustomerId");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "effectiveCustomerId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
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

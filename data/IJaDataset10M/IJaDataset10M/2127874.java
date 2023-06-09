package org.broadleafcommerce.vendor.cybersource.service.api;

public class ECAuthenticateReply implements java.io.Serializable {

    private java.math.BigInteger reasonCode;

    private java.lang.String requestDateTime;

    private java.lang.String processorResponse;

    private java.lang.String reconciliationID;

    private java.lang.String checkpointSummary;

    private java.lang.String fraudShieldIndicators;

    public ECAuthenticateReply() {
    }

    public ECAuthenticateReply(java.math.BigInteger reasonCode, java.lang.String requestDateTime, java.lang.String processorResponse, java.lang.String reconciliationID, java.lang.String checkpointSummary, java.lang.String fraudShieldIndicators) {
        this.reasonCode = reasonCode;
        this.requestDateTime = requestDateTime;
        this.processorResponse = processorResponse;
        this.reconciliationID = reconciliationID;
        this.checkpointSummary = checkpointSummary;
        this.fraudShieldIndicators = fraudShieldIndicators;
    }

    /**
     * Gets the reasonCode value for this ECAuthenticateReply.
     * 
     * @return reasonCode
     */
    public java.math.BigInteger getReasonCode() {
        return reasonCode;
    }

    /**
     * Sets the reasonCode value for this ECAuthenticateReply.
     * 
     * @param reasonCode
     */
    public void setReasonCode(java.math.BigInteger reasonCode) {
        this.reasonCode = reasonCode;
    }

    /**
     * Gets the requestDateTime value for this ECAuthenticateReply.
     * 
     * @return requestDateTime
     */
    public java.lang.String getRequestDateTime() {
        return requestDateTime;
    }

    /**
     * Sets the requestDateTime value for this ECAuthenticateReply.
     * 
     * @param requestDateTime
     */
    public void setRequestDateTime(java.lang.String requestDateTime) {
        this.requestDateTime = requestDateTime;
    }

    /**
     * Gets the processorResponse value for this ECAuthenticateReply.
     * 
     * @return processorResponse
     */
    public java.lang.String getProcessorResponse() {
        return processorResponse;
    }

    /**
     * Sets the processorResponse value for this ECAuthenticateReply.
     * 
     * @param processorResponse
     */
    public void setProcessorResponse(java.lang.String processorResponse) {
        this.processorResponse = processorResponse;
    }

    /**
     * Gets the reconciliationID value for this ECAuthenticateReply.
     * 
     * @return reconciliationID
     */
    public java.lang.String getReconciliationID() {
        return reconciliationID;
    }

    /**
     * Sets the reconciliationID value for this ECAuthenticateReply.
     * 
     * @param reconciliationID
     */
    public void setReconciliationID(java.lang.String reconciliationID) {
        this.reconciliationID = reconciliationID;
    }

    /**
     * Gets the checkpointSummary value for this ECAuthenticateReply.
     * 
     * @return checkpointSummary
     */
    public java.lang.String getCheckpointSummary() {
        return checkpointSummary;
    }

    /**
     * Sets the checkpointSummary value for this ECAuthenticateReply.
     * 
     * @param checkpointSummary
     */
    public void setCheckpointSummary(java.lang.String checkpointSummary) {
        this.checkpointSummary = checkpointSummary;
    }

    /**
     * Gets the fraudShieldIndicators value for this ECAuthenticateReply.
     * 
     * @return fraudShieldIndicators
     */
    public java.lang.String getFraudShieldIndicators() {
        return fraudShieldIndicators;
    }

    /**
     * Sets the fraudShieldIndicators value for this ECAuthenticateReply.
     * 
     * @param fraudShieldIndicators
     */
    public void setFraudShieldIndicators(java.lang.String fraudShieldIndicators) {
        this.fraudShieldIndicators = fraudShieldIndicators;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ECAuthenticateReply)) return false;
        ECAuthenticateReply other = (ECAuthenticateReply) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.reasonCode == null && other.getReasonCode() == null) || (this.reasonCode != null && this.reasonCode.equals(other.getReasonCode()))) && ((this.requestDateTime == null && other.getRequestDateTime() == null) || (this.requestDateTime != null && this.requestDateTime.equals(other.getRequestDateTime()))) && ((this.processorResponse == null && other.getProcessorResponse() == null) || (this.processorResponse != null && this.processorResponse.equals(other.getProcessorResponse()))) && ((this.reconciliationID == null && other.getReconciliationID() == null) || (this.reconciliationID != null && this.reconciliationID.equals(other.getReconciliationID()))) && ((this.checkpointSummary == null && other.getCheckpointSummary() == null) || (this.checkpointSummary != null && this.checkpointSummary.equals(other.getCheckpointSummary()))) && ((this.fraudShieldIndicators == null && other.getFraudShieldIndicators() == null) || (this.fraudShieldIndicators != null && this.fraudShieldIndicators.equals(other.getFraudShieldIndicators())));
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
        if (getReasonCode() != null) {
            _hashCode += getReasonCode().hashCode();
        }
        if (getRequestDateTime() != null) {
            _hashCode += getRequestDateTime().hashCode();
        }
        if (getProcessorResponse() != null) {
            _hashCode += getProcessorResponse().hashCode();
        }
        if (getReconciliationID() != null) {
            _hashCode += getReconciliationID().hashCode();
        }
        if (getCheckpointSummary() != null) {
            _hashCode += getCheckpointSummary().hashCode();
        }
        if (getFraudShieldIndicators() != null) {
            _hashCode += getFraudShieldIndicators().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(ECAuthenticateReply.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:schemas-cybersource-com:transaction-data-1.49", "ECAuthenticateReply"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reasonCode");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:schemas-cybersource-com:transaction-data-1.49", "reasonCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "integer"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("requestDateTime");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:schemas-cybersource-com:transaction-data-1.49", "requestDateTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("processorResponse");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:schemas-cybersource-com:transaction-data-1.49", "processorResponse"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reconciliationID");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:schemas-cybersource-com:transaction-data-1.49", "reconciliationID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("checkpointSummary");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:schemas-cybersource-com:transaction-data-1.49", "checkpointSummary"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fraudShieldIndicators");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:schemas-cybersource-com:transaction-data-1.49", "fraudShieldIndicators"));
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

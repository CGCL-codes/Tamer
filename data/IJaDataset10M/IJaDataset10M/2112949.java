package com.konakartadmin.app;

public class AdminIpnHistory implements java.io.Serializable {

    private java.util.Calendar dateAdded;

    private java.lang.String gatewayFullResponse;

    private java.lang.String gatewayResult;

    private java.lang.String gatewayTransactionId;

    private int id;

    private java.lang.String konakartResultDescription;

    private int konakartResultId;

    private java.lang.String moduleCode;

    private int orderId;

    public AdminIpnHistory() {
    }

    public AdminIpnHistory(java.util.Calendar dateAdded, java.lang.String gatewayFullResponse, java.lang.String gatewayResult, java.lang.String gatewayTransactionId, int id, java.lang.String konakartResultDescription, int konakartResultId, java.lang.String moduleCode, int orderId) {
        this.dateAdded = dateAdded;
        this.gatewayFullResponse = gatewayFullResponse;
        this.gatewayResult = gatewayResult;
        this.gatewayTransactionId = gatewayTransactionId;
        this.id = id;
        this.konakartResultDescription = konakartResultDescription;
        this.konakartResultId = konakartResultId;
        this.moduleCode = moduleCode;
        this.orderId = orderId;
    }

    /**
     * Gets the dateAdded value for this AdminIpnHistory.
     * 
     * @return dateAdded
     */
    public java.util.Calendar getDateAdded() {
        return dateAdded;
    }

    /**
     * Sets the dateAdded value for this AdminIpnHistory.
     * 
     * @param dateAdded
     */
    public void setDateAdded(java.util.Calendar dateAdded) {
        this.dateAdded = dateAdded;
    }

    /**
     * Gets the gatewayFullResponse value for this AdminIpnHistory.
     * 
     * @return gatewayFullResponse
     */
    public java.lang.String getGatewayFullResponse() {
        return gatewayFullResponse;
    }

    /**
     * Sets the gatewayFullResponse value for this AdminIpnHistory.
     * 
     * @param gatewayFullResponse
     */
    public void setGatewayFullResponse(java.lang.String gatewayFullResponse) {
        this.gatewayFullResponse = gatewayFullResponse;
    }

    /**
     * Gets the gatewayResult value for this AdminIpnHistory.
     * 
     * @return gatewayResult
     */
    public java.lang.String getGatewayResult() {
        return gatewayResult;
    }

    /**
     * Sets the gatewayResult value for this AdminIpnHistory.
     * 
     * @param gatewayResult
     */
    public void setGatewayResult(java.lang.String gatewayResult) {
        this.gatewayResult = gatewayResult;
    }

    /**
     * Gets the gatewayTransactionId value for this AdminIpnHistory.
     * 
     * @return gatewayTransactionId
     */
    public java.lang.String getGatewayTransactionId() {
        return gatewayTransactionId;
    }

    /**
     * Sets the gatewayTransactionId value for this AdminIpnHistory.
     * 
     * @param gatewayTransactionId
     */
    public void setGatewayTransactionId(java.lang.String gatewayTransactionId) {
        this.gatewayTransactionId = gatewayTransactionId;
    }

    /**
     * Gets the id value for this AdminIpnHistory.
     * 
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id value for this AdminIpnHistory.
     * 
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the konakartResultDescription value for this AdminIpnHistory.
     * 
     * @return konakartResultDescription
     */
    public java.lang.String getKonakartResultDescription() {
        return konakartResultDescription;
    }

    /**
     * Sets the konakartResultDescription value for this AdminIpnHistory.
     * 
     * @param konakartResultDescription
     */
    public void setKonakartResultDescription(java.lang.String konakartResultDescription) {
        this.konakartResultDescription = konakartResultDescription;
    }

    /**
     * Gets the konakartResultId value for this AdminIpnHistory.
     * 
     * @return konakartResultId
     */
    public int getKonakartResultId() {
        return konakartResultId;
    }

    /**
     * Sets the konakartResultId value for this AdminIpnHistory.
     * 
     * @param konakartResultId
     */
    public void setKonakartResultId(int konakartResultId) {
        this.konakartResultId = konakartResultId;
    }

    /**
     * Gets the moduleCode value for this AdminIpnHistory.
     * 
     * @return moduleCode
     */
    public java.lang.String getModuleCode() {
        return moduleCode;
    }

    /**
     * Sets the moduleCode value for this AdminIpnHistory.
     * 
     * @param moduleCode
     */
    public void setModuleCode(java.lang.String moduleCode) {
        this.moduleCode = moduleCode;
    }

    /**
     * Gets the orderId value for this AdminIpnHistory.
     * 
     * @return orderId
     */
    public int getOrderId() {
        return orderId;
    }

    /**
     * Sets the orderId value for this AdminIpnHistory.
     * 
     * @param orderId
     */
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AdminIpnHistory)) return false;
        AdminIpnHistory other = (AdminIpnHistory) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.dateAdded == null && other.getDateAdded() == null) || (this.dateAdded != null && this.dateAdded.equals(other.getDateAdded()))) && ((this.gatewayFullResponse == null && other.getGatewayFullResponse() == null) || (this.gatewayFullResponse != null && this.gatewayFullResponse.equals(other.getGatewayFullResponse()))) && ((this.gatewayResult == null && other.getGatewayResult() == null) || (this.gatewayResult != null && this.gatewayResult.equals(other.getGatewayResult()))) && ((this.gatewayTransactionId == null && other.getGatewayTransactionId() == null) || (this.gatewayTransactionId != null && this.gatewayTransactionId.equals(other.getGatewayTransactionId()))) && this.id == other.getId() && ((this.konakartResultDescription == null && other.getKonakartResultDescription() == null) || (this.konakartResultDescription != null && this.konakartResultDescription.equals(other.getKonakartResultDescription()))) && this.konakartResultId == other.getKonakartResultId() && ((this.moduleCode == null && other.getModuleCode() == null) || (this.moduleCode != null && this.moduleCode.equals(other.getModuleCode()))) && this.orderId == other.getOrderId();
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
        if (getDateAdded() != null) {
            _hashCode += getDateAdded().hashCode();
        }
        if (getGatewayFullResponse() != null) {
            _hashCode += getGatewayFullResponse().hashCode();
        }
        if (getGatewayResult() != null) {
            _hashCode += getGatewayResult().hashCode();
        }
        if (getGatewayTransactionId() != null) {
            _hashCode += getGatewayTransactionId().hashCode();
        }
        _hashCode += getId();
        if (getKonakartResultDescription() != null) {
            _hashCode += getKonakartResultDescription().hashCode();
        }
        _hashCode += getKonakartResultId();
        if (getModuleCode() != null) {
            _hashCode += getModuleCode().hashCode();
        }
        _hashCode += getOrderId();
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(AdminIpnHistory.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://app.konakartadmin.com", "AdminIpnHistory"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dateAdded");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dateAdded"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("gatewayFullResponse");
        elemField.setXmlName(new javax.xml.namespace.QName("", "gatewayFullResponse"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("gatewayResult");
        elemField.setXmlName(new javax.xml.namespace.QName("", "gatewayResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("gatewayTransactionId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "gatewayTransactionId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("konakartResultDescription");
        elemField.setXmlName(new javax.xml.namespace.QName("", "konakartResultDescription"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("konakartResultId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "konakartResultId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("moduleCode");
        elemField.setXmlName(new javax.xml.namespace.QName("", "moduleCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("orderId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "orderId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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

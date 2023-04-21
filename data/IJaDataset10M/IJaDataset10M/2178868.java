package com.google.api.adwords.v200909.info;

/**
 * Represents the API usage information.
 */
public class ApiUsageInfo implements java.io.Serializable {

    private com.google.api.adwords.v200909.info.ApiUsageRecord[] apiUsageRecords;

    private java.lang.Long cost;

    public ApiUsageInfo() {
    }

    public ApiUsageInfo(com.google.api.adwords.v200909.info.ApiUsageRecord[] apiUsageRecords, java.lang.Long cost) {
        this.apiUsageRecords = apiUsageRecords;
        this.cost = cost;
    }

    /**
     * Gets the apiUsageRecords value for this ApiUsageInfo.
     * 
     * @return apiUsageRecords   * The list of API usage for the specific clients.
     */
    public com.google.api.adwords.v200909.info.ApiUsageRecord[] getApiUsageRecords() {
        return apiUsageRecords;
    }

    /**
     * Sets the apiUsageRecords value for this ApiUsageInfo.
     * 
     * @param apiUsageRecords   * The list of API usage for the specific clients.
     */
    public void setApiUsageRecords(com.google.api.adwords.v200909.info.ApiUsageRecord[] apiUsageRecords) {
        this.apiUsageRecords = apiUsageRecords;
    }

    public com.google.api.adwords.v200909.info.ApiUsageRecord getApiUsageRecords(int i) {
        return this.apiUsageRecords[i];
    }

    public void setApiUsageRecords(int i, com.google.api.adwords.v200909.info.ApiUsageRecord _value) {
        this.apiUsageRecords[i] = _value;
    }

    /**
     * Gets the cost value for this ApiUsageInfo.
     * 
     * @return cost   * The cost is set when the API usage is been fetched for all
     * clients.
     */
    public java.lang.Long getCost() {
        return cost;
    }

    /**
     * Sets the cost value for this ApiUsageInfo.
     * 
     * @param cost   * The cost is set when the API usage is been fetched for all
     * clients.
     */
    public void setCost(java.lang.Long cost) {
        this.cost = cost;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ApiUsageInfo)) return false;
        ApiUsageInfo other = (ApiUsageInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.apiUsageRecords == null && other.getApiUsageRecords() == null) || (this.apiUsageRecords != null && java.util.Arrays.equals(this.apiUsageRecords, other.getApiUsageRecords()))) && ((this.cost == null && other.getCost() == null) || (this.cost != null && this.cost.equals(other.getCost())));
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
        if (getApiUsageRecords() != null) {
            for (int i = 0; i < java.lang.reflect.Array.getLength(getApiUsageRecords()); i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getApiUsageRecords(), i);
                if (obj != null && !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getCost() != null) {
            _hashCode += getCost().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(ApiUsageInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/info/v200909", "ApiUsageInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("apiUsageRecords");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/info/v200909", "apiUsageRecords"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/info/v200909", "ApiUsageRecord"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cost");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/info/v200909", "cost"));
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

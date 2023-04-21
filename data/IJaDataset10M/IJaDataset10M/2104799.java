package com.google.api.adwords.v201008.cm;

/**
 * Indicates too many ads were added/enabled under the specified adgroup.
 */
public class AdGroupAdCountLimitExceeded extends com.google.api.adwords.v201008.cm.EntityCountLimitExceeded implements java.io.Serializable {

    public AdGroupAdCountLimitExceeded() {
    }

    public AdGroupAdCountLimitExceeded(java.lang.String fieldPath, java.lang.String trigger, java.lang.String errorString, java.lang.String apiErrorType, com.google.api.adwords.v201008.cm.EntityCountLimitExceededReason reason, java.lang.String enclosingId, java.lang.Integer limit) {
        super(fieldPath, trigger, errorString, apiErrorType, reason, enclosingId, limit);
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AdGroupAdCountLimitExceeded)) return false;
        AdGroupAdCountLimitExceeded other = (AdGroupAdCountLimitExceeded) obj;
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

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(AdGroupAdCountLimitExceeded.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201008", "AdGroupAdCountLimitExceeded"));
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

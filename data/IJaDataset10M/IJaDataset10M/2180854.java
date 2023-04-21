package com.google.api.ads.dfp.v201111;

/**
 * Represents the delete action that can be performed on
 *             {@link CustomTargetingKey} objects. Deleting a key will
 * not delete the
 *             {@link CustomTargetingValue} objects associated with it.
 * Also, if a custom
 *             targeting key that has been deleted is recreated, any
 * previous custom
 *             targeting values associated with it that were not deleted
 * will continue to
 *             exist.
 */
public class DeleteCustomTargetingKeys extends com.google.api.ads.dfp.v201111.CustomTargetingKeyAction implements java.io.Serializable {

    public DeleteCustomTargetingKeys() {
    }

    public DeleteCustomTargetingKeys(java.lang.String customTargetingKeyActionType) {
        super(customTargetingKeyActionType);
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DeleteCustomTargetingKeys)) return false;
        DeleteCustomTargetingKeys other = (DeleteCustomTargetingKeys) obj;
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

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(DeleteCustomTargetingKeys.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201111", "DeleteCustomTargetingKeys"));
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

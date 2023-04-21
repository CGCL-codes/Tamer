package org.csapi.www.schema.parlayx.sms.v1_0;

public class DeliveryStatusType implements java.io.Serializable {

    private org.csapi.www.schema.parlayx.common.v1_0.EndUserIdentifier destinationAddress;

    private org.csapi.www.schema.parlayx.sms.v1_0.DeliveryStatus deliveryStatus;

    public DeliveryStatusType() {
    }

    public DeliveryStatusType(org.csapi.www.schema.parlayx.common.v1_0.EndUserIdentifier destinationAddress, org.csapi.www.schema.parlayx.sms.v1_0.DeliveryStatus deliveryStatus) {
        this.destinationAddress = destinationAddress;
        this.deliveryStatus = deliveryStatus;
    }

    /**
     * Gets the destinationAddress value for this DeliveryStatusType.
     * 
     * @return destinationAddress
     */
    public org.csapi.www.schema.parlayx.common.v1_0.EndUserIdentifier getDestinationAddress() {
        return destinationAddress;
    }

    /**
     * Sets the destinationAddress value for this DeliveryStatusType.
     * 
     * @param destinationAddress
     */
    public void setDestinationAddress(org.csapi.www.schema.parlayx.common.v1_0.EndUserIdentifier destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    /**
     * Gets the deliveryStatus value for this DeliveryStatusType.
     * 
     * @return deliveryStatus
     */
    public org.csapi.www.schema.parlayx.sms.v1_0.DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    /**
     * Sets the deliveryStatus value for this DeliveryStatusType.
     * 
     * @param deliveryStatus
     */
    public void setDeliveryStatus(org.csapi.www.schema.parlayx.sms.v1_0.DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof DeliveryStatusType)) return false;
        DeliveryStatusType other = (DeliveryStatusType) obj;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.destinationAddress == null && other.getDestinationAddress() == null) || (this.destinationAddress != null && this.destinationAddress.equals(other.getDestinationAddress()))) && ((this.deliveryStatus == null && other.getDeliveryStatus() == null) || (this.deliveryStatus != null && this.deliveryStatus.equals(other.getDeliveryStatus())));
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
        if (getDestinationAddress() != null) {
            _hashCode += getDestinationAddress().hashCode();
        }
        if (getDeliveryStatus() != null) {
            _hashCode += getDeliveryStatus().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(DeliveryStatusType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.csapi.org/schema/parlayx/sms/v1_0", "DeliveryStatusType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("destinationAddress");
        elemField.setXmlName(new javax.xml.namespace.QName("", "destinationAddress"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.csapi.org/schema/parlayx/common/v1_0", "EndUserIdentifier"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("deliveryStatus");
        elemField.setXmlName(new javax.xml.namespace.QName("", "deliveryStatus"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.csapi.org/schema/parlayx/sms/v1_0", "DeliveryStatus"));
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

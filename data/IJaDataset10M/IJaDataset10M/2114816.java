package net.myphpshop.www.myPHPShopAdmin;

public class ChangeDeliveryCostMsgReply implements java.io.Serializable {

    private java.lang.String deliveryCostId;

    public ChangeDeliveryCostMsgReply() {
    }

    public ChangeDeliveryCostMsgReply(java.lang.String deliveryCostId) {
        this.deliveryCostId = deliveryCostId;
    }

    /**
     * Gets the deliveryCostId value for this ChangeDeliveryCostMsgReply.
     * 
     * @return deliveryCostId
     */
    public java.lang.String getDeliveryCostId() {
        return deliveryCostId;
    }

    /**
     * Sets the deliveryCostId value for this ChangeDeliveryCostMsgReply.
     * 
     * @param deliveryCostId
     */
    public void setDeliveryCostId(java.lang.String deliveryCostId) {
        this.deliveryCostId = deliveryCostId;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ChangeDeliveryCostMsgReply)) return false;
        ChangeDeliveryCostMsgReply other = (ChangeDeliveryCostMsgReply) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.deliveryCostId == null && other.getDeliveryCostId() == null) || (this.deliveryCostId != null && this.deliveryCostId.equals(other.getDeliveryCostId())));
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
        if (getDeliveryCostId() != null) {
            _hashCode += getDeliveryCostId().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(ChangeDeliveryCostMsgReply.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.myphpshop.net/myPHPShopAdmin", ">ChangeDeliveryCostMsgReply"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("deliveryCostId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.myphpshop.net/myPHPShopAdmin", "DeliveryCostId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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

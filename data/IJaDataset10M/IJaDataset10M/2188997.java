package org.oasis_open.docs.wsn._2004._06.wsn_WS_BaseNotification_1_2_draft_01_xsd;

public class TopicPathDialectUnknownFaultType extends org.oasis_open.docs.wsrf._2004._06.wsrf_WS_BaseFaults_1_2_draft_01_xsd.BaseFaultType implements java.io.Serializable {

    public TopicPathDialectUnknownFaultType() {
    }

    public TopicPathDialectUnknownFaultType(java.util.Calendar timestamp, org.xmlsoap.schemas.ws._2004._03.addressing.EndpointReferenceType originator, org.oasis_open.docs.wsrf._2004._06.wsrf_WS_BaseFaults_1_2_draft_01_xsd.BaseFaultTypeErrorCode errorCode, org.oasis_open.docs.wsrf._2004._06.wsrf_WS_BaseFaults_1_2_draft_01_xsd.BaseFaultTypeDescription[] description, org.oasis_open.docs.wsrf._2004._06.wsrf_WS_BaseFaults_1_2_draft_01_xsd.BaseFaultType[] faultCause) {
        super(timestamp, originator, errorCode, description, faultCause);
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TopicPathDialectUnknownFaultType)) return false;
        TopicPathDialectUnknownFaultType other = (TopicPathDialectUnknownFaultType) obj;
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

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(TopicPathDialectUnknownFaultType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://docs.oasis-open.org/wsn/2004/06/wsn-WS-BaseNotification-1.2-draft-01.xsd", "TopicPathDialectUnknownFaultType"));
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

package es.caib.pagos.services.wsdl;

public class PingResponse implements java.io.Serializable {

    private java.lang.String pingResult;

    public PingResponse() {
    }

    public PingResponse(java.lang.String pingResult) {
        this.pingResult = pingResult;
    }

    /**
     * Gets the pingResult value for this PingResponse.
     * 
     * @return pingResult
     */
    public java.lang.String getPingResult() {
        return pingResult;
    }

    /**
     * Sets the pingResult value for this PingResponse.
     * 
     * @param pingResult
     */
    public void setPingResult(java.lang.String pingResult) {
        this.pingResult = pingResult;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PingResponse)) return false;
        PingResponse other = (PingResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.pingResult == null && other.getPingResult() == null) || (this.pingResult != null && this.pingResult.equals(other.getPingResult())));
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
        if (getPingResult() != null) {
            _hashCode += getPingResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(PingResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.atib.es/services/", ">pingResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pingResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.atib.es/services/", "pingResult"));
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

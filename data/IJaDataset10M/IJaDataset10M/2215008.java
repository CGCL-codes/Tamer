package gov.cdc.ncphi.phgrid.amds.stubs;

public class AmdsQueryRequestAMDSQueryRequest implements java.io.Serializable {

    private v2.AMDSQueryRequest AMDSQueryRequest;

    public AmdsQueryRequestAMDSQueryRequest() {
    }

    public AmdsQueryRequestAMDSQueryRequest(v2.AMDSQueryRequest AMDSQueryRequest) {
        this.AMDSQueryRequest = AMDSQueryRequest;
    }

    /**
     * Gets the AMDSQueryRequest value for this AmdsQueryRequestAMDSQueryRequest.
     * 
     * @return AMDSQueryRequest
     */
    public v2.AMDSQueryRequest getAMDSQueryRequest() {
        return AMDSQueryRequest;
    }

    /**
     * Sets the AMDSQueryRequest value for this AmdsQueryRequestAMDSQueryRequest.
     * 
     * @param AMDSQueryRequest
     */
    public void setAMDSQueryRequest(v2.AMDSQueryRequest AMDSQueryRequest) {
        this.AMDSQueryRequest = AMDSQueryRequest;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AmdsQueryRequestAMDSQueryRequest)) return false;
        AmdsQueryRequestAMDSQueryRequest other = (AmdsQueryRequestAMDSQueryRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.AMDSQueryRequest == null && other.getAMDSQueryRequest() == null) || (this.AMDSQueryRequest != null && this.AMDSQueryRequest.equals(other.getAMDSQueryRequest())));
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
        if (getAMDSQueryRequest() != null) {
            _hashCode += getAMDSQueryRequest().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(AmdsQueryRequestAMDSQueryRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://amds.phgrid.ncphi.cdc.gov/AMDS", ">>AmdsQueryRequest>aMDSQueryRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("AMDSQueryRequest");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ncphi.phgrid.net/schemas/amds/v2", "AMDSQueryRequest"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://ncphi.phgrid.net/schemas/amds/v2", ">AMDSQueryRequest"));
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

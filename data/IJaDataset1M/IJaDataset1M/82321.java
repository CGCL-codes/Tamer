package com.microsoft.sharepoint.webpartpages;

public class GetWebPartProperties2Response implements java.io.Serializable {

    private com.microsoft.sharepoint.webpartpages.GetWebPartProperties2ResponseGetWebPartProperties2Result getWebPartProperties2Result;

    public GetWebPartProperties2Response() {
    }

    public GetWebPartProperties2Response(com.microsoft.sharepoint.webpartpages.GetWebPartProperties2ResponseGetWebPartProperties2Result getWebPartProperties2Result) {
        this.getWebPartProperties2Result = getWebPartProperties2Result;
    }

    /**
     * Gets the getWebPartProperties2Result value for this GetWebPartProperties2Response.
     * 
     * @return getWebPartProperties2Result
     */
    public com.microsoft.sharepoint.webpartpages.GetWebPartProperties2ResponseGetWebPartProperties2Result getGetWebPartProperties2Result() {
        return getWebPartProperties2Result;
    }

    /**
     * Sets the getWebPartProperties2Result value for this GetWebPartProperties2Response.
     * 
     * @param getWebPartProperties2Result
     */
    public void setGetWebPartProperties2Result(com.microsoft.sharepoint.webpartpages.GetWebPartProperties2ResponseGetWebPartProperties2Result getWebPartProperties2Result) {
        this.getWebPartProperties2Result = getWebPartProperties2Result;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetWebPartProperties2Response)) return false;
        GetWebPartProperties2Response other = (GetWebPartProperties2Response) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.getWebPartProperties2Result == null && other.getGetWebPartProperties2Result() == null) || (this.getWebPartProperties2Result != null && this.getWebPartProperties2Result.equals(other.getGetWebPartProperties2Result())));
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
        if (getGetWebPartProperties2Result() != null) {
            _hashCode += getGetWebPartProperties2Result().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(GetWebPartProperties2Response.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://microsoft.com/sharepoint/webpartpages", ">GetWebPartProperties2Response"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getWebPartProperties2Result");
        elemField.setXmlName(new javax.xml.namespace.QName("http://microsoft.com/sharepoint/webpartpages", "GetWebPartProperties2Result"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://microsoft.com/sharepoint/webpartpages", ">>GetWebPartProperties2Response>GetWebPartProperties2Result"));
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

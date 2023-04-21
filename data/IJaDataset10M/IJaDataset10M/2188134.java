package com.microsoft.schemas.sharepoint.soap.directory;

public class GetGroupCollectionFromRoleResponse implements java.io.Serializable {

    private com.microsoft.schemas.sharepoint.soap.directory.GetGroupCollectionFromRoleResponseGetGroupCollectionFromRoleResult getGroupCollectionFromRoleResult;

    public GetGroupCollectionFromRoleResponse() {
    }

    public GetGroupCollectionFromRoleResponse(com.microsoft.schemas.sharepoint.soap.directory.GetGroupCollectionFromRoleResponseGetGroupCollectionFromRoleResult getGroupCollectionFromRoleResult) {
        this.getGroupCollectionFromRoleResult = getGroupCollectionFromRoleResult;
    }

    /**
     * Gets the getGroupCollectionFromRoleResult value for this GetGroupCollectionFromRoleResponse.
     * 
     * @return getGroupCollectionFromRoleResult
     */
    public com.microsoft.schemas.sharepoint.soap.directory.GetGroupCollectionFromRoleResponseGetGroupCollectionFromRoleResult getGetGroupCollectionFromRoleResult() {
        return getGroupCollectionFromRoleResult;
    }

    /**
     * Sets the getGroupCollectionFromRoleResult value for this GetGroupCollectionFromRoleResponse.
     * 
     * @param getGroupCollectionFromRoleResult
     */
    public void setGetGroupCollectionFromRoleResult(com.microsoft.schemas.sharepoint.soap.directory.GetGroupCollectionFromRoleResponseGetGroupCollectionFromRoleResult getGroupCollectionFromRoleResult) {
        this.getGroupCollectionFromRoleResult = getGroupCollectionFromRoleResult;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetGroupCollectionFromRoleResponse)) return false;
        GetGroupCollectionFromRoleResponse other = (GetGroupCollectionFromRoleResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.getGroupCollectionFromRoleResult == null && other.getGetGroupCollectionFromRoleResult() == null) || (this.getGroupCollectionFromRoleResult != null && this.getGroupCollectionFromRoleResult.equals(other.getGetGroupCollectionFromRoleResult())));
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
        if (getGetGroupCollectionFromRoleResult() != null) {
            _hashCode += getGetGroupCollectionFromRoleResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(GetGroupCollectionFromRoleResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.microsoft.com/sharepoint/soap/directory/", ">GetGroupCollectionFromRoleResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getGroupCollectionFromRoleResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.microsoft.com/sharepoint/soap/directory/", "GetGroupCollectionFromRoleResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.microsoft.com/sharepoint/soap/directory/", ">>GetGroupCollectionFromRoleResponse>GetGroupCollectionFromRoleResult"));
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

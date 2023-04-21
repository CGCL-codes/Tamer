package com.microsoft.schemas.sharepoint.soap.ois;

public class RenameResponse implements java.io.Serializable {

    private com.microsoft.schemas.sharepoint.soap.ois.RenameResponseRenameResult renameResult;

    public RenameResponse() {
    }

    public RenameResponse(com.microsoft.schemas.sharepoint.soap.ois.RenameResponseRenameResult renameResult) {
        this.renameResult = renameResult;
    }

    /**
     * Gets the renameResult value for this RenameResponse.
     * 
     * @return renameResult
     */
    public com.microsoft.schemas.sharepoint.soap.ois.RenameResponseRenameResult getRenameResult() {
        return renameResult;
    }

    /**
     * Sets the renameResult value for this RenameResponse.
     * 
     * @param renameResult
     */
    public void setRenameResult(com.microsoft.schemas.sharepoint.soap.ois.RenameResponseRenameResult renameResult) {
        this.renameResult = renameResult;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RenameResponse)) return false;
        RenameResponse other = (RenameResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.renameResult == null && other.getRenameResult() == null) || (this.renameResult != null && this.renameResult.equals(other.getRenameResult())));
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
        if (getRenameResult() != null) {
            _hashCode += getRenameResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(RenameResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.microsoft.com/sharepoint/soap/ois/", ">RenameResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("renameResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.microsoft.com/sharepoint/soap/ois/", "RenameResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.microsoft.com/sharepoint/soap/ois/", ">>RenameResponse>RenameResult"));
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

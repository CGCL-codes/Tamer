package com.microsoft.schemas.sharepoint.soap.meetings;

public class GetMeetingsInformation implements java.io.Serializable {

    private org.apache.axis.types.UnsignedInt requestFlags;

    private org.apache.axis.types.UnsignedInt lcid;

    public GetMeetingsInformation() {
    }

    public GetMeetingsInformation(org.apache.axis.types.UnsignedInt requestFlags, org.apache.axis.types.UnsignedInt lcid) {
        this.requestFlags = requestFlags;
        this.lcid = lcid;
    }

    /**
     * Gets the requestFlags value for this GetMeetingsInformation.
     * 
     * @return requestFlags
     */
    public org.apache.axis.types.UnsignedInt getRequestFlags() {
        return requestFlags;
    }

    /**
     * Sets the requestFlags value for this GetMeetingsInformation.
     * 
     * @param requestFlags
     */
    public void setRequestFlags(org.apache.axis.types.UnsignedInt requestFlags) {
        this.requestFlags = requestFlags;
    }

    /**
     * Gets the lcid value for this GetMeetingsInformation.
     * 
     * @return lcid
     */
    public org.apache.axis.types.UnsignedInt getLcid() {
        return lcid;
    }

    /**
     * Sets the lcid value for this GetMeetingsInformation.
     * 
     * @param lcid
     */
    public void setLcid(org.apache.axis.types.UnsignedInt lcid) {
        this.lcid = lcid;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetMeetingsInformation)) return false;
        GetMeetingsInformation other = (GetMeetingsInformation) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.requestFlags == null && other.getRequestFlags() == null) || (this.requestFlags != null && this.requestFlags.equals(other.getRequestFlags()))) && ((this.lcid == null && other.getLcid() == null) || (this.lcid != null && this.lcid.equals(other.getLcid())));
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
        if (getRequestFlags() != null) {
            _hashCode += getRequestFlags().hashCode();
        }
        if (getLcid() != null) {
            _hashCode += getLcid().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(GetMeetingsInformation.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.microsoft.com/sharepoint/soap/meetings/", ">GetMeetingsInformation"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("requestFlags");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.microsoft.com/sharepoint/soap/meetings/", "requestFlags"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "unsignedInt"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lcid");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.microsoft.com/sharepoint/soap/meetings/", "lcid"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "unsignedInt"));
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

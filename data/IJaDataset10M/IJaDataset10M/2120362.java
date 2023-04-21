package com.microsoft.schemas.sharepoint.soap.meetings;

public class AddMeetingFromICalResponse implements java.io.Serializable {

    private com.microsoft.schemas.sharepoint.soap.meetings.AddMeetingFromICalResponseAddMeetingFromICalResult addMeetingFromICalResult;

    public AddMeetingFromICalResponse() {
    }

    public AddMeetingFromICalResponse(com.microsoft.schemas.sharepoint.soap.meetings.AddMeetingFromICalResponseAddMeetingFromICalResult addMeetingFromICalResult) {
        this.addMeetingFromICalResult = addMeetingFromICalResult;
    }

    /**
     * Gets the addMeetingFromICalResult value for this AddMeetingFromICalResponse.
     * 
     * @return addMeetingFromICalResult
     */
    public com.microsoft.schemas.sharepoint.soap.meetings.AddMeetingFromICalResponseAddMeetingFromICalResult getAddMeetingFromICalResult() {
        return addMeetingFromICalResult;
    }

    /**
     * Sets the addMeetingFromICalResult value for this AddMeetingFromICalResponse.
     * 
     * @param addMeetingFromICalResult
     */
    public void setAddMeetingFromICalResult(com.microsoft.schemas.sharepoint.soap.meetings.AddMeetingFromICalResponseAddMeetingFromICalResult addMeetingFromICalResult) {
        this.addMeetingFromICalResult = addMeetingFromICalResult;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AddMeetingFromICalResponse)) return false;
        AddMeetingFromICalResponse other = (AddMeetingFromICalResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.addMeetingFromICalResult == null && other.getAddMeetingFromICalResult() == null) || (this.addMeetingFromICalResult != null && this.addMeetingFromICalResult.equals(other.getAddMeetingFromICalResult())));
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
        if (getAddMeetingFromICalResult() != null) {
            _hashCode += getAddMeetingFromICalResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(AddMeetingFromICalResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.microsoft.com/sharepoint/soap/meetings/", ">AddMeetingFromICalResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("addMeetingFromICalResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.microsoft.com/sharepoint/soap/meetings/", "AddMeetingFromICalResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.microsoft.com/sharepoint/soap/meetings/", ">>AddMeetingFromICalResponse>AddMeetingFromICalResult"));
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

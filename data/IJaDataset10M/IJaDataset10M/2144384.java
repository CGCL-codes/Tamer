package org.openmeetings.axis.services;

public class DeleteUserByExternalUserIdAndType implements java.io.Serializable {

    private java.lang.String SID;

    private java.lang.Long externalUserId;

    private java.lang.String externalUserType;

    public DeleteUserByExternalUserIdAndType() {
    }

    public DeleteUserByExternalUserIdAndType(java.lang.String SID, java.lang.Long externalUserId, java.lang.String externalUserType) {
        this.SID = SID;
        this.externalUserId = externalUserId;
        this.externalUserType = externalUserType;
    }

    /**
     * Gets the SID value for this DeleteUserByExternalUserIdAndType.
     * 
     * @return SID
     */
    public java.lang.String getSID() {
        return SID;
    }

    /**
     * Sets the SID value for this DeleteUserByExternalUserIdAndType.
     * 
     * @param SID
     */
    public void setSID(java.lang.String SID) {
        this.SID = SID;
    }

    /**
     * Gets the externalUserId value for this DeleteUserByExternalUserIdAndType.
     * 
     * @return externalUserId
     */
    public java.lang.Long getExternalUserId() {
        return externalUserId;
    }

    /**
     * Sets the externalUserId value for this DeleteUserByExternalUserIdAndType.
     * 
     * @param externalUserId
     */
    public void setExternalUserId(java.lang.Long externalUserId) {
        this.externalUserId = externalUserId;
    }

    /**
     * Gets the externalUserType value for this DeleteUserByExternalUserIdAndType.
     * 
     * @return externalUserType
     */
    public java.lang.String getExternalUserType() {
        return externalUserType;
    }

    /**
     * Sets the externalUserType value for this DeleteUserByExternalUserIdAndType.
     * 
     * @param externalUserType
     */
    public void setExternalUserType(java.lang.String externalUserType) {
        this.externalUserType = externalUserType;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DeleteUserByExternalUserIdAndType)) return false;
        DeleteUserByExternalUserIdAndType other = (DeleteUserByExternalUserIdAndType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.SID == null && other.getSID() == null) || (this.SID != null && this.SID.equals(other.getSID()))) && ((this.externalUserId == null && other.getExternalUserId() == null) || (this.externalUserId != null && this.externalUserId.equals(other.getExternalUserId()))) && ((this.externalUserType == null && other.getExternalUserType() == null) || (this.externalUserType != null && this.externalUserType.equals(other.getExternalUserType())));
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
        if (getSID() != null) {
            _hashCode += getSID().hashCode();
        }
        if (getExternalUserId() != null) {
            _hashCode += getExternalUserId().hashCode();
        }
        if (getExternalUserType() != null) {
            _hashCode += getExternalUserType().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(DeleteUserByExternalUserIdAndType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://services.axis.openmeetings.org", ">deleteUserByExternalUserIdAndType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("SID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://services.axis.openmeetings.org", "SID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("externalUserId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://services.axis.openmeetings.org", "externalUserId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("externalUserType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://services.axis.openmeetings.org", "externalUserType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
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

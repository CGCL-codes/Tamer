package com.siebel.www.xml.GJZQAccountInfoPid;

public class AccountManager implements java.io.Serializable {

    private java.lang.String empCellPhone;

    private java.lang.String empEmailAddress;

    private java.lang.String empLastName;

    private java.lang.String empPhone;

    public AccountManager() {
    }

    public AccountManager(java.lang.String empCellPhone, java.lang.String empEmailAddress, java.lang.String empLastName, java.lang.String empPhone) {
        this.empCellPhone = empCellPhone;
        this.empEmailAddress = empEmailAddress;
        this.empLastName = empLastName;
        this.empPhone = empPhone;
    }

    /**
     * Gets the empCellPhone value for this AccountManager.
     * 
     * @return empCellPhone
     */
    public java.lang.String getEmpCellPhone() {
        return empCellPhone;
    }

    /**
     * Sets the empCellPhone value for this AccountManager.
     * 
     * @param empCellPhone
     */
    public void setEmpCellPhone(java.lang.String empCellPhone) {
        this.empCellPhone = empCellPhone;
    }

    /**
     * Gets the empEmailAddress value for this AccountManager.
     * 
     * @return empEmailAddress
     */
    public java.lang.String getEmpEmailAddress() {
        return empEmailAddress;
    }

    /**
     * Sets the empEmailAddress value for this AccountManager.
     * 
     * @param empEmailAddress
     */
    public void setEmpEmailAddress(java.lang.String empEmailAddress) {
        this.empEmailAddress = empEmailAddress;
    }

    /**
     * Gets the empLastName value for this AccountManager.
     * 
     * @return empLastName
     */
    public java.lang.String getEmpLastName() {
        return empLastName;
    }

    /**
     * Sets the empLastName value for this AccountManager.
     * 
     * @param empLastName
     */
    public void setEmpLastName(java.lang.String empLastName) {
        this.empLastName = empLastName;
    }

    /**
     * Gets the empPhone value for this AccountManager.
     * 
     * @return empPhone
     */
    public java.lang.String getEmpPhone() {
        return empPhone;
    }

    /**
     * Sets the empPhone value for this AccountManager.
     * 
     * @param empPhone
     */
    public void setEmpPhone(java.lang.String empPhone) {
        this.empPhone = empPhone;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AccountManager)) return false;
        AccountManager other = (AccountManager) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.empCellPhone == null && other.getEmpCellPhone() == null) || (this.empCellPhone != null && this.empCellPhone.equals(other.getEmpCellPhone()))) && ((this.empEmailAddress == null && other.getEmpEmailAddress() == null) || (this.empEmailAddress != null && this.empEmailAddress.equals(other.getEmpEmailAddress()))) && ((this.empLastName == null && other.getEmpLastName() == null) || (this.empLastName != null && this.empLastName.equals(other.getEmpLastName()))) && ((this.empPhone == null && other.getEmpPhone() == null) || (this.empPhone != null && this.empPhone.equals(other.getEmpPhone())));
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
        if (getEmpCellPhone() != null) {
            _hashCode += getEmpCellPhone().hashCode();
        }
        if (getEmpEmailAddress() != null) {
            _hashCode += getEmpEmailAddress().hashCode();
        }
        if (getEmpLastName() != null) {
            _hashCode += getEmpLastName().hashCode();
        }
        if (getEmpPhone() != null) {
            _hashCode += getEmpPhone().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(AccountManager.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.siebel.com/xml/GJZQAccountInfoPid", "AccountManager"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("empCellPhone");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.siebel.com/xml/GJZQAccountInfoPid", "EmpCellPhone"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("empEmailAddress");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.siebel.com/xml/GJZQAccountInfoPid", "EmpEmailAddress"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("empLastName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.siebel.com/xml/GJZQAccountInfoPid", "EmpLastName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("empPhone");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.siebel.com/xml/GJZQAccountInfoPid", "EmpPhone"));
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

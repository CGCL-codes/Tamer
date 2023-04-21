package net.virtuemart.www.VM_Users;

public class AddVendorInput implements java.io.Serializable {

    private net.virtuemart.www.VM_Tools.LoginInfo loginInfo;

    private net.virtuemart.www.VM_Users.Vendor vendor;

    public AddVendorInput() {
    }

    public AddVendorInput(net.virtuemart.www.VM_Tools.LoginInfo loginInfo, net.virtuemart.www.VM_Users.Vendor vendor) {
        this.loginInfo = loginInfo;
        this.vendor = vendor;
    }

    /**
     * Gets the loginInfo value for this AddVendorInput.
     * 
     * @return loginInfo
     */
    public net.virtuemart.www.VM_Tools.LoginInfo getLoginInfo() {
        return loginInfo;
    }

    /**
     * Sets the loginInfo value for this AddVendorInput.
     * 
     * @param loginInfo
     */
    public void setLoginInfo(net.virtuemart.www.VM_Tools.LoginInfo loginInfo) {
        this.loginInfo = loginInfo;
    }

    /**
     * Gets the vendor value for this AddVendorInput.
     * 
     * @return vendor
     */
    public net.virtuemart.www.VM_Users.Vendor getVendor() {
        return vendor;
    }

    /**
     * Sets the vendor value for this AddVendorInput.
     * 
     * @param vendor
     */
    public void setVendor(net.virtuemart.www.VM_Users.Vendor vendor) {
        this.vendor = vendor;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AddVendorInput)) return false;
        AddVendorInput other = (AddVendorInput) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.loginInfo == null && other.getLoginInfo() == null) || (this.loginInfo != null && this.loginInfo.equals(other.getLoginInfo()))) && ((this.vendor == null && other.getVendor() == null) || (this.vendor != null && this.vendor.equals(other.getVendor())));
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
        if (getLoginInfo() != null) {
            _hashCode += getLoginInfo().hashCode();
        }
        if (getVendor() != null) {
            _hashCode += getVendor().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(AddVendorInput.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.virtuemart.net/VM_Users/", "AddVendorInput"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("loginInfo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "loginInfo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.virtuemart.net/VM_Users/", "loginInfo"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("vendor");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Vendor"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.virtuemart.net/VM_Users/", "Vendor"));
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

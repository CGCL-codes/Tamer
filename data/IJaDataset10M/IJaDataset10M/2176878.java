package com.siebel.www.xml.GJZQAccountInfo;

public class AccountInfoTopElmt implements java.io.Serializable {

    private com.siebel.www.xml.GJZQAccountInfo.AccountInfo accountInfo;

    public AccountInfoTopElmt() {
    }

    public AccountInfoTopElmt(com.siebel.www.xml.GJZQAccountInfo.AccountInfo accountInfo) {
        this.accountInfo = accountInfo;
    }

    /**
     * Gets the accountInfo value for this AccountInfoTopElmt.
     * 
     * @return accountInfo
     */
    public com.siebel.www.xml.GJZQAccountInfo.AccountInfo getAccountInfo() {
        return accountInfo;
    }

    /**
     * Sets the accountInfo value for this AccountInfoTopElmt.
     * 
     * @param accountInfo
     */
    public void setAccountInfo(com.siebel.www.xml.GJZQAccountInfo.AccountInfo accountInfo) {
        this.accountInfo = accountInfo;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AccountInfoTopElmt)) return false;
        AccountInfoTopElmt other = (AccountInfoTopElmt) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.accountInfo == null && other.getAccountInfo() == null) || (this.accountInfo != null && this.accountInfo.equals(other.getAccountInfo())));
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
        if (getAccountInfo() != null) {
            _hashCode += getAccountInfo().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(AccountInfoTopElmt.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.siebel.com/xml/GJZQAccountInfo", "AccountInfoTopElmt"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("accountInfo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.siebel.com/xml/GJZQAccountInfo", "AccountInfo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.siebel.com/xml/GJZQAccountInfo", "AccountInfo"));
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

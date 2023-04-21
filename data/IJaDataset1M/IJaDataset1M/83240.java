package net.myphpshop.www.myPHPShopAdmin;

public class ChangeShortInfoMsg implements java.io.Serializable {

    private net.myphpshop.www.myPHPShopAdmin.SignOn signOn;

    private net.myphpshop.www.myPHPShopAdmin.ShortInfo shortInfo;

    public ChangeShortInfoMsg() {
    }

    public ChangeShortInfoMsg(net.myphpshop.www.myPHPShopAdmin.SignOn signOn, net.myphpshop.www.myPHPShopAdmin.ShortInfo shortInfo) {
        this.signOn = signOn;
        this.shortInfo = shortInfo;
    }

    /**
     * Gets the signOn value for this ChangeShortInfoMsg.
     * 
     * @return signOn
     */
    public net.myphpshop.www.myPHPShopAdmin.SignOn getSignOn() {
        return signOn;
    }

    /**
     * Sets the signOn value for this ChangeShortInfoMsg.
     * 
     * @param signOn
     */
    public void setSignOn(net.myphpshop.www.myPHPShopAdmin.SignOn signOn) {
        this.signOn = signOn;
    }

    /**
     * Gets the shortInfo value for this ChangeShortInfoMsg.
     * 
     * @return shortInfo
     */
    public net.myphpshop.www.myPHPShopAdmin.ShortInfo getShortInfo() {
        return shortInfo;
    }

    /**
     * Sets the shortInfo value for this ChangeShortInfoMsg.
     * 
     * @param shortInfo
     */
    public void setShortInfo(net.myphpshop.www.myPHPShopAdmin.ShortInfo shortInfo) {
        this.shortInfo = shortInfo;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ChangeShortInfoMsg)) return false;
        ChangeShortInfoMsg other = (ChangeShortInfoMsg) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.signOn == null && other.getSignOn() == null) || (this.signOn != null && this.signOn.equals(other.getSignOn()))) && ((this.shortInfo == null && other.getShortInfo() == null) || (this.shortInfo != null && this.shortInfo.equals(other.getShortInfo())));
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
        if (getSignOn() != null) {
            _hashCode += getSignOn().hashCode();
        }
        if (getShortInfo() != null) {
            _hashCode += getShortInfo().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(ChangeShortInfoMsg.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.myphpshop.net/myPHPShopAdmin", ">ChangeShortInfoMsg"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("signOn");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.myphpshop.net/myPHPShopAdmin", "SignOn"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.myphpshop.net/myPHPShopAdmin", ">SignOn"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("shortInfo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.myphpshop.net/myPHPShopAdmin", "ShortInfo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.myphpshop.net/myPHPShopAdmin", ">ShortInfo"));
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

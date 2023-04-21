package net.myphpshop.www.myPHPShopAdmin;

public class Currency implements java.io.Serializable {

    private net.myphpshop.www.myPHPShopAdmin.CurrencyCode currencyCode;

    private net.myphpshop.www.myPHPShopAdmin.CurrencyDetail[] detail;

    public Currency() {
    }

    public Currency(net.myphpshop.www.myPHPShopAdmin.CurrencyCode currencyCode, net.myphpshop.www.myPHPShopAdmin.CurrencyDetail[] detail) {
        this.currencyCode = currencyCode;
        this.detail = detail;
    }

    /**
     * Gets the currencyCode value for this Currency.
     * 
     * @return currencyCode
     */
    public net.myphpshop.www.myPHPShopAdmin.CurrencyCode getCurrencyCode() {
        return currencyCode;
    }

    /**
     * Sets the currencyCode value for this Currency.
     * 
     * @param currencyCode
     */
    public void setCurrencyCode(net.myphpshop.www.myPHPShopAdmin.CurrencyCode currencyCode) {
        this.currencyCode = currencyCode;
    }

    /**
     * Gets the detail value for this Currency.
     * 
     * @return detail
     */
    public net.myphpshop.www.myPHPShopAdmin.CurrencyDetail[] getDetail() {
        return detail;
    }

    /**
     * Sets the detail value for this Currency.
     * 
     * @param detail
     */
    public void setDetail(net.myphpshop.www.myPHPShopAdmin.CurrencyDetail[] detail) {
        this.detail = detail;
    }

    public net.myphpshop.www.myPHPShopAdmin.CurrencyDetail getDetail(int i) {
        return this.detail[i];
    }

    public void setDetail(int i, net.myphpshop.www.myPHPShopAdmin.CurrencyDetail _value) {
        this.detail[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Currency)) return false;
        Currency other = (Currency) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.currencyCode == null && other.getCurrencyCode() == null) || (this.currencyCode != null && this.currencyCode.equals(other.getCurrencyCode()))) && ((this.detail == null && other.getDetail() == null) || (this.detail != null && java.util.Arrays.equals(this.detail, other.getDetail())));
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
        if (getCurrencyCode() != null) {
            _hashCode += getCurrencyCode().hashCode();
        }
        if (getDetail() != null) {
            for (int i = 0; i < java.lang.reflect.Array.getLength(getDetail()); i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDetail(), i);
                if (obj != null && !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(Currency.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.myphpshop.net/myPHPShopAdmin", ">Currency"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("currencyCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.myphpshop.net/myPHPShopAdmin", "CurrencyCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.myphpshop.net/myPHPShopAdmin", "CurrencyCode"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("detail");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.myphpshop.net/myPHPShopAdmin", "Detail"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.myphpshop.net/myPHPShopAdmin", ">>Currency>Detail"));
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
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

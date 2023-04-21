package net.myphpshop.www.myPHPShopAdmin;

public class ProductDetail implements java.io.Serializable {

    private java.lang.String name;

    private java.lang.String description;

    private net.myphpshop.www.myPHPShopAdmin.Language language;

    public ProductDetail() {
    }

    public ProductDetail(java.lang.String name, java.lang.String description, net.myphpshop.www.myPHPShopAdmin.Language language) {
        this.name = name;
        this.description = description;
        this.language = language;
    }

    /**
     * Gets the name value for this ProductDetail.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }

    /**
     * Sets the name value for this ProductDetail.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }

    /**
     * Gets the description value for this ProductDetail.
     * 
     * @return description
     */
    public java.lang.String getDescription() {
        return description;
    }

    /**
     * Sets the description value for this ProductDetail.
     * 
     * @param description
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    /**
     * Gets the language value for this ProductDetail.
     * 
     * @return language
     */
    public net.myphpshop.www.myPHPShopAdmin.Language getLanguage() {
        return language;
    }

    /**
     * Sets the language value for this ProductDetail.
     * 
     * @param language
     */
    public void setLanguage(net.myphpshop.www.myPHPShopAdmin.Language language) {
        this.language = language;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ProductDetail)) return false;
        ProductDetail other = (ProductDetail) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.name == null && other.getName() == null) || (this.name != null && this.name.equals(other.getName()))) && ((this.description == null && other.getDescription() == null) || (this.description != null && this.description.equals(other.getDescription()))) && ((this.language == null && other.getLanguage() == null) || (this.language != null && this.language.equals(other.getLanguage())));
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
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (getDescription() != null) {
            _hashCode += getDescription().hashCode();
        }
        if (getLanguage() != null) {
            _hashCode += getLanguage().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(ProductDetail.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.myphpshop.net/myPHPShopAdmin", ">>Product>Detail"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("language");
        attrField.setXmlName(new javax.xml.namespace.QName("", "language"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.myphpshop.net/myPHPShopAdmin", "Language"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("name");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.myphpshop.net/myPHPShopAdmin", "Name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("description");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.myphpshop.net/myPHPShopAdmin", "Description"));
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

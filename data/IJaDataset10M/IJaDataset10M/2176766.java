package com.konakartadmin.app;

public class AdminCategoryDescription implements java.io.Serializable {

    private int categoryId;

    private com.konakartadmin.app.AdminLanguage language;

    private int languageId;

    private java.lang.String name;

    public AdminCategoryDescription() {
    }

    public AdminCategoryDescription(int categoryId, com.konakartadmin.app.AdminLanguage language, int languageId, java.lang.String name) {
        this.categoryId = categoryId;
        this.language = language;
        this.languageId = languageId;
        this.name = name;
    }

    /**
     * Gets the categoryId value for this AdminCategoryDescription.
     * 
     * @return categoryId
     */
    public int getCategoryId() {
        return categoryId;
    }

    /**
     * Sets the categoryId value for this AdminCategoryDescription.
     * 
     * @param categoryId
     */
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * Gets the language value for this AdminCategoryDescription.
     * 
     * @return language
     */
    public com.konakartadmin.app.AdminLanguage getLanguage() {
        return language;
    }

    /**
     * Sets the language value for this AdminCategoryDescription.
     * 
     * @param language
     */
    public void setLanguage(com.konakartadmin.app.AdminLanguage language) {
        this.language = language;
    }

    /**
     * Gets the languageId value for this AdminCategoryDescription.
     * 
     * @return languageId
     */
    public int getLanguageId() {
        return languageId;
    }

    /**
     * Sets the languageId value for this AdminCategoryDescription.
     * 
     * @param languageId
     */
    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    /**
     * Gets the name value for this AdminCategoryDescription.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }

    /**
     * Sets the name value for this AdminCategoryDescription.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AdminCategoryDescription)) return false;
        AdminCategoryDescription other = (AdminCategoryDescription) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && this.categoryId == other.getCategoryId() && ((this.language == null && other.getLanguage() == null) || (this.language != null && this.language.equals(other.getLanguage()))) && this.languageId == other.getLanguageId() && ((this.name == null && other.getName() == null) || (this.name != null && this.name.equals(other.getName())));
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
        _hashCode += getCategoryId();
        if (getLanguage() != null) {
            _hashCode += getLanguage().hashCode();
        }
        _hashCode += getLanguageId();
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(AdminCategoryDescription.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://app.konakartadmin.com", "AdminCategoryDescription"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("categoryId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "categoryId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("language");
        elemField.setXmlName(new javax.xml.namespace.QName("", "language"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://app.konakartadmin.com", "AdminLanguage"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("languageId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "languageId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("name");
        elemField.setXmlName(new javax.xml.namespace.QName("", "name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
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

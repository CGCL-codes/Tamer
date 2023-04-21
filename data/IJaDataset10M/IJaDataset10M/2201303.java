package com.google.api.adwords.v201003.o;

/**
 * A {@link SearchParameter} for {@code KEYWORD} {@link IdeaType}s
 * that
 *             sets a keyword category that all search results should
 * belong to.
 *             This search parameter will be retired soon in favor of
 * {@link CategoryProductsAndServicesSearchParameter}, which uses the
 * newer
 *             "Products and Services" taxonomy.
 *             <p>This search parameter can be used in bulk keyword requests
 * through the {@link com.google.ads.api.services.targetingideas.TargetingIdeaService#getBulkKeywordIdeas(TargetingIdeaSelector)}
 * method.
 *             <p>This element is supported by following {@link IdeaType}s:
 * KEYWORD.
 *             <p>This element is supported by following {@link RequestType}s:
 * IDEAS.
 */
public class KeywordCategoryIdSearchParameter extends com.google.api.adwords.v201003.o.SearchParameter implements java.io.Serializable {

    private java.lang.Integer categoryId;

    public KeywordCategoryIdSearchParameter() {
    }

    public KeywordCategoryIdSearchParameter(java.lang.String searchParameterType, java.lang.Integer categoryId) {
        super(searchParameterType);
        this.categoryId = categoryId;
    }

    /**
     * Gets the categoryId value for this KeywordCategoryIdSearchParameter.
     * 
     * @return categoryId   * A keyword category ID that all search results should belong
     * to.
     */
    public java.lang.Integer getCategoryId() {
        return categoryId;
    }

    /**
     * Sets the categoryId value for this KeywordCategoryIdSearchParameter.
     * 
     * @param categoryId   * A keyword category ID that all search results should belong
     * to.
     */
    public void setCategoryId(java.lang.Integer categoryId) {
        this.categoryId = categoryId;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof KeywordCategoryIdSearchParameter)) return false;
        KeywordCategoryIdSearchParameter other = (KeywordCategoryIdSearchParameter) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && ((this.categoryId == null && other.getCategoryId() == null) || (this.categoryId != null && this.categoryId.equals(other.getCategoryId())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;

    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (getCategoryId() != null) {
            _hashCode += getCategoryId().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(KeywordCategoryIdSearchParameter.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/o/v201003", "KeywordCategoryIdSearchParameter"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("categoryId");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/o/v201003", "categoryId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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

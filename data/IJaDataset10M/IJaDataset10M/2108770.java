package com.google.api.adwords.v200909.o;

/**
 * A {@link SearchParameter} for {@code KEYWORD} {@link IdeaType}s
 * that
 *             specifies the {@link KeywordMatchType}s that all results
 * must match.
 *             For example, results may be limited to only Broad or Exact
 * matches.
 *             Setting no {@link KeywordMatchTypeSearchParameter} will
 * match all
 *             targeting ideas, regardless of {@link KeywordMatchType}.
 * If multiple
 *             {@link KeywordMatchType}s are set, a result need only
 * match one
 *             match type to be returned.
 *             <p>This element is supported by following {@link IdeaType}s:
 * KEYWORD.
 *             <p>This element is supported by following {@link RequestType}s:
 * IDEAS, STATS.
 */
public class KeywordMatchTypeSearchParameter extends com.google.api.adwords.v200909.o.SearchParameter implements java.io.Serializable {

    private com.google.api.adwords.v200909.cm.KeywordMatchType[] keywordMatchTypes;

    public KeywordMatchTypeSearchParameter() {
    }

    public KeywordMatchTypeSearchParameter(java.lang.String searchParameterType, com.google.api.adwords.v200909.cm.KeywordMatchType[] keywordMatchTypes) {
        super(searchParameterType);
        this.keywordMatchTypes = keywordMatchTypes;
    }

    /**
     * Gets the keywordMatchTypes value for this KeywordMatchTypeSearchParameter.
     * 
     * @return keywordMatchTypes   * A Set of {@link KeywordMatchType}s that results must match.
     * Results
     *                     need only match a single match type in the set.
     * <span class="constraint ContentsDistinct">This field must contain
     * distinct elements.</span>
     *                     <span class="constraint ContentsNotNull">This
     * field must not contain {@code null} elements.</span>
     *                     <span class="constraint NotEmpty">This field must
     * contain at least one element.</span>
     *                     <span class="constraint Required">This field is
     * required and should not be {@code null}.</span>
     */
    public com.google.api.adwords.v200909.cm.KeywordMatchType[] getKeywordMatchTypes() {
        return keywordMatchTypes;
    }

    /**
     * Sets the keywordMatchTypes value for this KeywordMatchTypeSearchParameter.
     * 
     * @param keywordMatchTypes   * A Set of {@link KeywordMatchType}s that results must match.
     * Results
     *                     need only match a single match type in the set.
     * <span class="constraint ContentsDistinct">This field must contain
     * distinct elements.</span>
     *                     <span class="constraint ContentsNotNull">This
     * field must not contain {@code null} elements.</span>
     *                     <span class="constraint NotEmpty">This field must
     * contain at least one element.</span>
     *                     <span class="constraint Required">This field is
     * required and should not be {@code null}.</span>
     */
    public void setKeywordMatchTypes(com.google.api.adwords.v200909.cm.KeywordMatchType[] keywordMatchTypes) {
        this.keywordMatchTypes = keywordMatchTypes;
    }

    public com.google.api.adwords.v200909.cm.KeywordMatchType getKeywordMatchTypes(int i) {
        return this.keywordMatchTypes[i];
    }

    public void setKeywordMatchTypes(int i, com.google.api.adwords.v200909.cm.KeywordMatchType _value) {
        this.keywordMatchTypes[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof KeywordMatchTypeSearchParameter)) return false;
        KeywordMatchTypeSearchParameter other = (KeywordMatchTypeSearchParameter) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && ((this.keywordMatchTypes == null && other.getKeywordMatchTypes() == null) || (this.keywordMatchTypes != null && java.util.Arrays.equals(this.keywordMatchTypes, other.getKeywordMatchTypes())));
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
        if (getKeywordMatchTypes() != null) {
            for (int i = 0; i < java.lang.reflect.Array.getLength(getKeywordMatchTypes()); i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getKeywordMatchTypes(), i);
                if (obj != null && !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(KeywordMatchTypeSearchParameter.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/o/v200909", "KeywordMatchTypeSearchParameter"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("keywordMatchTypes");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/o/v200909", "keywordMatchTypes"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "KeywordMatchType"));
        elemField.setMinOccurs(0);
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

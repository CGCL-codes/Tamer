package com.google.api.adwords.v201109.cm;

/**
 * Represents a keyword.
 */
public class Keyword extends com.google.api.adwords.v201109.cm.Criterion implements java.io.Serializable {

    private java.lang.String text;

    private com.google.api.adwords.v201109.cm.KeywordMatchType matchType;

    public Keyword() {
    }

    public Keyword(java.lang.Long id, com.google.api.adwords.v201109.cm.CriterionType type, java.lang.String criterionType, java.lang.String text, com.google.api.adwords.v201109.cm.KeywordMatchType matchType) {
        super(id, type, criterionType);
        this.text = text;
        this.matchType = matchType;
    }

    /**
     * Gets the text value for this Keyword.
     * 
     * @return text   * Text of this keyword (at most 80 characters and ten words).
     * <span class="constraint Selectable">This field can be selected using
     * the value "KeywordText".</span><span class="constraint Filterable">This
     * field can be filtered on.</span>
     *                     <span class="constraint MatchesRegex">This string
     * must match the regular expression '[^\x00]*'</span>
     *                     <span class="constraint Required">This field is
     * required and should not be {@code null} when it is contained within
     * {@link Operator}s : ADD.</span>
     */
    public java.lang.String getText() {
        return text;
    }

    /**
     * Sets the text value for this Keyword.
     * 
     * @param text   * Text of this keyword (at most 80 characters and ten words).
     * <span class="constraint Selectable">This field can be selected using
     * the value "KeywordText".</span><span class="constraint Filterable">This
     * field can be filtered on.</span>
     *                     <span class="constraint MatchesRegex">This string
     * must match the regular expression '[^\x00]*'</span>
     *                     <span class="constraint Required">This field is
     * required and should not be {@code null} when it is contained within
     * {@link Operator}s : ADD.</span>
     */
    public void setText(java.lang.String text) {
        this.text = text;
    }

    /**
     * Gets the matchType value for this Keyword.
     * 
     * @return matchType   * Match type of this keyword.
     *                     <span class="constraint Selectable">This field
     * can be selected using the value "KeywordMatchType".</span><span class="constraint
     * Filterable">This field can be filtered on.</span>
     *                     <span class="constraint Required">This field is
     * required and should not be {@code null} when it is contained within
     * {@link Operator}s : ADD.</span>
     */
    public com.google.api.adwords.v201109.cm.KeywordMatchType getMatchType() {
        return matchType;
    }

    /**
     * Sets the matchType value for this Keyword.
     * 
     * @param matchType   * Match type of this keyword.
     *                     <span class="constraint Selectable">This field
     * can be selected using the value "KeywordMatchType".</span><span class="constraint
     * Filterable">This field can be filtered on.</span>
     *                     <span class="constraint Required">This field is
     * required and should not be {@code null} when it is contained within
     * {@link Operator}s : ADD.</span>
     */
    public void setMatchType(com.google.api.adwords.v201109.cm.KeywordMatchType matchType) {
        this.matchType = matchType;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Keyword)) return false;
        Keyword other = (Keyword) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && ((this.text == null && other.getText() == null) || (this.text != null && this.text.equals(other.getText()))) && ((this.matchType == null && other.getMatchType() == null) || (this.matchType != null && this.matchType.equals(other.getMatchType())));
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
        if (getText() != null) {
            _hashCode += getText().hashCode();
        }
        if (getMatchType() != null) {
            _hashCode += getMatchType().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(Keyword.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "Keyword"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("text");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "text"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("matchType");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "matchType"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "KeywordMatchType"));
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

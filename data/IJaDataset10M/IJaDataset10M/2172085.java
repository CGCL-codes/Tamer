package com.google.api.ads.dfp.v201201;

/**
 * Represents a list variable defined in a creative template. This
 * is similar to
 *             {@link StringCreativeTemplateVariable}, except that there
 * are possible choices to
 *             choose from.
 *             <p>
 *             Use {@link StringCreativeTemplateVariableValue} to specify
 * the value
 *             for this variable when creating {@link TemplateCreative}
 * from the {@link TemplateCreative}.
 */
public class ListStringCreativeTemplateVariable extends com.google.api.ads.dfp.v201201.StringCreativeTemplateVariable implements java.io.Serializable {

    private com.google.api.ads.dfp.v201201.ListStringCreativeTemplateVariableVariableChoice[] choices;

    private java.lang.Boolean allowOtherChoice;

    public ListStringCreativeTemplateVariable() {
    }

    public ListStringCreativeTemplateVariable(java.lang.String label, java.lang.String uniqueName, java.lang.String description, java.lang.Boolean isRequired, java.lang.String creativeTemplateVariableType, java.lang.String defaultValue, com.google.api.ads.dfp.v201201.ListStringCreativeTemplateVariableVariableChoice[] choices, java.lang.Boolean allowOtherChoice) {
        super(label, uniqueName, description, isRequired, creativeTemplateVariableType, defaultValue);
        this.choices = choices;
        this.allowOtherChoice = allowOtherChoice;
    }

    /**
     * Gets the choices value for this ListStringCreativeTemplateVariable.
     * 
     * @return choices   * The values within the list users need to select from.
     */
    public com.google.api.ads.dfp.v201201.ListStringCreativeTemplateVariableVariableChoice[] getChoices() {
        return choices;
    }

    /**
     * Sets the choices value for this ListStringCreativeTemplateVariable.
     * 
     * @param choices   * The values within the list users need to select from.
     */
    public void setChoices(com.google.api.ads.dfp.v201201.ListStringCreativeTemplateVariableVariableChoice[] choices) {
        this.choices = choices;
    }

    public com.google.api.ads.dfp.v201201.ListStringCreativeTemplateVariableVariableChoice getChoices(int i) {
        return this.choices[i];
    }

    public void setChoices(int i, com.google.api.ads.dfp.v201201.ListStringCreativeTemplateVariableVariableChoice _value) {
        this.choices[i] = _value;
    }

    /**
     * Gets the allowOtherChoice value for this ListStringCreativeTemplateVariable.
     * 
     * @return allowOtherChoice   * {@code true} if a user can specifiy an 'other' value.
     *                     For example, if a variable called backgroundColor
     * is defined as a list
     *                     with values: red, green, blue, this boolean can
     * be set to allow a user
     *                     to enter a value not on the list such as purple.
     */
    public java.lang.Boolean getAllowOtherChoice() {
        return allowOtherChoice;
    }

    /**
     * Sets the allowOtherChoice value for this ListStringCreativeTemplateVariable.
     * 
     * @param allowOtherChoice   * {@code true} if a user can specifiy an 'other' value.
     *                     For example, if a variable called backgroundColor
     * is defined as a list
     *                     with values: red, green, blue, this boolean can
     * be set to allow a user
     *                     to enter a value not on the list such as purple.
     */
    public void setAllowOtherChoice(java.lang.Boolean allowOtherChoice) {
        this.allowOtherChoice = allowOtherChoice;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ListStringCreativeTemplateVariable)) return false;
        ListStringCreativeTemplateVariable other = (ListStringCreativeTemplateVariable) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && ((this.choices == null && other.getChoices() == null) || (this.choices != null && java.util.Arrays.equals(this.choices, other.getChoices()))) && ((this.allowOtherChoice == null && other.getAllowOtherChoice() == null) || (this.allowOtherChoice != null && this.allowOtherChoice.equals(other.getAllowOtherChoice())));
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
        if (getChoices() != null) {
            for (int i = 0; i < java.lang.reflect.Array.getLength(getChoices()); i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getChoices(), i);
                if (obj != null && !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getAllowOtherChoice() != null) {
            _hashCode += getAllowOtherChoice().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(ListStringCreativeTemplateVariable.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201201", "ListStringCreativeTemplateVariable"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("choices");
        elemField.setXmlName(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201201", "choices"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201201", "ListStringCreativeTemplateVariable.VariableChoice"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("allowOtherChoice");
        elemField.setXmlName(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201201", "allowOtherChoice"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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

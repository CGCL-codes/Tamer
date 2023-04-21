package com.google.api.adwords.v201003.o;

/**
 * A {@link SearchParameter} for {@code PLACEMENT} {@link IdeaType}s
 * used to
 *             filter the results by the {@link AdType}.
 *             <p>This element is supported by following {@link IdeaType}s:
 * PLACEMENT.
 *             <p>This element is supported by following {@link RequestType}s:
 * IDEAS.
 */
public class AdTypeSearchParameter extends com.google.api.adwords.v201003.o.SearchParameter implements java.io.Serializable {

    private com.google.api.adwords.v201003.o.SiteConstantsAdType[] adTypes;

    public AdTypeSearchParameter() {
    }

    public AdTypeSearchParameter(java.lang.String searchParameterType, com.google.api.adwords.v201003.o.SiteConstantsAdType[] adTypes) {
        super(searchParameterType);
        this.adTypes = adTypes;
    }

    /**
     * Gets the adTypes value for this AdTypeSearchParameter.
     * 
     * @return adTypes   * A set of {@linke AdType}s desired in the results. For example,
     * specify
     *                     {@code DISPLAY} {@link AdType} to get {@code PLACEMENT}
     * {@link IdeaType}s
     *                     that support display ads (independent of the ad
     * size).
     *                     <span class="constraint ContentsDistinct">This
     * field must contain distinct elements.</span>
     *                     <span class="constraint ContentsNotNull">This
     * field must not contain {@code null} elements.</span>
     *                     <span class="constraint NotEmpty">This field must
     * contain at least one element.</span>
     *                     <span class="constraint Required">This field is
     * required and should not be {@code null}.</span>
     */
    public com.google.api.adwords.v201003.o.SiteConstantsAdType[] getAdTypes() {
        return adTypes;
    }

    /**
     * Sets the adTypes value for this AdTypeSearchParameter.
     * 
     * @param adTypes   * A set of {@linke AdType}s desired in the results. For example,
     * specify
     *                     {@code DISPLAY} {@link AdType} to get {@code PLACEMENT}
     * {@link IdeaType}s
     *                     that support display ads (independent of the ad
     * size).
     *                     <span class="constraint ContentsDistinct">This
     * field must contain distinct elements.</span>
     *                     <span class="constraint ContentsNotNull">This
     * field must not contain {@code null} elements.</span>
     *                     <span class="constraint NotEmpty">This field must
     * contain at least one element.</span>
     *                     <span class="constraint Required">This field is
     * required and should not be {@code null}.</span>
     */
    public void setAdTypes(com.google.api.adwords.v201003.o.SiteConstantsAdType[] adTypes) {
        this.adTypes = adTypes;
    }

    public com.google.api.adwords.v201003.o.SiteConstantsAdType getAdTypes(int i) {
        return this.adTypes[i];
    }

    public void setAdTypes(int i, com.google.api.adwords.v201003.o.SiteConstantsAdType _value) {
        this.adTypes[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AdTypeSearchParameter)) return false;
        AdTypeSearchParameter other = (AdTypeSearchParameter) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && ((this.adTypes == null && other.getAdTypes() == null) || (this.adTypes != null && java.util.Arrays.equals(this.adTypes, other.getAdTypes())));
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
        if (getAdTypes() != null) {
            for (int i = 0; i < java.lang.reflect.Array.getLength(getAdTypes()); i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAdTypes(), i);
                if (obj != null && !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(AdTypeSearchParameter.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/o/v201003", "AdTypeSearchParameter"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("adTypes");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/o/v201003", "adTypes"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/o/v201003", "SiteConstants.AdType"));
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

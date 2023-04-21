package com.google.api.adwords.v201003.cm;

/**
 * Immutable structure to hold a gender target.
 */
public class GenderTarget extends com.google.api.adwords.v201003.cm.DemographicTarget implements java.io.Serializable {

    private com.google.api.adwords.v201003.cm.GenderTargetGender gender;

    public GenderTarget() {
    }

    public GenderTarget(java.lang.String targetType, java.lang.Integer bidModifier, com.google.api.adwords.v201003.cm.GenderTargetGender gender) {
        super(targetType, bidModifier);
        this.gender = gender;
    }

    /**
     * Gets the gender value for this GenderTarget.
     * 
     * @return gender   * The gender segment to be targeted.
     *                     <span class="constraint Required">This field is
     * required and should not be {@code null}.</span>
     */
    public com.google.api.adwords.v201003.cm.GenderTargetGender getGender() {
        return gender;
    }

    /**
     * Sets the gender value for this GenderTarget.
     * 
     * @param gender   * The gender segment to be targeted.
     *                     <span class="constraint Required">This field is
     * required and should not be {@code null}.</span>
     */
    public void setGender(com.google.api.adwords.v201003.cm.GenderTargetGender gender) {
        this.gender = gender;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GenderTarget)) return false;
        GenderTarget other = (GenderTarget) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && ((this.gender == null && other.getGender() == null) || (this.gender != null && this.gender.equals(other.getGender())));
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
        if (getGender() != null) {
            _hashCode += getGender().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(GenderTarget.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "GenderTarget"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("gender");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "gender"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "GenderTarget.Gender"));
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

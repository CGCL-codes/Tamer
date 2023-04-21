package com.google.api.adwords.v200909.cm;

/**
 * Represents US metropolitan regions (metros) for targeting.
 *             The list of metros available for targeting are listed
 *             <a href="http://code.google.com/apis/adwords/docs/appendix/metrocodes.html">
 * here.</a>
 */
public class MetroTarget extends com.google.api.adwords.v200909.cm.GeoTarget implements java.io.Serializable {

    private java.lang.String metroCode;

    public MetroTarget() {
    }

    public MetroTarget(java.lang.String targetType, java.lang.Boolean excluded, java.lang.String metroCode) {
        super(targetType, excluded);
        this.metroCode = metroCode;
    }

    /**
     * Gets the metroCode value for this MetroTarget.
     * 
     * @return metroCode   * Metro code.
     *                     <span class="constraint Required">This field is
     * required and should not be {@code null}.</span>
     *                     <span class="constraint StringLength">This string
     * must not be empty.</span>
     */
    public java.lang.String getMetroCode() {
        return metroCode;
    }

    /**
     * Sets the metroCode value for this MetroTarget.
     * 
     * @param metroCode   * Metro code.
     *                     <span class="constraint Required">This field is
     * required and should not be {@code null}.</span>
     *                     <span class="constraint StringLength">This string
     * must not be empty.</span>
     */
    public void setMetroCode(java.lang.String metroCode) {
        this.metroCode = metroCode;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MetroTarget)) return false;
        MetroTarget other = (MetroTarget) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && ((this.metroCode == null && other.getMetroCode() == null) || (this.metroCode != null && this.metroCode.equals(other.getMetroCode())));
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
        if (getMetroCode() != null) {
            _hashCode += getMetroCode().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(MetroTarget.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "MetroTarget"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("metroCode");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "metroCode"));
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

package com.google.api.adwords.v201109.cm;

/**
 * Represents an id indicating a grouping of Ads under some heuristic.
 */
public class AdUnionId implements java.io.Serializable {

    private java.lang.Long id;

    private java.lang.String adUnionIdType;

    public AdUnionId() {
    }

    public AdUnionId(java.lang.Long id, java.lang.String adUnionIdType) {
        this.id = id;
        this.adUnionIdType = adUnionIdType;
    }

    /**
     * Gets the id value for this AdUnionId.
     * 
     * @return id   * The ID of the ad union
     *                 <span class="constraint InRange">This field must be
     * greater than or equal to 1.</span>
     */
    public java.lang.Long getId() {
        return id;
    }

    /**
     * Sets the id value for this AdUnionId.
     * 
     * @param id   * The ID of the ad union
     *                 <span class="constraint InRange">This field must be
     * greater than or equal to 1.</span>
     */
    public void setId(java.lang.Long id) {
        this.id = id;
    }

    /**
     * Gets the adUnionIdType value for this AdUnionId.
     * 
     * @return adUnionIdType   * Indicates that this instance is a subtype of AdUnionId.
     *                 Although this field is returned in the response, it
     * is ignored on input
     *                 and cannot be selected. Specify xsi:type instead.
     */
    public java.lang.String getAdUnionIdType() {
        return adUnionIdType;
    }

    /**
     * Sets the adUnionIdType value for this AdUnionId.
     * 
     * @param adUnionIdType   * Indicates that this instance is a subtype of AdUnionId.
     *                 Although this field is returned in the response, it
     * is ignored on input
     *                 and cannot be selected. Specify xsi:type instead.
     */
    public void setAdUnionIdType(java.lang.String adUnionIdType) {
        this.adUnionIdType = adUnionIdType;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AdUnionId)) return false;
        AdUnionId other = (AdUnionId) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.id == null && other.getId() == null) || (this.id != null && this.id.equals(other.getId()))) && ((this.adUnionIdType == null && other.getAdUnionIdType() == null) || (this.adUnionIdType != null && this.adUnionIdType.equals(other.getAdUnionIdType())));
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
        if (getId() != null) {
            _hashCode += getId().hashCode();
        }
        if (getAdUnionIdType() != null) {
            _hashCode += getAdUnionIdType().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(AdUnionId.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "AdUnionId"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("adUnionIdType");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "AdUnionId.Type"));
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

package com.google.api.adwords.v201101.cm;

/**
 * Class representing a location with its geographic coordinates.
 */
public class GeoLocation implements java.io.Serializable {

    private com.google.api.adwords.v201101.cm.GeoPoint geoPoint;

    private com.google.api.adwords.v201101.cm.Address address;

    private byte[] encodedLocation;

    private java.lang.String geoLocationType;

    public GeoLocation() {
    }

    public GeoLocation(com.google.api.adwords.v201101.cm.GeoPoint geoPoint, com.google.api.adwords.v201101.cm.Address address, byte[] encodedLocation, java.lang.String geoLocationType) {
        this.geoPoint = geoPoint;
        this.address = address;
        this.encodedLocation = encodedLocation;
        this.geoLocationType = geoLocationType;
    }

    /**
     * Gets the geoPoint value for this GeoLocation.
     * 
     * @return geoPoint   * Latitude/Longitude of the location.
     *                 <span class="constraint Required">This field is required
     * and should not be {@code null}.</span>
     */
    public com.google.api.adwords.v201101.cm.GeoPoint getGeoPoint() {
        return geoPoint;
    }

    /**
     * Sets the geoPoint value for this GeoLocation.
     * 
     * @param geoPoint   * Latitude/Longitude of the location.
     *                 <span class="constraint Required">This field is required
     * and should not be {@code null}.</span>
     */
    public void setGeoPoint(com.google.api.adwords.v201101.cm.GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }

    /**
     * Gets the address value for this GeoLocation.
     * 
     * @return address   * Address of the location.
     *                 <span class="constraint Required">This field is required
     * and should not be {@code null}.</span>
     */
    public com.google.api.adwords.v201101.cm.Address getAddress() {
        return address;
    }

    /**
     * Sets the address value for this GeoLocation.
     * 
     * @param address   * Address of the location.
     *                 <span class="constraint Required">This field is required
     * and should not be {@code null}.</span>
     */
    public void setAddress(com.google.api.adwords.v201101.cm.Address address) {
        this.address = address;
    }

    /**
     * Gets the encodedLocation value for this GeoLocation.
     * 
     * @return encodedLocation   * Encoded location information.
     *                 <span class="constraint Required">This field is required
     * and should not be {@code null}.</span>
     */
    public byte[] getEncodedLocation() {
        return encodedLocation;
    }

    /**
     * Sets the encodedLocation value for this GeoLocation.
     * 
     * @param encodedLocation   * Encoded location information.
     *                 <span class="constraint Required">This field is required
     * and should not be {@code null}.</span>
     */
    public void setEncodedLocation(byte[] encodedLocation) {
        this.encodedLocation = encodedLocation;
    }

    /**
     * Gets the geoLocationType value for this GeoLocation.
     * 
     * @return geoLocationType   * This field indicates the subtype of GeoLocation of this instance.
     * It is ignored
     *                 on input, and instead xsi:type should be specified.
     */
    public java.lang.String getGeoLocationType() {
        return geoLocationType;
    }

    /**
     * Sets the geoLocationType value for this GeoLocation.
     * 
     * @param geoLocationType   * This field indicates the subtype of GeoLocation of this instance.
     * It is ignored
     *                 on input, and instead xsi:type should be specified.
     */
    public void setGeoLocationType(java.lang.String geoLocationType) {
        this.geoLocationType = geoLocationType;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GeoLocation)) return false;
        GeoLocation other = (GeoLocation) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.geoPoint == null && other.getGeoPoint() == null) || (this.geoPoint != null && this.geoPoint.equals(other.getGeoPoint()))) && ((this.address == null && other.getAddress() == null) || (this.address != null && this.address.equals(other.getAddress()))) && ((this.encodedLocation == null && other.getEncodedLocation() == null) || (this.encodedLocation != null && java.util.Arrays.equals(this.encodedLocation, other.getEncodedLocation()))) && ((this.geoLocationType == null && other.getGeoLocationType() == null) || (this.geoLocationType != null && this.geoLocationType.equals(other.getGeoLocationType())));
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
        if (getGeoPoint() != null) {
            _hashCode += getGeoPoint().hashCode();
        }
        if (getAddress() != null) {
            _hashCode += getAddress().hashCode();
        }
        if (getEncodedLocation() != null) {
            for (int i = 0; i < java.lang.reflect.Array.getLength(getEncodedLocation()); i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getEncodedLocation(), i);
                if (obj != null && !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getGeoLocationType() != null) {
            _hashCode += getGeoLocationType().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(GeoLocation.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201101", "GeoLocation"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("geoPoint");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201101", "geoPoint"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201101", "GeoPoint"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("address");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201101", "address"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201101", "Address"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("encodedLocation");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201101", "encodedLocation"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("geoLocationType");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201101", "GeoLocation.Type"));
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

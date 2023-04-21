package com.capeclear.www.globalweather.xsd;

public class Phenomenon implements java.io.Serializable {

    private com.capeclear.www.globalweather.xsd.PhenomenonType type;

    private com.capeclear.www.globalweather.xsd.PhenomenonIntensity intensity;

    private java.lang.String string;

    public Phenomenon() {
    }

    public Phenomenon(com.capeclear.www.globalweather.xsd.PhenomenonType type, com.capeclear.www.globalweather.xsd.PhenomenonIntensity intensity, java.lang.String string) {
        this.type = type;
        this.intensity = intensity;
        this.string = string;
    }

    /**
     * Gets the type value for this Phenomenon.
     * 
     * @return type
     */
    public com.capeclear.www.globalweather.xsd.PhenomenonType getType() {
        return type;
    }

    /**
     * Sets the type value for this Phenomenon.
     * 
     * @param type
     */
    public void setType(com.capeclear.www.globalweather.xsd.PhenomenonType type) {
        this.type = type;
    }

    /**
     * Gets the intensity value for this Phenomenon.
     * 
     * @return intensity
     */
    public com.capeclear.www.globalweather.xsd.PhenomenonIntensity getIntensity() {
        return intensity;
    }

    /**
     * Sets the intensity value for this Phenomenon.
     * 
     * @param intensity
     */
    public void setIntensity(com.capeclear.www.globalweather.xsd.PhenomenonIntensity intensity) {
        this.intensity = intensity;
    }

    /**
     * Gets the string value for this Phenomenon.
     * 
     * @return string
     */
    public java.lang.String getString() {
        return string;
    }

    /**
     * Sets the string value for this Phenomenon.
     * 
     * @param string
     */
    public void setString(java.lang.String string) {
        this.string = string;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Phenomenon)) return false;
        Phenomenon other = (Phenomenon) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.type == null && other.getType() == null) || (this.type != null && this.type.equals(other.getType()))) && ((this.intensity == null && other.getIntensity() == null) || (this.intensity != null && this.intensity.equals(other.getIntensity()))) && ((this.string == null && other.getString() == null) || (this.string != null && this.string.equals(other.getString())));
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
        if (getType() != null) {
            _hashCode += getType().hashCode();
        }
        if (getIntensity() != null) {
            _hashCode += getIntensity().hashCode();
        }
        if (getString() != null) {
            _hashCode += getString().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(Phenomenon.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.capeclear.com/globalweather/xsd/", "Phenomenon"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("type");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.capeclear.com/globalweather/xsd/", "type"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.capeclear.com/globalweather/xsd/", "PhenomenonType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("intensity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.capeclear.com/globalweather/xsd/", "intensity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.capeclear.com/globalweather/xsd/", "PhenomenonIntensity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("string");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.capeclear.com/globalweather/xsd/", "string"));
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

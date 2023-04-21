package net.phgrid.ncphi.schemas.amds.v1;

public class TimePeriodType implements java.io.Serializable, org.apache.axis.encoding.SimpleType {

    private java.lang.String _value;

    private java.lang.String aggregationType;

    public TimePeriodType() {
    }

    public TimePeriodType(java.lang.String _value) {
        this._value = _value;
    }

    public java.lang.String toString() {
        return _value;
    }

    /**
     * Gets the _value value for this TimePeriodType.
     * 
     * @return _value
     */
    public java.lang.String get_value() {
        return _value;
    }

    /**
     * Sets the _value value for this TimePeriodType.
     * 
     * @param _value
     */
    public void set_value(java.lang.String _value) {
        this._value = _value;
    }

    /**
     * Gets the aggregationType value for this TimePeriodType.
     * 
     * @return aggregationType
     */
    public java.lang.String getAggregationType() {
        return aggregationType;
    }

    /**
     * Sets the aggregationType value for this TimePeriodType.
     * 
     * @param aggregationType
     */
    public void setAggregationType(java.lang.String aggregationType) {
        this.aggregationType = aggregationType;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TimePeriodType)) return false;
        TimePeriodType other = (TimePeriodType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this._value == null && other.get_value() == null) || (this._value != null && this._value.equals(other.get_value()))) && ((this.aggregationType == null && other.getAggregationType() == null) || (this.aggregationType != null && this.aggregationType.equals(other.getAggregationType())));
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
        if (get_value() != null) {
            _hashCode += get_value().hashCode();
        }
        if (getAggregationType() != null) {
            _hashCode += getAggregationType().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(TimePeriodType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ncphi.phgrid.net/schemas/amds/v1", "TimePeriodType"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("aggregationType");
        attrField.setXmlName(new javax.xml.namespace.QName("", "aggregationType"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://ncphi.phgrid.net/schemas/amds/v1", "TimeAggregationType"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("_value");
        elemField.setXmlName(new javax.xml.namespace.QName("", "_value"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
        return new org.apache.axis.encoding.ser.SimpleSerializer(_javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(java.lang.String mechType, java.lang.Class _javaType, javax.xml.namespace.QName _xmlType) {
        return new org.apache.axis.encoding.ser.SimpleDeserializer(_javaType, _xmlType, typeDesc);
    }
}

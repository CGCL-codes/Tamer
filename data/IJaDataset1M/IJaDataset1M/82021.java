package org.xplanner.soap.ver06;

public abstract class DomainData implements java.io.Serializable {

    private int id;

    private java.util.Calendar lastUpdateTime;

    public DomainData() {
    }

    public DomainData(int id, java.util.Calendar lastUpdateTime) {
        this.id = id;
        this.lastUpdateTime = lastUpdateTime;
    }

    /**
     * Gets the id value for this DomainData.
     * 
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id value for this DomainData.
     * 
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the lastUpdateTime value for this DomainData.
     * 
     * @return lastUpdateTime
     */
    public java.util.Calendar getLastUpdateTime() {
        return lastUpdateTime;
    }

    /**
     * Sets the lastUpdateTime value for this DomainData.
     * 
     * @param lastUpdateTime
     */
    public void setLastUpdateTime(java.util.Calendar lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DomainData)) return false;
        DomainData other = (DomainData) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && this.id == other.getId() && ((this.lastUpdateTime == null && other.getLastUpdateTime() == null) || (this.lastUpdateTime != null && this.lastUpdateTime.equals(other.getLastUpdateTime())));
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
        _hashCode += getId();
        if (getLastUpdateTime() != null) {
            _hashCode += getLastUpdateTime().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(DomainData.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://domain.soap.xplanner.technoetic.com", "DomainData"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lastUpdateTime");
        elemField.setXmlName(new javax.xml.namespace.QName("", "lastUpdateTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
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

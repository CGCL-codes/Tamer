package CommonAggregateComponents_2.xsd.schema.ubl.specification.names.oasis;

public class PartyIdentificationType implements java.io.Serializable {

    private CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.IDType ID;

    private long hjid;

    public PartyIdentificationType() {
    }

    public PartyIdentificationType(CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.IDType ID, long hjid) {
        this.ID = ID;
        this.hjid = hjid;
    }

    /**
     * Gets the ID value for this PartyIdentificationType.
     * 
     * @return ID
     */
    public CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.IDType getID() {
        return ID;
    }

    /**
     * Sets the ID value for this PartyIdentificationType.
     * 
     * @param ID
     */
    public void setID(CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.IDType ID) {
        this.ID = ID;
    }

    /**
     * Gets the hjid value for this PartyIdentificationType.
     * 
     * @return hjid
     */
    public long getHjid() {
        return hjid;
    }

    /**
     * Sets the hjid value for this PartyIdentificationType.
     * 
     * @param hjid
     */
    public void setHjid(long hjid) {
        this.hjid = hjid;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PartyIdentificationType)) return false;
        PartyIdentificationType other = (PartyIdentificationType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.ID == null && other.getID() == null) || (this.ID != null && this.ID.equals(other.getID()))) && this.hjid == other.getHjid();
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
        if (getID() != null) {
            _hashCode += getID().hashCode();
        }
        _hashCode += new Long(getHjid()).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(PartyIdentificationType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2", "PartyIdentificationType"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("hjid");
        attrField.setXmlName(new javax.xml.namespace.QName("", "Hjid"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ID");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "ID"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "IDType"));
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

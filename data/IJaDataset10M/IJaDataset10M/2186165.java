package CommonAggregateComponents_2.xsd.schema.ubl.specification.names.oasis;

public class PricingReferenceType implements java.io.Serializable {

    private CommonAggregateComponents_2.xsd.schema.ubl.specification.names.oasis.ItemLocationQuantityType originalItemLocationQuantity;

    private CommonAggregateComponents_2.xsd.schema.ubl.specification.names.oasis.PriceType[] alternativeConditionPrice;

    private long hjid;

    public PricingReferenceType() {
    }

    public PricingReferenceType(CommonAggregateComponents_2.xsd.schema.ubl.specification.names.oasis.ItemLocationQuantityType originalItemLocationQuantity, CommonAggregateComponents_2.xsd.schema.ubl.specification.names.oasis.PriceType[] alternativeConditionPrice, long hjid) {
        this.originalItemLocationQuantity = originalItemLocationQuantity;
        this.alternativeConditionPrice = alternativeConditionPrice;
        this.hjid = hjid;
    }

    /**
     * Gets the originalItemLocationQuantity value for this PricingReferenceType.
     * 
     * @return originalItemLocationQuantity
     */
    public CommonAggregateComponents_2.xsd.schema.ubl.specification.names.oasis.ItemLocationQuantityType getOriginalItemLocationQuantity() {
        return originalItemLocationQuantity;
    }

    /**
     * Sets the originalItemLocationQuantity value for this PricingReferenceType.
     * 
     * @param originalItemLocationQuantity
     */
    public void setOriginalItemLocationQuantity(CommonAggregateComponents_2.xsd.schema.ubl.specification.names.oasis.ItemLocationQuantityType originalItemLocationQuantity) {
        this.originalItemLocationQuantity = originalItemLocationQuantity;
    }

    /**
     * Gets the alternativeConditionPrice value for this PricingReferenceType.
     * 
     * @return alternativeConditionPrice
     */
    public CommonAggregateComponents_2.xsd.schema.ubl.specification.names.oasis.PriceType[] getAlternativeConditionPrice() {
        return alternativeConditionPrice;
    }

    /**
     * Sets the alternativeConditionPrice value for this PricingReferenceType.
     * 
     * @param alternativeConditionPrice
     */
    public void setAlternativeConditionPrice(CommonAggregateComponents_2.xsd.schema.ubl.specification.names.oasis.PriceType[] alternativeConditionPrice) {
        this.alternativeConditionPrice = alternativeConditionPrice;
    }

    public CommonAggregateComponents_2.xsd.schema.ubl.specification.names.oasis.PriceType getAlternativeConditionPrice(int i) {
        return this.alternativeConditionPrice[i];
    }

    public void setAlternativeConditionPrice(int i, CommonAggregateComponents_2.xsd.schema.ubl.specification.names.oasis.PriceType _value) {
        this.alternativeConditionPrice[i] = _value;
    }

    /**
     * Gets the hjid value for this PricingReferenceType.
     * 
     * @return hjid
     */
    public long getHjid() {
        return hjid;
    }

    /**
     * Sets the hjid value for this PricingReferenceType.
     * 
     * @param hjid
     */
    public void setHjid(long hjid) {
        this.hjid = hjid;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PricingReferenceType)) return false;
        PricingReferenceType other = (PricingReferenceType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.originalItemLocationQuantity == null && other.getOriginalItemLocationQuantity() == null) || (this.originalItemLocationQuantity != null && this.originalItemLocationQuantity.equals(other.getOriginalItemLocationQuantity()))) && ((this.alternativeConditionPrice == null && other.getAlternativeConditionPrice() == null) || (this.alternativeConditionPrice != null && java.util.Arrays.equals(this.alternativeConditionPrice, other.getAlternativeConditionPrice()))) && this.hjid == other.getHjid();
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
        if (getOriginalItemLocationQuantity() != null) {
            _hashCode += getOriginalItemLocationQuantity().hashCode();
        }
        if (getAlternativeConditionPrice() != null) {
            for (int i = 0; i < java.lang.reflect.Array.getLength(getAlternativeConditionPrice()); i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAlternativeConditionPrice(), i);
                if (obj != null && !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += new Long(getHjid()).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(PricingReferenceType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2", "PricingReferenceType"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("hjid");
        attrField.setXmlName(new javax.xml.namespace.QName("", "Hjid"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("originalItemLocationQuantity");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2", "OriginalItemLocationQuantity"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2", "ItemLocationQuantityType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("alternativeConditionPrice");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2", "AlternativeConditionPrice"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2", "PriceType"));
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

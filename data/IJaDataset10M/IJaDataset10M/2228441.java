package CommonAggregateComponents_2.xsd.schema.ubl.specification.names.oasis;

public class DeliveryTermsType implements java.io.Serializable {

    private CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.IDType ID;

    private CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.SpecialTermsType specialTerms;

    private CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.LossRiskResponsibilityCodeType lossRiskResponsibilityCode;

    private CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.LossRiskType lossRisk;

    private CommonAggregateComponents_2.xsd.schema.ubl.specification.names.oasis.LocationType deliveryLocation;

    private CommonAggregateComponents_2.xsd.schema.ubl.specification.names.oasis.AllowanceChargeType allowanceCharge;

    private long hjid;

    public DeliveryTermsType() {
    }

    public DeliveryTermsType(CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.IDType ID, CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.SpecialTermsType specialTerms, CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.LossRiskResponsibilityCodeType lossRiskResponsibilityCode, CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.LossRiskType lossRisk, CommonAggregateComponents_2.xsd.schema.ubl.specification.names.oasis.LocationType deliveryLocation, CommonAggregateComponents_2.xsd.schema.ubl.specification.names.oasis.AllowanceChargeType allowanceCharge, long hjid) {
        this.ID = ID;
        this.specialTerms = specialTerms;
        this.lossRiskResponsibilityCode = lossRiskResponsibilityCode;
        this.lossRisk = lossRisk;
        this.deliveryLocation = deliveryLocation;
        this.allowanceCharge = allowanceCharge;
        this.hjid = hjid;
    }

    /**
     * Gets the ID value for this DeliveryTermsType.
     * 
     * @return ID
     */
    public CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.IDType getID() {
        return ID;
    }

    /**
     * Sets the ID value for this DeliveryTermsType.
     * 
     * @param ID
     */
    public void setID(CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.IDType ID) {
        this.ID = ID;
    }

    /**
     * Gets the specialTerms value for this DeliveryTermsType.
     * 
     * @return specialTerms
     */
    public CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.SpecialTermsType getSpecialTerms() {
        return specialTerms;
    }

    /**
     * Sets the specialTerms value for this DeliveryTermsType.
     * 
     * @param specialTerms
     */
    public void setSpecialTerms(CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.SpecialTermsType specialTerms) {
        this.specialTerms = specialTerms;
    }

    /**
     * Gets the lossRiskResponsibilityCode value for this DeliveryTermsType.
     * 
     * @return lossRiskResponsibilityCode
     */
    public CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.LossRiskResponsibilityCodeType getLossRiskResponsibilityCode() {
        return lossRiskResponsibilityCode;
    }

    /**
     * Sets the lossRiskResponsibilityCode value for this DeliveryTermsType.
     * 
     * @param lossRiskResponsibilityCode
     */
    public void setLossRiskResponsibilityCode(CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.LossRiskResponsibilityCodeType lossRiskResponsibilityCode) {
        this.lossRiskResponsibilityCode = lossRiskResponsibilityCode;
    }

    /**
     * Gets the lossRisk value for this DeliveryTermsType.
     * 
     * @return lossRisk
     */
    public CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.LossRiskType getLossRisk() {
        return lossRisk;
    }

    /**
     * Sets the lossRisk value for this DeliveryTermsType.
     * 
     * @param lossRisk
     */
    public void setLossRisk(CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.LossRiskType lossRisk) {
        this.lossRisk = lossRisk;
    }

    /**
     * Gets the deliveryLocation value for this DeliveryTermsType.
     * 
     * @return deliveryLocation
     */
    public CommonAggregateComponents_2.xsd.schema.ubl.specification.names.oasis.LocationType getDeliveryLocation() {
        return deliveryLocation;
    }

    /**
     * Sets the deliveryLocation value for this DeliveryTermsType.
     * 
     * @param deliveryLocation
     */
    public void setDeliveryLocation(CommonAggregateComponents_2.xsd.schema.ubl.specification.names.oasis.LocationType deliveryLocation) {
        this.deliveryLocation = deliveryLocation;
    }

    /**
     * Gets the allowanceCharge value for this DeliveryTermsType.
     * 
     * @return allowanceCharge
     */
    public CommonAggregateComponents_2.xsd.schema.ubl.specification.names.oasis.AllowanceChargeType getAllowanceCharge() {
        return allowanceCharge;
    }

    /**
     * Sets the allowanceCharge value for this DeliveryTermsType.
     * 
     * @param allowanceCharge
     */
    public void setAllowanceCharge(CommonAggregateComponents_2.xsd.schema.ubl.specification.names.oasis.AllowanceChargeType allowanceCharge) {
        this.allowanceCharge = allowanceCharge;
    }

    /**
     * Gets the hjid value for this DeliveryTermsType.
     * 
     * @return hjid
     */
    public long getHjid() {
        return hjid;
    }

    /**
     * Sets the hjid value for this DeliveryTermsType.
     * 
     * @param hjid
     */
    public void setHjid(long hjid) {
        this.hjid = hjid;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DeliveryTermsType)) return false;
        DeliveryTermsType other = (DeliveryTermsType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.ID == null && other.getID() == null) || (this.ID != null && this.ID.equals(other.getID()))) && ((this.specialTerms == null && other.getSpecialTerms() == null) || (this.specialTerms != null && this.specialTerms.equals(other.getSpecialTerms()))) && ((this.lossRiskResponsibilityCode == null && other.getLossRiskResponsibilityCode() == null) || (this.lossRiskResponsibilityCode != null && this.lossRiskResponsibilityCode.equals(other.getLossRiskResponsibilityCode()))) && ((this.lossRisk == null && other.getLossRisk() == null) || (this.lossRisk != null && this.lossRisk.equals(other.getLossRisk()))) && ((this.deliveryLocation == null && other.getDeliveryLocation() == null) || (this.deliveryLocation != null && this.deliveryLocation.equals(other.getDeliveryLocation()))) && ((this.allowanceCharge == null && other.getAllowanceCharge() == null) || (this.allowanceCharge != null && this.allowanceCharge.equals(other.getAllowanceCharge()))) && this.hjid == other.getHjid();
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
        if (getSpecialTerms() != null) {
            _hashCode += getSpecialTerms().hashCode();
        }
        if (getLossRiskResponsibilityCode() != null) {
            _hashCode += getLossRiskResponsibilityCode().hashCode();
        }
        if (getLossRisk() != null) {
            _hashCode += getLossRisk().hashCode();
        }
        if (getDeliveryLocation() != null) {
            _hashCode += getDeliveryLocation().hashCode();
        }
        if (getAllowanceCharge() != null) {
            _hashCode += getAllowanceCharge().hashCode();
        }
        _hashCode += new Long(getHjid()).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(DeliveryTermsType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2", "DeliveryTermsType"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("hjid");
        attrField.setXmlName(new javax.xml.namespace.QName("", "Hjid"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ID");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "ID"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "IDType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("specialTerms");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "SpecialTerms"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "SpecialTermsType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lossRiskResponsibilityCode");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "LossRiskResponsibilityCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "LossRiskResponsibilityCodeType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lossRisk");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "LossRisk"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "LossRiskType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("deliveryLocation");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2", "DeliveryLocation"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2", "LocationType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("allowanceCharge");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2", "AllowanceCharge"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2", "AllowanceChargeType"));
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

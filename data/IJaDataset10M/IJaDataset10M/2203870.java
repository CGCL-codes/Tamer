package com.google.api.adwords.v201101.cm;

/**
 * Represents a criterion in an ad group, used with AdGroupCriterionService.
 */
public class AdGroupCriterion implements java.io.Serializable {

    private java.lang.Long adGroupId;

    private com.google.api.adwords.v201101.cm.CriterionUse criterionUse;

    private com.google.api.adwords.v201101.cm.Criterion criterion;

    private java.lang.String adGroupCriterionType;

    public AdGroupCriterion() {
    }

    public AdGroupCriterion(java.lang.Long adGroupId, com.google.api.adwords.v201101.cm.CriterionUse criterionUse, com.google.api.adwords.v201101.cm.Criterion criterion, java.lang.String adGroupCriterionType) {
        this.adGroupId = adGroupId;
        this.criterionUse = criterionUse;
        this.criterion = criterion;
        this.adGroupCriterionType = adGroupCriterionType;
    }

    /**
     * Gets the adGroupId value for this AdGroupCriterion.
     * 
     * @return adGroupId   * The ad group this criterion is in.
     *                 <span class="constraint Selectable">This field can
     * be selected using the value "AdGroupId".</span><span class="constraint
     * Filterable">This field can be filtered on.</span>
     *                 <span class="constraint Required">This field is required
     * and should not be {@code null}.</span>
     */
    public java.lang.Long getAdGroupId() {
        return adGroupId;
    }

    /**
     * Sets the adGroupId value for this AdGroupCriterion.
     * 
     * @param adGroupId   * The ad group this criterion is in.
     *                 <span class="constraint Selectable">This field can
     * be selected using the value "AdGroupId".</span><span class="constraint
     * Filterable">This field can be filtered on.</span>
     *                 <span class="constraint Required">This field is required
     * and should not be {@code null}.</span>
     */
    public void setAdGroupId(java.lang.Long adGroupId) {
        this.adGroupId = adGroupId;
    }

    /**
     * Gets the criterionUse value for this AdGroupCriterion.
     * 
     * @return criterionUse   * <span class="constraint Selectable">This field can be selected
     * using the value "CriterionUse".</span><span class="constraint Filterable">This
     * field can be filtered on.</span>
     *                 <span class="constraint ReadOnly">This field is read
     * only and should not be set.  If this field is sent to the API, it
     * will be ignored.</span>
     */
    public com.google.api.adwords.v201101.cm.CriterionUse getCriterionUse() {
        return criterionUse;
    }

    /**
     * Sets the criterionUse value for this AdGroupCriterion.
     * 
     * @param criterionUse   * <span class="constraint Selectable">This field can be selected
     * using the value "CriterionUse".</span><span class="constraint Filterable">This
     * field can be filtered on.</span>
     *                 <span class="constraint ReadOnly">This field is read
     * only and should not be set.  If this field is sent to the API, it
     * will be ignored.</span>
     */
    public void setCriterionUse(com.google.api.adwords.v201101.cm.CriterionUse criterionUse) {
        this.criterionUse = criterionUse;
    }

    /**
     * Gets the criterion value for this AdGroupCriterion.
     * 
     * @return criterion   * The criterion part of the ad group criterion.
     *                 <span class="constraint Required">This field is required
     * and should not be {@code null}.</span>
     */
    public com.google.api.adwords.v201101.cm.Criterion getCriterion() {
        return criterion;
    }

    /**
     * Sets the criterion value for this AdGroupCriterion.
     * 
     * @param criterion   * The criterion part of the ad group criterion.
     *                 <span class="constraint Required">This field is required
     * and should not be {@code null}.</span>
     */
    public void setCriterion(com.google.api.adwords.v201101.cm.Criterion criterion) {
        this.criterion = criterion;
    }

    /**
     * Gets the adGroupCriterionType value for this AdGroupCriterion.
     * 
     * @return adGroupCriterionType   * This field indicates the subtype of AdGroupCriterion of this
     * instance.  It is ignored
     *                 on input, and instead xsi:type should be specified.
     */
    public java.lang.String getAdGroupCriterionType() {
        return adGroupCriterionType;
    }

    /**
     * Sets the adGroupCriterionType value for this AdGroupCriterion.
     * 
     * @param adGroupCriterionType   * This field indicates the subtype of AdGroupCriterion of this
     * instance.  It is ignored
     *                 on input, and instead xsi:type should be specified.
     */
    public void setAdGroupCriterionType(java.lang.String adGroupCriterionType) {
        this.adGroupCriterionType = adGroupCriterionType;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AdGroupCriterion)) return false;
        AdGroupCriterion other = (AdGroupCriterion) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.adGroupId == null && other.getAdGroupId() == null) || (this.adGroupId != null && this.adGroupId.equals(other.getAdGroupId()))) && ((this.criterionUse == null && other.getCriterionUse() == null) || (this.criterionUse != null && this.criterionUse.equals(other.getCriterionUse()))) && ((this.criterion == null && other.getCriterion() == null) || (this.criterion != null && this.criterion.equals(other.getCriterion()))) && ((this.adGroupCriterionType == null && other.getAdGroupCriterionType() == null) || (this.adGroupCriterionType != null && this.adGroupCriterionType.equals(other.getAdGroupCriterionType())));
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
        if (getAdGroupId() != null) {
            _hashCode += getAdGroupId().hashCode();
        }
        if (getCriterionUse() != null) {
            _hashCode += getCriterionUse().hashCode();
        }
        if (getCriterion() != null) {
            _hashCode += getCriterion().hashCode();
        }
        if (getAdGroupCriterionType() != null) {
            _hashCode += getAdGroupCriterionType().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(AdGroupCriterion.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201101", "AdGroupCriterion"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("adGroupId");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201101", "adGroupId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("criterionUse");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201101", "criterionUse"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201101", "CriterionUse"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("criterion");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201101", "criterion"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201101", "Criterion"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("adGroupCriterionType");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201101", "AdGroupCriterion.Type"));
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

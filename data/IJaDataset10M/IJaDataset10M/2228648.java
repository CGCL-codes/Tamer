package com.google.api.adwords.v201003.cm;

/**
 * Represents an ad level ad extension override. An ad extension override
 * specifies the ad extension that must be used if the ad is served with
 * any ad extension data.
 */
public class AdExtensionOverride implements java.io.Serializable {

    private java.lang.Long adId;

    private com.google.api.adwords.v201003.cm.AdExtension adExtension;

    private com.google.api.adwords.v201003.cm.OverrideInfo overrideInfo;

    private com.google.api.adwords.v201003.cm.AdExtensionOverrideStatus status;

    private com.google.api.adwords.v201003.cm.AdExtensionOverrideApprovalStatus approvalStatus;

    private com.google.api.adwords.v201003.cm.AdExtensionOverrideStats stats;

    public AdExtensionOverride() {
    }

    public AdExtensionOverride(java.lang.Long adId, com.google.api.adwords.v201003.cm.AdExtension adExtension, com.google.api.adwords.v201003.cm.OverrideInfo overrideInfo, com.google.api.adwords.v201003.cm.AdExtensionOverrideStatus status, com.google.api.adwords.v201003.cm.AdExtensionOverrideApprovalStatus approvalStatus, com.google.api.adwords.v201003.cm.AdExtensionOverrideStats stats) {
        this.adId = adId;
        this.adExtension = adExtension;
        this.overrideInfo = overrideInfo;
        this.status = status;
        this.approvalStatus = approvalStatus;
        this.stats = stats;
    }

    /**
     * Gets the adId value for this AdExtensionOverride.
     * 
     * @return adId   * ID of the ad being overridden by adExtension
     *                 <span class="constraint Required">This field is required
     * and should not be {@code null}.</span>
     */
    public java.lang.Long getAdId() {
        return adId;
    }

    /**
     * Sets the adId value for this AdExtensionOverride.
     * 
     * @param adId   * ID of the ad being overridden by adExtension
     *                 <span class="constraint Required">This field is required
     * and should not be {@code null}.</span>
     */
    public void setAdId(java.lang.Long adId) {
        this.adId = adId;
    }

    /**
     * Gets the adExtension value for this AdExtensionOverride.
     * 
     * @return adExtension   * The override ad extension
     *                 <span class="constraint Required">This field is required
     * and should not be {@code null}.</span>
     */
    public com.google.api.adwords.v201003.cm.AdExtension getAdExtension() {
        return adExtension;
    }

    /**
     * Sets the adExtension value for this AdExtensionOverride.
     * 
     * @param adExtension   * The override ad extension
     *                 <span class="constraint Required">This field is required
     * and should not be {@code null}.</span>
     */
    public void setAdExtension(com.google.api.adwords.v201003.cm.AdExtension adExtension) {
        this.adExtension = adExtension;
    }

    /**
     * Gets the overrideInfo value for this AdExtensionOverride.
     * 
     * @return overrideInfo   * Additional extension override info to augment the ad extension
     * override.
     *                 If none is specified, default settings will be used.
     */
    public com.google.api.adwords.v201003.cm.OverrideInfo getOverrideInfo() {
        return overrideInfo;
    }

    /**
     * Sets the overrideInfo value for this AdExtensionOverride.
     * 
     * @param overrideInfo   * Additional extension override info to augment the ad extension
     * override.
     *                 If none is specified, default settings will be used.
     */
    public void setOverrideInfo(com.google.api.adwords.v201003.cm.OverrideInfo overrideInfo) {
        this.overrideInfo = overrideInfo;
    }

    /**
     * Gets the status value for this AdExtensionOverride.
     * 
     * @return status   * Status of ad extension override at the ad level. The status
     * will start
     *                 ACTIVE on add and will become DELETED on remove (ie.
     * cannot modify status)
     *                 <span class="constraint ReadOnly">This field is read
     * only and should not be set.  If this field is sent to the API, it
     * will be ignored.</span>
     */
    public com.google.api.adwords.v201003.cm.AdExtensionOverrideStatus getStatus() {
        return status;
    }

    /**
     * Sets the status value for this AdExtensionOverride.
     * 
     * @param status   * Status of ad extension override at the ad level. The status
     * will start
     *                 ACTIVE on add and will become DELETED on remove (ie.
     * cannot modify status)
     *                 <span class="constraint ReadOnly">This field is read
     * only and should not be set.  If this field is sent to the API, it
     * will be ignored.</span>
     */
    public void setStatus(com.google.api.adwords.v201003.cm.AdExtensionOverrideStatus status) {
        this.status = status;
    }

    /**
     * Gets the approvalStatus value for this AdExtensionOverride.
     * 
     * @return approvalStatus   * Approval status
     *                 <span class="constraint ReadOnly">This field is read
     * only and should not be set.  If this field is sent to the API, it
     * will be ignored.</span>
     */
    public com.google.api.adwords.v201003.cm.AdExtensionOverrideApprovalStatus getApprovalStatus() {
        return approvalStatus;
    }

    /**
     * Sets the approvalStatus value for this AdExtensionOverride.
     * 
     * @param approvalStatus   * Approval status
     *                 <span class="constraint ReadOnly">This field is read
     * only and should not be set.  If this field is sent to the API, it
     * will be ignored.</span>
     */
    public void setApprovalStatus(com.google.api.adwords.v201003.cm.AdExtensionOverrideApprovalStatus approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    /**
     * Gets the stats value for this AdExtensionOverride.
     * 
     * @return stats   * Stats for the ad extension override
     *                 <span class="constraint ReadOnly">This field is read
     * only and should not be set.  If this field is sent to the API, it
     * will be ignored.</span>
     */
    public com.google.api.adwords.v201003.cm.AdExtensionOverrideStats getStats() {
        return stats;
    }

    /**
     * Sets the stats value for this AdExtensionOverride.
     * 
     * @param stats   * Stats for the ad extension override
     *                 <span class="constraint ReadOnly">This field is read
     * only and should not be set.  If this field is sent to the API, it
     * will be ignored.</span>
     */
    public void setStats(com.google.api.adwords.v201003.cm.AdExtensionOverrideStats stats) {
        this.stats = stats;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AdExtensionOverride)) return false;
        AdExtensionOverride other = (AdExtensionOverride) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.adId == null && other.getAdId() == null) || (this.adId != null && this.adId.equals(other.getAdId()))) && ((this.adExtension == null && other.getAdExtension() == null) || (this.adExtension != null && this.adExtension.equals(other.getAdExtension()))) && ((this.overrideInfo == null && other.getOverrideInfo() == null) || (this.overrideInfo != null && this.overrideInfo.equals(other.getOverrideInfo()))) && ((this.status == null && other.getStatus() == null) || (this.status != null && this.status.equals(other.getStatus()))) && ((this.approvalStatus == null && other.getApprovalStatus() == null) || (this.approvalStatus != null && this.approvalStatus.equals(other.getApprovalStatus()))) && ((this.stats == null && other.getStats() == null) || (this.stats != null && this.stats.equals(other.getStats())));
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
        if (getAdId() != null) {
            _hashCode += getAdId().hashCode();
        }
        if (getAdExtension() != null) {
            _hashCode += getAdExtension().hashCode();
        }
        if (getOverrideInfo() != null) {
            _hashCode += getOverrideInfo().hashCode();
        }
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        if (getApprovalStatus() != null) {
            _hashCode += getApprovalStatus().hashCode();
        }
        if (getStats() != null) {
            _hashCode += getStats().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(AdExtensionOverride.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "AdExtensionOverride"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("adId");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "adId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("adExtension");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "adExtension"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "AdExtension"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("overrideInfo");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "overrideInfo"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "OverrideInfo"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "status"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "AdExtensionOverride.Status"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("approvalStatus");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "approvalStatus"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "AdExtensionOverride.ApprovalStatus"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("stats");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "stats"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "AdExtensionOverrideStats"));
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

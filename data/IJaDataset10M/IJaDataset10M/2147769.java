package com.google.api.adwords.v201109.cm;

/**
 * Represents a campaign level ad extension. A campaign ad extension
 * specifies
 *             a campaign and an ad extension which can extend any ad
 * under that campaign.
 */
public class CampaignAdExtension implements java.io.Serializable {

    private java.lang.Long campaignId;

    private com.google.api.adwords.v201109.cm.AdExtension adExtension;

    private com.google.api.adwords.v201109.cm.CampaignAdExtensionStatus status;

    private com.google.api.adwords.v201109.cm.CampaignAdExtensionApprovalStatus approvalStatus;

    private com.google.api.adwords.v201109.cm.CampaignAdExtensionStats stats;

    public CampaignAdExtension() {
    }

    public CampaignAdExtension(java.lang.Long campaignId, com.google.api.adwords.v201109.cm.AdExtension adExtension, com.google.api.adwords.v201109.cm.CampaignAdExtensionStatus status, com.google.api.adwords.v201109.cm.CampaignAdExtensionApprovalStatus approvalStatus, com.google.api.adwords.v201109.cm.CampaignAdExtensionStats stats) {
        this.campaignId = campaignId;
        this.adExtension = adExtension;
        this.status = status;
        this.approvalStatus = approvalStatus;
        this.stats = stats;
    }

    /**
     * Gets the campaignId value for this CampaignAdExtension.
     * 
     * @return campaignId   * Campaign ID of the campaign to which the ad extension is connected.
     * <span class="constraint Selectable">This field can be selected using
     * the value "CampaignId".</span><span class="constraint Filterable">This
     * field can be filtered on.</span>
     *                 <span class="constraint Required">This field is required
     * and should not be {@code null} when it is contained within {@link
     * Operator}s : ADD, REMOVE.</span>
     */
    public java.lang.Long getCampaignId() {
        return campaignId;
    }

    /**
     * Sets the campaignId value for this CampaignAdExtension.
     * 
     * @param campaignId   * Campaign ID of the campaign to which the ad extension is connected.
     * <span class="constraint Selectable">This field can be selected using
     * the value "CampaignId".</span><span class="constraint Filterable">This
     * field can be filtered on.</span>
     *                 <span class="constraint Required">This field is required
     * and should not be {@code null} when it is contained within {@link
     * Operator}s : ADD, REMOVE.</span>
     */
    public void setCampaignId(java.lang.Long campaignId) {
        this.campaignId = campaignId;
    }

    /**
     * Gets the adExtension value for this CampaignAdExtension.
     * 
     * @return adExtension   * Ad extension.
     *                 <span class="constraint Required">This field is required
     * and should not be {@code null}.</span>
     */
    public com.google.api.adwords.v201109.cm.AdExtension getAdExtension() {
        return adExtension;
    }

    /**
     * Sets the adExtension value for this CampaignAdExtension.
     * 
     * @param adExtension   * Ad extension.
     *                 <span class="constraint Required">This field is required
     * and should not be {@code null}.</span>
     */
    public void setAdExtension(com.google.api.adwords.v201109.cm.AdExtension adExtension) {
        this.adExtension = adExtension;
    }

    /**
     * Gets the status value for this CampaignAdExtension.
     * 
     * @return status   * Status of ad extension in the campaign - defaults to {@code
     * ACTIVE} on add
     *                 if unspecified.
     *                 <span class="constraint Selectable">This field can
     * be selected using the value "Status".</span><span class="constraint
     * Filterable">This field can be filtered on.</span>
     */
    public com.google.api.adwords.v201109.cm.CampaignAdExtensionStatus getStatus() {
        return status;
    }

    /**
     * Sets the status value for this CampaignAdExtension.
     * 
     * @param status   * Status of ad extension in the campaign - defaults to {@code
     * ACTIVE} on add
     *                 if unspecified.
     *                 <span class="constraint Selectable">This field can
     * be selected using the value "Status".</span><span class="constraint
     * Filterable">This field can be filtered on.</span>
     */
    public void setStatus(com.google.api.adwords.v201109.cm.CampaignAdExtensionStatus status) {
        this.status = status;
    }

    /**
     * Gets the approvalStatus value for this CampaignAdExtension.
     * 
     * @return approvalStatus   * Approval status.
     *                 <span class="constraint Selectable">This field can
     * be selected using the value "ApprovalStatus".</span><span class="constraint
     * Filterable">This field can be filtered on.</span>
     */
    public com.google.api.adwords.v201109.cm.CampaignAdExtensionApprovalStatus getApprovalStatus() {
        return approvalStatus;
    }

    /**
     * Sets the approvalStatus value for this CampaignAdExtension.
     * 
     * @param approvalStatus   * Approval status.
     *                 <span class="constraint Selectable">This field can
     * be selected using the value "ApprovalStatus".</span><span class="constraint
     * Filterable">This field can be filtered on.</span>
     */
    public void setApprovalStatus(com.google.api.adwords.v201109.cm.CampaignAdExtensionApprovalStatus approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    /**
     * Gets the stats value for this CampaignAdExtension.
     * 
     * @return stats   * Stats for the campaign ad extension.
     *                 <span class="constraint ReadOnly">This field is read
     * only and should not be set.  If this field is sent to the API, it
     * will be ignored.</span>
     */
    public com.google.api.adwords.v201109.cm.CampaignAdExtensionStats getStats() {
        return stats;
    }

    /**
     * Sets the stats value for this CampaignAdExtension.
     * 
     * @param stats   * Stats for the campaign ad extension.
     *                 <span class="constraint ReadOnly">This field is read
     * only and should not be set.  If this field is sent to the API, it
     * will be ignored.</span>
     */
    public void setStats(com.google.api.adwords.v201109.cm.CampaignAdExtensionStats stats) {
        this.stats = stats;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CampaignAdExtension)) return false;
        CampaignAdExtension other = (CampaignAdExtension) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.campaignId == null && other.getCampaignId() == null) || (this.campaignId != null && this.campaignId.equals(other.getCampaignId()))) && ((this.adExtension == null && other.getAdExtension() == null) || (this.adExtension != null && this.adExtension.equals(other.getAdExtension()))) && ((this.status == null && other.getStatus() == null) || (this.status != null && this.status.equals(other.getStatus()))) && ((this.approvalStatus == null && other.getApprovalStatus() == null) || (this.approvalStatus != null && this.approvalStatus.equals(other.getApprovalStatus()))) && ((this.stats == null && other.getStats() == null) || (this.stats != null && this.stats.equals(other.getStats())));
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
        if (getCampaignId() != null) {
            _hashCode += getCampaignId().hashCode();
        }
        if (getAdExtension() != null) {
            _hashCode += getAdExtension().hashCode();
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

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(CampaignAdExtension.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "CampaignAdExtension"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("campaignId");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "campaignId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("adExtension");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "adExtension"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "AdExtension"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "status"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "CampaignAdExtension.Status"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("approvalStatus");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "approvalStatus"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "CampaignAdExtension.ApprovalStatus"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("stats");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "stats"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "CampaignAdExtensionStats"));
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

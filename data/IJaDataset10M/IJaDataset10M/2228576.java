package com.google.api.adwords.v201109.cm;

/**
 * Contains a subset of {@link CriterionBidLandscapePage} objects
 * resulting from the
 *             filtering and paging of
 *             {@link com.google.ads.api.services.campaignmgmt.data.DataService#getCriterionBidLandscape}
 * call.
 */
public class CriterionBidLandscapePage extends com.google.api.adwords.v201109.cm.NoStatsPage implements java.io.Serializable {

    private com.google.api.adwords.v201109.cm.CriterionBidLandscape[] entries;

    public CriterionBidLandscapePage() {
    }

    public CriterionBidLandscapePage(java.lang.Integer totalNumEntries, java.lang.String pageType, com.google.api.adwords.v201109.cm.CriterionBidLandscape[] entries) {
        super(totalNumEntries, pageType);
        this.entries = entries;
    }

    /**
     * Gets the entries value for this CriterionBidLandscapePage.
     * 
     * @return entries   * The result entries in this page.
     */
    public com.google.api.adwords.v201109.cm.CriterionBidLandscape[] getEntries() {
        return entries;
    }

    /**
     * Sets the entries value for this CriterionBidLandscapePage.
     * 
     * @param entries   * The result entries in this page.
     */
    public void setEntries(com.google.api.adwords.v201109.cm.CriterionBidLandscape[] entries) {
        this.entries = entries;
    }

    public com.google.api.adwords.v201109.cm.CriterionBidLandscape getEntries(int i) {
        return this.entries[i];
    }

    public void setEntries(int i, com.google.api.adwords.v201109.cm.CriterionBidLandscape _value) {
        this.entries[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CriterionBidLandscapePage)) return false;
        CriterionBidLandscapePage other = (CriterionBidLandscapePage) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && ((this.entries == null && other.getEntries() == null) || (this.entries != null && java.util.Arrays.equals(this.entries, other.getEntries())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;

    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (getEntries() != null) {
            for (int i = 0; i < java.lang.reflect.Array.getLength(getEntries()); i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getEntries(), i);
                if (obj != null && !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(CriterionBidLandscapePage.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "CriterionBidLandscapePage"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("entries");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "entries"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "CriterionBidLandscape"));
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

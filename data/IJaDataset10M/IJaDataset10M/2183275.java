package com.google.api.adwords.v200909.cm;

/**
 * For selecting jobs whose information will be returned from a get
 * method call
 *             of a job service.
 *             
 *             <p>The selector is immutable so use the inner Builder
 * class to create one.
 */
public abstract class JobSelector implements java.io.Serializable {

    private java.lang.String[] customerJobKeys;

    private java.lang.Boolean includeHistory;

    private java.lang.Boolean includeStats;

    private java.lang.String jobSelectorType;

    public JobSelector() {
    }

    public JobSelector(java.lang.String[] customerJobKeys, java.lang.Boolean includeHistory, java.lang.Boolean includeStats, java.lang.String jobSelectorType) {
        this.customerJobKeys = customerJobKeys;
        this.includeHistory = includeHistory;
        this.includeStats = includeStats;
        this.jobSelectorType = jobSelectorType;
    }

    /**
     * Gets the customerJobKeys value for this JobSelector.
     * 
     * @return customerJobKeys   * The list of jobs, specified by customer keys, to be selected.
     * An empty
     *                 list indicates all jobs for the effective customer
     * should be selected.
     *                 <span class="constraint ContentsDistinct">This field
     * must contain distinct elements.</span>
     *                 <span class="constraint ContentsNotNull">This field
     * must not contain {@code null} elements.</span>
     */
    public java.lang.String[] getCustomerJobKeys() {
        return customerJobKeys;
    }

    /**
     * Sets the customerJobKeys value for this JobSelector.
     * 
     * @param customerJobKeys   * The list of jobs, specified by customer keys, to be selected.
     * An empty
     *                 list indicates all jobs for the effective customer
     * should be selected.
     *                 <span class="constraint ContentsDistinct">This field
     * must contain distinct elements.</span>
     *                 <span class="constraint ContentsNotNull">This field
     * must not contain {@code null} elements.</span>
     */
    public void setCustomerJobKeys(java.lang.String[] customerJobKeys) {
        this.customerJobKeys = customerJobKeys;
    }

    public java.lang.String getCustomerJobKeys(int i) {
        return this.customerJobKeys[i];
    }

    public void setCustomerJobKeys(int i, java.lang.String _value) {
        this.customerJobKeys[i] = _value;
    }

    /**
     * Gets the includeHistory value for this JobSelector.
     * 
     * @return includeHistory   * Indicates if the event history should be included in the each
     * of the
     *                 returned jobs.
     */
    public java.lang.Boolean getIncludeHistory() {
        return includeHistory;
    }

    /**
     * Sets the includeHistory value for this JobSelector.
     * 
     * @param includeHistory   * Indicates if the event history should be included in the each
     * of the
     *                 returned jobs.
     */
    public void setIncludeHistory(java.lang.Boolean includeHistory) {
        this.includeHistory = includeHistory;
    }

    /**
     * Gets the includeStats value for this JobSelector.
     * 
     * @return includeStats   * Indicates if the processing statistics should be included in
     * the each of
     *                 the returned jobs.
     */
    public java.lang.Boolean getIncludeStats() {
        return includeStats;
    }

    /**
     * Sets the includeStats value for this JobSelector.
     * 
     * @param includeStats   * Indicates if the processing statistics should be included in
     * the each of
     *                 the returned jobs.
     */
    public void setIncludeStats(java.lang.Boolean includeStats) {
        this.includeStats = includeStats;
    }

    /**
     * Gets the jobSelectorType value for this JobSelector.
     * 
     * @return jobSelectorType   * This field indicates the subtype of JobSelector of this instance.
     * It is ignored
     *                 on input, and instead xsi:type should be specified.
     */
    public java.lang.String getJobSelectorType() {
        return jobSelectorType;
    }

    /**
     * Sets the jobSelectorType value for this JobSelector.
     * 
     * @param jobSelectorType   * This field indicates the subtype of JobSelector of this instance.
     * It is ignored
     *                 on input, and instead xsi:type should be specified.
     */
    public void setJobSelectorType(java.lang.String jobSelectorType) {
        this.jobSelectorType = jobSelectorType;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof JobSelector)) return false;
        JobSelector other = (JobSelector) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.customerJobKeys == null && other.getCustomerJobKeys() == null) || (this.customerJobKeys != null && java.util.Arrays.equals(this.customerJobKeys, other.getCustomerJobKeys()))) && ((this.includeHistory == null && other.getIncludeHistory() == null) || (this.includeHistory != null && this.includeHistory.equals(other.getIncludeHistory()))) && ((this.includeStats == null && other.getIncludeStats() == null) || (this.includeStats != null && this.includeStats.equals(other.getIncludeStats()))) && ((this.jobSelectorType == null && other.getJobSelectorType() == null) || (this.jobSelectorType != null && this.jobSelectorType.equals(other.getJobSelectorType())));
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
        if (getCustomerJobKeys() != null) {
            for (int i = 0; i < java.lang.reflect.Array.getLength(getCustomerJobKeys()); i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getCustomerJobKeys(), i);
                if (obj != null && !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getIncludeHistory() != null) {
            _hashCode += getIncludeHistory().hashCode();
        }
        if (getIncludeStats() != null) {
            _hashCode += getIncludeStats().hashCode();
        }
        if (getJobSelectorType() != null) {
            _hashCode += getJobSelectorType().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(JobSelector.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "JobSelector"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("customerJobKeys");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "customerJobKeys"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("includeHistory");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "includeHistory"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("includeStats");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "includeStats"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("jobSelectorType");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "JobSelector.Type"));
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

package v1;

public class TimeSeriesQueryResponse implements java.io.Serializable {

    private v1.DateType date;

    private java.lang.String timeAggregation;

    private v1.SyndromeSetType syndromeSet;

    private v1.SpatialGroupingSetType spatialGroupingSet;

    private java.lang.String[] availableNode;

    private v1.TimeSeriesRecordType[] timeSeriesRecord;

    public TimeSeriesQueryResponse() {
    }

    public TimeSeriesQueryResponse(java.lang.String[] availableNode, v1.DateType date, v1.SpatialGroupingSetType spatialGroupingSet, v1.SyndromeSetType syndromeSet, java.lang.String timeAggregation, v1.TimeSeriesRecordType[] timeSeriesRecord) {
        this.date = date;
        this.timeAggregation = timeAggregation;
        this.syndromeSet = syndromeSet;
        this.spatialGroupingSet = spatialGroupingSet;
        this.availableNode = availableNode;
        this.timeSeriesRecord = timeSeriesRecord;
    }

    /**
     * Gets the date value for this TimeSeriesQueryResponse.
     * 
     * @return date
     */
    public v1.DateType getDate() {
        return date;
    }

    /**
     * Sets the date value for this TimeSeriesQueryResponse.
     * 
     * @param date
     */
    public void setDate(v1.DateType date) {
        this.date = date;
    }

    /**
     * Gets the timeAggregation value for this TimeSeriesQueryResponse.
     * 
     * @return timeAggregation
     */
    public java.lang.String getTimeAggregation() {
        return timeAggregation;
    }

    /**
     * Sets the timeAggregation value for this TimeSeriesQueryResponse.
     * 
     * @param timeAggregation
     */
    public void setTimeAggregation(java.lang.String timeAggregation) {
        this.timeAggregation = timeAggregation;
    }

    /**
     * Gets the syndromeSet value for this TimeSeriesQueryResponse.
     * 
     * @return syndromeSet
     */
    public v1.SyndromeSetType getSyndromeSet() {
        return syndromeSet;
    }

    /**
     * Sets the syndromeSet value for this TimeSeriesQueryResponse.
     * 
     * @param syndromeSet
     */
    public void setSyndromeSet(v1.SyndromeSetType syndromeSet) {
        this.syndromeSet = syndromeSet;
    }

    /**
     * Gets the spatialGroupingSet value for this TimeSeriesQueryResponse.
     * 
     * @return spatialGroupingSet
     */
    public v1.SpatialGroupingSetType getSpatialGroupingSet() {
        return spatialGroupingSet;
    }

    /**
     * Sets the spatialGroupingSet value for this TimeSeriesQueryResponse.
     * 
     * @param spatialGroupingSet
     */
    public void setSpatialGroupingSet(v1.SpatialGroupingSetType spatialGroupingSet) {
        this.spatialGroupingSet = spatialGroupingSet;
    }

    /**
     * Gets the availableNode value for this TimeSeriesQueryResponse.
     * 
     * @return availableNode
     */
    public java.lang.String[] getAvailableNode() {
        return availableNode;
    }

    /**
     * Sets the availableNode value for this TimeSeriesQueryResponse.
     * 
     * @param availableNode
     */
    public void setAvailableNode(java.lang.String[] availableNode) {
        this.availableNode = availableNode;
    }

    public java.lang.String getAvailableNode(int i) {
        return this.availableNode[i];
    }

    public void setAvailableNode(int i, java.lang.String _value) {
        this.availableNode[i] = _value;
    }

    /**
     * Gets the timeSeriesRecord value for this TimeSeriesQueryResponse.
     * 
     * @return timeSeriesRecord
     */
    public v1.TimeSeriesRecordType[] getTimeSeriesRecord() {
        return timeSeriesRecord;
    }

    /**
     * Sets the timeSeriesRecord value for this TimeSeriesQueryResponse.
     * 
     * @param timeSeriesRecord
     */
    public void setTimeSeriesRecord(v1.TimeSeriesRecordType[] timeSeriesRecord) {
        this.timeSeriesRecord = timeSeriesRecord;
    }

    public v1.TimeSeriesRecordType getTimeSeriesRecord(int i) {
        return this.timeSeriesRecord[i];
    }

    public void setTimeSeriesRecord(int i, v1.TimeSeriesRecordType _value) {
        this.timeSeriesRecord[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TimeSeriesQueryResponse)) return false;
        TimeSeriesQueryResponse other = (TimeSeriesQueryResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.date == null && other.getDate() == null) || (this.date != null && this.date.equals(other.getDate()))) && ((this.timeAggregation == null && other.getTimeAggregation() == null) || (this.timeAggregation != null && this.timeAggregation.equals(other.getTimeAggregation()))) && ((this.syndromeSet == null && other.getSyndromeSet() == null) || (this.syndromeSet != null && this.syndromeSet.equals(other.getSyndromeSet()))) && ((this.spatialGroupingSet == null && other.getSpatialGroupingSet() == null) || (this.spatialGroupingSet != null && this.spatialGroupingSet.equals(other.getSpatialGroupingSet()))) && ((this.availableNode == null && other.getAvailableNode() == null) || (this.availableNode != null && java.util.Arrays.equals(this.availableNode, other.getAvailableNode()))) && ((this.timeSeriesRecord == null && other.getTimeSeriesRecord() == null) || (this.timeSeriesRecord != null && java.util.Arrays.equals(this.timeSeriesRecord, other.getTimeSeriesRecord())));
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
        if (getDate() != null) {
            _hashCode += getDate().hashCode();
        }
        if (getTimeAggregation() != null) {
            _hashCode += getTimeAggregation().hashCode();
        }
        if (getSyndromeSet() != null) {
            _hashCode += getSyndromeSet().hashCode();
        }
        if (getSpatialGroupingSet() != null) {
            _hashCode += getSpatialGroupingSet().hashCode();
        }
        if (getAvailableNode() != null) {
            for (int i = 0; i < java.lang.reflect.Array.getLength(getAvailableNode()); i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAvailableNode(), i);
                if (obj != null && !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getTimeSeriesRecord() != null) {
            for (int i = 0; i < java.lang.reflect.Array.getLength(getTimeSeriesRecord()); i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getTimeSeriesRecord(), i);
                if (obj != null && !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(TimeSeriesQueryResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ncphi.phgrid.net/schemas/amds/v1", ">TimeSeriesQueryResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("date");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ncphi.phgrid.net/schemas/amds/v1", "Date"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://ncphi.phgrid.net/schemas/amds/v1", "DateType"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("timeAggregation");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ncphi.phgrid.net/schemas/amds/v1", "TimeAggregation"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("syndromeSet");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ncphi.phgrid.net/schemas/amds/v1", "SyndromeSet"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://ncphi.phgrid.net/schemas/amds/v1", "SyndromeSetType"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("spatialGroupingSet");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ncphi.phgrid.net/schemas/amds/v1", "SpatialGroupingSet"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://ncphi.phgrid.net/schemas/amds/v1", "SpatialGroupingSetType"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("availableNode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ncphi.phgrid.net/schemas/amds/v1", "AvailableNode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("timeSeriesRecord");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ncphi.phgrid.net/schemas/amds/v1", "TimeSeriesRecord"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://ncphi.phgrid.net/schemas/amds/v1", "TimeSeriesRecordType"));
        elemField.setMinOccurs(0);
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

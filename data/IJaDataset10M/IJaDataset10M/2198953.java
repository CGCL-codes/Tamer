package tab.liveodd;

public class EventApproximatesResponse implements java.io.Serializable {

    private tab.liveodd.EventApproximatesResponseEventApproximatesResult eventApproximatesResult;

    public EventApproximatesResponse() {
    }

    public EventApproximatesResponse(tab.liveodd.EventApproximatesResponseEventApproximatesResult eventApproximatesResult) {
        this.eventApproximatesResult = eventApproximatesResult;
    }

    /**
     * Gets the eventApproximatesResult value for this EventApproximatesResponse.
     * 
     * @return eventApproximatesResult
     */
    public tab.liveodd.EventApproximatesResponseEventApproximatesResult getEventApproximatesResult() {
        return eventApproximatesResult;
    }

    /**
     * Sets the eventApproximatesResult value for this EventApproximatesResponse.
     * 
     * @param eventApproximatesResult
     */
    public void setEventApproximatesResult(tab.liveodd.EventApproximatesResponseEventApproximatesResult eventApproximatesResult) {
        this.eventApproximatesResult = eventApproximatesResult;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof EventApproximatesResponse)) return false;
        EventApproximatesResponse other = (EventApproximatesResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.eventApproximatesResult == null && other.getEventApproximatesResult() == null) || (this.eventApproximatesResult != null && this.eventApproximatesResult.equals(other.getEventApproximatesResult())));
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
        if (getEventApproximatesResult() != null) {
            _hashCode += getEventApproximatesResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(EventApproximatesResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://direct.tab.com.au/LiveOdds/", ">EventApproximatesResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("eventApproximatesResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://direct.tab.com.au/LiveOdds/", "EventApproximatesResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://direct.tab.com.au/LiveOdds/", ">>EventApproximatesResponse>EventApproximatesResult"));
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

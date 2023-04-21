package com.alcatel.xmlapi.phone;

public class AlcCallRecordCapabilities implements java.io.Serializable {

    private com.alcatel.xmlapi.phone.AlcStatus status;

    private boolean startRecord;

    private boolean stopRecord;

    private boolean pauseRecord;

    private boolean continueRecord;

    private boolean getRecordState;

    public AlcCallRecordCapabilities() {
    }

    public com.alcatel.xmlapi.phone.AlcStatus getStatus() {
        return status;
    }

    public void setStatus(com.alcatel.xmlapi.phone.AlcStatus status) {
        this.status = status;
    }

    public boolean isStartRecord() {
        return startRecord;
    }

    public void setStartRecord(boolean startRecord) {
        this.startRecord = startRecord;
    }

    public boolean isStopRecord() {
        return stopRecord;
    }

    public void setStopRecord(boolean stopRecord) {
        this.stopRecord = stopRecord;
    }

    public boolean isPauseRecord() {
        return pauseRecord;
    }

    public void setPauseRecord(boolean pauseRecord) {
        this.pauseRecord = pauseRecord;
    }

    public boolean isContinueRecord() {
        return continueRecord;
    }

    public void setContinueRecord(boolean continueRecord) {
        this.continueRecord = continueRecord;
    }

    public boolean isGetRecordState() {
        return getRecordState;
    }

    public void setGetRecordState(boolean getRecordState) {
        this.getRecordState = getRecordState;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AlcCallRecordCapabilities)) return false;
        AlcCallRecordCapabilities other = (AlcCallRecordCapabilities) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.status == null && other.getStatus() == null) || (this.status != null && this.status.equals(other.getStatus()))) && this.startRecord == other.isStartRecord() && this.stopRecord == other.isStopRecord() && this.pauseRecord == other.isPauseRecord() && this.continueRecord == other.isContinueRecord() && this.getRecordState == other.isGetRecordState();
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
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        _hashCode += new Boolean(isStartRecord()).hashCode();
        _hashCode += new Boolean(isStopRecord()).hashCode();
        _hashCode += new Boolean(isPauseRecord()).hashCode();
        _hashCode += new Boolean(isContinueRecord()).hashCode();
        _hashCode += new Boolean(isGetRecordState()).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(AlcCallRecordCapabilities.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://xmlapi.alcatel.com/phone", "AlcCallRecordCapabilities"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("", "status"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://xmlapi.alcatel.com/phone", "AlcStatus"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("startRecord");
        elemField.setXmlName(new javax.xml.namespace.QName("", "startRecord"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("stopRecord");
        elemField.setXmlName(new javax.xml.namespace.QName("", "stopRecord"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pauseRecord");
        elemField.setXmlName(new javax.xml.namespace.QName("", "pauseRecord"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("continueRecord");
        elemField.setXmlName(new javax.xml.namespace.QName("", "continueRecord"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getRecordState");
        elemField.setXmlName(new javax.xml.namespace.QName("", "getRecordState"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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

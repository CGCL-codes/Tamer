package org.sf.jspread.marketdata.inetats.webservice;

public class StockStatsResult implements java.io.Serializable {

    private java.lang.String time;

    private org.sf.jspread.marketdata.inetats.webservice.UserData userData;

    private org.sf.jspread.marketdata.inetats.webservice.StockStatData stockStatData;

    public StockStatsResult() {
    }

    public java.lang.String getTime() {
        return time;
    }

    public void setTime(java.lang.String time) {
        this.time = time;
    }

    public org.sf.jspread.marketdata.inetats.webservice.UserData getUserData() {
        return userData;
    }

    public void setUserData(org.sf.jspread.marketdata.inetats.webservice.UserData userData) {
        this.userData = userData;
    }

    public org.sf.jspread.marketdata.inetats.webservice.StockStatData getStockStatData() {
        return stockStatData;
    }

    public void setStockStatData(org.sf.jspread.marketdata.inetats.webservice.StockStatData stockStatData) {
        this.stockStatData = stockStatData;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof StockStatsResult)) return false;
        StockStatsResult other = (StockStatsResult) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.time == null && other.getTime() == null) || (this.time != null && this.time.equals(other.getTime()))) && ((this.userData == null && other.getUserData() == null) || (this.userData != null && this.userData.equals(other.getUserData()))) && ((this.stockStatData == null && other.getStockStatData() == null) || (this.stockStatData != null && this.stockStatData.equals(other.getStockStatData())));
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
        if (getTime() != null) {
            _hashCode += getTime().hashCode();
        }
        if (getUserData() != null) {
            _hashCode += getUserData().hashCode();
        }
        if (getStockStatData() != null) {
            _hashCode += getStockStatData().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(StockStatsResult.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:Instinet", "StockStatsResult"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("time");
        elemField.setXmlName(new javax.xml.namespace.QName("", "time"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userData");
        elemField.setXmlName(new javax.xml.namespace.QName("", "userData"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:Instinet", "UserData"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("stockStatData");
        elemField.setXmlName(new javax.xml.namespace.QName("", "stockStatData"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:Instinet", "StockStatData"));
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

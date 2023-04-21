package org.sf.jspread.marketdata.inetats.webservice;

public class MatchType implements java.io.Serializable {

    private org.sf.jspread.marketdata.inetats.webservice.SharesType matchedShares;

    private org.sf.jspread.marketdata.inetats.webservice.PriceType matchPrice;

    private org.sf.jspread.marketdata.inetats.webservice.IslandTimeType matchTime;

    private org.sf.jspread.marketdata.inetats.webservice.SideType matchType;

    private org.sf.jspread.marketdata.inetats.webservice.ReferenceType refNumb;

    public MatchType() {
    }

    public org.sf.jspread.marketdata.inetats.webservice.SharesType getMatchedShares() {
        return matchedShares;
    }

    public void setMatchedShares(org.sf.jspread.marketdata.inetats.webservice.SharesType matchedShares) {
        this.matchedShares = matchedShares;
    }

    public org.sf.jspread.marketdata.inetats.webservice.PriceType getMatchPrice() {
        return matchPrice;
    }

    public void setMatchPrice(org.sf.jspread.marketdata.inetats.webservice.PriceType matchPrice) {
        this.matchPrice = matchPrice;
    }

    public org.sf.jspread.marketdata.inetats.webservice.IslandTimeType getMatchTime() {
        return matchTime;
    }

    public void setMatchTime(org.sf.jspread.marketdata.inetats.webservice.IslandTimeType matchTime) {
        this.matchTime = matchTime;
    }

    public org.sf.jspread.marketdata.inetats.webservice.SideType getMatchType() {
        return matchType;
    }

    public void setMatchType(org.sf.jspread.marketdata.inetats.webservice.SideType matchType) {
        this.matchType = matchType;
    }

    public org.sf.jspread.marketdata.inetats.webservice.ReferenceType getRefNumb() {
        return refNumb;
    }

    public void setRefNumb(org.sf.jspread.marketdata.inetats.webservice.ReferenceType refNumb) {
        this.refNumb = refNumb;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MatchType)) return false;
        MatchType other = (MatchType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.matchedShares == null && other.getMatchedShares() == null) || (this.matchedShares != null && this.matchedShares.equals(other.getMatchedShares()))) && ((this.matchPrice == null && other.getMatchPrice() == null) || (this.matchPrice != null && this.matchPrice.equals(other.getMatchPrice()))) && ((this.matchTime == null && other.getMatchTime() == null) || (this.matchTime != null && this.matchTime.equals(other.getMatchTime()))) && ((this.matchType == null && other.getMatchType() == null) || (this.matchType != null && this.matchType.equals(other.getMatchType()))) && ((this.refNumb == null && other.getRefNumb() == null) || (this.refNumb != null && this.refNumb.equals(other.getRefNumb())));
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
        if (getMatchedShares() != null) {
            _hashCode += getMatchedShares().hashCode();
        }
        if (getMatchPrice() != null) {
            _hashCode += getMatchPrice().hashCode();
        }
        if (getMatchTime() != null) {
            _hashCode += getMatchTime().hashCode();
        }
        if (getMatchType() != null) {
            _hashCode += getMatchType().hashCode();
        }
        if (getRefNumb() != null) {
            _hashCode += getRefNumb().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(MatchType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:Instinet", "matchType"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("refNumb");
        attrField.setXmlName(new javax.xml.namespace.QName("", "refNumb"));
        attrField.setXmlType(new javax.xml.namespace.QName("urn:Instinet", "referenceType"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("matchedShares");
        elemField.setXmlName(new javax.xml.namespace.QName("", "matchedShares"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:Instinet", "sharesType"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("matchPrice");
        elemField.setXmlName(new javax.xml.namespace.QName("", "matchPrice"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:Instinet", "priceType"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("matchTime");
        elemField.setXmlName(new javax.xml.namespace.QName("", "matchTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:Instinet", "islandTimeType"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("matchType");
        elemField.setXmlName(new javax.xml.namespace.QName("", "matchType"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:Instinet", "sideType"));
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

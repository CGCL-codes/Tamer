package org.dbe.demos.amazon.ws;

public class AsinRequest implements java.io.Serializable {

    private java.lang.String asin;

    private java.lang.String tag;

    private java.lang.String type;

    private java.lang.String devtag;

    private java.lang.String offer;

    private java.lang.String offerpage;

    private java.lang.String locale;

    public AsinRequest() {
    }

    public AsinRequest(java.lang.String asin, java.lang.String tag, java.lang.String type, java.lang.String devtag, java.lang.String offer, java.lang.String offerpage, java.lang.String locale) {
        this.asin = asin;
        this.tag = tag;
        this.type = type;
        this.devtag = devtag;
        this.offer = offer;
        this.offerpage = offerpage;
        this.locale = locale;
    }

    /**
     * Gets the asin value for this AsinRequest.
     * 
     * @return asin
     */
    public java.lang.String getAsin() {
        return asin;
    }

    /**
     * Sets the asin value for this AsinRequest.
     * 
     * @param asin
     */
    public void setAsin(java.lang.String asin) {
        this.asin = asin;
    }

    /**
     * Gets the tag value for this AsinRequest.
     * 
     * @return tag
     */
    public java.lang.String getTag() {
        return tag;
    }

    /**
     * Sets the tag value for this AsinRequest.
     * 
     * @param tag
     */
    public void setTag(java.lang.String tag) {
        this.tag = tag;
    }

    /**
     * Gets the type value for this AsinRequest.
     * 
     * @return type
     */
    public java.lang.String getType() {
        return type;
    }

    /**
     * Sets the type value for this AsinRequest.
     * 
     * @param type
     */
    public void setType(java.lang.String type) {
        this.type = type;
    }

    /**
     * Gets the devtag value for this AsinRequest.
     * 
     * @return devtag
     */
    public java.lang.String getDevtag() {
        return devtag;
    }

    /**
     * Sets the devtag value for this AsinRequest.
     * 
     * @param devtag
     */
    public void setDevtag(java.lang.String devtag) {
        this.devtag = devtag;
    }

    /**
     * Gets the offer value for this AsinRequest.
     * 
     * @return offer
     */
    public java.lang.String getOffer() {
        return offer;
    }

    /**
     * Sets the offer value for this AsinRequest.
     * 
     * @param offer
     */
    public void setOffer(java.lang.String offer) {
        this.offer = offer;
    }

    /**
     * Gets the offerpage value for this AsinRequest.
     * 
     * @return offerpage
     */
    public java.lang.String getOfferpage() {
        return offerpage;
    }

    /**
     * Sets the offerpage value for this AsinRequest.
     * 
     * @param offerpage
     */
    public void setOfferpage(java.lang.String offerpage) {
        this.offerpage = offerpage;
    }

    /**
     * Gets the locale value for this AsinRequest.
     * 
     * @return locale
     */
    public java.lang.String getLocale() {
        return locale;
    }

    /**
     * Sets the locale value for this AsinRequest.
     * 
     * @param locale
     */
    public void setLocale(java.lang.String locale) {
        this.locale = locale;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AsinRequest)) return false;
        AsinRequest other = (AsinRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.asin == null && other.getAsin() == null) || (this.asin != null && this.asin.equals(other.getAsin()))) && ((this.tag == null && other.getTag() == null) || (this.tag != null && this.tag.equals(other.getTag()))) && ((this.type == null && other.getType() == null) || (this.type != null && this.type.equals(other.getType()))) && ((this.devtag == null && other.getDevtag() == null) || (this.devtag != null && this.devtag.equals(other.getDevtag()))) && ((this.offer == null && other.getOffer() == null) || (this.offer != null && this.offer.equals(other.getOffer()))) && ((this.offerpage == null && other.getOfferpage() == null) || (this.offerpage != null && this.offerpage.equals(other.getOfferpage()))) && ((this.locale == null && other.getLocale() == null) || (this.locale != null && this.locale.equals(other.getLocale())));
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
        if (getAsin() != null) {
            _hashCode += getAsin().hashCode();
        }
        if (getTag() != null) {
            _hashCode += getTag().hashCode();
        }
        if (getType() != null) {
            _hashCode += getType().hashCode();
        }
        if (getDevtag() != null) {
            _hashCode += getDevtag().hashCode();
        }
        if (getOffer() != null) {
            _hashCode += getOffer().hashCode();
        }
        if (getOfferpage() != null) {
            _hashCode += getOfferpage().hashCode();
        }
        if (getLocale() != null) {
            _hashCode += getLocale().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(AsinRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://soap.amazon.com", "AsinRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("asin");
        elemField.setXmlName(new javax.xml.namespace.QName("", "asin"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tag");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tag"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("type");
        elemField.setXmlName(new javax.xml.namespace.QName("", "type"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("devtag");
        elemField.setXmlName(new javax.xml.namespace.QName("", "devtag"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("offer");
        elemField.setXmlName(new javax.xml.namespace.QName("", "offer"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("offerpage");
        elemField.setXmlName(new javax.xml.namespace.QName("", "offerpage"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("locale");
        elemField.setXmlName(new javax.xml.namespace.QName("", "locale"));
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

package com.google.api.adwords.v200909.cm;

/**
 * Represents a mobile image ad.
 *             
 *             <p>For more information, see the
 *             <a href="http://adwords.google.com/support/aw/bin/answer.py?answer=83248"
 * >mobile ads guidelines</a>.</p>
 */
public class MobileImageAd extends com.google.api.adwords.v200909.cm.Ad implements java.io.Serializable {

    private com.google.api.adwords.v200909.cm.MarkupLanguageType[] markupLanguages;

    private java.lang.String[] mobileCarriers;

    private com.google.api.adwords.v200909.cm.Image image;

    public MobileImageAd() {
    }

    public MobileImageAd(java.lang.Long id, java.lang.String url, java.lang.String displayUrl, com.google.api.adwords.v200909.cm.AdApprovalStatus approvalStatus, java.lang.String[] disapprovalReasons, java.lang.String adType, com.google.api.adwords.v200909.cm.MarkupLanguageType[] markupLanguages, java.lang.String[] mobileCarriers, com.google.api.adwords.v200909.cm.Image image) {
        super(id, url, displayUrl, approvalStatus, disapprovalReasons, adType);
        this.markupLanguages = markupLanguages;
        this.mobileCarriers = mobileCarriers;
        this.image = image;
    }

    /**
     * Gets the markupLanguages value for this MobileImageAd.
     * 
     * @return markupLanguages   * The list of markup languages to use for the mobile ad.
     */
    public com.google.api.adwords.v200909.cm.MarkupLanguageType[] getMarkupLanguages() {
        return markupLanguages;
    }

    /**
     * Sets the markupLanguages value for this MobileImageAd.
     * 
     * @param markupLanguages   * The list of markup languages to use for the mobile ad.
     */
    public void setMarkupLanguages(com.google.api.adwords.v200909.cm.MarkupLanguageType[] markupLanguages) {
        this.markupLanguages = markupLanguages;
    }

    public com.google.api.adwords.v200909.cm.MarkupLanguageType getMarkupLanguages(int i) {
        return this.markupLanguages[i];
    }

    public void setMarkupLanguages(int i, com.google.api.adwords.v200909.cm.MarkupLanguageType _value) {
        this.markupLanguages[i] = _value;
    }

    /**
     * Gets the mobileCarriers value for this MobileImageAd.
     * 
     * @return mobileCarriers   * The list of mobile carriers to use for the mobile ad.  Each
     * string
     *                     must be of the form 'CarrierName@CountryCode'.
     * To specify that
     *                     all available carriers are to be used, use the
     * reserved keyword
     *                     'ALLCARRIERS'. See <a
     *                     href="/apis/adwords/docs/appendix/mobilecarriers.html">available
     * mobile carriers</a>.
     *                     <span class="constraint Required">This field is
     * required and should not be {@code null}.</span>
     */
    public java.lang.String[] getMobileCarriers() {
        return mobileCarriers;
    }

    /**
     * Sets the mobileCarriers value for this MobileImageAd.
     * 
     * @param mobileCarriers   * The list of mobile carriers to use for the mobile ad.  Each
     * string
     *                     must be of the form 'CarrierName@CountryCode'.
     * To specify that
     *                     all available carriers are to be used, use the
     * reserved keyword
     *                     'ALLCARRIERS'. See <a
     *                     href="/apis/adwords/docs/appendix/mobilecarriers.html">available
     * mobile carriers</a>.
     *                     <span class="constraint Required">This field is
     * required and should not be {@code null}.</span>
     */
    public void setMobileCarriers(java.lang.String[] mobileCarriers) {
        this.mobileCarriers = mobileCarriers;
    }

    public java.lang.String getMobileCarriers(int i) {
        return this.mobileCarriers[i];
    }

    public void setMobileCarriers(int i, java.lang.String _value) {
        this.mobileCarriers[i] = _value;
    }

    /**
     * Gets the image value for this MobileImageAd.
     * 
     * @return image   * Image to be used in the mobile image ad.
     */
    public com.google.api.adwords.v200909.cm.Image getImage() {
        return image;
    }

    /**
     * Sets the image value for this MobileImageAd.
     * 
     * @param image   * Image to be used in the mobile image ad.
     */
    public void setImage(com.google.api.adwords.v200909.cm.Image image) {
        this.image = image;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MobileImageAd)) return false;
        MobileImageAd other = (MobileImageAd) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && ((this.markupLanguages == null && other.getMarkupLanguages() == null) || (this.markupLanguages != null && java.util.Arrays.equals(this.markupLanguages, other.getMarkupLanguages()))) && ((this.mobileCarriers == null && other.getMobileCarriers() == null) || (this.mobileCarriers != null && java.util.Arrays.equals(this.mobileCarriers, other.getMobileCarriers()))) && ((this.image == null && other.getImage() == null) || (this.image != null && this.image.equals(other.getImage())));
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
        if (getMarkupLanguages() != null) {
            for (int i = 0; i < java.lang.reflect.Array.getLength(getMarkupLanguages()); i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getMarkupLanguages(), i);
                if (obj != null && !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getMobileCarriers() != null) {
            for (int i = 0; i < java.lang.reflect.Array.getLength(getMobileCarriers()); i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getMobileCarriers(), i);
                if (obj != null && !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getImage() != null) {
            _hashCode += getImage().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(MobileImageAd.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "MobileImageAd"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("markupLanguages");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "markupLanguages"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "MarkupLanguageType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mobileCarriers");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "mobileCarriers"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("image");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "image"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "Image"));
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

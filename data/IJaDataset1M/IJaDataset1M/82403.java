package com.google.api.adwords.v201003.cm;

/**
 * Page of media returned by the {@link MediaService} which includes
 * the media.
 */
public class MediaPage implements java.io.Serializable {

    private com.google.api.adwords.v201003.cm.Media[] media;

    private java.lang.Integer totalNumEntries;

    public MediaPage() {
    }

    public MediaPage(com.google.api.adwords.v201003.cm.Media[] media, java.lang.Integer totalNumEntries) {
        this.media = media;
        this.totalNumEntries = totalNumEntries;
    }

    /**
     * Gets the media value for this MediaPage.
     * 
     * @return media   * The result entries in this page.
     */
    public com.google.api.adwords.v201003.cm.Media[] getMedia() {
        return media;
    }

    /**
     * Sets the media value for this MediaPage.
     * 
     * @param media   * The result entries in this page.
     */
    public void setMedia(com.google.api.adwords.v201003.cm.Media[] media) {
        this.media = media;
    }

    public com.google.api.adwords.v201003.cm.Media getMedia(int i) {
        return this.media[i];
    }

    public void setMedia(int i, com.google.api.adwords.v201003.cm.Media _value) {
        this.media[i] = _value;
    }

    /**
     * Gets the totalNumEntries value for this MediaPage.
     * 
     * @return totalNumEntries   * Total number of entries in the result that this page is a part
     * of.
     */
    public java.lang.Integer getTotalNumEntries() {
        return totalNumEntries;
    }

    /**
     * Sets the totalNumEntries value for this MediaPage.
     * 
     * @param totalNumEntries   * Total number of entries in the result that this page is a part
     * of.
     */
    public void setTotalNumEntries(java.lang.Integer totalNumEntries) {
        this.totalNumEntries = totalNumEntries;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MediaPage)) return false;
        MediaPage other = (MediaPage) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.media == null && other.getMedia() == null) || (this.media != null && java.util.Arrays.equals(this.media, other.getMedia()))) && ((this.totalNumEntries == null && other.getTotalNumEntries() == null) || (this.totalNumEntries != null && this.totalNumEntries.equals(other.getTotalNumEntries())));
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
        if (getMedia() != null) {
            for (int i = 0; i < java.lang.reflect.Array.getLength(getMedia()); i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getMedia(), i);
                if (obj != null && !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getTotalNumEntries() != null) {
            _hashCode += getTotalNumEntries().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(MediaPage.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "MediaPage"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("media");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "media"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "Media"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("totalNumEntries");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "totalNumEntries"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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

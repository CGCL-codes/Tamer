package org.dbe.demos.amazon.ws;

public class ArtistRequest implements java.io.Serializable {

    private java.lang.String artist;

    private java.lang.String page;

    private java.lang.String mode;

    private java.lang.String tag;

    private java.lang.String type;

    private java.lang.String devtag;

    private java.lang.String sort;

    private java.lang.String variations;

    private java.lang.String locale;

    public ArtistRequest() {
    }

    public ArtistRequest(java.lang.String artist, java.lang.String page, java.lang.String mode, java.lang.String tag, java.lang.String type, java.lang.String devtag, java.lang.String sort, java.lang.String variations, java.lang.String locale) {
        this.artist = artist;
        this.page = page;
        this.mode = mode;
        this.tag = tag;
        this.type = type;
        this.devtag = devtag;
        this.sort = sort;
        this.variations = variations;
        this.locale = locale;
    }

    /**
     * Gets the artist value for this ArtistRequest.
     * 
     * @return artist
     */
    public java.lang.String getArtist() {
        return artist;
    }

    /**
     * Sets the artist value for this ArtistRequest.
     * 
     * @param artist
     */
    public void setArtist(java.lang.String artist) {
        this.artist = artist;
    }

    /**
     * Gets the page value for this ArtistRequest.
     * 
     * @return page
     */
    public java.lang.String getPage() {
        return page;
    }

    /**
     * Sets the page value for this ArtistRequest.
     * 
     * @param page
     */
    public void setPage(java.lang.String page) {
        this.page = page;
    }

    /**
     * Gets the mode value for this ArtistRequest.
     * 
     * @return mode
     */
    public java.lang.String getMode() {
        return mode;
    }

    /**
     * Sets the mode value for this ArtistRequest.
     * 
     * @param mode
     */
    public void setMode(java.lang.String mode) {
        this.mode = mode;
    }

    /**
     * Gets the tag value for this ArtistRequest.
     * 
     * @return tag
     */
    public java.lang.String getTag() {
        return tag;
    }

    /**
     * Sets the tag value for this ArtistRequest.
     * 
     * @param tag
     */
    public void setTag(java.lang.String tag) {
        this.tag = tag;
    }

    /**
     * Gets the type value for this ArtistRequest.
     * 
     * @return type
     */
    public java.lang.String getType() {
        return type;
    }

    /**
     * Sets the type value for this ArtistRequest.
     * 
     * @param type
     */
    public void setType(java.lang.String type) {
        this.type = type;
    }

    /**
     * Gets the devtag value for this ArtistRequest.
     * 
     * @return devtag
     */
    public java.lang.String getDevtag() {
        return devtag;
    }

    /**
     * Sets the devtag value for this ArtistRequest.
     * 
     * @param devtag
     */
    public void setDevtag(java.lang.String devtag) {
        this.devtag = devtag;
    }

    /**
     * Gets the sort value for this ArtistRequest.
     * 
     * @return sort
     */
    public java.lang.String getSort() {
        return sort;
    }

    /**
     * Sets the sort value for this ArtistRequest.
     * 
     * @param sort
     */
    public void setSort(java.lang.String sort) {
        this.sort = sort;
    }

    /**
     * Gets the variations value for this ArtistRequest.
     * 
     * @return variations
     */
    public java.lang.String getVariations() {
        return variations;
    }

    /**
     * Sets the variations value for this ArtistRequest.
     * 
     * @param variations
     */
    public void setVariations(java.lang.String variations) {
        this.variations = variations;
    }

    /**
     * Gets the locale value for this ArtistRequest.
     * 
     * @return locale
     */
    public java.lang.String getLocale() {
        return locale;
    }

    /**
     * Sets the locale value for this ArtistRequest.
     * 
     * @param locale
     */
    public void setLocale(java.lang.String locale) {
        this.locale = locale;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ArtistRequest)) return false;
        ArtistRequest other = (ArtistRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.artist == null && other.getArtist() == null) || (this.artist != null && this.artist.equals(other.getArtist()))) && ((this.page == null && other.getPage() == null) || (this.page != null && this.page.equals(other.getPage()))) && ((this.mode == null && other.getMode() == null) || (this.mode != null && this.mode.equals(other.getMode()))) && ((this.tag == null && other.getTag() == null) || (this.tag != null && this.tag.equals(other.getTag()))) && ((this.type == null && other.getType() == null) || (this.type != null && this.type.equals(other.getType()))) && ((this.devtag == null && other.getDevtag() == null) || (this.devtag != null && this.devtag.equals(other.getDevtag()))) && ((this.sort == null && other.getSort() == null) || (this.sort != null && this.sort.equals(other.getSort()))) && ((this.variations == null && other.getVariations() == null) || (this.variations != null && this.variations.equals(other.getVariations()))) && ((this.locale == null && other.getLocale() == null) || (this.locale != null && this.locale.equals(other.getLocale())));
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
        if (getArtist() != null) {
            _hashCode += getArtist().hashCode();
        }
        if (getPage() != null) {
            _hashCode += getPage().hashCode();
        }
        if (getMode() != null) {
            _hashCode += getMode().hashCode();
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
        if (getSort() != null) {
            _hashCode += getSort().hashCode();
        }
        if (getVariations() != null) {
            _hashCode += getVariations().hashCode();
        }
        if (getLocale() != null) {
            _hashCode += getLocale().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(ArtistRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://soap.amazon.com", "ArtistRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("artist");
        elemField.setXmlName(new javax.xml.namespace.QName("", "artist"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("page");
        elemField.setXmlName(new javax.xml.namespace.QName("", "page"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mode");
        elemField.setXmlName(new javax.xml.namespace.QName("", "mode"));
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
        elemField.setFieldName("sort");
        elemField.setXmlName(new javax.xml.namespace.QName("", "sort"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("variations");
        elemField.setXmlName(new javax.xml.namespace.QName("", "variations"));
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

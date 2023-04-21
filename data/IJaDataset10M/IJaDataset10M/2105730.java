package org.icestandard.ICE.V20.subscribe;

public class ContentMetadataType implements java.io.Serializable, org.apache.axis.encoding.AnyContentType, org.apache.axis.encoding.MixedContentType {

    private org.icestandard.ICE.V20.simpledatatypes.TextType[] text;

    private org.apache.axis.message.MessageElement[] _any;

    private boolean atomicUse;

    private boolean editable;

    private org.apache.axis.types.Token ipStatus;

    private org.apache.axis.types.Token license;

    private org.apache.axis.types.Token rightsHolder;

    private boolean showCredit;

    private org.icestandard.ICE.V20.simpledatatypes.ItemTypes itemType;

    public ContentMetadataType() {
    }

    public ContentMetadataType(org.icestandard.ICE.V20.simpledatatypes.TextType[] text, org.apache.axis.message.MessageElement[] _any, boolean atomicUse, boolean editable, org.apache.axis.types.Token ipStatus, org.apache.axis.types.Token license, org.apache.axis.types.Token rightsHolder, boolean showCredit, org.icestandard.ICE.V20.simpledatatypes.ItemTypes itemType) {
        this.text = text;
        this._any = _any;
        this.atomicUse = atomicUse;
        this.editable = editable;
        this.ipStatus = ipStatus;
        this.license = license;
        this.rightsHolder = rightsHolder;
        this.showCredit = showCredit;
        this.itemType = itemType;
    }

    /**
     * Gets the text value for this ContentMetadataType.
     * 
     * @return text
     */
    public org.icestandard.ICE.V20.simpledatatypes.TextType[] getText() {
        return text;
    }

    /**
     * Sets the text value for this ContentMetadataType.
     * 
     * @param text
     */
    public void setText(org.icestandard.ICE.V20.simpledatatypes.TextType[] text) {
        this.text = text;
    }

    public org.icestandard.ICE.V20.simpledatatypes.TextType getText(int i) {
        return this.text[i];
    }

    public void setText(int i, org.icestandard.ICE.V20.simpledatatypes.TextType _value) {
        this.text[i] = _value;
    }

    /**
     * Gets the _any value for this ContentMetadataType.
     * 
     * @return _any
     */
    public org.apache.axis.message.MessageElement[] get_any() {
        return _any;
    }

    /**
     * Sets the _any value for this ContentMetadataType.
     * 
     * @param _any
     */
    public void set_any(org.apache.axis.message.MessageElement[] _any) {
        this._any = _any;
    }

    /**
     * Gets the atomicUse value for this ContentMetadataType.
     * 
     * @return atomicUse
     */
    public boolean isAtomicUse() {
        return atomicUse;
    }

    /**
     * Sets the atomicUse value for this ContentMetadataType.
     * 
     * @param atomicUse
     */
    public void setAtomicUse(boolean atomicUse) {
        this.atomicUse = atomicUse;
    }

    /**
     * Gets the editable value for this ContentMetadataType.
     * 
     * @return editable
     */
    public boolean isEditable() {
        return editable;
    }

    /**
     * Sets the editable value for this ContentMetadataType.
     * 
     * @param editable
     */
    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    /**
     * Gets the ipStatus value for this ContentMetadataType.
     * 
     * @return ipStatus
     */
    public org.apache.axis.types.Token getIpStatus() {
        return ipStatus;
    }

    /**
     * Sets the ipStatus value for this ContentMetadataType.
     * 
     * @param ipStatus
     */
    public void setIpStatus(org.apache.axis.types.Token ipStatus) {
        this.ipStatus = ipStatus;
    }

    /**
     * Gets the license value for this ContentMetadataType.
     * 
     * @return license
     */
    public org.apache.axis.types.Token getLicense() {
        return license;
    }

    /**
     * Sets the license value for this ContentMetadataType.
     * 
     * @param license
     */
    public void setLicense(org.apache.axis.types.Token license) {
        this.license = license;
    }

    /**
     * Gets the rightsHolder value for this ContentMetadataType.
     * 
     * @return rightsHolder
     */
    public org.apache.axis.types.Token getRightsHolder() {
        return rightsHolder;
    }

    /**
     * Sets the rightsHolder value for this ContentMetadataType.
     * 
     * @param rightsHolder
     */
    public void setRightsHolder(org.apache.axis.types.Token rightsHolder) {
        this.rightsHolder = rightsHolder;
    }

    /**
     * Gets the showCredit value for this ContentMetadataType.
     * 
     * @return showCredit
     */
    public boolean isShowCredit() {
        return showCredit;
    }

    /**
     * Sets the showCredit value for this ContentMetadataType.
     * 
     * @param showCredit
     */
    public void setShowCredit(boolean showCredit) {
        this.showCredit = showCredit;
    }

    /**
     * Gets the itemType value for this ContentMetadataType.
     * 
     * @return itemType
     */
    public org.icestandard.ICE.V20.simpledatatypes.ItemTypes getItemType() {
        return itemType;
    }

    /**
     * Sets the itemType value for this ContentMetadataType.
     * 
     * @param itemType
     */
    public void setItemType(org.icestandard.ICE.V20.simpledatatypes.ItemTypes itemType) {
        this.itemType = itemType;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ContentMetadataType)) return false;
        ContentMetadataType other = (ContentMetadataType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.text == null && other.getText() == null) || (this.text != null && java.util.Arrays.equals(this.text, other.getText()))) && ((this._any == null && other.get_any() == null) || (this._any != null && java.util.Arrays.equals(this._any, other.get_any()))) && this.atomicUse == other.isAtomicUse() && this.editable == other.isEditable() && ((this.ipStatus == null && other.getIpStatus() == null) || (this.ipStatus != null && this.ipStatus.equals(other.getIpStatus()))) && ((this.license == null && other.getLicense() == null) || (this.license != null && this.license.equals(other.getLicense()))) && ((this.rightsHolder == null && other.getRightsHolder() == null) || (this.rightsHolder != null && this.rightsHolder.equals(other.getRightsHolder()))) && this.showCredit == other.isShowCredit() && ((this.itemType == null && other.getItemType() == null) || (this.itemType != null && this.itemType.equals(other.getItemType())));
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
        if (getText() != null) {
            for (int i = 0; i < java.lang.reflect.Array.getLength(getText()); i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getText(), i);
                if (obj != null && !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (get_any() != null) {
            for (int i = 0; i < java.lang.reflect.Array.getLength(get_any()); i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(get_any(), i);
                if (obj != null && !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += (isAtomicUse() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isEditable() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getIpStatus() != null) {
            _hashCode += getIpStatus().hashCode();
        }
        if (getLicense() != null) {
            _hashCode += getLicense().hashCode();
        }
        if (getRightsHolder() != null) {
            _hashCode += getRightsHolder().hashCode();
        }
        _hashCode += (isShowCredit() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getItemType() != null) {
            _hashCode += getItemType().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(ContentMetadataType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://icestandard.org/ICE/V20/subscribe", "content-metadataType"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("atomicUse");
        attrField.setXmlName(new javax.xml.namespace.QName("", "atomic-use"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("editable");
        attrField.setXmlName(new javax.xml.namespace.QName("", "editable"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("ipStatus");
        attrField.setXmlName(new javax.xml.namespace.QName("", "ip-status"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "token"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("license");
        attrField.setXmlName(new javax.xml.namespace.QName("", "license"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "token"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("rightsHolder");
        attrField.setXmlName(new javax.xml.namespace.QName("", "rights-holder"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "token"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("showCredit");
        attrField.setXmlName(new javax.xml.namespace.QName("", "show-credit"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("itemType");
        attrField.setXmlName(new javax.xml.namespace.QName("", "item-type"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://icestandard.org/ICE/V20/simpledatatypes", "item-types"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("text");
        elemField.setXmlName(new javax.xml.namespace.QName("http://icestandard.org/ICE/V20/subscribe", "text"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://icestandard.org/ICE/V20/simpledatatypes", "textType"));
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

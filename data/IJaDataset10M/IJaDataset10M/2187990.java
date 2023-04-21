package org.icestandard.ICE.V20.delivery;

public class PackageType implements java.io.Serializable, org.apache.axis.encoding.AnyContentType {

    private org.icestandard.ICE.V20.delivery.CmPackage cmPackage;

    private org.apache.axis.message.MessageElement[] _any;

    private org.apache.axis.types.Token packageId;

    private org.apache.axis.types.Token subscriptionId;

    private boolean fullupdate;

    private boolean confirmation;

    private org.apache.axis.types.Token newState;

    private org.apache.axis.types.Token oldState;

    public PackageType() {
    }

    public PackageType(org.icestandard.ICE.V20.delivery.CmPackage cmPackage, org.apache.axis.message.MessageElement[] _any, org.apache.axis.types.Token packageId, org.apache.axis.types.Token subscriptionId, boolean fullupdate, boolean confirmation, org.apache.axis.types.Token newState, org.apache.axis.types.Token oldState) {
        this.cmPackage = cmPackage;
        this._any = _any;
        this.packageId = packageId;
        this.subscriptionId = subscriptionId;
        this.fullupdate = fullupdate;
        this.confirmation = confirmation;
        this.newState = newState;
        this.oldState = oldState;
    }

    /**
     * Gets the cmPackage value for this PackageType.
     * 
     * @return cmPackage
     */
    public org.icestandard.ICE.V20.delivery.CmPackage getCmPackage() {
        return cmPackage;
    }

    /**
     * Sets the cmPackage value for this PackageType.
     * 
     * @param cmPackage
     */
    public void setCmPackage(org.icestandard.ICE.V20.delivery.CmPackage cmPackage) {
        this.cmPackage = cmPackage;
    }

    /**
     * Gets the _any value for this PackageType.
     * 
     * @return _any
     */
    public org.apache.axis.message.MessageElement[] get_any() {
        return _any;
    }

    /**
     * Sets the _any value for this PackageType.
     * 
     * @param _any
     */
    public void set_any(org.apache.axis.message.MessageElement[] _any) {
        this._any = _any;
    }

    /**
     * Gets the packageId value for this PackageType.
     * 
     * @return packageId
     */
    public org.apache.axis.types.Token getPackageId() {
        return packageId;
    }

    /**
     * Sets the packageId value for this PackageType.
     * 
     * @param packageId
     */
    public void setPackageId(org.apache.axis.types.Token packageId) {
        this.packageId = packageId;
    }

    /**
     * Gets the subscriptionId value for this PackageType.
     * 
     * @return subscriptionId
     */
    public org.apache.axis.types.Token getSubscriptionId() {
        return subscriptionId;
    }

    /**
     * Sets the subscriptionId value for this PackageType.
     * 
     * @param subscriptionId
     */
    public void setSubscriptionId(org.apache.axis.types.Token subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    /**
     * Gets the fullupdate value for this PackageType.
     * 
     * @return fullupdate
     */
    public boolean isFullupdate() {
        return fullupdate;
    }

    /**
     * Sets the fullupdate value for this PackageType.
     * 
     * @param fullupdate
     */
    public void setFullupdate(boolean fullupdate) {
        this.fullupdate = fullupdate;
    }

    /**
     * Gets the confirmation value for this PackageType.
     * 
     * @return confirmation
     */
    public boolean isConfirmation() {
        return confirmation;
    }

    /**
     * Sets the confirmation value for this PackageType.
     * 
     * @param confirmation
     */
    public void setConfirmation(boolean confirmation) {
        this.confirmation = confirmation;
    }

    /**
     * Gets the newState value for this PackageType.
     * 
     * @return newState
     */
    public org.apache.axis.types.Token getNewState() {
        return newState;
    }

    /**
     * Sets the newState value for this PackageType.
     * 
     * @param newState
     */
    public void setNewState(org.apache.axis.types.Token newState) {
        this.newState = newState;
    }

    /**
     * Gets the oldState value for this PackageType.
     * 
     * @return oldState
     */
    public org.apache.axis.types.Token getOldState() {
        return oldState;
    }

    /**
     * Sets the oldState value for this PackageType.
     * 
     * @param oldState
     */
    public void setOldState(org.apache.axis.types.Token oldState) {
        this.oldState = oldState;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PackageType)) return false;
        PackageType other = (PackageType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.cmPackage == null && other.getCmPackage() == null) || (this.cmPackage != null && this.cmPackage.equals(other.getCmPackage()))) && ((this._any == null && other.get_any() == null) || (this._any != null && java.util.Arrays.equals(this._any, other.get_any()))) && ((this.packageId == null && other.getPackageId() == null) || (this.packageId != null && this.packageId.equals(other.getPackageId()))) && ((this.subscriptionId == null && other.getSubscriptionId() == null) || (this.subscriptionId != null && this.subscriptionId.equals(other.getSubscriptionId()))) && this.fullupdate == other.isFullupdate() && this.confirmation == other.isConfirmation() && ((this.newState == null && other.getNewState() == null) || (this.newState != null && this.newState.equals(other.getNewState()))) && ((this.oldState == null && other.getOldState() == null) || (this.oldState != null && this.oldState.equals(other.getOldState())));
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
        if (getCmPackage() != null) {
            _hashCode += getCmPackage().hashCode();
        }
        if (get_any() != null) {
            for (int i = 0; i < java.lang.reflect.Array.getLength(get_any()); i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(get_any(), i);
                if (obj != null && !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getPackageId() != null) {
            _hashCode += getPackageId().hashCode();
        }
        if (getSubscriptionId() != null) {
            _hashCode += getSubscriptionId().hashCode();
        }
        _hashCode += (isFullupdate() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isConfirmation() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getNewState() != null) {
            _hashCode += getNewState().hashCode();
        }
        if (getOldState() != null) {
            _hashCode += getOldState().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(PackageType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://icestandard.org/ICE/V20/delivery", "packageType"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("packageId");
        attrField.setXmlName(new javax.xml.namespace.QName("", "package-id"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "token"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("subscriptionId");
        attrField.setXmlName(new javax.xml.namespace.QName("", "subscription-id"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "token"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("fullupdate");
        attrField.setXmlName(new javax.xml.namespace.QName("", "fullupdate"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("confirmation");
        attrField.setXmlName(new javax.xml.namespace.QName("", "confirmation"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("newState");
        attrField.setXmlName(new javax.xml.namespace.QName("", "new-state"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "token"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("oldState");
        attrField.setXmlName(new javax.xml.namespace.QName("", "old-state"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "token"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cmPackage");
        elemField.setXmlName(new javax.xml.namespace.QName("http://icestandard.org/ICE/V20/delivery", "cm.package"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://icestandard.org/ICE/V20/delivery", "cm.package"));
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

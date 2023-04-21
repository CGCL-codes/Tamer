package net.wesodi.updateService;

public class UpdateMeta implements java.io.Serializable {

    private java.lang.String loginID;

    private java.lang.String loginPW;

    private java.lang.String fileName;

    private int updateAction;

    private net.wesodi.updateService.ProductInfo productInfo;

    public UpdateMeta() {
    }

    public UpdateMeta(java.lang.String loginID, java.lang.String loginPW, java.lang.String fileName, int updateAction, net.wesodi.updateService.ProductInfo productInfo) {
        this.loginID = loginID;
        this.loginPW = loginPW;
        this.fileName = fileName;
        this.updateAction = updateAction;
        this.productInfo = productInfo;
    }

    /**
     * Gets the loginID value for this UpdateMeta.
     * 
     * @return loginID
     */
    public java.lang.String getLoginID() {
        return loginID;
    }

    /**
     * Sets the loginID value for this UpdateMeta.
     * 
     * @param loginID
     */
    public void setLoginID(java.lang.String loginID) {
        this.loginID = loginID;
    }

    /**
     * Gets the loginPW value for this UpdateMeta.
     * 
     * @return loginPW
     */
    public java.lang.String getLoginPW() {
        return loginPW;
    }

    /**
     * Sets the loginPW value for this UpdateMeta.
     * 
     * @param loginPW
     */
    public void setLoginPW(java.lang.String loginPW) {
        this.loginPW = loginPW;
    }

    /**
     * Gets the fileName value for this UpdateMeta.
     * 
     * @return fileName
     */
    public java.lang.String getFileName() {
        return fileName;
    }

    /**
     * Sets the fileName value for this UpdateMeta.
     * 
     * @param fileName
     */
    public void setFileName(java.lang.String fileName) {
        this.fileName = fileName;
    }

    /**
     * Gets the updateAction value for this UpdateMeta.
     * 
     * @return updateAction
     */
    public int getUpdateAction() {
        return updateAction;
    }

    /**
     * Sets the updateAction value for this UpdateMeta.
     * 
     * @param updateAction
     */
    public void setUpdateAction(int updateAction) {
        this.updateAction = updateAction;
    }

    /**
     * Gets the productInfo value for this UpdateMeta.
     * 
     * @return productInfo
     */
    public net.wesodi.updateService.ProductInfo getProductInfo() {
        return productInfo;
    }

    /**
     * Sets the productInfo value for this UpdateMeta.
     * 
     * @param productInfo
     */
    public void setProductInfo(net.wesodi.updateService.ProductInfo productInfo) {
        this.productInfo = productInfo;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof UpdateMeta)) return false;
        UpdateMeta other = (UpdateMeta) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.loginID == null && other.getLoginID() == null) || (this.loginID != null && this.loginID.equals(other.getLoginID()))) && ((this.loginPW == null && other.getLoginPW() == null) || (this.loginPW != null && this.loginPW.equals(other.getLoginPW()))) && ((this.fileName == null && other.getFileName() == null) || (this.fileName != null && this.fileName.equals(other.getFileName()))) && this.updateAction == other.getUpdateAction() && ((this.productInfo == null && other.getProductInfo() == null) || (this.productInfo != null && this.productInfo.equals(other.getProductInfo())));
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
        if (getLoginID() != null) {
            _hashCode += getLoginID().hashCode();
        }
        if (getLoginPW() != null) {
            _hashCode += getLoginPW().hashCode();
        }
        if (getFileName() != null) {
            _hashCode += getFileName().hashCode();
        }
        _hashCode += getUpdateAction();
        if (getProductInfo() != null) {
            _hashCode += getProductInfo().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(UpdateMeta.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://wesodi.net/updateService", "UpdateMeta"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("loginID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "loginID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("loginPW");
        elemField.setXmlName(new javax.xml.namespace.QName("", "loginPW"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fileName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fileName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("updateAction");
        elemField.setXmlName(new javax.xml.namespace.QName("", "updateAction"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("productInfo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://wesodi.net/updateService", "productInfo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://wesodi.net/updateService", ">productInfo"));
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

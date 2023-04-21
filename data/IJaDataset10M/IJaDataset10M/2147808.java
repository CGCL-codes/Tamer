package com.konakartadmin.app;

public class KKModule implements java.io.Serializable {

    private java.lang.String configKeyStub;

    private com.konakartadmin.app.KKConfiguration[] configs;

    private java.lang.String implementationFileName;

    private java.lang.String[] keys;

    private java.lang.String moduleCode;

    private int moduleType;

    private java.lang.String title;

    public KKModule() {
    }

    public KKModule(java.lang.String configKeyStub, com.konakartadmin.app.KKConfiguration[] configs, java.lang.String implementationFileName, java.lang.String[] keys, java.lang.String moduleCode, int moduleType, java.lang.String title) {
        this.configKeyStub = configKeyStub;
        this.configs = configs;
        this.implementationFileName = implementationFileName;
        this.keys = keys;
        this.moduleCode = moduleCode;
        this.moduleType = moduleType;
        this.title = title;
    }

    /**
     * Gets the configKeyStub value for this KKModule.
     * 
     * @return configKeyStub
     */
    public java.lang.String getConfigKeyStub() {
        return configKeyStub;
    }

    /**
     * Sets the configKeyStub value for this KKModule.
     * 
     * @param configKeyStub
     */
    public void setConfigKeyStub(java.lang.String configKeyStub) {
        this.configKeyStub = configKeyStub;
    }

    /**
     * Gets the configs value for this KKModule.
     * 
     * @return configs
     */
    public com.konakartadmin.app.KKConfiguration[] getConfigs() {
        return configs;
    }

    /**
     * Sets the configs value for this KKModule.
     * 
     * @param configs
     */
    public void setConfigs(com.konakartadmin.app.KKConfiguration[] configs) {
        this.configs = configs;
    }

    /**
     * Gets the implementationFileName value for this KKModule.
     * 
     * @return implementationFileName
     */
    public java.lang.String getImplementationFileName() {
        return implementationFileName;
    }

    /**
     * Sets the implementationFileName value for this KKModule.
     * 
     * @param implementationFileName
     */
    public void setImplementationFileName(java.lang.String implementationFileName) {
        this.implementationFileName = implementationFileName;
    }

    /**
     * Gets the keys value for this KKModule.
     * 
     * @return keys
     */
    public java.lang.String[] getKeys() {
        return keys;
    }

    /**
     * Sets the keys value for this KKModule.
     * 
     * @param keys
     */
    public void setKeys(java.lang.String[] keys) {
        this.keys = keys;
    }

    /**
     * Gets the moduleCode value for this KKModule.
     * 
     * @return moduleCode
     */
    public java.lang.String getModuleCode() {
        return moduleCode;
    }

    /**
     * Sets the moduleCode value for this KKModule.
     * 
     * @param moduleCode
     */
    public void setModuleCode(java.lang.String moduleCode) {
        this.moduleCode = moduleCode;
    }

    /**
     * Gets the moduleType value for this KKModule.
     * 
     * @return moduleType
     */
    public int getModuleType() {
        return moduleType;
    }

    /**
     * Sets the moduleType value for this KKModule.
     * 
     * @param moduleType
     */
    public void setModuleType(int moduleType) {
        this.moduleType = moduleType;
    }

    /**
     * Gets the title value for this KKModule.
     * 
     * @return title
     */
    public java.lang.String getTitle() {
        return title;
    }

    /**
     * Sets the title value for this KKModule.
     * 
     * @param title
     */
    public void setTitle(java.lang.String title) {
        this.title = title;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof KKModule)) return false;
        KKModule other = (KKModule) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.configKeyStub == null && other.getConfigKeyStub() == null) || (this.configKeyStub != null && this.configKeyStub.equals(other.getConfigKeyStub()))) && ((this.configs == null && other.getConfigs() == null) || (this.configs != null && java.util.Arrays.equals(this.configs, other.getConfigs()))) && ((this.implementationFileName == null && other.getImplementationFileName() == null) || (this.implementationFileName != null && this.implementationFileName.equals(other.getImplementationFileName()))) && ((this.keys == null && other.getKeys() == null) || (this.keys != null && java.util.Arrays.equals(this.keys, other.getKeys()))) && ((this.moduleCode == null && other.getModuleCode() == null) || (this.moduleCode != null && this.moduleCode.equals(other.getModuleCode()))) && this.moduleType == other.getModuleType() && ((this.title == null && other.getTitle() == null) || (this.title != null && this.title.equals(other.getTitle())));
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
        if (getConfigKeyStub() != null) {
            _hashCode += getConfigKeyStub().hashCode();
        }
        if (getConfigs() != null) {
            for (int i = 0; i < java.lang.reflect.Array.getLength(getConfigs()); i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getConfigs(), i);
                if (obj != null && !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getImplementationFileName() != null) {
            _hashCode += getImplementationFileName().hashCode();
        }
        if (getKeys() != null) {
            for (int i = 0; i < java.lang.reflect.Array.getLength(getKeys()); i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getKeys(), i);
                if (obj != null && !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getModuleCode() != null) {
            _hashCode += getModuleCode().hashCode();
        }
        _hashCode += getModuleType();
        if (getTitle() != null) {
            _hashCode += getTitle().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(KKModule.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://app.konakartadmin.com", "KKModule"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("configKeyStub");
        elemField.setXmlName(new javax.xml.namespace.QName("", "configKeyStub"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("configs");
        elemField.setXmlName(new javax.xml.namespace.QName("", "configs"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://app.konakartadmin.com", "KKConfiguration"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("implementationFileName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "implementationFileName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("keys");
        elemField.setXmlName(new javax.xml.namespace.QName("", "keys"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("moduleCode");
        elemField.setXmlName(new javax.xml.namespace.QName("", "moduleCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("moduleType");
        elemField.setXmlName(new javax.xml.namespace.QName("", "moduleType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("title");
        elemField.setXmlName(new javax.xml.namespace.QName("", "title"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
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

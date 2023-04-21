package client.nofity;

public class SearchResultScene implements java.io.Serializable {

    private java.lang.Integer sceneId;

    private java.lang.String sceneName;

    private java.lang.String sceneType;

    private java.lang.String provinceCode;

    private java.lang.String cityCode;

    public SearchResultScene() {
    }

    public SearchResultScene(java.lang.Integer sceneId, java.lang.String sceneName, java.lang.String sceneType, java.lang.String provinceCode, java.lang.String cityCode) {
        this.sceneId = sceneId;
        this.sceneName = sceneName;
        this.sceneType = sceneType;
        this.provinceCode = provinceCode;
        this.cityCode = cityCode;
    }

    /**
     * Gets the sceneId value for this SearchResultScene.
     * 
     * @return sceneId
     */
    public java.lang.Integer getSceneId() {
        return sceneId;
    }

    /**
     * Sets the sceneId value for this SearchResultScene.
     * 
     * @param sceneId
     */
    public void setSceneId(java.lang.Integer sceneId) {
        this.sceneId = sceneId;
    }

    /**
     * Gets the sceneName value for this SearchResultScene.
     * 
     * @return sceneName
     */
    public java.lang.String getSceneName() {
        return sceneName;
    }

    /**
     * Sets the sceneName value for this SearchResultScene.
     * 
     * @param sceneName
     */
    public void setSceneName(java.lang.String sceneName) {
        this.sceneName = sceneName;
    }

    /**
     * Gets the sceneType value for this SearchResultScene.
     * 
     * @return sceneType
     */
    public java.lang.String getSceneType() {
        return sceneType;
    }

    /**
     * Sets the sceneType value for this SearchResultScene.
     * 
     * @param sceneType
     */
    public void setSceneType(java.lang.String sceneType) {
        this.sceneType = sceneType;
    }

    /**
     * Gets the provinceCode value for this SearchResultScene.
     * 
     * @return provinceCode
     */
    public java.lang.String getProvinceCode() {
        return provinceCode;
    }

    /**
     * Sets the provinceCode value for this SearchResultScene.
     * 
     * @param provinceCode
     */
    public void setProvinceCode(java.lang.String provinceCode) {
        this.provinceCode = provinceCode;
    }

    /**
     * Gets the cityCode value for this SearchResultScene.
     * 
     * @return cityCode
     */
    public java.lang.String getCityCode() {
        return cityCode;
    }

    /**
     * Sets the cityCode value for this SearchResultScene.
     * 
     * @param cityCode
     */
    public void setCityCode(java.lang.String cityCode) {
        this.cityCode = cityCode;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SearchResultScene)) return false;
        SearchResultScene other = (SearchResultScene) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.sceneId == null && other.getSceneId() == null) || (this.sceneId != null && this.sceneId.equals(other.getSceneId()))) && ((this.sceneName == null && other.getSceneName() == null) || (this.sceneName != null && this.sceneName.equals(other.getSceneName()))) && ((this.sceneType == null && other.getSceneType() == null) || (this.sceneType != null && this.sceneType.equals(other.getSceneType()))) && ((this.provinceCode == null && other.getProvinceCode() == null) || (this.provinceCode != null && this.provinceCode.equals(other.getProvinceCode()))) && ((this.cityCode == null && other.getCityCode() == null) || (this.cityCode != null && this.cityCode.equals(other.getCityCode())));
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
        if (getSceneId() != null) {
            _hashCode += getSceneId().hashCode();
        }
        if (getSceneName() != null) {
            _hashCode += getSceneName().hashCode();
        }
        if (getSceneType() != null) {
            _hashCode += getSceneType().hashCode();
        }
        if (getProvinceCode() != null) {
            _hashCode += getProvinceCode().hashCode();
        }
        if (getCityCode() != null) {
            _hashCode += getCityCode().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(SearchResultScene.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/SearchResult.xsd", ">>SearchResult>Scene"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sceneId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/SearchResult.xsd", "SceneId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sceneName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/SearchResult.xsd", "SceneName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sceneType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/SearchResult.xsd", "SceneType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("provinceCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/SearchResult.xsd", "ProvinceCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cityCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/SearchResult.xsd", "CityCode"));
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

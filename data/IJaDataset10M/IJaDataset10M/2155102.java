package client;

public class LoginResult implements java.io.Serializable {

    private int appAuth;

    private int returnValue;

    public LoginResult() {
    }

    public LoginResult(int appAuth, int returnValue) {
        this.appAuth = appAuth;
        this.returnValue = returnValue;
    }

    /**
     * Gets the appAuth value for this LoginResult.
     * 
     * @return appAuth
     */
    public int getAppAuth() {
        return appAuth;
    }

    /**
     * Sets the appAuth value for this LoginResult.
     * 
     * @param appAuth
     */
    public void setAppAuth(int appAuth) {
        this.appAuth = appAuth;
    }

    /**
     * Gets the returnValue value for this LoginResult.
     * 
     * @return returnValue
     */
    public int getReturnValue() {
        return returnValue;
    }

    /**
     * Sets the returnValue value for this LoginResult.
     * 
     * @param returnValue
     */
    public void setReturnValue(int returnValue) {
        this.returnValue = returnValue;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof LoginResult)) return false;
        LoginResult other = (LoginResult) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && this.appAuth == other.getAppAuth() && this.returnValue == other.getReturnValue();
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
        _hashCode += getAppAuth();
        _hashCode += getReturnValue();
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(LoginResult.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://iamsweb.gmcc.net/WS/", "LoginResult"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("appAuth");
        elemField.setXmlName(new javax.xml.namespace.QName("http://iamsweb.gmcc.net/WS/", "appAuth"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("returnValue");
        elemField.setXmlName(new javax.xml.namespace.QName("http://iamsweb.gmcc.net/WS/", "returnValue"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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

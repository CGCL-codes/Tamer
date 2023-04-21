package cn.myapps.webservice.gmcczj.iamsweb.ws;

public class SMSResult implements java.io.Serializable {

    private int appAuth;

    private cn.myapps.webservice.gmcczj.iamsweb.ws.SmsDetail[] smsInfo;

    public SMSResult() {
    }

    public SMSResult(int appAuth, cn.myapps.webservice.gmcczj.iamsweb.ws.SmsDetail[] smsInfo) {
        this.appAuth = appAuth;
        this.smsInfo = smsInfo;
    }

    /**
     * Gets the appAuth value for this SMSResult.
     * 
     * @return appAuth
     */
    public int getAppAuth() {
        return appAuth;
    }

    /**
     * Sets the appAuth value for this SMSResult.
     * 
     * @param appAuth
     */
    public void setAppAuth(int appAuth) {
        this.appAuth = appAuth;
    }

    /**
     * Gets the smsInfo value for this SMSResult.
     * 
     * @return smsInfo
     */
    public cn.myapps.webservice.gmcczj.iamsweb.ws.SmsDetail[] getSmsInfo() {
        return smsInfo;
    }

    /**
     * Sets the smsInfo value for this SMSResult.
     * 
     * @param smsInfo
     */
    public void setSmsInfo(cn.myapps.webservice.gmcczj.iamsweb.ws.SmsDetail[] smsInfo) {
        this.smsInfo = smsInfo;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SMSResult)) return false;
        SMSResult other = (SMSResult) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && this.appAuth == other.getAppAuth() && ((this.smsInfo == null && other.getSmsInfo() == null) || (this.smsInfo != null && java.util.Arrays.equals(this.smsInfo, other.getSmsInfo())));
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
        if (getSmsInfo() != null) {
            for (int i = 0; i < java.lang.reflect.Array.getLength(getSmsInfo()); i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getSmsInfo(), i);
                if (obj != null && !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(SMSResult.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://iamsweb.gmcc.net/WS/", "SMSResult"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("appAuth");
        elemField.setXmlName(new javax.xml.namespace.QName("http://iamsweb.gmcc.net/WS/", "appAuth"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("smsInfo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://iamsweb.gmcc.net/WS/", "smsInfo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://iamsweb.gmcc.net/WS/", "smsDetail"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://iamsweb.gmcc.net/WS/", "smsDetail"));
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

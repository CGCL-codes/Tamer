package com.konakartadmin.app;

public class AdminDBConnector implements java.io.Serializable {

    private boolean connected;

    private java.lang.String connectionString;

    private java.lang.String driver;

    private java.lang.String errorMsg;

    private java.lang.String password;

    private java.lang.String user;

    public AdminDBConnector() {
    }

    public AdminDBConnector(boolean connected, java.lang.String connectionString, java.lang.String driver, java.lang.String errorMsg, java.lang.String password, java.lang.String user) {
        this.connected = connected;
        this.connectionString = connectionString;
        this.driver = driver;
        this.errorMsg = errorMsg;
        this.password = password;
        this.user = user;
    }

    /**
     * Gets the connected value for this AdminDBConnector.
     * 
     * @return connected
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * Sets the connected value for this AdminDBConnector.
     * 
     * @param connected
     */
    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    /**
     * Gets the connectionString value for this AdminDBConnector.
     * 
     * @return connectionString
     */
    public java.lang.String getConnectionString() {
        return connectionString;
    }

    /**
     * Sets the connectionString value for this AdminDBConnector.
     * 
     * @param connectionString
     */
    public void setConnectionString(java.lang.String connectionString) {
        this.connectionString = connectionString;
    }

    /**
     * Gets the driver value for this AdminDBConnector.
     * 
     * @return driver
     */
    public java.lang.String getDriver() {
        return driver;
    }

    /**
     * Sets the driver value for this AdminDBConnector.
     * 
     * @param driver
     */
    public void setDriver(java.lang.String driver) {
        this.driver = driver;
    }

    /**
     * Gets the errorMsg value for this AdminDBConnector.
     * 
     * @return errorMsg
     */
    public java.lang.String getErrorMsg() {
        return errorMsg;
    }

    /**
     * Sets the errorMsg value for this AdminDBConnector.
     * 
     * @param errorMsg
     */
    public void setErrorMsg(java.lang.String errorMsg) {
        this.errorMsg = errorMsg;
    }

    /**
     * Gets the password value for this AdminDBConnector.
     * 
     * @return password
     */
    public java.lang.String getPassword() {
        return password;
    }

    /**
     * Sets the password value for this AdminDBConnector.
     * 
     * @param password
     */
    public void setPassword(java.lang.String password) {
        this.password = password;
    }

    /**
     * Gets the user value for this AdminDBConnector.
     * 
     * @return user
     */
    public java.lang.String getUser() {
        return user;
    }

    /**
     * Sets the user value for this AdminDBConnector.
     * 
     * @param user
     */
    public void setUser(java.lang.String user) {
        this.user = user;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AdminDBConnector)) return false;
        AdminDBConnector other = (AdminDBConnector) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && this.connected == other.isConnected() && ((this.connectionString == null && other.getConnectionString() == null) || (this.connectionString != null && this.connectionString.equals(other.getConnectionString()))) && ((this.driver == null && other.getDriver() == null) || (this.driver != null && this.driver.equals(other.getDriver()))) && ((this.errorMsg == null && other.getErrorMsg() == null) || (this.errorMsg != null && this.errorMsg.equals(other.getErrorMsg()))) && ((this.password == null && other.getPassword() == null) || (this.password != null && this.password.equals(other.getPassword()))) && ((this.user == null && other.getUser() == null) || (this.user != null && this.user.equals(other.getUser())));
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
        _hashCode += (isConnected() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getConnectionString() != null) {
            _hashCode += getConnectionString().hashCode();
        }
        if (getDriver() != null) {
            _hashCode += getDriver().hashCode();
        }
        if (getErrorMsg() != null) {
            _hashCode += getErrorMsg().hashCode();
        }
        if (getPassword() != null) {
            _hashCode += getPassword().hashCode();
        }
        if (getUser() != null) {
            _hashCode += getUser().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(AdminDBConnector.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://app.konakartadmin.com", "AdminDBConnector"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("connected");
        elemField.setXmlName(new javax.xml.namespace.QName("", "connected"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("connectionString");
        elemField.setXmlName(new javax.xml.namespace.QName("", "connectionString"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("driver");
        elemField.setXmlName(new javax.xml.namespace.QName("", "driver"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("errorMsg");
        elemField.setXmlName(new javax.xml.namespace.QName("", "errorMsg"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("password");
        elemField.setXmlName(new javax.xml.namespace.QName("", "password"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("user");
        elemField.setXmlName(new javax.xml.namespace.QName("", "user"));
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

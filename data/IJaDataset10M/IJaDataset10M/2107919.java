package edu.ipfw.nitroblog.ws.consumer;

public class GetRecentBlogEntries implements java.io.Serializable {

    private java.lang.String blogId;

    private java.lang.String username;

    private java.lang.String password;

    private int numberOfPorts;

    public GetRecentBlogEntries() {
    }

    public GetRecentBlogEntries(java.lang.String blogId, java.lang.String username, java.lang.String password, int numberOfPorts) {
        this.blogId = blogId;
        this.username = username;
        this.password = password;
        this.numberOfPorts = numberOfPorts;
    }

    /**
     * Gets the blogId value for this GetRecentBlogEntries.
     * 
     * @return blogId
     */
    public java.lang.String getBlogId() {
        return blogId;
    }

    /**
     * Sets the blogId value for this GetRecentBlogEntries.
     * 
     * @param blogId
     */
    public void setBlogId(java.lang.String blogId) {
        this.blogId = blogId;
    }

    /**
     * Gets the username value for this GetRecentBlogEntries.
     * 
     * @return username
     */
    public java.lang.String getUsername() {
        return username;
    }

    /**
     * Sets the username value for this GetRecentBlogEntries.
     * 
     * @param username
     */
    public void setUsername(java.lang.String username) {
        this.username = username;
    }

    /**
     * Gets the password value for this GetRecentBlogEntries.
     * 
     * @return password
     */
    public java.lang.String getPassword() {
        return password;
    }

    /**
     * Sets the password value for this GetRecentBlogEntries.
     * 
     * @param password
     */
    public void setPassword(java.lang.String password) {
        this.password = password;
    }

    /**
     * Gets the numberOfPorts value for this GetRecentBlogEntries.
     * 
     * @return numberOfPorts
     */
    public int getNumberOfPorts() {
        return numberOfPorts;
    }

    /**
     * Sets the numberOfPorts value for this GetRecentBlogEntries.
     * 
     * @param numberOfPorts
     */
    public void setNumberOfPorts(int numberOfPorts) {
        this.numberOfPorts = numberOfPorts;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetRecentBlogEntries)) return false;
        GetRecentBlogEntries other = (GetRecentBlogEntries) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.blogId == null && other.getBlogId() == null) || (this.blogId != null && this.blogId.equals(other.getBlogId()))) && ((this.username == null && other.getUsername() == null) || (this.username != null && this.username.equals(other.getUsername()))) && ((this.password == null && other.getPassword() == null) || (this.password != null && this.password.equals(other.getPassword()))) && this.numberOfPorts == other.getNumberOfPorts();
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
        if (getBlogId() != null) {
            _hashCode += getBlogId().hashCode();
        }
        if (getUsername() != null) {
            _hashCode += getUsername().hashCode();
        }
        if (getPassword() != null) {
            _hashCode += getPassword().hashCode();
        }
        _hashCode += getNumberOfPorts();
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(GetRecentBlogEntries.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ws.pebble.sourceforge.net/", "getRecentBlogEntries"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("blogId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "blogId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("username");
        elemField.setXmlName(new javax.xml.namespace.QName("", "username"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("password");
        elemField.setXmlName(new javax.xml.namespace.QName("", "password"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numberOfPorts");
        elemField.setXmlName(new javax.xml.namespace.QName("", "numberOfPorts"));
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

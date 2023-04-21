package com.konakartadmin.app;

public class AdminProductNotification implements java.io.Serializable {

    private int custId;

    private java.util.Calendar dateAdded;

    private int id;

    private int productId;

    public AdminProductNotification() {
    }

    public AdminProductNotification(int custId, java.util.Calendar dateAdded, int id, int productId) {
        this.custId = custId;
        this.dateAdded = dateAdded;
        this.id = id;
        this.productId = productId;
    }

    /**
     * Gets the custId value for this AdminProductNotification.
     * 
     * @return custId
     */
    public int getCustId() {
        return custId;
    }

    /**
     * Sets the custId value for this AdminProductNotification.
     * 
     * @param custId
     */
    public void setCustId(int custId) {
        this.custId = custId;
    }

    /**
     * Gets the dateAdded value for this AdminProductNotification.
     * 
     * @return dateAdded
     */
    public java.util.Calendar getDateAdded() {
        return dateAdded;
    }

    /**
     * Sets the dateAdded value for this AdminProductNotification.
     * 
     * @param dateAdded
     */
    public void setDateAdded(java.util.Calendar dateAdded) {
        this.dateAdded = dateAdded;
    }

    /**
     * Gets the id value for this AdminProductNotification.
     * 
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id value for this AdminProductNotification.
     * 
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the productId value for this AdminProductNotification.
     * 
     * @return productId
     */
    public int getProductId() {
        return productId;
    }

    /**
     * Sets the productId value for this AdminProductNotification.
     * 
     * @param productId
     */
    public void setProductId(int productId) {
        this.productId = productId;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AdminProductNotification)) return false;
        AdminProductNotification other = (AdminProductNotification) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && this.custId == other.getCustId() && ((this.dateAdded == null && other.getDateAdded() == null) || (this.dateAdded != null && this.dateAdded.equals(other.getDateAdded()))) && this.id == other.getId() && this.productId == other.getProductId();
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
        _hashCode += getCustId();
        if (getDateAdded() != null) {
            _hashCode += getDateAdded().hashCode();
        }
        _hashCode += getId();
        _hashCode += getProductId();
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(AdminProductNotification.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://app.konakartadmin.com", "AdminProductNotification"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("custId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "custId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dateAdded");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dateAdded"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("productId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "productId"));
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

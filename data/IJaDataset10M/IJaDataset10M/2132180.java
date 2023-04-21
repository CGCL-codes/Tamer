package com.konakartadmin.app;

public class AdminOrderTotal implements java.io.Serializable {

    private java.lang.String className;

    private int id;

    private int orderId;

    private int sortOrder;

    private java.lang.String text;

    private java.lang.String title;

    private java.math.BigDecimal value;

    public AdminOrderTotal() {
    }

    public AdminOrderTotal(java.lang.String className, int id, int orderId, int sortOrder, java.lang.String text, java.lang.String title, java.math.BigDecimal value) {
        this.className = className;
        this.id = id;
        this.orderId = orderId;
        this.sortOrder = sortOrder;
        this.text = text;
        this.title = title;
        this.value = value;
    }

    /**
     * Gets the className value for this AdminOrderTotal.
     * 
     * @return className
     */
    public java.lang.String getClassName() {
        return className;
    }

    /**
     * Sets the className value for this AdminOrderTotal.
     * 
     * @param className
     */
    public void setClassName(java.lang.String className) {
        this.className = className;
    }

    /**
     * Gets the id value for this AdminOrderTotal.
     * 
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id value for this AdminOrderTotal.
     * 
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the orderId value for this AdminOrderTotal.
     * 
     * @return orderId
     */
    public int getOrderId() {
        return orderId;
    }

    /**
     * Sets the orderId value for this AdminOrderTotal.
     * 
     * @param orderId
     */
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    /**
     * Gets the sortOrder value for this AdminOrderTotal.
     * 
     * @return sortOrder
     */
    public int getSortOrder() {
        return sortOrder;
    }

    /**
     * Sets the sortOrder value for this AdminOrderTotal.
     * 
     * @param sortOrder
     */
    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    /**
     * Gets the text value for this AdminOrderTotal.
     * 
     * @return text
     */
    public java.lang.String getText() {
        return text;
    }

    /**
     * Sets the text value for this AdminOrderTotal.
     * 
     * @param text
     */
    public void setText(java.lang.String text) {
        this.text = text;
    }

    /**
     * Gets the title value for this AdminOrderTotal.
     * 
     * @return title
     */
    public java.lang.String getTitle() {
        return title;
    }

    /**
     * Sets the title value for this AdminOrderTotal.
     * 
     * @param title
     */
    public void setTitle(java.lang.String title) {
        this.title = title;
    }

    /**
     * Gets the value value for this AdminOrderTotal.
     * 
     * @return value
     */
    public java.math.BigDecimal getValue() {
        return value;
    }

    /**
     * Sets the value value for this AdminOrderTotal.
     * 
     * @param value
     */
    public void setValue(java.math.BigDecimal value) {
        this.value = value;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AdminOrderTotal)) return false;
        AdminOrderTotal other = (AdminOrderTotal) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.className == null && other.getClassName() == null) || (this.className != null && this.className.equals(other.getClassName()))) && this.id == other.getId() && this.orderId == other.getOrderId() && this.sortOrder == other.getSortOrder() && ((this.text == null && other.getText() == null) || (this.text != null && this.text.equals(other.getText()))) && ((this.title == null && other.getTitle() == null) || (this.title != null && this.title.equals(other.getTitle()))) && ((this.value == null && other.getValue() == null) || (this.value != null && this.value.equals(other.getValue())));
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
        if (getClassName() != null) {
            _hashCode += getClassName().hashCode();
        }
        _hashCode += getId();
        _hashCode += getOrderId();
        _hashCode += getSortOrder();
        if (getText() != null) {
            _hashCode += getText().hashCode();
        }
        if (getTitle() != null) {
            _hashCode += getTitle().hashCode();
        }
        if (getValue() != null) {
            _hashCode += getValue().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(AdminOrderTotal.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://app.konakartadmin.com", "AdminOrderTotal"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("className");
        elemField.setXmlName(new javax.xml.namespace.QName("", "className"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("orderId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "orderId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sortOrder");
        elemField.setXmlName(new javax.xml.namespace.QName("", "sortOrder"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("text");
        elemField.setXmlName(new javax.xml.namespace.QName("", "text"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("title");
        elemField.setXmlName(new javax.xml.namespace.QName("", "title"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("value");
        elemField.setXmlName(new javax.xml.namespace.QName("", "value"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "decimal"));
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

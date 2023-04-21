package tudu.web.ws.bean;

/**
 * WsTodoList.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 * 
 * @author Julien Dubois
 */
public class WsTodoList implements java.io.Serializable {

    private static final long serialVersionUID = -5336990686847403065L;

    private java.lang.String listId;

    private java.lang.String name;

    public WsTodoList() {
    }

    public WsTodoList(java.lang.String listId, java.lang.String name) {
        this.listId = listId;
        this.name = name;
    }

    /**
     * Gets the listId value for this WsTodoList.
     * 
     * @return listId
     */
    public java.lang.String getListId() {
        return listId;
    }

    /**
     * Sets the listId value for this WsTodoList.
     * 
     * @param listId
     */
    public void setListId(java.lang.String listId) {
        this.listId = listId;
    }

    /**
     * Gets the name value for this WsTodoList.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }

    /**
     * Sets the name value for this WsTodoList.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof WsTodoList)) return false;
        WsTodoList other = (WsTodoList) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.listId == null && other.getListId() == null) || (this.listId != null && this.listId.equals(other.getListId()))) && ((this.name == null && other.getName() == null) || (this.name != null && this.name.equals(other.getName())));
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
        if (getListId() != null) {
            _hashCode += getListId().hashCode();
        }
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(WsTodoList.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://bean.ws.web.tudu", "WsTodoList"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("listId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "listId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("name");
        elemField.setXmlName(new javax.xml.namespace.QName("", "name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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

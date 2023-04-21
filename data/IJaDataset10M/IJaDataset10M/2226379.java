package net.myphpshop.www.myPHPShopAdmin;

public class Role implements java.io.Serializable {

    private java.lang.String roleName;

    private net.myphpshop.www.myPHPShopAdmin.Function[] function;

    public Role() {
    }

    public Role(java.lang.String roleName, net.myphpshop.www.myPHPShopAdmin.Function[] function) {
        this.roleName = roleName;
        this.function = function;
    }

    /**
     * Gets the roleName value for this Role.
     * 
     * @return roleName
     */
    public java.lang.String getRoleName() {
        return roleName;
    }

    /**
     * Sets the roleName value for this Role.
     * 
     * @param roleName
     */
    public void setRoleName(java.lang.String roleName) {
        this.roleName = roleName;
    }

    /**
     * Gets the function value for this Role.
     * 
     * @return function
     */
    public net.myphpshop.www.myPHPShopAdmin.Function[] getFunction() {
        return function;
    }

    /**
     * Sets the function value for this Role.
     * 
     * @param function
     */
    public void setFunction(net.myphpshop.www.myPHPShopAdmin.Function[] function) {
        this.function = function;
    }

    public net.myphpshop.www.myPHPShopAdmin.Function getFunction(int i) {
        return this.function[i];
    }

    public void setFunction(int i, net.myphpshop.www.myPHPShopAdmin.Function _value) {
        this.function[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Role)) return false;
        Role other = (Role) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.roleName == null && other.getRoleName() == null) || (this.roleName != null && this.roleName.equals(other.getRoleName()))) && ((this.function == null && other.getFunction() == null) || (this.function != null && java.util.Arrays.equals(this.function, other.getFunction())));
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
        if (getRoleName() != null) {
            _hashCode += getRoleName().hashCode();
        }
        if (getFunction() != null) {
            for (int i = 0; i < java.lang.reflect.Array.getLength(getFunction()); i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getFunction(), i);
                if (obj != null && !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(Role.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.myphpshop.net/myPHPShopAdmin", ">Role"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("roleName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.myphpshop.net/myPHPShopAdmin", "RoleName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("function");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.myphpshop.net/myPHPShopAdmin", "Function"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.myphpshop.net/myPHPShopAdmin", "Function"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
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

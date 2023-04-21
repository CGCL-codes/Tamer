package org.localhost.services.RevCtrl.RevCtrlService;

public class StringMap implements java.io.Serializable {

    private org.localhost.services.RevCtrl.RevCtrlService.MapItem[] item;

    public StringMap() {
    }

    public StringMap(org.localhost.services.RevCtrl.RevCtrlService.MapItem[] item) {
        this.item = item;
    }

    /**
     * Gets the item value for this StringMap.
     * 
     * @return item
     */
    public org.localhost.services.RevCtrl.RevCtrlService.MapItem[] getItem() {
        return item;
    }

    /**
     * Sets the item value for this StringMap.
     * 
     * @param item
     */
    public void setItem(org.localhost.services.RevCtrl.RevCtrlService.MapItem[] item) {
        this.item = item;
    }

    public org.localhost.services.RevCtrl.RevCtrlService.MapItem getItem(int i) {
        return this.item[i];
    }

    public void setItem(int i, org.localhost.services.RevCtrl.RevCtrlService.MapItem _value) {
        this.item[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof StringMap)) return false;
        StringMap other = (StringMap) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.item == null && other.getItem() == null) || (this.item != null && java.util.Arrays.equals(this.item, other.getItem())));
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
        if (getItem() != null) {
            for (int i = 0; i < java.lang.reflect.Array.getLength(getItem()); i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getItem(), i);
                if (obj != null && !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(StringMap.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://services.localhost.org/RevCtrl/RevCtrlService", "stringMap"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("item");
        elemField.setXmlName(new javax.xml.namespace.QName("", "item"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://services.localhost.org/RevCtrl/RevCtrlService", "mapItem"));
        elemField.setMinOccurs(0);
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

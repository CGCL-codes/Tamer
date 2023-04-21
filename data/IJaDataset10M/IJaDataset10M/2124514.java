package ws.net;

public class PesquisarTitulos implements java.io.Serializable {

    private java.lang.String inquerito;

    public PesquisarTitulos() {
    }

    public PesquisarTitulos(java.lang.String inquerito) {
        this.inquerito = inquerito;
    }

    /**
     * Gets the inquerito value for this PesquisarTitulos.
     * 
     * @return inquerito
     */
    public java.lang.String getInquerito() {
        return inquerito;
    }

    /**
     * Sets the inquerito value for this PesquisarTitulos.
     * 
     * @param inquerito
     */
    public void setInquerito(java.lang.String inquerito) {
        this.inquerito = inquerito;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PesquisarTitulos)) return false;
        PesquisarTitulos other = (PesquisarTitulos) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.inquerito == null && other.getInquerito() == null) || (this.inquerito != null && this.inquerito.equals(other.getInquerito())));
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
        if (getInquerito() != null) {
            _hashCode += getInquerito().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(PesquisarTitulos.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://net.ws", ">PesquisarTitulos"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("inquerito");
        elemField.setXmlName(new javax.xml.namespace.QName("http://net.ws", "inquerito"));
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

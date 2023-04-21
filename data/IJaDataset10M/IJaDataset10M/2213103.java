package coordinator.wscoordination;

import coordinator.wsaddressing.*;

public class RegisterResponseType implements java.io.Serializable, org.apache.axis.encoding.AnyContentType {

    private EndpointReferenceType coordinatorProtocolService;

    private org.apache.axis.message.MessageElement[] _any;

    public RegisterResponseType() {
    }

    public RegisterResponseType(EndpointReferenceType coordinatorProtocolService, org.apache.axis.message.MessageElement[] _any) {
        this.coordinatorProtocolService = coordinatorProtocolService;
        this._any = _any;
    }

    /**
     * Gets the coordinatorProtocolService value for this RegisterResponseType.
     * 
     * @return coordinatorProtocolService
     */
    public EndpointReferenceType getCoordinatorProtocolService() {
        return coordinatorProtocolService;
    }

    /**
     * Sets the coordinatorProtocolService value for this RegisterResponseType.
     * 
     * @param coordinatorProtocolService
     */
    public void setCoordinatorProtocolService(EndpointReferenceType coordinatorProtocolService) {
        this.coordinatorProtocolService = coordinatorProtocolService;
    }

    /**
     * Gets the _any value for this RegisterResponseType.
     * 
     * @return _any
     */
    public org.apache.axis.message.MessageElement[] get_any() {
        return _any;
    }

    /**
     * Sets the _any value for this RegisterResponseType.
     * 
     * @param _any
     */
    public void set_any(org.apache.axis.message.MessageElement[] _any) {
        this._any = _any;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RegisterResponseType)) return false;
        RegisterResponseType other = (RegisterResponseType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.coordinatorProtocolService == null && other.getCoordinatorProtocolService() == null) || (this.coordinatorProtocolService != null && this.coordinatorProtocolService.equals(other.getCoordinatorProtocolService()))) && ((this._any == null && other.get_any() == null) || (this._any != null && java.util.Arrays.equals(this._any, other.get_any())));
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
        if (getCoordinatorProtocolService() != null) {
            _hashCode += getCoordinatorProtocolService().hashCode();
        }
        if (get_any() != null) {
            for (int i = 0; i < java.lang.reflect.Array.getLength(get_any()); i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(get_any(), i);
                if (obj != null && !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(RegisterResponseType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/10/wscoor", "RegisterResponseType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("coordinatorProtocolService");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/10/wscoor", "CoordinatorProtocolService"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/08/addressing", "EndpointReferenceType"));
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

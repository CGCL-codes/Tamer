package coordinator.wscoordination;

public class CreateCoordinationContextType implements java.io.Serializable, org.apache.axis.encoding.AnyContentType {

    private Expires expires;

    private CreateCoordinationContextTypeCurrentContext currentContext;

    private org.apache.axis.types.URI coordinationType;

    private org.apache.axis.message.MessageElement[] _any;

    public CreateCoordinationContextType() {
    }

    public CreateCoordinationContextType(Expires expires, CreateCoordinationContextTypeCurrentContext currentContext, org.apache.axis.types.URI coordinationType, org.apache.axis.message.MessageElement[] _any) {
        this.expires = expires;
        this.currentContext = currentContext;
        this.coordinationType = coordinationType;
        this._any = _any;
    }

    /**
     * Gets the expires value for this CreateCoordinationContextType.
     * 
     * @return expires
     */
    public Expires getExpires() {
        return expires;
    }

    /**
     * Sets the expires value for this CreateCoordinationContextType.
     * 
     * @param expires
     */
    public void setExpires(Expires expires) {
        this.expires = expires;
    }

    /**
     * Gets the currentContext value for this CreateCoordinationContextType.
     * 
     * @return currentContext
     */
    public CreateCoordinationContextTypeCurrentContext getCurrentContext() {
        return currentContext;
    }

    /**
     * Sets the currentContext value for this CreateCoordinationContextType.
     * 
     * @param currentContext
     */
    public void setCurrentContext(CreateCoordinationContextTypeCurrentContext currentContext) {
        this.currentContext = currentContext;
    }

    /**
     * Gets the coordinationType value for this CreateCoordinationContextType.
     * 
     * @return coordinationType
     */
    public org.apache.axis.types.URI getCoordinationType() {
        return coordinationType;
    }

    /**
     * Sets the coordinationType value for this CreateCoordinationContextType.
     * 
     * @param coordinationType
     */
    public void setCoordinationType(org.apache.axis.types.URI coordinationType) {
        this.coordinationType = coordinationType;
    }

    /**
     * Gets the _any value for this CreateCoordinationContextType.
     * 
     * @return _any
     */
    public org.apache.axis.message.MessageElement[] get_any() {
        return _any;
    }

    /**
     * Sets the _any value for this CreateCoordinationContextType.
     * 
     * @param _any
     */
    public void set_any(org.apache.axis.message.MessageElement[] _any) {
        this._any = _any;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CreateCoordinationContextType)) return false;
        CreateCoordinationContextType other = (CreateCoordinationContextType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.expires == null && other.getExpires() == null) || (this.expires != null && this.expires.equals(other.getExpires()))) && ((this.currentContext == null && other.getCurrentContext() == null) || (this.currentContext != null && this.currentContext.equals(other.getCurrentContext()))) && ((this.coordinationType == null && other.getCoordinationType() == null) || (this.coordinationType != null && this.coordinationType.equals(other.getCoordinationType()))) && ((this._any == null && other.get_any() == null) || (this._any != null && java.util.Arrays.equals(this._any, other.get_any())));
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
        if (getExpires() != null) {
            _hashCode += getExpires().hashCode();
        }
        if (getCurrentContext() != null) {
            _hashCode += getCurrentContext().hashCode();
        }
        if (getCoordinationType() != null) {
            _hashCode += getCoordinationType().hashCode();
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

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(CreateCoordinationContextType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/10/wscoor", "CreateCoordinationContextType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("expires");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/10/wscoor", "Expires"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/10/wscoor", ">Expires"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("currentContext");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/10/wscoor", "CurrentContext"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/10/wscoor", ">CreateCoordinationContextType>CurrentContext"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("coordinationType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/10/wscoor", "CoordinationType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyURI"));
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

package rallyws.api;

public class WorkspaceDomainObject extends rallyws.api.DomainObject implements java.io.Serializable {

    private rallyws.api.Workspace workspace;

    public WorkspaceDomainObject() {
    }

    public WorkspaceDomainObject(java.lang.String ref, long objectVersion, java.lang.String type, java.lang.String refObjectName, long rallyAPIMajor, long rallyAPIMinor, java.util.Calendar creationDate, java.lang.Long objectID, rallyws.api.Subscription subscription, rallyws.api.Workspace workspace) {
        super(ref, objectVersion, type, refObjectName, rallyAPIMajor, rallyAPIMinor, creationDate, objectID, subscription);
        this.workspace = workspace;
    }

    /**
     * Gets the workspace value for this WorkspaceDomainObject.
     * 
     * @return workspace
     */
    public rallyws.api.Workspace getWorkspace() {
        return workspace;
    }

    /**
     * Sets the workspace value for this WorkspaceDomainObject.
     * 
     * @param workspace
     */
    public void setWorkspace(rallyws.api.Workspace workspace) {
        this.workspace = workspace;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof WorkspaceDomainObject)) return false;
        WorkspaceDomainObject other = (WorkspaceDomainObject) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && ((this.workspace == null && other.getWorkspace() == null) || (this.workspace != null && this.workspace.equals(other.getWorkspace())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;

    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (getWorkspace() != null) {
            _hashCode += getWorkspace().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(WorkspaceDomainObject.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://rallydev.com/webservice/v1_09/domain", "WorkspaceDomainObject"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("workspace");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Workspace"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://rallydev.com/webservice/v1_09/domain", "Workspace"));
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

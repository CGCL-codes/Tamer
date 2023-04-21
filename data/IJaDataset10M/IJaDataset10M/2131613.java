package org.localhost.services.RevCtrl.RevCtrlService;

public class AddProjectRequest implements java.io.Serializable {

    private java.lang.String projectName;

    private java.lang.String projectSources;

    public AddProjectRequest() {
    }

    public AddProjectRequest(java.lang.String projectName, java.lang.String projectSources) {
        this.projectName = projectName;
        this.projectSources = projectSources;
    }

    /**
     * Gets the projectName value for this AddProjectRequest.
     * 
     * @return projectName
     */
    public java.lang.String getProjectName() {
        return projectName;
    }

    /**
     * Sets the projectName value for this AddProjectRequest.
     * 
     * @param projectName
     */
    public void setProjectName(java.lang.String projectName) {
        this.projectName = projectName;
    }

    /**
     * Gets the projectSources value for this AddProjectRequest.
     * 
     * @return projectSources
     */
    public java.lang.String getProjectSources() {
        return projectSources;
    }

    /**
     * Sets the projectSources value for this AddProjectRequest.
     * 
     * @param projectSources
     */
    public void setProjectSources(java.lang.String projectSources) {
        this.projectSources = projectSources;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AddProjectRequest)) return false;
        AddProjectRequest other = (AddProjectRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.projectName == null && other.getProjectName() == null) || (this.projectName != null && this.projectName.equals(other.getProjectName()))) && ((this.projectSources == null && other.getProjectSources() == null) || (this.projectSources != null && this.projectSources.equals(other.getProjectSources())));
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
        if (getProjectName() != null) {
            _hashCode += getProjectName().hashCode();
        }
        if (getProjectSources() != null) {
            _hashCode += getProjectSources().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(AddProjectRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://services.localhost.org/RevCtrl/RevCtrlService", ">addProjectRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("projectName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "projectName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("projectSources");
        elemField.setXmlName(new javax.xml.namespace.QName("", "projectSources"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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

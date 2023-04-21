package com.rallydev.webservice.v1_0.domain;

public class Project extends com.rallydev.webservice.v1_0.domain.WorkspaceDomainObject implements java.io.Serializable {

    private java.lang.String description;

    private com.rallydev.webservice.v1_0.domain.Iteration[] iterations;

    private java.lang.String name;

    private java.lang.String notes;

    private java.lang.String owner;

    private com.rallydev.webservice.v1_0.domain.Release[] releases;

    private java.lang.String state;

    public Project() {
    }

    public Project(org.apache.axis.types.URI ref, long objectVersion, java.lang.String type, java.lang.String refObjectName, long rallyAPIMajor, long rallyAPIMinor, java.util.Calendar creationDate, java.lang.Long objectID, com.rallydev.webservice.v1_0.domain.Subscription subscription, com.rallydev.webservice.v1_0.domain.Workspace workspace, java.lang.String description, com.rallydev.webservice.v1_0.domain.Iteration[] iterations, java.lang.String name, java.lang.String notes, java.lang.String owner, com.rallydev.webservice.v1_0.domain.Release[] releases, java.lang.String state) {
        super(ref, objectVersion, type, refObjectName, rallyAPIMajor, rallyAPIMinor, creationDate, objectID, subscription, workspace);
        this.description = description;
        this.iterations = iterations;
        this.name = name;
        this.notes = notes;
        this.owner = owner;
        this.releases = releases;
        this.state = state;
    }

    /**
     * Gets the description value for this Project.
     * 
     * @return description
     */
    public java.lang.String getDescription() {
        return description;
    }

    /**
     * Sets the description value for this Project.
     * 
     * @param description
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    /**
     * Gets the iterations value for this Project.
     * 
     * @return iterations
     */
    public com.rallydev.webservice.v1_0.domain.Iteration[] getIterations() {
        return iterations;
    }

    /**
     * Sets the iterations value for this Project.
     * 
     * @param iterations
     */
    public void setIterations(com.rallydev.webservice.v1_0.domain.Iteration[] iterations) {
        this.iterations = iterations;
    }

    /**
     * Gets the name value for this Project.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }

    /**
     * Sets the name value for this Project.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }

    /**
     * Gets the notes value for this Project.
     * 
     * @return notes
     */
    public java.lang.String getNotes() {
        return notes;
    }

    /**
     * Sets the notes value for this Project.
     * 
     * @param notes
     */
    public void setNotes(java.lang.String notes) {
        this.notes = notes;
    }

    /**
     * Gets the owner value for this Project.
     * 
     * @return owner
     */
    public java.lang.String getOwner() {
        return owner;
    }

    /**
     * Sets the owner value for this Project.
     * 
     * @param owner
     */
    public void setOwner(java.lang.String owner) {
        this.owner = owner;
    }

    /**
     * Gets the releases value for this Project.
     * 
     * @return releases
     */
    public com.rallydev.webservice.v1_0.domain.Release[] getReleases() {
        return releases;
    }

    /**
     * Sets the releases value for this Project.
     * 
     * @param releases
     */
    public void setReleases(com.rallydev.webservice.v1_0.domain.Release[] releases) {
        this.releases = releases;
    }

    /**
     * Gets the state value for this Project.
     * 
     * @return state
     */
    public java.lang.String getState() {
        return state;
    }

    /**
     * Sets the state value for this Project.
     * 
     * @param state
     */
    public void setState(java.lang.String state) {
        this.state = state;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Project)) return false;
        Project other = (Project) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && ((this.description == null && other.getDescription() == null) || (this.description != null && this.description.equals(other.getDescription()))) && ((this.iterations == null && other.getIterations() == null) || (this.iterations != null && java.util.Arrays.equals(this.iterations, other.getIterations()))) && ((this.name == null && other.getName() == null) || (this.name != null && this.name.equals(other.getName()))) && ((this.notes == null && other.getNotes() == null) || (this.notes != null && this.notes.equals(other.getNotes()))) && ((this.owner == null && other.getOwner() == null) || (this.owner != null && this.owner.equals(other.getOwner()))) && ((this.releases == null && other.getReleases() == null) || (this.releases != null && java.util.Arrays.equals(this.releases, other.getReleases()))) && ((this.state == null && other.getState() == null) || (this.state != null && this.state.equals(other.getState())));
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
        if (getDescription() != null) {
            _hashCode += getDescription().hashCode();
        }
        if (getIterations() != null) {
            for (int i = 0; i < java.lang.reflect.Array.getLength(getIterations()); i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getIterations(), i);
                if (obj != null && !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (getNotes() != null) {
            _hashCode += getNotes().hashCode();
        }
        if (getOwner() != null) {
            _hashCode += getOwner().hashCode();
        }
        if (getReleases() != null) {
            for (int i = 0; i < java.lang.reflect.Array.getLength(getReleases()); i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getReleases(), i);
                if (obj != null && !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getState() != null) {
            _hashCode += getState().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(Project.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://rallydev.com/webservice/v1_0/domain", "Project"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("description");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Description"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("iterations");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Iterations"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://rallydev.com/webservice/v1_0/domain", "Iteration"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("", "Iteration"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("name");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("notes");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Notes"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("owner");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Owner"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("releases");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Releases"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://rallydev.com/webservice/v1_0/domain", "Release"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("", "Release"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("state");
        elemField.setXmlName(new javax.xml.namespace.QName("", "State"));
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

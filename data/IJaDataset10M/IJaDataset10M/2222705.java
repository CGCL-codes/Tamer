package com.rallydev.webservice.v1_0.domain;

public class Artifact extends com.rallydev.webservice.v1_0.domain.WorkspaceDomainObject implements java.io.Serializable {

    private java.lang.String description;

    private java.lang.String formattedID;

    private java.lang.String name;

    private java.lang.String notes;

    private java.lang.String owner;

    private com.rallydev.webservice.v1_0.domain.RevisionHistory revisionHistory;

    public Artifact() {
    }

    public Artifact(org.apache.axis.types.URI ref, long objectVersion, java.lang.String type, java.lang.String refObjectName, long rallyAPIMajor, long rallyAPIMinor, java.util.Calendar creationDate, java.lang.Long objectID, com.rallydev.webservice.v1_0.domain.Subscription subscription, com.rallydev.webservice.v1_0.domain.Workspace workspace, java.lang.String description, java.lang.String formattedID, java.lang.String name, java.lang.String notes, java.lang.String owner, com.rallydev.webservice.v1_0.domain.RevisionHistory revisionHistory) {
        super(ref, objectVersion, type, refObjectName, rallyAPIMajor, rallyAPIMinor, creationDate, objectID, subscription, workspace);
        this.description = description;
        this.formattedID = formattedID;
        this.name = name;
        this.notes = notes;
        this.owner = owner;
        this.revisionHistory = revisionHistory;
    }

    /**
     * Gets the description value for this Artifact.
     * 
     * @return description
     */
    public java.lang.String getDescription() {
        return description;
    }

    /**
     * Sets the description value for this Artifact.
     * 
     * @param description
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    /**
     * Gets the formattedID value for this Artifact.
     * 
     * @return formattedID
     */
    public java.lang.String getFormattedID() {
        return formattedID;
    }

    /**
     * Sets the formattedID value for this Artifact.
     * 
     * @param formattedID
     */
    public void setFormattedID(java.lang.String formattedID) {
        this.formattedID = formattedID;
    }

    /**
     * Gets the name value for this Artifact.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }

    /**
     * Sets the name value for this Artifact.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }

    /**
     * Gets the notes value for this Artifact.
     * 
     * @return notes
     */
    public java.lang.String getNotes() {
        return notes;
    }

    /**
     * Sets the notes value for this Artifact.
     * 
     * @param notes
     */
    public void setNotes(java.lang.String notes) {
        this.notes = notes;
    }

    /**
     * Gets the owner value for this Artifact.
     * 
     * @return owner
     */
    public java.lang.String getOwner() {
        return owner;
    }

    /**
     * Sets the owner value for this Artifact.
     * 
     * @param owner
     */
    public void setOwner(java.lang.String owner) {
        this.owner = owner;
    }

    /**
     * Gets the revisionHistory value for this Artifact.
     * 
     * @return revisionHistory
     */
    public com.rallydev.webservice.v1_0.domain.RevisionHistory getRevisionHistory() {
        return revisionHistory;
    }

    /**
     * Sets the revisionHistory value for this Artifact.
     * 
     * @param revisionHistory
     */
    public void setRevisionHistory(com.rallydev.webservice.v1_0.domain.RevisionHistory revisionHistory) {
        this.revisionHistory = revisionHistory;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Artifact)) return false;
        Artifact other = (Artifact) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && ((this.description == null && other.getDescription() == null) || (this.description != null && this.description.equals(other.getDescription()))) && ((this.formattedID == null && other.getFormattedID() == null) || (this.formattedID != null && this.formattedID.equals(other.getFormattedID()))) && ((this.name == null && other.getName() == null) || (this.name != null && this.name.equals(other.getName()))) && ((this.notes == null && other.getNotes() == null) || (this.notes != null && this.notes.equals(other.getNotes()))) && ((this.owner == null && other.getOwner() == null) || (this.owner != null && this.owner.equals(other.getOwner()))) && ((this.revisionHistory == null && other.getRevisionHistory() == null) || (this.revisionHistory != null && this.revisionHistory.equals(other.getRevisionHistory())));
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
        if (getFormattedID() != null) {
            _hashCode += getFormattedID().hashCode();
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
        if (getRevisionHistory() != null) {
            _hashCode += getRevisionHistory().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(Artifact.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://rallydev.com/webservice/v1_0/domain", "Artifact"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("description");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Description"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("formattedID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "FormattedID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
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
        elemField.setFieldName("revisionHistory");
        elemField.setXmlName(new javax.xml.namespace.QName("", "RevisionHistory"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://rallydev.com/webservice/v1_0/domain", "RevisionHistory"));
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

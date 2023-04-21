package org.openmeetings.axis.services;

public class AddRoomWithModerationExternalTypeAndAudioType implements java.io.Serializable {

    private java.lang.String SID;

    private java.lang.String name;

    private java.lang.Long roomtypes_id;

    private java.lang.String comment;

    private java.lang.Long numberOfPartizipants;

    private java.lang.Boolean ispublic;

    private java.lang.Boolean appointment;

    private java.lang.Boolean isDemoRoom;

    private java.lang.Integer demoTime;

    private java.lang.Boolean isModeratedRoom;

    private java.lang.String externalRoomType;

    private java.lang.Boolean allowUserQuestions;

    private java.lang.Boolean isAudioOnly;

    public AddRoomWithModerationExternalTypeAndAudioType() {
    }

    public AddRoomWithModerationExternalTypeAndAudioType(java.lang.String SID, java.lang.String name, java.lang.Long roomtypes_id, java.lang.String comment, java.lang.Long numberOfPartizipants, java.lang.Boolean ispublic, java.lang.Boolean appointment, java.lang.Boolean isDemoRoom, java.lang.Integer demoTime, java.lang.Boolean isModeratedRoom, java.lang.String externalRoomType, java.lang.Boolean allowUserQuestions, java.lang.Boolean isAudioOnly) {
        this.SID = SID;
        this.name = name;
        this.roomtypes_id = roomtypes_id;
        this.comment = comment;
        this.numberOfPartizipants = numberOfPartizipants;
        this.ispublic = ispublic;
        this.appointment = appointment;
        this.isDemoRoom = isDemoRoom;
        this.demoTime = demoTime;
        this.isModeratedRoom = isModeratedRoom;
        this.externalRoomType = externalRoomType;
        this.allowUserQuestions = allowUserQuestions;
        this.isAudioOnly = isAudioOnly;
    }

    /**
     * Gets the SID value for this AddRoomWithModerationExternalTypeAndAudioType.
     * 
     * @return SID
     */
    public java.lang.String getSID() {
        return SID;
    }

    /**
     * Sets the SID value for this AddRoomWithModerationExternalTypeAndAudioType.
     * 
     * @param SID
     */
    public void setSID(java.lang.String SID) {
        this.SID = SID;
    }

    /**
     * Gets the name value for this AddRoomWithModerationExternalTypeAndAudioType.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }

    /**
     * Sets the name value for this AddRoomWithModerationExternalTypeAndAudioType.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }

    /**
     * Gets the roomtypes_id value for this AddRoomWithModerationExternalTypeAndAudioType.
     * 
     * @return roomtypes_id
     */
    public java.lang.Long getRoomtypes_id() {
        return roomtypes_id;
    }

    /**
     * Sets the roomtypes_id value for this AddRoomWithModerationExternalTypeAndAudioType.
     * 
     * @param roomtypes_id
     */
    public void setRoomtypes_id(java.lang.Long roomtypes_id) {
        this.roomtypes_id = roomtypes_id;
    }

    /**
     * Gets the comment value for this AddRoomWithModerationExternalTypeAndAudioType.
     * 
     * @return comment
     */
    public java.lang.String getComment() {
        return comment;
    }

    /**
     * Sets the comment value for this AddRoomWithModerationExternalTypeAndAudioType.
     * 
     * @param comment
     */
    public void setComment(java.lang.String comment) {
        this.comment = comment;
    }

    /**
     * Gets the numberOfPartizipants value for this AddRoomWithModerationExternalTypeAndAudioType.
     * 
     * @return numberOfPartizipants
     */
    public java.lang.Long getNumberOfPartizipants() {
        return numberOfPartizipants;
    }

    /**
     * Sets the numberOfPartizipants value for this AddRoomWithModerationExternalTypeAndAudioType.
     * 
     * @param numberOfPartizipants
     */
    public void setNumberOfPartizipants(java.lang.Long numberOfPartizipants) {
        this.numberOfPartizipants = numberOfPartizipants;
    }

    /**
     * Gets the ispublic value for this AddRoomWithModerationExternalTypeAndAudioType.
     * 
     * @return ispublic
     */
    public java.lang.Boolean getIspublic() {
        return ispublic;
    }

    /**
     * Sets the ispublic value for this AddRoomWithModerationExternalTypeAndAudioType.
     * 
     * @param ispublic
     */
    public void setIspublic(java.lang.Boolean ispublic) {
        this.ispublic = ispublic;
    }

    /**
     * Gets the appointment value for this AddRoomWithModerationExternalTypeAndAudioType.
     * 
     * @return appointment
     */
    public java.lang.Boolean getAppointment() {
        return appointment;
    }

    /**
     * Sets the appointment value for this AddRoomWithModerationExternalTypeAndAudioType.
     * 
     * @param appointment
     */
    public void setAppointment(java.lang.Boolean appointment) {
        this.appointment = appointment;
    }

    /**
     * Gets the isDemoRoom value for this AddRoomWithModerationExternalTypeAndAudioType.
     * 
     * @return isDemoRoom
     */
    public java.lang.Boolean getIsDemoRoom() {
        return isDemoRoom;
    }

    /**
     * Sets the isDemoRoom value for this AddRoomWithModerationExternalTypeAndAudioType.
     * 
     * @param isDemoRoom
     */
    public void setIsDemoRoom(java.lang.Boolean isDemoRoom) {
        this.isDemoRoom = isDemoRoom;
    }

    /**
     * Gets the demoTime value for this AddRoomWithModerationExternalTypeAndAudioType.
     * 
     * @return demoTime
     */
    public java.lang.Integer getDemoTime() {
        return demoTime;
    }

    /**
     * Sets the demoTime value for this AddRoomWithModerationExternalTypeAndAudioType.
     * 
     * @param demoTime
     */
    public void setDemoTime(java.lang.Integer demoTime) {
        this.demoTime = demoTime;
    }

    /**
     * Gets the isModeratedRoom value for this AddRoomWithModerationExternalTypeAndAudioType.
     * 
     * @return isModeratedRoom
     */
    public java.lang.Boolean getIsModeratedRoom() {
        return isModeratedRoom;
    }

    /**
     * Sets the isModeratedRoom value for this AddRoomWithModerationExternalTypeAndAudioType.
     * 
     * @param isModeratedRoom
     */
    public void setIsModeratedRoom(java.lang.Boolean isModeratedRoom) {
        this.isModeratedRoom = isModeratedRoom;
    }

    /**
     * Gets the externalRoomType value for this AddRoomWithModerationExternalTypeAndAudioType.
     * 
     * @return externalRoomType
     */
    public java.lang.String getExternalRoomType() {
        return externalRoomType;
    }

    /**
     * Sets the externalRoomType value for this AddRoomWithModerationExternalTypeAndAudioType.
     * 
     * @param externalRoomType
     */
    public void setExternalRoomType(java.lang.String externalRoomType) {
        this.externalRoomType = externalRoomType;
    }

    /**
     * Gets the allowUserQuestions value for this AddRoomWithModerationExternalTypeAndAudioType.
     * 
     * @return allowUserQuestions
     */
    public java.lang.Boolean getAllowUserQuestions() {
        return allowUserQuestions;
    }

    /**
     * Sets the allowUserQuestions value for this AddRoomWithModerationExternalTypeAndAudioType.
     * 
     * @param allowUserQuestions
     */
    public void setAllowUserQuestions(java.lang.Boolean allowUserQuestions) {
        this.allowUserQuestions = allowUserQuestions;
    }

    /**
     * Gets the isAudioOnly value for this AddRoomWithModerationExternalTypeAndAudioType.
     * 
     * @return isAudioOnly
     */
    public java.lang.Boolean getIsAudioOnly() {
        return isAudioOnly;
    }

    /**
     * Sets the isAudioOnly value for this AddRoomWithModerationExternalTypeAndAudioType.
     * 
     * @param isAudioOnly
     */
    public void setIsAudioOnly(java.lang.Boolean isAudioOnly) {
        this.isAudioOnly = isAudioOnly;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AddRoomWithModerationExternalTypeAndAudioType)) return false;
        AddRoomWithModerationExternalTypeAndAudioType other = (AddRoomWithModerationExternalTypeAndAudioType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.SID == null && other.getSID() == null) || (this.SID != null && this.SID.equals(other.getSID()))) && ((this.name == null && other.getName() == null) || (this.name != null && this.name.equals(other.getName()))) && ((this.roomtypes_id == null && other.getRoomtypes_id() == null) || (this.roomtypes_id != null && this.roomtypes_id.equals(other.getRoomtypes_id()))) && ((this.comment == null && other.getComment() == null) || (this.comment != null && this.comment.equals(other.getComment()))) && ((this.numberOfPartizipants == null && other.getNumberOfPartizipants() == null) || (this.numberOfPartizipants != null && this.numberOfPartizipants.equals(other.getNumberOfPartizipants()))) && ((this.ispublic == null && other.getIspublic() == null) || (this.ispublic != null && this.ispublic.equals(other.getIspublic()))) && ((this.appointment == null && other.getAppointment() == null) || (this.appointment != null && this.appointment.equals(other.getAppointment()))) && ((this.isDemoRoom == null && other.getIsDemoRoom() == null) || (this.isDemoRoom != null && this.isDemoRoom.equals(other.getIsDemoRoom()))) && ((this.demoTime == null && other.getDemoTime() == null) || (this.demoTime != null && this.demoTime.equals(other.getDemoTime()))) && ((this.isModeratedRoom == null && other.getIsModeratedRoom() == null) || (this.isModeratedRoom != null && this.isModeratedRoom.equals(other.getIsModeratedRoom()))) && ((this.externalRoomType == null && other.getExternalRoomType() == null) || (this.externalRoomType != null && this.externalRoomType.equals(other.getExternalRoomType()))) && ((this.allowUserQuestions == null && other.getAllowUserQuestions() == null) || (this.allowUserQuestions != null && this.allowUserQuestions.equals(other.getAllowUserQuestions()))) && ((this.isAudioOnly == null && other.getIsAudioOnly() == null) || (this.isAudioOnly != null && this.isAudioOnly.equals(other.getIsAudioOnly())));
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
        if (getSID() != null) {
            _hashCode += getSID().hashCode();
        }
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (getRoomtypes_id() != null) {
            _hashCode += getRoomtypes_id().hashCode();
        }
        if (getComment() != null) {
            _hashCode += getComment().hashCode();
        }
        if (getNumberOfPartizipants() != null) {
            _hashCode += getNumberOfPartizipants().hashCode();
        }
        if (getIspublic() != null) {
            _hashCode += getIspublic().hashCode();
        }
        if (getAppointment() != null) {
            _hashCode += getAppointment().hashCode();
        }
        if (getIsDemoRoom() != null) {
            _hashCode += getIsDemoRoom().hashCode();
        }
        if (getDemoTime() != null) {
            _hashCode += getDemoTime().hashCode();
        }
        if (getIsModeratedRoom() != null) {
            _hashCode += getIsModeratedRoom().hashCode();
        }
        if (getExternalRoomType() != null) {
            _hashCode += getExternalRoomType().hashCode();
        }
        if (getAllowUserQuestions() != null) {
            _hashCode += getAllowUserQuestions().hashCode();
        }
        if (getIsAudioOnly() != null) {
            _hashCode += getIsAudioOnly().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(AddRoomWithModerationExternalTypeAndAudioType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://services.axis.openmeetings.org", ">addRoomWithModerationExternalTypeAndAudioType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("SID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://services.axis.openmeetings.org", "SID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("name");
        elemField.setXmlName(new javax.xml.namespace.QName("http://services.axis.openmeetings.org", "name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("roomtypes_id");
        elemField.setXmlName(new javax.xml.namespace.QName("http://services.axis.openmeetings.org", "roomtypes_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("comment");
        elemField.setXmlName(new javax.xml.namespace.QName("http://services.axis.openmeetings.org", "comment"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numberOfPartizipants");
        elemField.setXmlName(new javax.xml.namespace.QName("http://services.axis.openmeetings.org", "numberOfPartizipants"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ispublic");
        elemField.setXmlName(new javax.xml.namespace.QName("http://services.axis.openmeetings.org", "ispublic"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("appointment");
        elemField.setXmlName(new javax.xml.namespace.QName("http://services.axis.openmeetings.org", "appointment"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("isDemoRoom");
        elemField.setXmlName(new javax.xml.namespace.QName("http://services.axis.openmeetings.org", "isDemoRoom"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("demoTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://services.axis.openmeetings.org", "demoTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("isModeratedRoom");
        elemField.setXmlName(new javax.xml.namespace.QName("http://services.axis.openmeetings.org", "isModeratedRoom"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("externalRoomType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://services.axis.openmeetings.org", "externalRoomType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("allowUserQuestions");
        elemField.setXmlName(new javax.xml.namespace.QName("http://services.axis.openmeetings.org", "allowUserQuestions"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("isAudioOnly");
        elemField.setXmlName(new javax.xml.namespace.QName("http://services.axis.openmeetings.org", "isAudioOnly"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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

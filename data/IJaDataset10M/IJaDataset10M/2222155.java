package ispyb.server.webservice.smis;

public class ExperimentVO extends fr.esrf.smis.vos.SMISValueObject implements java.io.Serializable {

    private java.lang.Integer MES_UNI_STAT;

    private java.lang.Integer allocatedShifts;

    private java.lang.Boolean automaticCreation;

    private java.lang.Long beamlinePk;

    private ispyb.server.webservice.smis.BeamlineVO beamlineVO;

    private java.lang.Integer experimentMonth;

    private java.lang.Integer experimentYear;

    private java.lang.Long pk;

    private java.lang.Integer plannedShifts;

    private java.lang.Long proposalPk;

    private ispyb.server.webservice.smis.ProposalVO proposalVO;

    private java.lang.Integer provisionalAllocatedShifts;

    private java.lang.Integer requestedShifts;

    private ispyb.server.webservice.smis.ExpSessionVO[] sessions;

    public ExperimentVO() {
    }

    public java.lang.Integer getMES_UNI_STAT() {
        return MES_UNI_STAT;
    }

    public void setMES_UNI_STAT(java.lang.Integer MES_UNI_STAT) {
        this.MES_UNI_STAT = MES_UNI_STAT;
    }

    public java.lang.Integer getAllocatedShifts() {
        return allocatedShifts;
    }

    public void setAllocatedShifts(java.lang.Integer allocatedShifts) {
        this.allocatedShifts = allocatedShifts;
    }

    public java.lang.Boolean getAutomaticCreation() {
        return automaticCreation;
    }

    public void setAutomaticCreation(java.lang.Boolean automaticCreation) {
        this.automaticCreation = automaticCreation;
    }

    public java.lang.Long getBeamlinePk() {
        return beamlinePk;
    }

    public void setBeamlinePk(java.lang.Long beamlinePk) {
        this.beamlinePk = beamlinePk;
    }

    public ispyb.server.webservice.smis.BeamlineVO getBeamlineVO() {
        return beamlineVO;
    }

    public void setBeamlineVO(ispyb.server.webservice.smis.BeamlineVO beamlineVO) {
        this.beamlineVO = beamlineVO;
    }

    public java.lang.Integer getExperimentMonth() {
        return experimentMonth;
    }

    public void setExperimentMonth(java.lang.Integer experimentMonth) {
        this.experimentMonth = experimentMonth;
    }

    public java.lang.Integer getExperimentYear() {
        return experimentYear;
    }

    public void setExperimentYear(java.lang.Integer experimentYear) {
        this.experimentYear = experimentYear;
    }

    public java.lang.Long getPk() {
        return pk;
    }

    public void setPk(java.lang.Long pk) {
        this.pk = pk;
    }

    public java.lang.Integer getPlannedShifts() {
        return plannedShifts;
    }

    public void setPlannedShifts(java.lang.Integer plannedShifts) {
        this.plannedShifts = plannedShifts;
    }

    public java.lang.Long getProposalPk() {
        return proposalPk;
    }

    public void setProposalPk(java.lang.Long proposalPk) {
        this.proposalPk = proposalPk;
    }

    public ispyb.server.webservice.smis.ProposalVO getProposalVO() {
        return proposalVO;
    }

    public void setProposalVO(ispyb.server.webservice.smis.ProposalVO proposalVO) {
        this.proposalVO = proposalVO;
    }

    public java.lang.Integer getProvisionalAllocatedShifts() {
        return provisionalAllocatedShifts;
    }

    public void setProvisionalAllocatedShifts(java.lang.Integer provisionalAllocatedShifts) {
        this.provisionalAllocatedShifts = provisionalAllocatedShifts;
    }

    public java.lang.Integer getRequestedShifts() {
        return requestedShifts;
    }

    public void setRequestedShifts(java.lang.Integer requestedShifts) {
        this.requestedShifts = requestedShifts;
    }

    public ispyb.server.webservice.smis.ExpSessionVO[] getSessions() {
        return sessions;
    }

    public void setSessions(ispyb.server.webservice.smis.ExpSessionVO[] sessions) {
        this.sessions = sessions;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ExperimentVO)) return false;
        ExperimentVO other = (ExperimentVO) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && ((this.MES_UNI_STAT == null && other.getMES_UNI_STAT() == null) || (this.MES_UNI_STAT != null && this.MES_UNI_STAT.equals(other.getMES_UNI_STAT()))) && ((this.allocatedShifts == null && other.getAllocatedShifts() == null) || (this.allocatedShifts != null && this.allocatedShifts.equals(other.getAllocatedShifts()))) && ((this.automaticCreation == null && other.getAutomaticCreation() == null) || (this.automaticCreation != null && this.automaticCreation.equals(other.getAutomaticCreation()))) && ((this.beamlinePk == null && other.getBeamlinePk() == null) || (this.beamlinePk != null && this.beamlinePk.equals(other.getBeamlinePk()))) && ((this.beamlineVO == null && other.getBeamlineVO() == null) || (this.beamlineVO != null && this.beamlineVO.equals(other.getBeamlineVO()))) && ((this.experimentMonth == null && other.getExperimentMonth() == null) || (this.experimentMonth != null && this.experimentMonth.equals(other.getExperimentMonth()))) && ((this.experimentYear == null && other.getExperimentYear() == null) || (this.experimentYear != null && this.experimentYear.equals(other.getExperimentYear()))) && ((this.pk == null && other.getPk() == null) || (this.pk != null && this.pk.equals(other.getPk()))) && ((this.plannedShifts == null && other.getPlannedShifts() == null) || (this.plannedShifts != null && this.plannedShifts.equals(other.getPlannedShifts()))) && ((this.proposalPk == null && other.getProposalPk() == null) || (this.proposalPk != null && this.proposalPk.equals(other.getProposalPk()))) && ((this.proposalVO == null && other.getProposalVO() == null) || (this.proposalVO != null && this.proposalVO.equals(other.getProposalVO()))) && ((this.provisionalAllocatedShifts == null && other.getProvisionalAllocatedShifts() == null) || (this.provisionalAllocatedShifts != null && this.provisionalAllocatedShifts.equals(other.getProvisionalAllocatedShifts()))) && ((this.requestedShifts == null && other.getRequestedShifts() == null) || (this.requestedShifts != null && this.requestedShifts.equals(other.getRequestedShifts()))) && ((this.sessions == null && other.getSessions() == null) || (this.sessions != null && java.util.Arrays.equals(this.sessions, other.getSessions())));
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
        if (getMES_UNI_STAT() != null) {
            _hashCode += getMES_UNI_STAT().hashCode();
        }
        if (getAllocatedShifts() != null) {
            _hashCode += getAllocatedShifts().hashCode();
        }
        if (getAutomaticCreation() != null) {
            _hashCode += getAutomaticCreation().hashCode();
        }
        if (getBeamlinePk() != null) {
            _hashCode += getBeamlinePk().hashCode();
        }
        if (getBeamlineVO() != null) {
            _hashCode += getBeamlineVO().hashCode();
        }
        if (getExperimentMonth() != null) {
            _hashCode += getExperimentMonth().hashCode();
        }
        if (getExperimentYear() != null) {
            _hashCode += getExperimentYear().hashCode();
        }
        if (getPk() != null) {
            _hashCode += getPk().hashCode();
        }
        if (getPlannedShifts() != null) {
            _hashCode += getPlannedShifts().hashCode();
        }
        if (getProposalPk() != null) {
            _hashCode += getProposalPk().hashCode();
        }
        if (getProposalVO() != null) {
            _hashCode += getProposalVO().hashCode();
        }
        if (getProvisionalAllocatedShifts() != null) {
            _hashCode += getProvisionalAllocatedShifts().hashCode();
        }
        if (getRequestedShifts() != null) {
            _hashCode += getRequestedShifts().hashCode();
        }
        if (getSessions() != null) {
            for (int i = 0; i < java.lang.reflect.Array.getLength(getSessions()); i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getSessions(), i);
                if (obj != null && !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.jboss.axis.description.TypeDesc typeDesc = new org.jboss.axis.description.TypeDesc(ExperimentVO.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://SMISServerService.smis.esrf.fr", "ExperimentVO"));
        org.jboss.axis.description.ElementDesc elemField = new org.jboss.axis.description.ElementDesc();
        elemField.setFieldName("MES_UNI_STAT");
        elemField.setXmlName(new javax.xml.namespace.QName("", "MES_UNI_STAT"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "int"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.jboss.axis.description.ElementDesc();
        elemField.setFieldName("allocatedShifts");
        elemField.setXmlName(new javax.xml.namespace.QName("", "allocatedShifts"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "int"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.jboss.axis.description.ElementDesc();
        elemField.setFieldName("automaticCreation");
        elemField.setXmlName(new javax.xml.namespace.QName("", "automaticCreation"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "boolean"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.jboss.axis.description.ElementDesc();
        elemField.setFieldName("beamlinePk");
        elemField.setXmlName(new javax.xml.namespace.QName("", "beamlinePk"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "long"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.jboss.axis.description.ElementDesc();
        elemField.setFieldName("beamlineVO");
        elemField.setXmlName(new javax.xml.namespace.QName("", "beamlineVO"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://SMISServerService.smis.esrf.fr", "BeamlineVO"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.jboss.axis.description.ElementDesc();
        elemField.setFieldName("experimentMonth");
        elemField.setXmlName(new javax.xml.namespace.QName("", "experimentMonth"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "int"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.jboss.axis.description.ElementDesc();
        elemField.setFieldName("experimentYear");
        elemField.setXmlName(new javax.xml.namespace.QName("", "experimentYear"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "int"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.jboss.axis.description.ElementDesc();
        elemField.setFieldName("pk");
        elemField.setXmlName(new javax.xml.namespace.QName("", "pk"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "long"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.jboss.axis.description.ElementDesc();
        elemField.setFieldName("plannedShifts");
        elemField.setXmlName(new javax.xml.namespace.QName("", "plannedShifts"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "int"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.jboss.axis.description.ElementDesc();
        elemField.setFieldName("proposalPk");
        elemField.setXmlName(new javax.xml.namespace.QName("", "proposalPk"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "long"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.jboss.axis.description.ElementDesc();
        elemField.setFieldName("proposalVO");
        elemField.setXmlName(new javax.xml.namespace.QName("", "proposalVO"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://SMISServerService.smis.esrf.fr", "ProposalVO"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.jboss.axis.description.ElementDesc();
        elemField.setFieldName("provisionalAllocatedShifts");
        elemField.setXmlName(new javax.xml.namespace.QName("", "provisionalAllocatedShifts"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "int"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.jboss.axis.description.ElementDesc();
        elemField.setFieldName("requestedShifts");
        elemField.setXmlName(new javax.xml.namespace.QName("", "requestedShifts"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "int"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.jboss.axis.description.ElementDesc();
        elemField.setFieldName("sessions");
        elemField.setXmlName(new javax.xml.namespace.QName("", "sessions"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://SMISServerService.smis.esrf.fr", "ExpSessionVO"));
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.jboss.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.jboss.axis.encoding.Serializer getSerializer(java.lang.String mechType, java.lang.Class _javaType, javax.xml.namespace.QName _xmlType) {
        return new org.jboss.axis.encoding.ser.BeanSerializer(_javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.jboss.axis.encoding.Deserializer getDeserializer(java.lang.String mechType, java.lang.Class _javaType, javax.xml.namespace.QName _xmlType) {
        return new org.jboss.axis.encoding.ser.BeanDeserializer(_javaType, _xmlType, typeDesc);
    }
}

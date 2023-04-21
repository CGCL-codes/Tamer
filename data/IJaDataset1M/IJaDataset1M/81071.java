package ispyb.server.webservice.smis;

public class ProposalGradingVO extends fr.esrf.smis.vos.SMISValueObject implements java.io.Serializable {

    private java.lang.String comment;

    private java.lang.Float mark;

    private java.lang.Long pk;

    private java.lang.Long proposalPk;

    private ispyb.server.webservice.smis.ProposalVO proposalVO;

    private java.lang.Long reviewerPk;

    private ispyb.server.webservice.smis.ScientistVO reviewerVO;

    public ProposalGradingVO() {
    }

    public java.lang.String getComment() {
        return comment;
    }

    public void setComment(java.lang.String comment) {
        this.comment = comment;
    }

    public java.lang.Float getMark() {
        return mark;
    }

    public void setMark(java.lang.Float mark) {
        this.mark = mark;
    }

    public java.lang.Long getPk() {
        return pk;
    }

    public void setPk(java.lang.Long pk) {
        this.pk = pk;
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

    public java.lang.Long getReviewerPk() {
        return reviewerPk;
    }

    public void setReviewerPk(java.lang.Long reviewerPk) {
        this.reviewerPk = reviewerPk;
    }

    public ispyb.server.webservice.smis.ScientistVO getReviewerVO() {
        return reviewerVO;
    }

    public void setReviewerVO(ispyb.server.webservice.smis.ScientistVO reviewerVO) {
        this.reviewerVO = reviewerVO;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ProposalGradingVO)) return false;
        ProposalGradingVO other = (ProposalGradingVO) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && ((this.comment == null && other.getComment() == null) || (this.comment != null && this.comment.equals(other.getComment()))) && ((this.mark == null && other.getMark() == null) || (this.mark != null && this.mark.equals(other.getMark()))) && ((this.pk == null && other.getPk() == null) || (this.pk != null && this.pk.equals(other.getPk()))) && ((this.proposalPk == null && other.getProposalPk() == null) || (this.proposalPk != null && this.proposalPk.equals(other.getProposalPk()))) && ((this.proposalVO == null && other.getProposalVO() == null) || (this.proposalVO != null && this.proposalVO.equals(other.getProposalVO()))) && ((this.reviewerPk == null && other.getReviewerPk() == null) || (this.reviewerPk != null && this.reviewerPk.equals(other.getReviewerPk()))) && ((this.reviewerVO == null && other.getReviewerVO() == null) || (this.reviewerVO != null && this.reviewerVO.equals(other.getReviewerVO())));
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
        if (getComment() != null) {
            _hashCode += getComment().hashCode();
        }
        if (getMark() != null) {
            _hashCode += getMark().hashCode();
        }
        if (getPk() != null) {
            _hashCode += getPk().hashCode();
        }
        if (getProposalPk() != null) {
            _hashCode += getProposalPk().hashCode();
        }
        if (getProposalVO() != null) {
            _hashCode += getProposalVO().hashCode();
        }
        if (getReviewerPk() != null) {
            _hashCode += getReviewerPk().hashCode();
        }
        if (getReviewerVO() != null) {
            _hashCode += getReviewerVO().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.jboss.axis.description.TypeDesc typeDesc = new org.jboss.axis.description.TypeDesc(ProposalGradingVO.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://SMISServerService.smis.esrf.fr", "ProposalGradingVO"));
        org.jboss.axis.description.ElementDesc elemField = new org.jboss.axis.description.ElementDesc();
        elemField.setFieldName("comment");
        elemField.setXmlName(new javax.xml.namespace.QName("", "comment"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.jboss.axis.description.ElementDesc();
        elemField.setFieldName("mark");
        elemField.setXmlName(new javax.xml.namespace.QName("", "mark"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "float"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.jboss.axis.description.ElementDesc();
        elemField.setFieldName("pk");
        elemField.setXmlName(new javax.xml.namespace.QName("", "pk"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "long"));
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
        elemField.setFieldName("reviewerPk");
        elemField.setXmlName(new javax.xml.namespace.QName("", "reviewerPk"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "long"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.jboss.axis.description.ElementDesc();
        elemField.setFieldName("reviewerVO");
        elemField.setXmlName(new javax.xml.namespace.QName("", "reviewerVO"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://SMISServerService.smis.esrf.fr", "ScientistVO"));
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

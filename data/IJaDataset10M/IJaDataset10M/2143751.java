package cn.myapps.webservice.gmcczj.tempurl;

public class MessageDetail implements java.io.Serializable {

    private java.lang.String sendMsgID;

    private java.lang.String fromSip;

    private java.lang.String fromMobile;

    private java.lang.String content;

    private java.util.Calendar replyTime;

    private int status;

    public MessageDetail() {
    }

    public MessageDetail(java.lang.String sendMsgID, java.lang.String fromSip, java.lang.String fromMobile, java.lang.String content, java.util.Calendar replyTime, int status) {
        this.sendMsgID = sendMsgID;
        this.fromSip = fromSip;
        this.fromMobile = fromMobile;
        this.content = content;
        this.replyTime = replyTime;
        this.status = status;
    }

    /**
     * Gets the sendMsgID value for this MessageDetail.
     * 
     * @return sendMsgID
     */
    public java.lang.String getSendMsgID() {
        return sendMsgID;
    }

    /**
     * Sets the sendMsgID value for this MessageDetail.
     * 
     * @param sendMsgID
     */
    public void setSendMsgID(java.lang.String sendMsgID) {
        this.sendMsgID = sendMsgID;
    }

    /**
     * Gets the fromSip value for this MessageDetail.
     * 
     * @return fromSip
     */
    public java.lang.String getFromSip() {
        return fromSip;
    }

    /**
     * Sets the fromSip value for this MessageDetail.
     * 
     * @param fromSip
     */
    public void setFromSip(java.lang.String fromSip) {
        this.fromSip = fromSip;
    }

    /**
     * Gets the fromMobile value for this MessageDetail.
     * 
     * @return fromMobile
     */
    public java.lang.String getFromMobile() {
        return fromMobile;
    }

    /**
     * Sets the fromMobile value for this MessageDetail.
     * 
     * @param fromMobile
     */
    public void setFromMobile(java.lang.String fromMobile) {
        this.fromMobile = fromMobile;
    }

    /**
     * Gets the content value for this MessageDetail.
     * 
     * @return content
     */
    public java.lang.String getContent() {
        return content;
    }

    /**
     * Sets the content value for this MessageDetail.
     * 
     * @param content
     */
    public void setContent(java.lang.String content) {
        this.content = content;
    }

    /**
     * Gets the replyTime value for this MessageDetail.
     * 
     * @return replyTime
     */
    public java.util.Calendar getReplyTime() {
        return replyTime;
    }

    /**
     * Sets the replyTime value for this MessageDetail.
     * 
     * @param replyTime
     */
    public void setReplyTime(java.util.Calendar replyTime) {
        this.replyTime = replyTime;
    }

    /**
     * Gets the status value for this MessageDetail.
     * 
     * @return status
     */
    public int getStatus() {
        return status;
    }

    /**
     * Sets the status value for this MessageDetail.
     * 
     * @param status
     */
    public void setStatus(int status) {
        this.status = status;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MessageDetail)) return false;
        MessageDetail other = (MessageDetail) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.sendMsgID == null && other.getSendMsgID() == null) || (this.sendMsgID != null && this.sendMsgID.equals(other.getSendMsgID()))) && ((this.fromSip == null && other.getFromSip() == null) || (this.fromSip != null && this.fromSip.equals(other.getFromSip()))) && ((this.fromMobile == null && other.getFromMobile() == null) || (this.fromMobile != null && this.fromMobile.equals(other.getFromMobile()))) && ((this.content == null && other.getContent() == null) || (this.content != null && this.content.equals(other.getContent()))) && ((this.replyTime == null && other.getReplyTime() == null) || (this.replyTime != null && this.replyTime.equals(other.getReplyTime()))) && this.status == other.getStatus();
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
        if (getSendMsgID() != null) {
            _hashCode += getSendMsgID().hashCode();
        }
        if (getFromSip() != null) {
            _hashCode += getFromSip().hashCode();
        }
        if (getFromMobile() != null) {
            _hashCode += getFromMobile().hashCode();
        }
        if (getContent() != null) {
            _hashCode += getContent().hashCode();
        }
        if (getReplyTime() != null) {
            _hashCode += getReplyTime().hashCode();
        }
        _hashCode += getStatus();
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(MessageDetail.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", "MessageDetail"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sendMsgID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "SendMsgID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fromSip");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "FromSip"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fromMobile");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "FromMobile"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("content");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "Content"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("replyTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "ReplyTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "Status"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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

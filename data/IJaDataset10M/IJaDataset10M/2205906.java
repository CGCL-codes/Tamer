package cn.myapps.webservice.gmcczj.iamsweb.ws;

public class SendSMSInfo implements java.io.Serializable {

    private java.lang.String mobileNO;

    private java.lang.String userid;

    private int smsStatus;

    private java.lang.String smsID;

    private java.lang.String errMsg;

    public SendSMSInfo() {
    }

    public SendSMSInfo(java.lang.String mobileNO, java.lang.String userid, int smsStatus, java.lang.String smsID, java.lang.String errMsg) {
        this.mobileNO = mobileNO;
        this.userid = userid;
        this.smsStatus = smsStatus;
        this.smsID = smsID;
        this.errMsg = errMsg;
    }

    /**
     * Gets the mobileNO value for this SendSMSInfo.
     * 
     * @return mobileNO
     */
    public java.lang.String getMobileNO() {
        return mobileNO;
    }

    /**
     * Sets the mobileNO value for this SendSMSInfo.
     * 
     * @param mobileNO
     */
    public void setMobileNO(java.lang.String mobileNO) {
        this.mobileNO = mobileNO;
    }

    /**
     * Gets the userid value for this SendSMSInfo.
     * 
     * @return userid
     */
    public java.lang.String getUserid() {
        return userid;
    }

    /**
     * Sets the userid value for this SendSMSInfo.
     * 
     * @param userid
     */
    public void setUserid(java.lang.String userid) {
        this.userid = userid;
    }

    /**
     * Gets the smsStatus value for this SendSMSInfo.
     * 
     * @return smsStatus
     */
    public int getSmsStatus() {
        return smsStatus;
    }

    /**
     * Sets the smsStatus value for this SendSMSInfo.
     * 
     * @param smsStatus
     */
    public void setSmsStatus(int smsStatus) {
        this.smsStatus = smsStatus;
    }

    /**
     * Gets the smsID value for this SendSMSInfo.
     * 
     * @return smsID
     */
    public java.lang.String getSmsID() {
        return smsID;
    }

    /**
     * Sets the smsID value for this SendSMSInfo.
     * 
     * @param smsID
     */
    public void setSmsID(java.lang.String smsID) {
        this.smsID = smsID;
    }

    /**
     * Gets the errMsg value for this SendSMSInfo.
     * 
     * @return errMsg
     */
    public java.lang.String getErrMsg() {
        return errMsg;
    }

    /**
     * Sets the errMsg value for this SendSMSInfo.
     * 
     * @param errMsg
     */
    public void setErrMsg(java.lang.String errMsg) {
        this.errMsg = errMsg;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SendSMSInfo)) return false;
        SendSMSInfo other = (SendSMSInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.mobileNO == null && other.getMobileNO() == null) || (this.mobileNO != null && this.mobileNO.equals(other.getMobileNO()))) && ((this.userid == null && other.getUserid() == null) || (this.userid != null && this.userid.equals(other.getUserid()))) && this.smsStatus == other.getSmsStatus() && ((this.smsID == null && other.getSmsID() == null) || (this.smsID != null && this.smsID.equals(other.getSmsID()))) && ((this.errMsg == null && other.getErrMsg() == null) || (this.errMsg != null && this.errMsg.equals(other.getErrMsg())));
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
        if (getMobileNO() != null) {
            _hashCode += getMobileNO().hashCode();
        }
        if (getUserid() != null) {
            _hashCode += getUserid().hashCode();
        }
        _hashCode += getSmsStatus();
        if (getSmsID() != null) {
            _hashCode += getSmsID().hashCode();
        }
        if (getErrMsg() != null) {
            _hashCode += getErrMsg().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(SendSMSInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://iamsweb.gmcc.net/WS/", "SendSMSInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mobileNO");
        elemField.setXmlName(new javax.xml.namespace.QName("http://iamsweb.gmcc.net/WS/", "mobileNO"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userid");
        elemField.setXmlName(new javax.xml.namespace.QName("http://iamsweb.gmcc.net/WS/", "userid"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("smsStatus");
        elemField.setXmlName(new javax.xml.namespace.QName("http://iamsweb.gmcc.net/WS/", "smsStatus"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("smsID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://iamsweb.gmcc.net/WS/", "smsID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("errMsg");
        elemField.setXmlName(new javax.xml.namespace.QName("http://iamsweb.gmcc.net/WS/", "errMsg"));
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

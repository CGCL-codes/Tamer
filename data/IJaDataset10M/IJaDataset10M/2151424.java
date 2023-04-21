package edu.ucsb.cs.spinner.axis2.qbets;

public class MachineType implements java.io.Serializable {

    private java.lang.String machineName;

    private java.lang.String label;

    private java.lang.String[] loginHosts;

    private edu.ucsb.cs.spinner.axis2.qbets.QueueType[] queues;

    public MachineType() {
    }

    public MachineType(java.lang.String machineName, java.lang.String label, java.lang.String[] loginHosts, edu.ucsb.cs.spinner.axis2.qbets.QueueType[] queues) {
        this.machineName = machineName;
        this.label = label;
        this.loginHosts = loginHosts;
        this.queues = queues;
    }

    /**
     * Gets the machineName value for this MachineType.
     * 
     * @return machineName
     */
    public java.lang.String getMachineName() {
        return machineName;
    }

    /**
     * Sets the machineName value for this MachineType.
     * 
     * @param machineName
     */
    public void setMachineName(java.lang.String machineName) {
        this.machineName = machineName;
    }

    /**
     * Gets the label value for this MachineType.
     * 
     * @return label
     */
    public java.lang.String getLabel() {
        return label;
    }

    /**
     * Sets the label value for this MachineType.
     * 
     * @param label
     */
    public void setLabel(java.lang.String label) {
        this.label = label;
    }

    /**
     * Gets the loginHosts value for this MachineType.
     * 
     * @return loginHosts
     */
    public java.lang.String[] getLoginHosts() {
        return loginHosts;
    }

    /**
     * Sets the loginHosts value for this MachineType.
     * 
     * @param loginHosts
     */
    public void setLoginHosts(java.lang.String[] loginHosts) {
        this.loginHosts = loginHosts;
    }

    public java.lang.String getLoginHosts(int i) {
        return this.loginHosts[i];
    }

    public void setLoginHosts(int i, java.lang.String _value) {
        this.loginHosts[i] = _value;
    }

    /**
     * Gets the queues value for this MachineType.
     * 
     * @return queues
     */
    public edu.ucsb.cs.spinner.axis2.qbets.QueueType[] getQueues() {
        return queues;
    }

    /**
     * Sets the queues value for this MachineType.
     * 
     * @param queues
     */
    public void setQueues(edu.ucsb.cs.spinner.axis2.qbets.QueueType[] queues) {
        this.queues = queues;
    }

    public edu.ucsb.cs.spinner.axis2.qbets.QueueType getQueues(int i) {
        return this.queues[i];
    }

    public void setQueues(int i, edu.ucsb.cs.spinner.axis2.qbets.QueueType _value) {
        this.queues[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MachineType)) return false;
        MachineType other = (MachineType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.machineName == null && other.getMachineName() == null) || (this.machineName != null && this.machineName.equals(other.getMachineName()))) && ((this.label == null && other.getLabel() == null) || (this.label != null && this.label.equals(other.getLabel()))) && ((this.loginHosts == null && other.getLoginHosts() == null) || (this.loginHosts != null && java.util.Arrays.equals(this.loginHosts, other.getLoginHosts()))) && ((this.queues == null && other.getQueues() == null) || (this.queues != null && java.util.Arrays.equals(this.queues, other.getQueues())));
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
        if (getMachineName() != null) {
            _hashCode += getMachineName().hashCode();
        }
        if (getLabel() != null) {
            _hashCode += getLabel().hashCode();
        }
        if (getLoginHosts() != null) {
            for (int i = 0; i < java.lang.reflect.Array.getLength(getLoginHosts()); i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getLoginHosts(), i);
                if (obj != null && !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getQueues() != null) {
            for (int i = 0; i < java.lang.reflect.Array.getLength(getQueues()); i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getQueues(), i);
                if (obj != null && !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(MachineType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://spinner.cs.ucsb.edu:9090/axis2/qbets/", "machineType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("machineName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://spinner.cs.ucsb.edu:9090/axis2/qbets/", "machineName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("label");
        elemField.setXmlName(new javax.xml.namespace.QName("http://spinner.cs.ucsb.edu:9090/axis2/qbets/", "label"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("loginHosts");
        elemField.setXmlName(new javax.xml.namespace.QName("http://spinner.cs.ucsb.edu:9090/axis2/qbets/", "loginHosts"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("queues");
        elemField.setXmlName(new javax.xml.namespace.QName("http://spinner.cs.ucsb.edu:9090/axis2/qbets/", "queues"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://spinner.cs.ucsb.edu:9090/axis2/qbets/", "queueType"));
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
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

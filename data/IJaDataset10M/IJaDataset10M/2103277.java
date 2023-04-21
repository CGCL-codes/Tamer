package com.flexiant.extility;

public class FirewallRule implements java.io.Serializable {

    private java.lang.Long firewall_rule_id;

    private java.lang.Long firewall_id;

    private java.lang.Integer sequence_no;

    private java.lang.String rule_name;

    private java.lang.String direction;

    private java.lang.String state;

    private java.lang.Integer firewall_protocol_id;

    private java.lang.Integer local_port;

    private java.lang.Integer remote_port;

    private java.lang.Integer icmp_parameter_id;

    private java.lang.String remote_ipaddress;

    private java.lang.Integer bit_mask;

    private java.lang.String action;

    public FirewallRule() {
    }

    public FirewallRule(java.lang.Long firewall_rule_id, java.lang.Long firewall_id, java.lang.Integer sequence_no, java.lang.String rule_name, java.lang.String direction, java.lang.String state, java.lang.Integer firewall_protocol_id, java.lang.Integer local_port, java.lang.Integer remote_port, java.lang.Integer icmp_parameter_id, java.lang.String remote_ipaddress, java.lang.Integer bit_mask, java.lang.String action) {
        this.firewall_rule_id = firewall_rule_id;
        this.firewall_id = firewall_id;
        this.sequence_no = sequence_no;
        this.rule_name = rule_name;
        this.direction = direction;
        this.state = state;
        this.firewall_protocol_id = firewall_protocol_id;
        this.local_port = local_port;
        this.remote_port = remote_port;
        this.icmp_parameter_id = icmp_parameter_id;
        this.remote_ipaddress = remote_ipaddress;
        this.bit_mask = bit_mask;
        this.action = action;
    }

    /**
     * Gets the firewall_rule_id value for this FirewallRule.
     * 
     * @return firewall_rule_id
     */
    public java.lang.Long getFirewall_rule_id() {
        return firewall_rule_id;
    }

    /**
     * Sets the firewall_rule_id value for this FirewallRule.
     * 
     * @param firewall_rule_id
     */
    public void setFirewall_rule_id(java.lang.Long firewall_rule_id) {
        this.firewall_rule_id = firewall_rule_id;
    }

    /**
     * Gets the firewall_id value for this FirewallRule.
     * 
     * @return firewall_id
     */
    public java.lang.Long getFirewall_id() {
        return firewall_id;
    }

    /**
     * Sets the firewall_id value for this FirewallRule.
     * 
     * @param firewall_id
     */
    public void setFirewall_id(java.lang.Long firewall_id) {
        this.firewall_id = firewall_id;
    }

    /**
     * Gets the sequence_no value for this FirewallRule.
     * 
     * @return sequence_no
     */
    public java.lang.Integer getSequence_no() {
        return sequence_no;
    }

    /**
     * Sets the sequence_no value for this FirewallRule.
     * 
     * @param sequence_no
     */
    public void setSequence_no(java.lang.Integer sequence_no) {
        this.sequence_no = sequence_no;
    }

    /**
     * Gets the rule_name value for this FirewallRule.
     * 
     * @return rule_name
     */
    public java.lang.String getRule_name() {
        return rule_name;
    }

    /**
     * Sets the rule_name value for this FirewallRule.
     * 
     * @param rule_name
     */
    public void setRule_name(java.lang.String rule_name) {
        this.rule_name = rule_name;
    }

    /**
     * Gets the direction value for this FirewallRule.
     * 
     * @return direction
     */
    public java.lang.String getDirection() {
        return direction;
    }

    /**
     * Sets the direction value for this FirewallRule.
     * 
     * @param direction
     */
    public void setDirection(java.lang.String direction) {
        this.direction = direction;
    }

    /**
     * Gets the state value for this FirewallRule.
     * 
     * @return state
     */
    public java.lang.String getState() {
        return state;
    }

    /**
     * Sets the state value for this FirewallRule.
     * 
     * @param state
     */
    public void setState(java.lang.String state) {
        this.state = state;
    }

    /**
     * Gets the firewall_protocol_id value for this FirewallRule.
     * 
     * @return firewall_protocol_id
     */
    public java.lang.Integer getFirewall_protocol_id() {
        return firewall_protocol_id;
    }

    /**
     * Sets the firewall_protocol_id value for this FirewallRule.
     * 
     * @param firewall_protocol_id
     */
    public void setFirewall_protocol_id(java.lang.Integer firewall_protocol_id) {
        this.firewall_protocol_id = firewall_protocol_id;
    }

    /**
     * Gets the local_port value for this FirewallRule.
     * 
     * @return local_port
     */
    public java.lang.Integer getLocal_port() {
        return local_port;
    }

    /**
     * Sets the local_port value for this FirewallRule.
     * 
     * @param local_port
     */
    public void setLocal_port(java.lang.Integer local_port) {
        this.local_port = local_port;
    }

    /**
     * Gets the remote_port value for this FirewallRule.
     * 
     * @return remote_port
     */
    public java.lang.Integer getRemote_port() {
        return remote_port;
    }

    /**
     * Sets the remote_port value for this FirewallRule.
     * 
     * @param remote_port
     */
    public void setRemote_port(java.lang.Integer remote_port) {
        this.remote_port = remote_port;
    }

    /**
     * Gets the icmp_parameter_id value for this FirewallRule.
     * 
     * @return icmp_parameter_id
     */
    public java.lang.Integer getIcmp_parameter_id() {
        return icmp_parameter_id;
    }

    /**
     * Sets the icmp_parameter_id value for this FirewallRule.
     * 
     * @param icmp_parameter_id
     */
    public void setIcmp_parameter_id(java.lang.Integer icmp_parameter_id) {
        this.icmp_parameter_id = icmp_parameter_id;
    }

    /**
     * Gets the remote_ipaddress value for this FirewallRule.
     * 
     * @return remote_ipaddress
     */
    public java.lang.String getRemote_ipaddress() {
        return remote_ipaddress;
    }

    /**
     * Sets the remote_ipaddress value for this FirewallRule.
     * 
     * @param remote_ipaddress
     */
    public void setRemote_ipaddress(java.lang.String remote_ipaddress) {
        this.remote_ipaddress = remote_ipaddress;
    }

    /**
     * Gets the bit_mask value for this FirewallRule.
     * 
     * @return bit_mask
     */
    public java.lang.Integer getBit_mask() {
        return bit_mask;
    }

    /**
     * Sets the bit_mask value for this FirewallRule.
     * 
     * @param bit_mask
     */
    public void setBit_mask(java.lang.Integer bit_mask) {
        this.bit_mask = bit_mask;
    }

    /**
     * Gets the action value for this FirewallRule.
     * 
     * @return action
     */
    public java.lang.String getAction() {
        return action;
    }

    /**
     * Sets the action value for this FirewallRule.
     * 
     * @param action
     */
    public void setAction(java.lang.String action) {
        this.action = action;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof FirewallRule)) return false;
        FirewallRule other = (FirewallRule) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.firewall_rule_id == null && other.getFirewall_rule_id() == null) || (this.firewall_rule_id != null && this.firewall_rule_id.equals(other.getFirewall_rule_id()))) && ((this.firewall_id == null && other.getFirewall_id() == null) || (this.firewall_id != null && this.firewall_id.equals(other.getFirewall_id()))) && ((this.sequence_no == null && other.getSequence_no() == null) || (this.sequence_no != null && this.sequence_no.equals(other.getSequence_no()))) && ((this.rule_name == null && other.getRule_name() == null) || (this.rule_name != null && this.rule_name.equals(other.getRule_name()))) && ((this.direction == null && other.getDirection() == null) || (this.direction != null && this.direction.equals(other.getDirection()))) && ((this.state == null && other.getState() == null) || (this.state != null && this.state.equals(other.getState()))) && ((this.firewall_protocol_id == null && other.getFirewall_protocol_id() == null) || (this.firewall_protocol_id != null && this.firewall_protocol_id.equals(other.getFirewall_protocol_id()))) && ((this.local_port == null && other.getLocal_port() == null) || (this.local_port != null && this.local_port.equals(other.getLocal_port()))) && ((this.remote_port == null && other.getRemote_port() == null) || (this.remote_port != null && this.remote_port.equals(other.getRemote_port()))) && ((this.icmp_parameter_id == null && other.getIcmp_parameter_id() == null) || (this.icmp_parameter_id != null && this.icmp_parameter_id.equals(other.getIcmp_parameter_id()))) && ((this.remote_ipaddress == null && other.getRemote_ipaddress() == null) || (this.remote_ipaddress != null && this.remote_ipaddress.equals(other.getRemote_ipaddress()))) && ((this.bit_mask == null && other.getBit_mask() == null) || (this.bit_mask != null && this.bit_mask.equals(other.getBit_mask()))) && ((this.action == null && other.getAction() == null) || (this.action != null && this.action.equals(other.getAction())));
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
        if (getFirewall_rule_id() != null) {
            _hashCode += getFirewall_rule_id().hashCode();
        }
        if (getFirewall_id() != null) {
            _hashCode += getFirewall_id().hashCode();
        }
        if (getSequence_no() != null) {
            _hashCode += getSequence_no().hashCode();
        }
        if (getRule_name() != null) {
            _hashCode += getRule_name().hashCode();
        }
        if (getDirection() != null) {
            _hashCode += getDirection().hashCode();
        }
        if (getState() != null) {
            _hashCode += getState().hashCode();
        }
        if (getFirewall_protocol_id() != null) {
            _hashCode += getFirewall_protocol_id().hashCode();
        }
        if (getLocal_port() != null) {
            _hashCode += getLocal_port().hashCode();
        }
        if (getRemote_port() != null) {
            _hashCode += getRemote_port().hashCode();
        }
        if (getIcmp_parameter_id() != null) {
            _hashCode += getIcmp_parameter_id().hashCode();
        }
        if (getRemote_ipaddress() != null) {
            _hashCode += getRemote_ipaddress().hashCode();
        }
        if (getBit_mask() != null) {
            _hashCode += getBit_mask().hashCode();
        }
        if (getAction() != null) {
            _hashCode += getAction().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(FirewallRule.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://extility.flexiant.com", "FirewallRule"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("firewall_rule_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "firewall_rule_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("firewall_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "firewall_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sequence_no");
        elemField.setXmlName(new javax.xml.namespace.QName("", "sequence_no"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rule_name");
        elemField.setXmlName(new javax.xml.namespace.QName("", "rule_name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("direction");
        elemField.setXmlName(new javax.xml.namespace.QName("", "direction"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("state");
        elemField.setXmlName(new javax.xml.namespace.QName("", "state"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("firewall_protocol_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "firewall_protocol_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("local_port");
        elemField.setXmlName(new javax.xml.namespace.QName("", "local_port"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("remote_port");
        elemField.setXmlName(new javax.xml.namespace.QName("", "remote_port"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("icmp_parameter_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "icmp_parameter_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("remote_ipaddress");
        elemField.setXmlName(new javax.xml.namespace.QName("", "remote_ipaddress"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bit_mask");
        elemField.setXmlName(new javax.xml.namespace.QName("", "bit_mask"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("action");
        elemField.setXmlName(new javax.xml.namespace.QName("", "action"));
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

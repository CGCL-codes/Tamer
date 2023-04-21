package com.windsor.node.service.helper.schematron;

public class ValidatorLocator extends org.apache.axis.client.Service implements com.windsor.node.service.helper.schematron.Validator {

    private static final long serialVersionUID = 1;

    /**
     * A set of xml validattion services for the National Environmental
     * Information Exchange Network (NEIEN)
     */
    public ValidatorLocator() {
    }

    public ValidatorLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ValidatorLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    private java.lang.String ValidatorPortType_address = "https://tools.epacdxnode.net/xml/validator.wsdl";

    public java.lang.String getValidatorPortTypeAddress() {
        return ValidatorPortType_address;
    }

    private java.lang.String ValidatorPortTypeWSDDServiceName = "ValidatorPortType";

    public java.lang.String getValidatorPortTypeWSDDServiceName() {
        return ValidatorPortTypeWSDDServiceName;
    }

    public void setValidatorPortTypeWSDDServiceName(java.lang.String name) {
        ValidatorPortTypeWSDDServiceName = name;
    }

    public com.windsor.node.service.helper.schematron.ValidatorPortType_PortType getValidatorPortType() throws javax.xml.rpc.ServiceException {
        java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ValidatorPortType_address);
        } catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getValidatorPortType(endpoint);
    }

    public com.windsor.node.service.helper.schematron.ValidatorPortType_PortType getValidatorPortType(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.windsor.node.service.helper.schematron.ValidatorBindingStub _stub = new com.windsor.node.service.helper.schematron.ValidatorBindingStub(portAddress, this);
            _stub.setPortName(getValidatorPortTypeWSDDServiceName());
            return _stub;
        } catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setValidatorPortTypeEndpointAddress(java.lang.String address) {
        ValidatorPortType_address = address;
    }

    /**
     * For the given interface, get the stub implementation. If this service has
     * no port for the given interface, then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.windsor.node.service.helper.schematron.ValidatorPortType_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.windsor.node.service.helper.schematron.ValidatorBindingStub _stub = new com.windsor.node.service.helper.schematron.ValidatorBindingStub(new java.net.URL(ValidatorPortType_address), this);
                _stub.setPortName(getValidatorPortTypeWSDDServiceName());
                return _stub;
            }
        } catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation. If this service has
     * no port for the given interface, then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("ValidatorPortType".equals(inputPortName)) {
            return getValidatorPortType();
        } else {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.neien.org/schema/v1.0/validator.wsdl", "Validator");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.neien.org/schema/v1.0/validator.wsdl", "ValidatorPortType"));
        }
        return ports.iterator();
    }

    /**
     * Set the endpoint address for the specified port name.
     */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        if ("ValidatorPortType".equals(portName)) {
            setValidatorPortTypeEndpointAddress(address);
        } else {
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
     * Set the endpoint address for the specified port name.
     */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }
}

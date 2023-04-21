package com.siebel.asi;

public class IdentityActivate_ServiceLocator extends org.apache.axis.client.Service implements com.siebel.asi.IdentityActivate_Service {

    public IdentityActivate_ServiceLocator() {
    }

    public IdentityActivate_ServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public IdentityActivate_ServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    private java.lang.String IdentityActivate_address = "http://192.168.15.213:8080/eai_chs/start.swe?SWEExtSource=WebService&SWEExtCmd=Execute&UserName=GUESTEAI&Password=GUESTEAI";

    public java.lang.String getIdentityActivateAddress() {
        return IdentityActivate_address;
    }

    private java.lang.String IdentityActivateWSDDServiceName = "IdentityActivate";

    public java.lang.String getIdentityActivateWSDDServiceName() {
        return IdentityActivateWSDDServiceName;
    }

    public void setIdentityActivateWSDDServiceName(java.lang.String name) {
        IdentityActivateWSDDServiceName = name;
    }

    public com.siebel.asi.IdentityActivate_PortType getIdentityActivate() throws javax.xml.rpc.ServiceException {
        java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(IdentityActivate_address);
        } catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getIdentityActivate(endpoint);
    }

    public com.siebel.asi.IdentityActivate_PortType getIdentityActivate(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.siebel.asi.IdentityActivate_BindingStub _stub = new com.siebel.asi.IdentityActivate_BindingStub(portAddress, this);
            _stub.setPortName(getIdentityActivateWSDDServiceName());
            return _stub;
        } catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setIdentityActivateEndpointAddress(java.lang.String address) {
        IdentityActivate_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.siebel.asi.IdentityActivate_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.siebel.asi.IdentityActivate_BindingStub _stub = new com.siebel.asi.IdentityActivate_BindingStub(new java.net.URL(IdentityActivate_address), this);
                _stub.setPortName(getIdentityActivateWSDDServiceName());
                return _stub;
            }
        } catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("IdentityActivate".equals(inputPortName)) {
            return getIdentityActivate();
        } else {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://siebel.com/asi/", "IdentityActivate");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://siebel.com/asi/", "IdentityActivate"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        if ("IdentityActivate".equals(portName)) {
            setIdentityActivateEndpointAddress(address);
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

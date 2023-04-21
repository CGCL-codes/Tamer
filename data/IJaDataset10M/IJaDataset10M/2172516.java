package com.google.api.adwords.v200909.cm;

public class AdGroupAdServiceLocator extends org.apache.axis.client.Service implements com.google.api.adwords.v200909.cm.AdGroupAdService {

    public AdGroupAdServiceLocator() {
    }

    public AdGroupAdServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public AdGroupAdServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    private java.lang.String AdGroupAdServiceInterfacePort_address = "https://adwords.google.com/api/adwords/cm/v200909/AdGroupAdService";

    public java.lang.String getAdGroupAdServiceInterfacePortAddress() {
        return AdGroupAdServiceInterfacePort_address;
    }

    private java.lang.String AdGroupAdServiceInterfacePortWSDDServiceName = "AdGroupAdServiceInterfacePort";

    public java.lang.String getAdGroupAdServiceInterfacePortWSDDServiceName() {
        return AdGroupAdServiceInterfacePortWSDDServiceName;
    }

    public void setAdGroupAdServiceInterfacePortWSDDServiceName(java.lang.String name) {
        AdGroupAdServiceInterfacePortWSDDServiceName = name;
    }

    public com.google.api.adwords.v200909.cm.AdGroupAdServiceInterface getAdGroupAdServiceInterfacePort() throws javax.xml.rpc.ServiceException {
        java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(AdGroupAdServiceInterfacePort_address);
        } catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getAdGroupAdServiceInterfacePort(endpoint);
    }

    public com.google.api.adwords.v200909.cm.AdGroupAdServiceInterface getAdGroupAdServiceInterfacePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.google.api.adwords.v200909.cm.AdGroupAdServiceSoapBindingStub _stub = new com.google.api.adwords.v200909.cm.AdGroupAdServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getAdGroupAdServiceInterfacePortWSDDServiceName());
            return _stub;
        } catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setAdGroupAdServiceInterfacePortEndpointAddress(java.lang.String address) {
        AdGroupAdServiceInterfacePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.google.api.adwords.v200909.cm.AdGroupAdServiceInterface.class.isAssignableFrom(serviceEndpointInterface)) {
                com.google.api.adwords.v200909.cm.AdGroupAdServiceSoapBindingStub _stub = new com.google.api.adwords.v200909.cm.AdGroupAdServiceSoapBindingStub(new java.net.URL(AdGroupAdServiceInterfacePort_address), this);
                _stub.setPortName(getAdGroupAdServiceInterfacePortWSDDServiceName());
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
        if ("AdGroupAdServiceInterfacePort".equals(inputPortName)) {
            return getAdGroupAdServiceInterfacePort();
        } else {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "AdGroupAdService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "AdGroupAdServiceInterfacePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        if ("AdGroupAdServiceInterfacePort".equals(portName)) {
            setAdGroupAdServiceInterfacePortEndpointAddress(address);
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

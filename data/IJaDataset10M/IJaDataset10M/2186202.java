package com.google.api.adwords.v201003.cm;

public class BidLandscapeServiceLocator extends org.apache.axis.client.Service implements com.google.api.adwords.v201003.cm.BidLandscapeService {

    public BidLandscapeServiceLocator() {
    }

    public BidLandscapeServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public BidLandscapeServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    private java.lang.String BidLandscapeServiceInterfacePort_address = "https://adwords.google.com/api/adwords/cm/v201003/BidLandscapeService";

    public java.lang.String getBidLandscapeServiceInterfacePortAddress() {
        return BidLandscapeServiceInterfacePort_address;
    }

    private java.lang.String BidLandscapeServiceInterfacePortWSDDServiceName = "BidLandscapeServiceInterfacePort";

    public java.lang.String getBidLandscapeServiceInterfacePortWSDDServiceName() {
        return BidLandscapeServiceInterfacePortWSDDServiceName;
    }

    public void setBidLandscapeServiceInterfacePortWSDDServiceName(java.lang.String name) {
        BidLandscapeServiceInterfacePortWSDDServiceName = name;
    }

    public com.google.api.adwords.v201003.cm.BidLandscapeServiceInterface getBidLandscapeServiceInterfacePort() throws javax.xml.rpc.ServiceException {
        java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(BidLandscapeServiceInterfacePort_address);
        } catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getBidLandscapeServiceInterfacePort(endpoint);
    }

    public com.google.api.adwords.v201003.cm.BidLandscapeServiceInterface getBidLandscapeServiceInterfacePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.google.api.adwords.v201003.cm.BidLandscapeServiceSoapBindingStub _stub = new com.google.api.adwords.v201003.cm.BidLandscapeServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getBidLandscapeServiceInterfacePortWSDDServiceName());
            return _stub;
        } catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setBidLandscapeServiceInterfacePortEndpointAddress(java.lang.String address) {
        BidLandscapeServiceInterfacePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.google.api.adwords.v201003.cm.BidLandscapeServiceInterface.class.isAssignableFrom(serviceEndpointInterface)) {
                com.google.api.adwords.v201003.cm.BidLandscapeServiceSoapBindingStub _stub = new com.google.api.adwords.v201003.cm.BidLandscapeServiceSoapBindingStub(new java.net.URL(BidLandscapeServiceInterfacePort_address), this);
                _stub.setPortName(getBidLandscapeServiceInterfacePortWSDDServiceName());
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
        if ("BidLandscapeServiceInterfacePort".equals(inputPortName)) {
            return getBidLandscapeServiceInterfacePort();
        } else {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "BidLandscapeService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "BidLandscapeServiceInterfacePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        if ("BidLandscapeServiceInterfacePort".equals(portName)) {
            setBidLandscapeServiceInterfacePortEndpointAddress(address);
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

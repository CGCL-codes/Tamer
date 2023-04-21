package com.google.api.ads.dfp.v201201;

public class PlacementServiceLocator extends org.apache.axis.client.Service implements com.google.api.ads.dfp.v201201.PlacementService {

    public PlacementServiceLocator() {
    }

    public PlacementServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public PlacementServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    private java.lang.String PlacementServiceInterfacePort_address = "https://www.google.com/apis/ads/publisher/v201201/PlacementService";

    public java.lang.String getPlacementServiceInterfacePortAddress() {
        return PlacementServiceInterfacePort_address;
    }

    private java.lang.String PlacementServiceInterfacePortWSDDServiceName = "PlacementServiceInterfacePort";

    public java.lang.String getPlacementServiceInterfacePortWSDDServiceName() {
        return PlacementServiceInterfacePortWSDDServiceName;
    }

    public void setPlacementServiceInterfacePortWSDDServiceName(java.lang.String name) {
        PlacementServiceInterfacePortWSDDServiceName = name;
    }

    public com.google.api.ads.dfp.v201201.PlacementServiceInterface getPlacementServiceInterfacePort() throws javax.xml.rpc.ServiceException {
        java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(PlacementServiceInterfacePort_address);
        } catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getPlacementServiceInterfacePort(endpoint);
    }

    public com.google.api.ads.dfp.v201201.PlacementServiceInterface getPlacementServiceInterfacePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.google.api.ads.dfp.v201201.PlacementServiceSoapBindingStub _stub = new com.google.api.ads.dfp.v201201.PlacementServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getPlacementServiceInterfacePortWSDDServiceName());
            return _stub;
        } catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setPlacementServiceInterfacePortEndpointAddress(java.lang.String address) {
        PlacementServiceInterfacePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.google.api.ads.dfp.v201201.PlacementServiceInterface.class.isAssignableFrom(serviceEndpointInterface)) {
                com.google.api.ads.dfp.v201201.PlacementServiceSoapBindingStub _stub = new com.google.api.ads.dfp.v201201.PlacementServiceSoapBindingStub(new java.net.URL(PlacementServiceInterfacePort_address), this);
                _stub.setPortName(getPlacementServiceInterfacePortWSDDServiceName());
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
        if ("PlacementServiceInterfacePort".equals(inputPortName)) {
            return getPlacementServiceInterfacePort();
        } else {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201201", "PlacementService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201201", "PlacementServiceInterfacePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        if ("PlacementServiceInterfacePort".equals(portName)) {
            setPlacementServiceInterfacePortEndpointAddress(address);
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

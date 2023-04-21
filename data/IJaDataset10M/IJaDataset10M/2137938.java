package com.google.api.ads.dfp.v201203;

public class CreativeTemplateServiceLocator extends org.apache.axis.client.Service implements com.google.api.ads.dfp.v201203.CreativeTemplateService {

    public CreativeTemplateServiceLocator() {
    }

    public CreativeTemplateServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public CreativeTemplateServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    private java.lang.String CreativeTemplateServiceInterfacePort_address = "https://www.google.com/apis/ads/publisher/v201203/CreativeTemplateService";

    public java.lang.String getCreativeTemplateServiceInterfacePortAddress() {
        return CreativeTemplateServiceInterfacePort_address;
    }

    private java.lang.String CreativeTemplateServiceInterfacePortWSDDServiceName = "CreativeTemplateServiceInterfacePort";

    public java.lang.String getCreativeTemplateServiceInterfacePortWSDDServiceName() {
        return CreativeTemplateServiceInterfacePortWSDDServiceName;
    }

    public void setCreativeTemplateServiceInterfacePortWSDDServiceName(java.lang.String name) {
        CreativeTemplateServiceInterfacePortWSDDServiceName = name;
    }

    public com.google.api.ads.dfp.v201203.CreativeTemplateServiceInterface getCreativeTemplateServiceInterfacePort() throws javax.xml.rpc.ServiceException {
        java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(CreativeTemplateServiceInterfacePort_address);
        } catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getCreativeTemplateServiceInterfacePort(endpoint);
    }

    public com.google.api.ads.dfp.v201203.CreativeTemplateServiceInterface getCreativeTemplateServiceInterfacePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.google.api.ads.dfp.v201203.CreativeTemplateServiceSoapBindingStub _stub = new com.google.api.ads.dfp.v201203.CreativeTemplateServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getCreativeTemplateServiceInterfacePortWSDDServiceName());
            return _stub;
        } catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setCreativeTemplateServiceInterfacePortEndpointAddress(java.lang.String address) {
        CreativeTemplateServiceInterfacePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.google.api.ads.dfp.v201203.CreativeTemplateServiceInterface.class.isAssignableFrom(serviceEndpointInterface)) {
                com.google.api.ads.dfp.v201203.CreativeTemplateServiceSoapBindingStub _stub = new com.google.api.ads.dfp.v201203.CreativeTemplateServiceSoapBindingStub(new java.net.URL(CreativeTemplateServiceInterfacePort_address), this);
                _stub.setPortName(getCreativeTemplateServiceInterfacePortWSDDServiceName());
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
        if ("CreativeTemplateServiceInterfacePort".equals(inputPortName)) {
            return getCreativeTemplateServiceInterfacePort();
        } else {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "CreativeTemplateService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "CreativeTemplateServiceInterfacePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        if ("CreativeTemplateServiceInterfacePort".equals(portName)) {
            setCreativeTemplateServiceInterfacePortEndpointAddress(address);
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

package com.google.api.ads.dfp.v201203;

public class LabelServiceLocator extends org.apache.axis.client.Service implements com.google.api.ads.dfp.v201203.LabelService {

    public LabelServiceLocator() {
    }

    public LabelServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public LabelServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    private java.lang.String LabelServiceInterfacePort_address = "https://www.google.com/apis/ads/publisher/v201203/LabelService";

    public java.lang.String getLabelServiceInterfacePortAddress() {
        return LabelServiceInterfacePort_address;
    }

    private java.lang.String LabelServiceInterfacePortWSDDServiceName = "LabelServiceInterfacePort";

    public java.lang.String getLabelServiceInterfacePortWSDDServiceName() {
        return LabelServiceInterfacePortWSDDServiceName;
    }

    public void setLabelServiceInterfacePortWSDDServiceName(java.lang.String name) {
        LabelServiceInterfacePortWSDDServiceName = name;
    }

    public com.google.api.ads.dfp.v201203.LabelServiceInterface getLabelServiceInterfacePort() throws javax.xml.rpc.ServiceException {
        java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(LabelServiceInterfacePort_address);
        } catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getLabelServiceInterfacePort(endpoint);
    }

    public com.google.api.ads.dfp.v201203.LabelServiceInterface getLabelServiceInterfacePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.google.api.ads.dfp.v201203.LabelServiceSoapBindingStub _stub = new com.google.api.ads.dfp.v201203.LabelServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getLabelServiceInterfacePortWSDDServiceName());
            return _stub;
        } catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setLabelServiceInterfacePortEndpointAddress(java.lang.String address) {
        LabelServiceInterfacePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.google.api.ads.dfp.v201203.LabelServiceInterface.class.isAssignableFrom(serviceEndpointInterface)) {
                com.google.api.ads.dfp.v201203.LabelServiceSoapBindingStub _stub = new com.google.api.ads.dfp.v201203.LabelServiceSoapBindingStub(new java.net.URL(LabelServiceInterfacePort_address), this);
                _stub.setPortName(getLabelServiceInterfacePortWSDDServiceName());
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
        if ("LabelServiceInterfacePort".equals(inputPortName)) {
            return getLabelServiceInterfacePort();
        } else {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "LabelService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201203", "LabelServiceInterfacePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        if ("LabelServiceInterfacePort".equals(portName)) {
            setLabelServiceInterfacePortEndpointAddress(address);
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

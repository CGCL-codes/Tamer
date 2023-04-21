package com.google.api.ads.dfp.v201111;

public class UserServiceLocator extends org.apache.axis.client.Service implements com.google.api.ads.dfp.v201111.UserService {

    public UserServiceLocator() {
    }

    public UserServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public UserServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    private java.lang.String UserServiceInterfacePort_address = "https://www.google.com/apis/ads/publisher/v201111/UserService";

    public java.lang.String getUserServiceInterfacePortAddress() {
        return UserServiceInterfacePort_address;
    }

    private java.lang.String UserServiceInterfacePortWSDDServiceName = "UserServiceInterfacePort";

    public java.lang.String getUserServiceInterfacePortWSDDServiceName() {
        return UserServiceInterfacePortWSDDServiceName;
    }

    public void setUserServiceInterfacePortWSDDServiceName(java.lang.String name) {
        UserServiceInterfacePortWSDDServiceName = name;
    }

    public com.google.api.ads.dfp.v201111.UserServiceInterface getUserServiceInterfacePort() throws javax.xml.rpc.ServiceException {
        java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(UserServiceInterfacePort_address);
        } catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getUserServiceInterfacePort(endpoint);
    }

    public com.google.api.ads.dfp.v201111.UserServiceInterface getUserServiceInterfacePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.google.api.ads.dfp.v201111.UserServiceSoapBindingStub _stub = new com.google.api.ads.dfp.v201111.UserServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getUserServiceInterfacePortWSDDServiceName());
            return _stub;
        } catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setUserServiceInterfacePortEndpointAddress(java.lang.String address) {
        UserServiceInterfacePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.google.api.ads.dfp.v201111.UserServiceInterface.class.isAssignableFrom(serviceEndpointInterface)) {
                com.google.api.ads.dfp.v201111.UserServiceSoapBindingStub _stub = new com.google.api.ads.dfp.v201111.UserServiceSoapBindingStub(new java.net.URL(UserServiceInterfacePort_address), this);
                _stub.setPortName(getUserServiceInterfacePortWSDDServiceName());
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
        if ("UserServiceInterfacePort".equals(inputPortName)) {
            return getUserServiceInterfacePort();
        } else {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201111", "UserService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201111", "UserServiceInterfacePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        if ("UserServiceInterfacePort".equals(portName)) {
            setUserServiceInterfacePortEndpointAddress(address);
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

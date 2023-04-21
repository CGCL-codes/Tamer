package com.google.api.ads.dfp.v201111;

public class OrderServiceLocator extends org.apache.axis.client.Service implements com.google.api.ads.dfp.v201111.OrderService {

    public OrderServiceLocator() {
    }

    public OrderServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public OrderServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    private java.lang.String OrderServiceInterfacePort_address = "https://www.google.com/apis/ads/publisher/v201111/OrderService";

    public java.lang.String getOrderServiceInterfacePortAddress() {
        return OrderServiceInterfacePort_address;
    }

    private java.lang.String OrderServiceInterfacePortWSDDServiceName = "OrderServiceInterfacePort";

    public java.lang.String getOrderServiceInterfacePortWSDDServiceName() {
        return OrderServiceInterfacePortWSDDServiceName;
    }

    public void setOrderServiceInterfacePortWSDDServiceName(java.lang.String name) {
        OrderServiceInterfacePortWSDDServiceName = name;
    }

    public com.google.api.ads.dfp.v201111.OrderServiceInterface getOrderServiceInterfacePort() throws javax.xml.rpc.ServiceException {
        java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(OrderServiceInterfacePort_address);
        } catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getOrderServiceInterfacePort(endpoint);
    }

    public com.google.api.ads.dfp.v201111.OrderServiceInterface getOrderServiceInterfacePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.google.api.ads.dfp.v201111.OrderServiceSoapBindingStub _stub = new com.google.api.ads.dfp.v201111.OrderServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getOrderServiceInterfacePortWSDDServiceName());
            return _stub;
        } catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setOrderServiceInterfacePortEndpointAddress(java.lang.String address) {
        OrderServiceInterfacePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.google.api.ads.dfp.v201111.OrderServiceInterface.class.isAssignableFrom(serviceEndpointInterface)) {
                com.google.api.ads.dfp.v201111.OrderServiceSoapBindingStub _stub = new com.google.api.ads.dfp.v201111.OrderServiceSoapBindingStub(new java.net.URL(OrderServiceInterfacePort_address), this);
                _stub.setPortName(getOrderServiceInterfacePortWSDDServiceName());
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
        if ("OrderServiceInterfacePort".equals(inputPortName)) {
            return getOrderServiceInterfacePort();
        } else {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201111", "OrderService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201111", "OrderServiceInterfacePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        if ("OrderServiceInterfacePort".equals(portName)) {
            setOrderServiceInterfacePortEndpointAddress(address);
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

package org.tempuri;

public class IExhibitSrvserviceLocator extends org.apache.axis.client.Service implements org.tempuri.IExhibitSrvservice {

    public IExhibitSrvserviceLocator() {
    }

    public IExhibitSrvserviceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public IExhibitSrvserviceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    private java.lang.String IExhibitSrvPort_address = "http://212.98.183.107/exhibition/ExhibitSrv.dll/soap/IExhibitSrv";

    public java.lang.String getIExhibitSrvPortAddress() {
        return IExhibitSrvPort_address;
    }

    private java.lang.String IExhibitSrvPortWSDDServiceName = "IExhibitSrvPort";

    public java.lang.String getIExhibitSrvPortWSDDServiceName() {
        return IExhibitSrvPortWSDDServiceName;
    }

    public void setIExhibitSrvPortWSDDServiceName(java.lang.String name) {
        IExhibitSrvPortWSDDServiceName = name;
    }

    public org.tempuri.IExhibitSrv getIExhibitSrvPort() throws javax.xml.rpc.ServiceException {
        java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(IExhibitSrvPort_address);
        } catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getIExhibitSrvPort(endpoint);
    }

    public org.tempuri.IExhibitSrv getIExhibitSrvPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.tempuri.IExhibitSrvbindingStub _stub = new org.tempuri.IExhibitSrvbindingStub(portAddress, this);
            _stub.setPortName(getIExhibitSrvPortWSDDServiceName());
            return _stub;
        } catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setIExhibitSrvPortEndpointAddress(java.lang.String address) {
        IExhibitSrvPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (org.tempuri.IExhibitSrv.class.isAssignableFrom(serviceEndpointInterface)) {
                org.tempuri.IExhibitSrvbindingStub _stub = new org.tempuri.IExhibitSrvbindingStub(new java.net.URL(IExhibitSrvPort_address), this);
                _stub.setPortName(getIExhibitSrvPortWSDDServiceName());
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
        if ("IExhibitSrvPort".equals(inputPortName)) {
            return getIExhibitSrvPort();
        } else {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://tempuri.org/", "IExhibitSrvservice");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://tempuri.org/", "IExhibitSrvPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        if ("IExhibitSrvPort".equals(portName)) {
            setIExhibitSrvPortEndpointAddress(address);
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

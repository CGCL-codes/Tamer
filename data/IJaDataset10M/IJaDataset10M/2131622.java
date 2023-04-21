package cn.myapps.webservice.gmcczj.iamsweb.ws;

public class NotifyLocator extends org.apache.axis.client.Service implements cn.myapps.webservice.gmcczj.iamsweb.ws.Notify {

    public NotifyLocator() {
    }

    public NotifyLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public NotifyLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    private java.lang.String NotifySoap12_address = "http://tiap.zj.gmcc.net/ws/Notify.asmx";

    public java.lang.String getNotifySoap12Address() {
        return NotifySoap12_address;
    }

    private java.lang.String NotifySoap12WSDDServiceName = "NotifySoap12";

    public java.lang.String getNotifySoap12WSDDServiceName() {
        return NotifySoap12WSDDServiceName;
    }

    public void setNotifySoap12WSDDServiceName(java.lang.String name) {
        NotifySoap12WSDDServiceName = name;
    }

    public cn.myapps.webservice.gmcczj.iamsweb.ws.NotifySoap getNotifySoap12() throws javax.xml.rpc.ServiceException {
        java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(NotifySoap12_address);
        } catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getNotifySoap12(endpoint);
    }

    public cn.myapps.webservice.gmcczj.iamsweb.ws.NotifySoap getNotifySoap12(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            cn.myapps.webservice.gmcczj.iamsweb.ws.NotifySoap12Stub _stub = new cn.myapps.webservice.gmcczj.iamsweb.ws.NotifySoap12Stub(portAddress, this);
            _stub.setPortName(getNotifySoap12WSDDServiceName());
            return _stub;
        } catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setNotifySoap12EndpointAddress(java.lang.String address) {
        NotifySoap12_address = address;
    }

    private java.lang.String NotifySoap_address = "http://tiap.zj.gmcc.net/ws/Notify.asmx";

    public java.lang.String getNotifySoapAddress() {
        return NotifySoap_address;
    }

    private java.lang.String NotifySoapWSDDServiceName = "NotifySoap";

    public java.lang.String getNotifySoapWSDDServiceName() {
        return NotifySoapWSDDServiceName;
    }

    public void setNotifySoapWSDDServiceName(java.lang.String name) {
        NotifySoapWSDDServiceName = name;
    }

    public cn.myapps.webservice.gmcczj.iamsweb.ws.NotifySoap getNotifySoap() throws javax.xml.rpc.ServiceException {
        java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(NotifySoap_address);
        } catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getNotifySoap(endpoint);
    }

    public cn.myapps.webservice.gmcczj.iamsweb.ws.NotifySoap getNotifySoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            cn.myapps.webservice.gmcczj.iamsweb.ws.NotifySoapStub _stub = new cn.myapps.webservice.gmcczj.iamsweb.ws.NotifySoapStub(portAddress, this);
            _stub.setPortName(getNotifySoapWSDDServiceName());
            return _stub;
        } catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setNotifySoapEndpointAddress(java.lang.String address) {
        NotifySoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     * This service has multiple ports for a given interface;
     * the proxy implementation returned may be indeterminate.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (cn.myapps.webservice.gmcczj.iamsweb.ws.NotifySoap.class.isAssignableFrom(serviceEndpointInterface)) {
                cn.myapps.webservice.gmcczj.iamsweb.ws.NotifySoap12Stub _stub = new cn.myapps.webservice.gmcczj.iamsweb.ws.NotifySoap12Stub(new java.net.URL(NotifySoap12_address), this);
                _stub.setPortName(getNotifySoap12WSDDServiceName());
                return _stub;
            }
            if (cn.myapps.webservice.gmcczj.iamsweb.ws.NotifySoap.class.isAssignableFrom(serviceEndpointInterface)) {
                cn.myapps.webservice.gmcczj.iamsweb.ws.NotifySoapStub _stub = new cn.myapps.webservice.gmcczj.iamsweb.ws.NotifySoapStub(new java.net.URL(NotifySoap_address), this);
                _stub.setPortName(getNotifySoapWSDDServiceName());
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
        if ("NotifySoap12".equals(inputPortName)) {
            return getNotifySoap12();
        } else if ("NotifySoap".equals(inputPortName)) {
            return getNotifySoap();
        } else {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://iamsweb.gmcc.net/WS/", "Notify");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://iamsweb.gmcc.net/WS/", "NotifySoap12"));
            ports.add(new javax.xml.namespace.QName("http://iamsweb.gmcc.net/WS/", "NotifySoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        if ("NotifySoap12".equals(portName)) {
            setNotifySoap12EndpointAddress(address);
        } else if ("NotifySoap".equals(portName)) {
            setNotifySoapEndpointAddress(address);
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

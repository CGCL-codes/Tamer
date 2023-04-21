package cn.myapps.webservice.gmcczj.iamsweb.ws;

public class SSOLocator extends org.apache.axis.client.Service implements cn.myapps.webservice.gmcczj.iamsweb.ws.SSO {

    public SSOLocator() {
    }

    public SSOLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public SSOLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    private java.lang.String SSOSoap_address = "http://tiap.zj.gmcc.net/ws/SSO.asmx";

    public java.lang.String getSSOSoapAddress() {
        return SSOSoap_address;
    }

    private java.lang.String SSOSoapWSDDServiceName = "SSOSoap";

    public java.lang.String getSSOSoapWSDDServiceName() {
        return SSOSoapWSDDServiceName;
    }

    public void setSSOSoapWSDDServiceName(java.lang.String name) {
        SSOSoapWSDDServiceName = name;
    }

    public cn.myapps.webservice.gmcczj.iamsweb.ws.SSOSoap getSSOSoap() throws javax.xml.rpc.ServiceException {
        java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(SSOSoap_address);
        } catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getSSOSoap(endpoint);
    }

    public cn.myapps.webservice.gmcczj.iamsweb.ws.SSOSoap getSSOSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            cn.myapps.webservice.gmcczj.iamsweb.ws.SSOSoapStub _stub = new cn.myapps.webservice.gmcczj.iamsweb.ws.SSOSoapStub(portAddress, this);
            _stub.setPortName(getSSOSoapWSDDServiceName());
            return _stub;
        } catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setSSOSoapEndpointAddress(java.lang.String address) {
        SSOSoap_address = address;
    }

    private java.lang.String SSOSoap12_address = "http://tiap.zj.gmcc.net/ws/SSO.asmx";

    public java.lang.String getSSOSoap12Address() {
        return SSOSoap12_address;
    }

    private java.lang.String SSOSoap12WSDDServiceName = "SSOSoap12";

    public java.lang.String getSSOSoap12WSDDServiceName() {
        return SSOSoap12WSDDServiceName;
    }

    public void setSSOSoap12WSDDServiceName(java.lang.String name) {
        SSOSoap12WSDDServiceName = name;
    }

    public cn.myapps.webservice.gmcczj.iamsweb.ws.SSOSoap getSSOSoap12() throws javax.xml.rpc.ServiceException {
        java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(SSOSoap12_address);
        } catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getSSOSoap12(endpoint);
    }

    public cn.myapps.webservice.gmcczj.iamsweb.ws.SSOSoap getSSOSoap12(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            cn.myapps.webservice.gmcczj.iamsweb.ws.SSOSoap12Stub _stub = new cn.myapps.webservice.gmcczj.iamsweb.ws.SSOSoap12Stub(portAddress, this);
            _stub.setPortName(getSSOSoap12WSDDServiceName());
            return _stub;
        } catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setSSOSoap12EndpointAddress(java.lang.String address) {
        SSOSoap12_address = address;
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
            if (cn.myapps.webservice.gmcczj.iamsweb.ws.SSOSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                cn.myapps.webservice.gmcczj.iamsweb.ws.SSOSoapStub _stub = new cn.myapps.webservice.gmcczj.iamsweb.ws.SSOSoapStub(new java.net.URL(SSOSoap_address), this);
                _stub.setPortName(getSSOSoapWSDDServiceName());
                return _stub;
            }
            if (cn.myapps.webservice.gmcczj.iamsweb.ws.SSOSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                cn.myapps.webservice.gmcczj.iamsweb.ws.SSOSoap12Stub _stub = new cn.myapps.webservice.gmcczj.iamsweb.ws.SSOSoap12Stub(new java.net.URL(SSOSoap12_address), this);
                _stub.setPortName(getSSOSoap12WSDDServiceName());
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
        if ("SSOSoap".equals(inputPortName)) {
            return getSSOSoap();
        } else if ("SSOSoap12".equals(inputPortName)) {
            return getSSOSoap12();
        } else {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://iamsweb.gmcc.net/WS/", "SSO");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://iamsweb.gmcc.net/WS/", "SSOSoap"));
            ports.add(new javax.xml.namespace.QName("http://iamsweb.gmcc.net/WS/", "SSOSoap12"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        if ("SSOSoap".equals(portName)) {
            setSSOSoapEndpointAddress(address);
        } else if ("SSOSoap12".equals(portName)) {
            setSSOSoap12EndpointAddress(address);
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

package net.woodstock.rockapi.wssecurity;

public class CalculatorServiceLocator extends org.apache.axis.client.Service implements net.woodstock.rockapi.wssecurity.CalculatorService {

    public CalculatorServiceLocator() {
    }

    public CalculatorServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public CalculatorServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    private java.lang.String CalculatorPort_address = "http://127.0.0.1:8080/ws-security/Calculator";

    public java.lang.String getCalculatorPortAddress() {
        return CalculatorPort_address;
    }

    private java.lang.String CalculatorPortWSDDServiceName = "CalculatorPort";

    public java.lang.String getCalculatorPortWSDDServiceName() {
        return CalculatorPortWSDDServiceName;
    }

    public void setCalculatorPortWSDDServiceName(java.lang.String name) {
        CalculatorPortWSDDServiceName = name;
    }

    public net.woodstock.rockapi.wssecurity.Calculator getCalculatorPort() throws javax.xml.rpc.ServiceException {
        java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(CalculatorPort_address);
        } catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getCalculatorPort(endpoint);
    }

    public net.woodstock.rockapi.wssecurity.Calculator getCalculatorPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            net.woodstock.rockapi.wssecurity.CalculatorBindingStub _stub = new net.woodstock.rockapi.wssecurity.CalculatorBindingStub(portAddress, this);
            _stub.setPortName(getCalculatorPortWSDDServiceName());
            return _stub;
        } catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setCalculatorPortEndpointAddress(java.lang.String address) {
        CalculatorPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (net.woodstock.rockapi.wssecurity.Calculator.class.isAssignableFrom(serviceEndpointInterface)) {
                net.woodstock.rockapi.wssecurity.CalculatorBindingStub _stub = new net.woodstock.rockapi.wssecurity.CalculatorBindingStub(new java.net.URL(CalculatorPort_address), this);
                _stub.setPortName(getCalculatorPortWSDDServiceName());
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
        if ("CalculatorPort".equals(inputPortName)) {
            return getCalculatorPort();
        } else {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://rockapi.woodstock.net/wssecurity", "CalculatorService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://rockapi.woodstock.net/wssecurity", "CalculatorPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        if ("CalculatorPort".equals(portName)) {
            setCalculatorPortEndpointAddress(address);
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

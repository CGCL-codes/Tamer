package eu.medeia.mule.caex;

public class ESBServicesLocator extends org.apache.axis.client.Service implements eu.medeia.mule.caex.ESBServices {

    public ESBServicesLocator() {
    }

    public ESBServicesLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ESBServicesLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    private java.lang.String ESBServicesPort_address = "http://localhost:8080/caex";

    public java.lang.String getESBServicesPortAddress() {
        return ESBServicesPort_address;
    }

    private java.lang.String ESBServicesPortWSDDServiceName = "ESBServicesPort";

    public java.lang.String getESBServicesPortWSDDServiceName() {
        return ESBServicesPortWSDDServiceName;
    }

    public void setESBServicesPortWSDDServiceName(java.lang.String name) {
        ESBServicesPortWSDDServiceName = name;
    }

    public eu.medeia.mule.caex.ESBServicesPortType getESBServicesPort() throws javax.xml.rpc.ServiceException {
        java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ESBServicesPort_address);
        } catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getESBServicesPort(endpoint);
    }

    public eu.medeia.mule.caex.ESBServicesPortType getESBServicesPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            eu.medeia.mule.caex.ESBServicesSoapBindingStub _stub = new eu.medeia.mule.caex.ESBServicesSoapBindingStub(portAddress, this);
            _stub.setPortName(getESBServicesPortWSDDServiceName());
            return _stub;
        } catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setESBServicesPortEndpointAddress(java.lang.String address) {
        ESBServicesPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (eu.medeia.mule.caex.ESBServicesPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                eu.medeia.mule.caex.ESBServicesSoapBindingStub _stub = new eu.medeia.mule.caex.ESBServicesSoapBindingStub(new java.net.URL(ESBServicesPort_address), this);
                _stub.setPortName(getESBServicesPortWSDDServiceName());
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
        if ("ESBServicesPort".equals(inputPortName)) {
            return getESBServicesPort();
        } else {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://caex.mule.medeia.eu/", "ESBServices");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://caex.mule.medeia.eu/", "ESBServicesPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        if ("ESBServicesPort".equals(portName)) {
            setESBServicesPortEndpointAddress(address);
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

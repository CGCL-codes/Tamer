package it.cefriel.glue2.test.glue2WS.stub;

public class Glue2WSServiceLocator extends org.apache.axis.client.Service implements it.cefriel.glue2.test.glue2WS.stub.Glue2WSService {

    public Glue2WSServiceLocator() {
    }

    public Glue2WSServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public Glue2WSServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    private java.lang.String glue2WS_address = "http://localhost:8080/glue2/services/glue2WS";

    public java.lang.String getglue2WSAddress() {
        return glue2WS_address;
    }

    private java.lang.String glue2WSWSDDServiceName = "glue2WS";

    public java.lang.String getglue2WSWSDDServiceName() {
        return glue2WSWSDDServiceName;
    }

    public void setglue2WSWSDDServiceName(java.lang.String name) {
        glue2WSWSDDServiceName = name;
    }

    public it.cefriel.glue2.test.glue2WS.stub.Glue2WS getglue2WS() throws javax.xml.rpc.ServiceException {
        java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(glue2WS_address);
        } catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getglue2WS(endpoint);
    }

    public it.cefriel.glue2.test.glue2WS.stub.Glue2WS getglue2WS(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            it.cefriel.glue2.test.glue2WS.stub.Glue2WSSoapBindingStub _stub = new it.cefriel.glue2.test.glue2WS.stub.Glue2WSSoapBindingStub(portAddress, this);
            _stub.setPortName(getglue2WSWSDDServiceName());
            return _stub;
        } catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setglue2WSEndpointAddress(java.lang.String address) {
        glue2WS_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (it.cefriel.glue2.test.glue2WS.stub.Glue2WS.class.isAssignableFrom(serviceEndpointInterface)) {
                it.cefriel.glue2.test.glue2WS.stub.Glue2WSSoapBindingStub _stub = new it.cefriel.glue2.test.glue2WS.stub.Glue2WSSoapBindingStub(new java.net.URL(glue2WS_address), this);
                _stub.setPortName(getglue2WSWSDDServiceName());
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
        if ("glue2WS".equals(inputPortName)) {
            return getglue2WS();
        } else {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://glue2.cefriel.it/glue2WS", "Glue2WSService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://glue2.cefriel.it/glue2WS", "glue2WS"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        if ("glue2WS".equals(portName)) {
            setglue2WSEndpointAddress(address);
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

package webservice;

public class PersonaSOAServiceLocator extends org.apache.axis.client.Service implements webservice.PersonaSOAService {

    public PersonaSOAServiceLocator() {
    }

    public PersonaSOAServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public PersonaSOAServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    private java.lang.String PersonaSOA_address = "http://localhost:8080/codigoproveedorPersistenciaSOA/services/PersonaSOA";

    public java.lang.String getPersonaSOAAddress() {
        return PersonaSOA_address;
    }

    private java.lang.String PersonaSOAWSDDServiceName = "PersonaSOA";

    public java.lang.String getPersonaSOAWSDDServiceName() {
        return PersonaSOAWSDDServiceName;
    }

    public void setPersonaSOAWSDDServiceName(java.lang.String name) {
        PersonaSOAWSDDServiceName = name;
    }

    public webservice.PersonaSOA getPersonaSOA() throws javax.xml.rpc.ServiceException {
        java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(PersonaSOA_address);
        } catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getPersonaSOA(endpoint);
    }

    public webservice.PersonaSOA getPersonaSOA(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            webservice.PersonaSOASoapBindingStub _stub = new webservice.PersonaSOASoapBindingStub(portAddress, this);
            _stub.setPortName(getPersonaSOAWSDDServiceName());
            return _stub;
        } catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setPersonaSOAEndpointAddress(java.lang.String address) {
        PersonaSOA_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (webservice.PersonaSOA.class.isAssignableFrom(serviceEndpointInterface)) {
                webservice.PersonaSOASoapBindingStub _stub = new webservice.PersonaSOASoapBindingStub(new java.net.URL(PersonaSOA_address), this);
                _stub.setPortName(getPersonaSOAWSDDServiceName());
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
        if ("PersonaSOA".equals(inputPortName)) {
            return getPersonaSOA();
        } else {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://webservice", "PersonaSOAService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://webservice", "PersonaSOA"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        if ("PersonaSOA".equals(portName)) {
            setPersonaSOAEndpointAddress(address);
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

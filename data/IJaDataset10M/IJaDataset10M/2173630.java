package r2q2.processing.ws;

public class ProcessingEngineServiceLocator extends org.apache.axis.client.Service implements r2q2.processing.ws.ProcessingEngineService {

    public ProcessingEngineServiceLocator() {
    }

    public ProcessingEngineServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ProcessingEngineServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    private java.lang.String ProcessingEngine_address = "http://localhost:8080/r2q2/services/ProcessingEngine";

    public java.lang.String getProcessingEngineAddress() {
        return ProcessingEngine_address;
    }

    private java.lang.String ProcessingEngineWSDDServiceName = "ProcessingEngine";

    public java.lang.String getProcessingEngineWSDDServiceName() {
        return ProcessingEngineWSDDServiceName;
    }

    public void setProcessingEngineWSDDServiceName(java.lang.String name) {
        ProcessingEngineWSDDServiceName = name;
    }

    public r2q2.processing.ws.ProcessingEngine_PortType getProcessingEngine() throws javax.xml.rpc.ServiceException {
        java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ProcessingEngine_address);
        } catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getProcessingEngine(endpoint);
    }

    public r2q2.processing.ws.ProcessingEngine_PortType getProcessingEngine(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            r2q2.processing.ws.ProcessingEngineSoapBindingStub _stub = new r2q2.processing.ws.ProcessingEngineSoapBindingStub(portAddress, this);
            _stub.setPortName(getProcessingEngineWSDDServiceName());
            return _stub;
        } catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setProcessingEngineEndpointAddress(java.lang.String address) {
        ProcessingEngine_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (r2q2.processing.ws.ProcessingEngine_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                r2q2.processing.ws.ProcessingEngineSoapBindingStub _stub = new r2q2.processing.ws.ProcessingEngineSoapBindingStub(new java.net.URL(ProcessingEngine_address), this);
                _stub.setPortName(getProcessingEngineWSDDServiceName());
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
        if ("ProcessingEngine".equals(inputPortName)) {
            return getProcessingEngine();
        } else {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://localhost:8080/r2q2/services/ProcessingEngine", "ProcessingEngineService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://localhost:8080/r2q2/services/ProcessingEngine", "ProcessingEngine"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        if ("ProcessingEngine".equals(portName)) {
            setProcessingEngineEndpointAddress(address);
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

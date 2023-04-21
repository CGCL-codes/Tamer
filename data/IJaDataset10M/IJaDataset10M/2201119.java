package org.cocome.tradingsystem.webservices.axis.cardValidationService.generated;

public class CardValidationWsIfServiceLocator extends org.apache.axis.client.Service implements org.cocome.tradingsystem.webservices.axis.cardValidationService.generated.CardValidationWsIfService {

    public CardValidationWsIfServiceLocator() {
    }

    public CardValidationWsIfServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public CardValidationWsIfServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    private java.lang.String CardValidationService_address = "http://localhost:8080/axis/services/CardValidationService";

    public java.lang.String getCardValidationServiceAddress() {
        return CardValidationService_address;
    }

    private java.lang.String CardValidationServiceWSDDServiceName = "CardValidationService";

    public java.lang.String getCardValidationServiceWSDDServiceName() {
        return CardValidationServiceWSDDServiceName;
    }

    public void setCardValidationServiceWSDDServiceName(java.lang.String name) {
        CardValidationServiceWSDDServiceName = name;
    }

    public org.cocome.tradingsystem.webservices.axis.cardValidationService.generated.CardValidationWsIf getCardValidationService() throws javax.xml.rpc.ServiceException {
        java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(CardValidationService_address);
        } catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getCardValidationService(endpoint);
    }

    public org.cocome.tradingsystem.webservices.axis.cardValidationService.generated.CardValidationWsIf getCardValidationService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.cocome.tradingsystem.webservices.axis.cardValidationService.generated.CardValidationServiceSoapBindingStub _stub = new org.cocome.tradingsystem.webservices.axis.cardValidationService.generated.CardValidationServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getCardValidationServiceWSDDServiceName());
            return _stub;
        } catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setCardValidationServiceEndpointAddress(java.lang.String address) {
        CardValidationService_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (org.cocome.tradingsystem.webservices.axis.cardValidationService.generated.CardValidationWsIf.class.isAssignableFrom(serviceEndpointInterface)) {
                org.cocome.tradingsystem.webservices.axis.cardValidationService.generated.CardValidationServiceSoapBindingStub _stub = new org.cocome.tradingsystem.webservices.axis.cardValidationService.generated.CardValidationServiceSoapBindingStub(new java.net.URL(CardValidationService_address), this);
                _stub.setPortName(getCardValidationServiceWSDDServiceName());
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
        if ("CardValidationService".equals(inputPortName)) {
            return getCardValidationService();
        } else {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:cardValidationWs.webservices.tradingsystem.cocome.org", "CardValidationWsIfService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("urn:cardValidationWs.webservices.tradingsystem.cocome.org", "CardValidationService"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        if ("CardValidationService".equals(portName)) {
            setCardValidationServiceEndpointAddress(address);
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

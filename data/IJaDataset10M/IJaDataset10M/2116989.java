package com.google.api.adwords.v200909.cm;

public class CampaignCriterionServiceLocator extends org.apache.axis.client.Service implements com.google.api.adwords.v200909.cm.CampaignCriterionService {

    public CampaignCriterionServiceLocator() {
    }

    public CampaignCriterionServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public CampaignCriterionServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    private java.lang.String CampaignCriterionServiceInterfacePort_address = "https://adwords.google.com/api/adwords/cm/v200909/CampaignCriterionService";

    public java.lang.String getCampaignCriterionServiceInterfacePortAddress() {
        return CampaignCriterionServiceInterfacePort_address;
    }

    private java.lang.String CampaignCriterionServiceInterfacePortWSDDServiceName = "CampaignCriterionServiceInterfacePort";

    public java.lang.String getCampaignCriterionServiceInterfacePortWSDDServiceName() {
        return CampaignCriterionServiceInterfacePortWSDDServiceName;
    }

    public void setCampaignCriterionServiceInterfacePortWSDDServiceName(java.lang.String name) {
        CampaignCriterionServiceInterfacePortWSDDServiceName = name;
    }

    public com.google.api.adwords.v200909.cm.CampaignCriterionServiceInterface getCampaignCriterionServiceInterfacePort() throws javax.xml.rpc.ServiceException {
        java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(CampaignCriterionServiceInterfacePort_address);
        } catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getCampaignCriterionServiceInterfacePort(endpoint);
    }

    public com.google.api.adwords.v200909.cm.CampaignCriterionServiceInterface getCampaignCriterionServiceInterfacePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.google.api.adwords.v200909.cm.CampaignCriterionServiceSoapBindingStub _stub = new com.google.api.adwords.v200909.cm.CampaignCriterionServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getCampaignCriterionServiceInterfacePortWSDDServiceName());
            return _stub;
        } catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setCampaignCriterionServiceInterfacePortEndpointAddress(java.lang.String address) {
        CampaignCriterionServiceInterfacePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.google.api.adwords.v200909.cm.CampaignCriterionServiceInterface.class.isAssignableFrom(serviceEndpointInterface)) {
                com.google.api.adwords.v200909.cm.CampaignCriterionServiceSoapBindingStub _stub = new com.google.api.adwords.v200909.cm.CampaignCriterionServiceSoapBindingStub(new java.net.URL(CampaignCriterionServiceInterfacePort_address), this);
                _stub.setPortName(getCampaignCriterionServiceInterfacePortWSDDServiceName());
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
        if ("CampaignCriterionServiceInterfacePort".equals(inputPortName)) {
            return getCampaignCriterionServiceInterfacePort();
        } else {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "CampaignCriterionService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v200909", "CampaignCriterionServiceInterfacePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        if ("CampaignCriterionServiceInterfacePort".equals(portName)) {
            setCampaignCriterionServiceInterfacePortEndpointAddress(address);
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

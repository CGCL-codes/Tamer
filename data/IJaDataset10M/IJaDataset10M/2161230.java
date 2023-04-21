package soap._200306.ta.webservice.com.sct.pipeline;

public class TaLocator extends org.apache.axis.client.Service implements soap._200306.ta.webservice.com.sct.pipeline.Ta {

    public TaLocator() {
    }

    public TaLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public TaLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    /**
     * The PersonalAnnouncement Service enables an end-user to search,
     * retrieve and delete
     *     messages that are targeted to themselves. Messages may be searched
     * using a filter.
     */
    private java.lang.String PersonalAnnouncementPort_address = "http://luministest.nocccd.edu/ws/webservice/ta/200306/soap/PersonalAnnouncement";

    public java.lang.String getPersonalAnnouncementPortAddress() {
        return PersonalAnnouncementPort_address;
    }

    private java.lang.String PersonalAnnouncementPortWSDDServiceName = "PersonalAnnouncementPort";

    public java.lang.String getPersonalAnnouncementPortWSDDServiceName() {
        return PersonalAnnouncementPortWSDDServiceName;
    }

    public void setPersonalAnnouncementPortWSDDServiceName(java.lang.String name) {
        PersonalAnnouncementPortWSDDServiceName = name;
    }

    public msg._200306.ta.webservice.com.sct.pipeline.PersonalAnnouncement getPersonalAnnouncementPort() throws javax.xml.rpc.ServiceException {
        java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(PersonalAnnouncementPort_address);
        } catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getPersonalAnnouncementPort(endpoint);
    }

    public msg._200306.ta.webservice.com.sct.pipeline.PersonalAnnouncement getPersonalAnnouncementPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            soap._200306.ta.webservice.com.sct.pipeline.PersonalAnnouncementBindingStub _stub = new soap._200306.ta.webservice.com.sct.pipeline.PersonalAnnouncementBindingStub(portAddress, this);
            _stub.setPortName(getPersonalAnnouncementPortWSDDServiceName());
            return _stub;
        } catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setPersonalAnnouncementPortEndpointAddress(java.lang.String address) {
        PersonalAnnouncementPort_address = address;
    }

    /**
     * The TargetedAnnouncementAdmin Service provides an administrative
     * tool to send messages to a group of
     *     recipients based on a variety of attributes
     *     in addition to their user Ids, and delivered using a choice of
     * delivery agents
     *     including, but not limited to e-mail. Messages may themselves
     * be defined as templates
     *     to allow personalization of content based on the recipient's attributes.
     * It further
     *     allows management of the messages by allowing a recipient to retrieve
     * messages targeted
     *     to themselves, as well as for senders or administrators to retrieve
     * messages filtered by
     *     specified criteria.
     *     These services require the caller to be granted the ManageTargetedAnnouncement
     * permission.
     */
    private java.lang.String TargetedAnnouncementAdminPort_address = "http://luministest.nocccd.edu/ws/webservice/ta/200306/soap/TargetedAnnouncementAdmin";

    public java.lang.String getTargetedAnnouncementAdminPortAddress() {
        return TargetedAnnouncementAdminPort_address;
    }

    private java.lang.String TargetedAnnouncementAdminPortWSDDServiceName = "TargetedAnnouncementAdminPort";

    public java.lang.String getTargetedAnnouncementAdminPortWSDDServiceName() {
        return TargetedAnnouncementAdminPortWSDDServiceName;
    }

    public void setTargetedAnnouncementAdminPortWSDDServiceName(java.lang.String name) {
        TargetedAnnouncementAdminPortWSDDServiceName = name;
    }

    public msg._200306.ta.webservice.com.sct.pipeline.TargetedAnnouncementAdmin getTargetedAnnouncementAdminPort() throws javax.xml.rpc.ServiceException {
        java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(TargetedAnnouncementAdminPort_address);
        } catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getTargetedAnnouncementAdminPort(endpoint);
    }

    public msg._200306.ta.webservice.com.sct.pipeline.TargetedAnnouncementAdmin getTargetedAnnouncementAdminPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            soap._200306.ta.webservice.com.sct.pipeline.TargetedAnnouncementAdminBindingStub _stub = new soap._200306.ta.webservice.com.sct.pipeline.TargetedAnnouncementAdminBindingStub(portAddress, this);
            _stub.setPortName(getTargetedAnnouncementAdminPortWSDDServiceName());
            return _stub;
        } catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setTargetedAnnouncementAdminPortEndpointAddress(java.lang.String address) {
        TargetedAnnouncementAdminPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (msg._200306.ta.webservice.com.sct.pipeline.PersonalAnnouncement.class.isAssignableFrom(serviceEndpointInterface)) {
                soap._200306.ta.webservice.com.sct.pipeline.PersonalAnnouncementBindingStub _stub = new soap._200306.ta.webservice.com.sct.pipeline.PersonalAnnouncementBindingStub(new java.net.URL(PersonalAnnouncementPort_address), this);
                _stub.setPortName(getPersonalAnnouncementPortWSDDServiceName());
                return _stub;
            }
            if (msg._200306.ta.webservice.com.sct.pipeline.TargetedAnnouncementAdmin.class.isAssignableFrom(serviceEndpointInterface)) {
                soap._200306.ta.webservice.com.sct.pipeline.TargetedAnnouncementAdminBindingStub _stub = new soap._200306.ta.webservice.com.sct.pipeline.TargetedAnnouncementAdminBindingStub(new java.net.URL(TargetedAnnouncementAdminPort_address), this);
                _stub.setPortName(getTargetedAnnouncementAdminPortWSDDServiceName());
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
        if ("PersonalAnnouncementPort".equals(inputPortName)) {
            return getPersonalAnnouncementPort();
        } else if ("TargetedAnnouncementAdminPort".equals(inputPortName)) {
            return getTargetedAnnouncementAdminPort();
        } else {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:pipeline.sct.com:webservice:ta:200306:soap", "ta");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("urn:pipeline.sct.com:webservice:ta:200306:soap", "PersonalAnnouncementPort"));
            ports.add(new javax.xml.namespace.QName("urn:pipeline.sct.com:webservice:ta:200306:soap", "TargetedAnnouncementAdminPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        if ("PersonalAnnouncementPort".equals(portName)) {
            setPersonalAnnouncementPortEndpointAddress(address);
        } else if ("TargetedAnnouncementAdminPort".equals(portName)) {
            setTargetedAnnouncementAdminPortEndpointAddress(address);
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

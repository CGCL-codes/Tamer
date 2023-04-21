package de.boardgamesonline.bgo2.alhambra.server;

public class GameServerServiceLocator extends org.apache.axis.client.Service implements de.boardgamesonline.bgo2.alhambra.server.GameServerService {

    public GameServerServiceLocator() {
    }

    public GameServerServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public GameServerServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    private java.lang.String GameServer_address = "http://localhost:8080/alhambraserver/services/GameServer";

    public java.lang.String getGameServerAddress() {
        return GameServer_address;
    }

    private java.lang.String GameServerWSDDServiceName = "GameServer";

    public java.lang.String getGameServerWSDDServiceName() {
        return GameServerWSDDServiceName;
    }

    public void setGameServerWSDDServiceName(java.lang.String name) {
        GameServerWSDDServiceName = name;
    }

    public de.boardgamesonline.bgo2.alhambra.server.GameServer getGameServer() throws javax.xml.rpc.ServiceException {
        java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(GameServer_address);
        } catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getGameServer(endpoint);
    }

    public de.boardgamesonline.bgo2.alhambra.server.GameServer getGameServer(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            de.boardgamesonline.bgo2.alhambra.server.GameServerSoapBindingStub _stub = new de.boardgamesonline.bgo2.alhambra.server.GameServerSoapBindingStub(portAddress, this);
            _stub.setPortName(getGameServerWSDDServiceName());
            return _stub;
        } catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setGameServerEndpointAddress(java.lang.String address) {
        GameServer_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (de.boardgamesonline.bgo2.alhambra.server.GameServer.class.isAssignableFrom(serviceEndpointInterface)) {
                de.boardgamesonline.bgo2.alhambra.server.GameServerSoapBindingStub _stub = new de.boardgamesonline.bgo2.alhambra.server.GameServerSoapBindingStub(new java.net.URL(GameServer_address), this);
                _stub.setPortName(getGameServerWSDDServiceName());
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
        if ("GameServer".equals(inputPortName)) {
            return getGameServer();
        } else {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://server.alhambra.bgo2.boardgamesonline.de", "GameServerService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://server.alhambra.bgo2.boardgamesonline.de", "GameServer"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        if ("GameServer".equals(portName)) {
            setGameServerEndpointAddress(address);
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

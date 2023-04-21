package com.capeclear.www.globalweather.wsdl;

public class GlobalWeather_ServiceLocator extends org.apache.axis.client.Service implements com.capeclear.www.globalweather.wsdl.GlobalWeather_Service {

    public GlobalWeather_ServiceLocator() {
    }

    public GlobalWeather_ServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public GlobalWeather_ServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    private java.lang.String StationInfo_address = "http://live.capeclear.com/ccx/GlobalWeather";

    public java.lang.String getStationInfoAddress() {
        return StationInfo_address;
    }

    private java.lang.String StationInfoWSDDServiceName = "StationInfo";

    public java.lang.String getStationInfoWSDDServiceName() {
        return StationInfoWSDDServiceName;
    }

    public void setStationInfoWSDDServiceName(java.lang.String name) {
        StationInfoWSDDServiceName = name;
    }

    public com.capeclear.www.globalweather.wsdl.StationInfo getStationInfo() throws javax.xml.rpc.ServiceException {
        java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(StationInfo_address);
        } catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getStationInfo(endpoint);
    }

    public com.capeclear.www.globalweather.wsdl.StationInfo getStationInfo(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.capeclear.www.globalweather.wsdl.StationInfoBindingStub _stub = new com.capeclear.www.globalweather.wsdl.StationInfoBindingStub(portAddress, this);
            _stub.setPortName(getStationInfoWSDDServiceName());
            return _stub;
        } catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setStationInfoEndpointAddress(java.lang.String address) {
        StationInfo_address = address;
    }

    private java.lang.String GlobalWeather_address = "http://live.capeclear.com/ccx/GlobalWeather";

    public java.lang.String getGlobalWeatherAddress() {
        return GlobalWeather_address;
    }

    private java.lang.String GlobalWeatherWSDDServiceName = "GlobalWeather";

    public java.lang.String getGlobalWeatherWSDDServiceName() {
        return GlobalWeatherWSDDServiceName;
    }

    public void setGlobalWeatherWSDDServiceName(java.lang.String name) {
        GlobalWeatherWSDDServiceName = name;
    }

    public com.capeclear.www.globalweather.wsdl.GlobalWeather_PortType getGlobalWeather() throws javax.xml.rpc.ServiceException {
        java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(GlobalWeather_address);
        } catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getGlobalWeather(endpoint);
    }

    public com.capeclear.www.globalweather.wsdl.GlobalWeather_PortType getGlobalWeather(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.capeclear.www.globalweather.wsdl.GlobalWeatherBindingStub _stub = new com.capeclear.www.globalweather.wsdl.GlobalWeatherBindingStub(portAddress, this);
            _stub.setPortName(getGlobalWeatherWSDDServiceName());
            return _stub;
        } catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setGlobalWeatherEndpointAddress(java.lang.String address) {
        GlobalWeather_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.capeclear.www.globalweather.wsdl.StationInfo.class.isAssignableFrom(serviceEndpointInterface)) {
                com.capeclear.www.globalweather.wsdl.StationInfoBindingStub _stub = new com.capeclear.www.globalweather.wsdl.StationInfoBindingStub(new java.net.URL(StationInfo_address), this);
                _stub.setPortName(getStationInfoWSDDServiceName());
                return _stub;
            }
            if (com.capeclear.www.globalweather.wsdl.GlobalWeather_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.capeclear.www.globalweather.wsdl.GlobalWeatherBindingStub _stub = new com.capeclear.www.globalweather.wsdl.GlobalWeatherBindingStub(new java.net.URL(GlobalWeather_address), this);
                _stub.setPortName(getGlobalWeatherWSDDServiceName());
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
        if ("StationInfo".equals(inputPortName)) {
            return getStationInfo();
        } else if ("GlobalWeather".equals(inputPortName)) {
            return getGlobalWeather();
        } else {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.capeclear.com/globalweather/wsdl/", "GlobalWeather");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.capeclear.com/globalweather/wsdl/", "StationInfo"));
            ports.add(new javax.xml.namespace.QName("http://www.capeclear.com/globalweather/wsdl/", "GlobalWeather"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        if ("StationInfo".equals(portName)) {
            setStationInfoEndpointAddress(address);
        } else if ("GlobalWeather".equals(portName)) {
            setGlobalWeatherEndpointAddress(address);
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

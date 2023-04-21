package net.sf.jerkbot.plugins.meteo;

/**
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 *         [INSERT DESCRIPTION HERE]
 * @version 0.0.1
 */
public class WeatherLocator extends org.apache.axis.client.Service implements Weather {

    private java.lang.String WeatherSoap_address = "http://www.deeptraining.com/webservices/weather.asmx";

    private java.lang.String WeatherSoapWSDDServiceName = "WeatherSoap";

    private java.lang.String WeatherSoap12_address = "http://www.deeptraining.com/webservices/weather.asmx";

    private java.lang.String WeatherSoap12WSDDServiceName = "WeatherSoap12";

    private java.util.HashSet ports = null;

    /**
     * Provides weather forcasts for U.S. cities
     */
    public WeatherLocator() {
    }

    public WeatherLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public WeatherLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    public java.lang.String getWeatherSoapAddress() {
        return WeatherSoap_address;
    }

    public java.lang.String getWeatherSoapWSDDServiceName() {
        return WeatherSoapWSDDServiceName;
    }

    public void setWeatherSoapWSDDServiceName(java.lang.String name) {
        WeatherSoapWSDDServiceName = name;
    }

    public WeatherSoap getWeatherSoap() throws javax.xml.rpc.ServiceException {
        java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(WeatherSoap_address);
        } catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getWeatherSoap(endpoint);
    }

    public WeatherSoap getWeatherSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            WeatherSoapStub _stub = new WeatherSoapStub(portAddress, this);
            _stub.setPortName(getWeatherSoapWSDDServiceName());
            return _stub;
        } catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setWeatherSoapEndpointAddress(java.lang.String address) {
        WeatherSoap_address = address;
    }

    public java.lang.String getWeatherSoap12Address() {
        return WeatherSoap12_address;
    }

    public java.lang.String getWeatherSoap12WSDDServiceName() {
        return WeatherSoap12WSDDServiceName;
    }

    public void setWeatherSoap12WSDDServiceName(java.lang.String name) {
        WeatherSoap12WSDDServiceName = name;
    }

    public WeatherSoap getWeatherSoap12() throws javax.xml.rpc.ServiceException {
        java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(WeatherSoap12_address);
        } catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getWeatherSoap12(endpoint);
    }

    public WeatherSoap getWeatherSoap12(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            WeatherSoap12Stub _stub = new WeatherSoap12Stub(portAddress, this);
            _stub.setPortName(getWeatherSoap12WSDDServiceName());
            return _stub;
        } catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setWeatherSoap12EndpointAddress(java.lang.String address) {
        WeatherSoap12_address = address;
    }

    /**
     * For the given interface, get the stub implementation. If this service has
     * no port for the given interface, then ServiceException is thrown. This
     * service has multiple ports for a given interface; the proxy
     * implementation returned may be indeterminate.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (WeatherSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                WeatherSoapStub _stub = new WeatherSoapStub(new java.net.URL(WeatherSoap_address), this);
                _stub.setPortName(getWeatherSoapWSDDServiceName());
                return _stub;
            }
            if (WeatherSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                WeatherSoap12Stub _stub = new WeatherSoap12Stub(new java.net.URL(WeatherSoap12_address), this);
                _stub.setPortName(getWeatherSoap12WSDDServiceName());
                return _stub;
            }
        } catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + ((serviceEndpointInterface == null) ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation. If this service has
     * no port for the given interface, then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("WeatherSoap".equals(inputPortName)) {
            return getWeatherSoap();
        } else if ("WeatherSoap12".equals(inputPortName)) {
            return getWeatherSoap12();
        } else {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://litwinconsulting.com/webservices/", "Weather");
    }

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://litwinconsulting.com/webservices/", "WeatherSoap"));
            ports.add(new javax.xml.namespace.QName("http://litwinconsulting.com/webservices/", "WeatherSoap12"));
        }
        return ports.iterator();
    }

    /**
     * Set the endpoint address for the specified port name.
     */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        if ("WeatherSoap".equals(portName)) {
            setWeatherSoapEndpointAddress(address);
        } else if ("WeatherSoap12".equals(portName)) {
            setWeatherSoap12EndpointAddress(address);
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

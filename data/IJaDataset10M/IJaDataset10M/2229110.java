package br.inf.portalfiscal.www.nfe.wsdl.NfeStatusServico;

public class NfeStatusServicoLocator extends org.apache.axis.client.Service implements br.inf.portalfiscal.www.nfe.wsdl.NfeStatusServico.NfeStatusServico {

    /**
 * Serviço destinado à consulta do status do serviçoprestado pelo
 * Portal da Secretaria de Fazenda
 */
    public NfeStatusServicoLocator() {
    }

    public final String PRODUCAO = "1";

    public final String HOMOLOGACAO = "2";

    private static String ambiente = "2";

    public NfeStatusServicoLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public NfeStatusServicoLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    private java.lang.String NfeStatusServicoSoap12_address = ambiente.equals(PRODUCAO) ? "https://nfe.fazenda.sp.gov.br/nfeWEB/services/NfeStatusServico.asmx" : "https://homologacao.nfe.fazenda.sp.gov.br/nfeWEB/services/NfeStatusServico.asmx";

    public java.lang.String getNfeStatusServicoSoap12Address() {
        return NfeStatusServicoSoap12_address;
    }

    public void setNfeStatusServicoSoap12Address() {
        NfeStatusServicoSoap12_address = ambiente.equals(PRODUCAO) ? "https://nfe.fazenda.sp.gov.br/nfeWEB/services/NfeStatusServico.asmx" : "https://homologacao.nfe.fazenda.sp.gov.br/nfeWEB/services/NfeStatusServico.asmx";
    }

    private java.lang.String NfeStatusServicoSoap12WSDDServiceName = "NfeStatusServicoSoap12";

    public java.lang.String getNfeStatusServicoSoap12WSDDServiceName() {
        return NfeStatusServicoSoap12WSDDServiceName;
    }

    public void setNfeStatusServicoSoap12WSDDServiceName(java.lang.String name) {
        NfeStatusServicoSoap12WSDDServiceName = name;
    }

    public br.inf.portalfiscal.www.nfe.wsdl.NfeStatusServico.NfeStatusServicoSoap getNfeStatusServicoSoap12() throws javax.xml.rpc.ServiceException {
        java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(NfeStatusServicoSoap12_address);
        } catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getNfeStatusServicoSoap12(endpoint);
    }

    public br.inf.portalfiscal.www.nfe.wsdl.NfeStatusServico.NfeStatusServicoSoap getNfeStatusServicoSoap12(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            br.inf.portalfiscal.www.nfe.wsdl.NfeStatusServico.NfeStatusServicoSoap12Stub _stub = new br.inf.portalfiscal.www.nfe.wsdl.NfeStatusServico.NfeStatusServicoSoap12Stub(portAddress, this);
            _stub.setPortName(getNfeStatusServicoSoap12WSDDServiceName());
            return _stub;
        } catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setNfeStatusServicoSoap12EndpointAddress(java.lang.String address) {
        NfeStatusServicoSoap12_address = address;
    }

    private java.lang.String NfeStatusServicoSoap_address = ambiente.equals(PRODUCAO) ? "https://nfe.fazenda.sp.gov.br/nfeWEB/services/NfeStatusServico.asmx" : "https://homologacao.nfe.fazenda.sp.gov.br/nfeWEB/services/NfeStatusServico.asmx";

    public java.lang.String getNfeStatusServicoSoapAddress() {
        return NfeStatusServicoSoap_address;
    }

    public void setNfeStatusServicoSoapAddress() {
        NfeStatusServicoSoap_address = ambiente.equals(PRODUCAO) ? "https://nfe.fazenda.sp.gov.br/nfeWEB/services/NfeStatusServico.asmx" : "https://homologacao.nfe.fazenda.sp.gov.br/nfeWEB/services/NfeStatusServico.asmx";
    }

    private java.lang.String NfeStatusServicoSoapWSDDServiceName = "NfeStatusServicoSoap";

    public java.lang.String getNfeStatusServicoSoapWSDDServiceName() {
        return NfeStatusServicoSoapWSDDServiceName;
    }

    public void setNfeStatusServicoSoapWSDDServiceName(java.lang.String name) {
        NfeStatusServicoSoapWSDDServiceName = name;
    }

    public br.inf.portalfiscal.www.nfe.wsdl.NfeStatusServico.NfeStatusServicoSoap getNfeStatusServicoSoap() throws javax.xml.rpc.ServiceException {
        java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(NfeStatusServicoSoap_address);
        } catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getNfeStatusServicoSoap(endpoint);
    }

    public br.inf.portalfiscal.www.nfe.wsdl.NfeStatusServico.NfeStatusServicoSoap getNfeStatusServicoSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            br.inf.portalfiscal.www.nfe.wsdl.NfeStatusServico.NfeStatusServicoSoapStub _stub = new br.inf.portalfiscal.www.nfe.wsdl.NfeStatusServico.NfeStatusServicoSoapStub(portAddress, this);
            _stub.setPortName(getNfeStatusServicoSoapWSDDServiceName());
            return _stub;
        } catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setNfeStatusServicoSoapEndpointAddress(java.lang.String address) {
        NfeStatusServicoSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     * This service has multiple ports for a given interface;
     * the proxy implementation returned may be indeterminate.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (br.inf.portalfiscal.www.nfe.wsdl.NfeStatusServico.NfeStatusServicoSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                br.inf.portalfiscal.www.nfe.wsdl.NfeStatusServico.NfeStatusServicoSoap12Stub _stub = new br.inf.portalfiscal.www.nfe.wsdl.NfeStatusServico.NfeStatusServicoSoap12Stub(new java.net.URL(NfeStatusServicoSoap12_address), this);
                _stub.setPortName(getNfeStatusServicoSoap12WSDDServiceName());
                return _stub;
            }
            if (br.inf.portalfiscal.www.nfe.wsdl.NfeStatusServico.NfeStatusServicoSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                br.inf.portalfiscal.www.nfe.wsdl.NfeStatusServico.NfeStatusServicoSoapStub _stub = new br.inf.portalfiscal.www.nfe.wsdl.NfeStatusServico.NfeStatusServicoSoapStub(new java.net.URL(NfeStatusServicoSoap_address), this);
                _stub.setPortName(getNfeStatusServicoSoapWSDDServiceName());
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
        if ("NfeStatusServicoSoap12".equals(inputPortName)) {
            return getNfeStatusServicoSoap12();
        } else if ("NfeStatusServicoSoap".equals(inputPortName)) {
            return getNfeStatusServicoSoap();
        } else {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe/wsdl/NfeStatusServico", "NfeStatusServico");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe/wsdl/NfeStatusServico", "NfeStatusServicoSoap12"));
            ports.add(new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe/wsdl/NfeStatusServico", "NfeStatusServicoSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        if ("NfeStatusServicoSoap12".equals(portName)) {
            setNfeStatusServicoSoap12EndpointAddress(address);
        } else if ("NfeStatusServicoSoap".equals(portName)) {
            setNfeStatusServicoSoapEndpointAddress(address);
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

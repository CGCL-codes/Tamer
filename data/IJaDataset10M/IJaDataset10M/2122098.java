package br.inf.portalfiscal.www.nfe.wsdl.NfeConsulta;

public interface NfeConsulta extends javax.xml.rpc.Service {

    /**
 * Serviço destinado ao atendimento de solicitações de consulta da
 * situação atual da NF-e na Base de Dados do Portal sa Secretaria de
 * Fazenda Estatual.
 */
    public java.lang.String getNfeConsultaSoapAddress();

    public br.inf.portalfiscal.www.nfe.wsdl.NfeConsulta.NfeConsultaSoap getNfeConsultaSoap() throws javax.xml.rpc.ServiceException;

    public br.inf.portalfiscal.www.nfe.wsdl.NfeConsulta.NfeConsultaSoap getNfeConsultaSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;

    public java.lang.String getNfeConsultaSoap12Address();

    public br.inf.portalfiscal.www.nfe.wsdl.NfeConsulta.NfeConsultaSoap getNfeConsultaSoap12() throws javax.xml.rpc.ServiceException;

    public br.inf.portalfiscal.www.nfe.wsdl.NfeConsulta.NfeConsultaSoap getNfeConsultaSoap12(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}

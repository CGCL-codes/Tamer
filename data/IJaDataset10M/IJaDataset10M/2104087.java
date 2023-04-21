package issrg.globus.service;

public interface PermisAuthzService extends javax.xml.rpc.Service {

    public java.lang.String getPermisAuthzServicePortAddress();

    public issrg.globus.PermisAuthzServicePortType getPermisAuthzServicePort() throws javax.xml.rpc.ServiceException;

    public issrg.globus.PermisAuthzServicePortType getPermisAuthzServicePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}

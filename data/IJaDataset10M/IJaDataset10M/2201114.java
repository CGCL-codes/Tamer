package fr;

public interface SommerService extends javax.xml.rpc.Service {

    public java.lang.String getSommerAddress();

    public fr.Sommer getSommer() throws javax.xml.rpc.ServiceException;

    public fr.Sommer getSommer(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}

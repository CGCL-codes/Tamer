package spidr.export.filter;

public interface HiLoFilterWsService extends javax.xml.rpc.Service {

    public java.lang.String getHiLoFilterWsPortAddress();

    public HiLoFilterWsPortType getHiLoFilterWsPort() throws javax.xml.rpc.ServiceException;

    public HiLoFilterWsPortType getHiLoFilterWsPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}

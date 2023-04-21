package bgo.main;

public interface WebserviceService extends javax.xml.rpc.Service {

    public java.lang.String getWebserviceAddress();

    public bgo.main.Webservice getWebservice() throws javax.xml.rpc.ServiceException;

    public bgo.main.Webservice getWebservice(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}

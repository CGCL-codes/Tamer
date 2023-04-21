package UCD_pkg;

public interface UCDService extends javax.xml.rpc.Service {

    public java.lang.String getUCDAddress();

    public UCD_pkg.UCD getUCD() throws javax.xml.rpc.ServiceException;

    public UCD_pkg.UCD getUCD(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}

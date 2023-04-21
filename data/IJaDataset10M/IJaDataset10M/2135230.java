package localhost.magicmap.services.PositionFacade;

public interface PositionFacadeService extends javax.xml.rpc.Service {

    public java.lang.String getPositionFacadeAddress();

    public localhost.magicmap.services.PositionFacade.PositionFacade getPositionFacade() throws javax.xml.rpc.ServiceException;

    public localhost.magicmap.services.PositionFacade.PositionFacade getPositionFacade(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}

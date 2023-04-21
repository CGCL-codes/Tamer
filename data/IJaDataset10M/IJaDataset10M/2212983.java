package net.sourceforge.bexee.Travel;

public interface TravelServiceService extends javax.xml.rpc.Service {

    public java.lang.String getTravelServiceAddress();

    public net.sourceforge.bexee.Travel.TravelService getTravelService() throws javax.xml.rpc.ServiceException;

    public net.sourceforge.bexee.Travel.TravelService getTravelService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}

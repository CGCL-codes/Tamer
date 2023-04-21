package org.monet.services.setupservice;

public interface Setupservice_Service extends javax.xml.rpc.Service {

    public java.lang.String getSetupservicePortAddress();

    public org.monet.services.setupservice.Setupservice_PortType getSetupservicePort() throws javax.xml.rpc.ServiceException;

    public org.monet.services.setupservice.Setupservice_PortType getSetupservicePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}

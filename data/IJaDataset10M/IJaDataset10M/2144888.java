package wesodi.plugins.handDraufAdvertiser;

public interface HandDraufAdvertiser_Service extends javax.xml.rpc.Service {

    /**
 * HandDraufAdvertiserSOAP Service Version 1.
 */
    public java.lang.String getHandDraufAdvertiserSOAPAddress();

    public wesodi.plugins.handDraufAdvertiser.HandDraufAdvertiser_PortType getHandDraufAdvertiserSOAP() throws javax.xml.rpc.ServiceException;

    public wesodi.plugins.handDraufAdvertiser.HandDraufAdvertiser_PortType getHandDraufAdvertiserSOAP(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}

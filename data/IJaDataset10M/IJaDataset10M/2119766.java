package gov.sandia.ccaffeine.dc.user_iface.MVC.event;

import gov.sandia.ccaffeine.dc.user_iface.MVC.CcaConnectionBetweenTwoPorts;

public class CcaConnectTwoPortsEvent extends java.util.EventObject {

    static final long serialVersionUID = 1;

    protected CcaConnectionBetweenTwoPorts ccaConnection = null;

    /**
     * Get the cca connection that is between two ports.
     * @return The cca connection
     */
    public CcaConnectionBetweenTwoPorts getCcaConnection() {
        return (this.ccaConnection);
    }

    /**
     * Set the cca connection that is between two ports.
     * @param ccaConnection The cca connection
     */
    public void setCcaConnect(CcaConnectionBetweenTwoPorts ccaConnect) {
        this.ccaConnection = ccaConnect;
    }

    /**
     * Create a CcaConnectTwoPorts Event.
     * @param event The entity that generated this event.
     */
    public CcaConnectTwoPortsEvent(Object source) {
        super(source);
        this.ccaConnection = null;
    }

    /**
     * Create a CcaConnectTwoPorts Event.
     * @param event The entity that generated this event.
     * @param ccaConnect The cca connection that is between two ports.
     */
    public CcaConnectTwoPortsEvent(Object source, CcaConnectionBetweenTwoPorts ccaConnection) {
        super(source);
        this.ccaConnection = ccaConnection;
    }
}

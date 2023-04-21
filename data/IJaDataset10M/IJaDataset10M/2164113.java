package samples.connectors.mailconnector.ra.outbound;

import java.util.ResourceBundle;
import java.util.Vector;
import java.util.logging.Logger;
import javax.resource.spi.ConnectionEvent;
import javax.resource.spi.ConnectionEventListener;
import javax.resource.spi.ManagedConnection;

/**
 * The connector architecture provides an event callback mechanism that
 * enables an application server to receive notifications from a
 * ManagedConnection instance. The App Server implements this class in 
 * order to listen to event notifications from ManagedConnection instances.
 */
public class JavaMailConnectionEventListener {

    private Vector listeners;

    private ManagedConnection mcon;

    static Logger logger = Logger.getLogger("samples.connectors.mailconnector.ra.outbound", "samples.connectors.mailconnector.ra.outbound.LocalStrings");

    ResourceBundle resource = ResourceBundle.getBundle("samples.connectors.mailconnector.ra.outbound.LocalStrings");

    /**
     * Constructor.
     *
     * @param mcon  the managed connection that created this instance
     */
    public JavaMailConnectionEventListener(ManagedConnection mcon) {
        logger.info(" 3C.- Db4oConnectionEventListener::Constructor");
        listeners = new Vector();
        this.mcon = mcon;
    }

    /**
     * Sends a connection event to the application server.
     *
     * @param eventType  the ConnectionEvent type
     * @param ex  exception indicating a connection-related error
     * @param connectionHandle  the connection handle associated with the 
     *                          ManagedConnection instance
     */
    public void sendEvent(int eventType, Exception ex, Object connectionHandle) {
        Vector list = (Vector) listeners.clone();
        ConnectionEvent ce = null;
        if (ex == null) {
            ce = new ConnectionEvent(mcon, eventType);
        } else {
            ce = new ConnectionEvent(mcon, eventType, ex);
        }
        if (connectionHandle != null) {
            ce.setConnectionHandle(connectionHandle);
        }
        for (int i = 0; i < list.size(); i++) {
            ConnectionEventListener l = (ConnectionEventListener) list.elementAt(i);
            switch(eventType) {
                case ConnectionEvent.CONNECTION_CLOSED:
                    l.connectionClosed(ce);
                    break;
                case ConnectionEvent.LOCAL_TRANSACTION_STARTED:
                    l.localTransactionStarted(ce);
                    break;
                case ConnectionEvent.LOCAL_TRANSACTION_COMMITTED:
                    l.localTransactionCommitted(ce);
                    break;
                case ConnectionEvent.LOCAL_TRANSACTION_ROLLEDBACK:
                    l.localTransactionRolledback(ce);
                    break;
                case ConnectionEvent.CONNECTION_ERROR_OCCURRED:
                    l.connectionErrorOccurred(ce);
                    break;
                default:
                    throw new IllegalArgumentException(resource.getString("ILLEGAL_EVENT_TYPE") + eventType);
            }
        }
    }

    /**
     * Adds a connection event listener to the ManagedConnection Listener 
     * instance. The registered ConnectionEventListener instances are notified
     * of connection close and error events and of local transaction-related 
     * events on the ManagedConnection.
     *
     * @param listener  a new ConnectionEventListener to be registered
     */
    public void addConnectorListener(ConnectionEventListener listener) {
        listeners.addElement(listener);
    }

    /**
     * Removes an already registered connection event listener.
     *
     * @param listener the already registered connection event listener to be 
     *                 removed
     */
    public void removeConnectorListener(ConnectionEventListener listener) {
        listeners.removeElement(listener);
    }
}

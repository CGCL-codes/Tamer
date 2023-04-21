package tuwien.auto.eicl.event;

import java.util.EventObject;

/**
 * <p>
 * This object notifies a thread that the connection was closed.
 * 
 * @author bernhard erb
 * @see tuwien.auto.eicl.CEMI_Connection
 */
public class DisconnectEvent extends EventObject {

    static final long serialVersionUID = 1;

    String disconnectMessage;

    /**
     * Constructor
     * 
     * @param arg0
     *            The calling connection object.
     * @param _DisconnectMessage
     *            The reason for connection ending.
     */
    public DisconnectEvent(Object arg0, String _DisconnectMessage) {
        super(arg0);
        disconnectMessage = _DisconnectMessage;
    }

    /**
     * Returns the disconnect message.
     * 
     * @return The disconnect message.
     */
    public String getDisconnectMessage() {
        return disconnectMessage;
    }
}

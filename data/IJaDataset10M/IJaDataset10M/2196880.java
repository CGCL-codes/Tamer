package tuwien.auto.eicl.event;

import java.util.EventListener;

/**
 * <p>
 * With an implementation of this interface you can register a client to the
 * CEMI_Connection object. This event listener is invoked on every incoming
 * cEMI_L_DATA packet and on disconnect.
 * 
 * @see tuwien.auto.eicl.event.FrameEvent
 * @see tuwien.auto.eicl.event.DisconnectEvent
 * @see tuwien.auto.eicl.CEMI_Connection
 * @author Bernhard Erb
 */
public interface EICLEventListener extends EventListener {

    /**
     * A new frame has been received. The frame can be accessed through the
     * EICLEvent object.
     * 
     * @param e
     *            the event object
     */
    public void newFrameReceived(FrameEvent e);

    /**
     * The connection has been closed.
     * 
     * @param e
     *            The reason for connection ending.
     */
    public void serverDisconnected(DisconnectEvent e);
}

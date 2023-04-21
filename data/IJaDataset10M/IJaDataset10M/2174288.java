package org.mobicents.media.server.ctrl.mgcp.signal;

import jain.protocol.ip.mgcp.message.parms.EventName;
import org.mobicents.media.server.ctrl.mgcp.UnknownActivityException;
import org.mobicents.media.server.spi.Connection;
import org.mobicents.media.server.spi.Endpoint;

/**
 * Two way communicator between entities.
 * 
 * @author kulikov
 */
public interface Dispatcher {

    /**
     * Trigered when new event detected.
     * 
     * @param event the event detected.
     */
    public void onEvent(EventName event);

    /**
     * Indicates that operation is completed
     */
    public void completed();

    /**
     * Request for endpoint.
     * 
     * @return endpoint object.
     */
    public Endpoint getEndpoint();

    /**
     * Request for connection.
     * 
     * @param  ID the local identifier of the connection.
     * @return media server connection
     */
    public Connection getConnection(String ID) throws UnknownActivityException;
}

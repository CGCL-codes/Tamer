package org.nakedobjects.remoting.server;

import java.io.IOException;
import org.nakedobjects.commons.exceptions.NakedObjectException;
import org.nakedobjects.remoting.client.ClientConnection;
import org.nakedobjects.remoting.exchange.Request;
import org.nakedobjects.remoting.facade.ServerFacade;
import org.nakedobjects.remoting.protocol.ServerMarshaller;

/**
 * Acts as the mediator between the {@link ServerMarshaller} (which pulls stuff off the
 * transport and pushes stuff on) and the {@link ServerFacade}, ie the rest of the Naked Objects System.
 * 
 * @see ClientConnection
 */
public interface ServerConnection {

    ServerFacade getServerFacade();

    Request readRequest() throws IOException;

    void sendResponse(Object response) throws IOException;

    void sendError(NakedObjectException exception) throws IOException;
}

package org.objectstyle.cayenne.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface of a Cayenne remote service.
 * 
 * @since 1.2
 * @see org.objectstyle.cayenne.remote.hessian.service.HessianServlet
 * @author Andrus Adamchik
 */
public interface RemoteService extends Remote {

    /**
     * Establishes a dedicated session with Cayenne DataChannel, returning session id.
     */
    RemoteSession establishSession() throws RemoteException;

    /**
     * Creates a new session with the specified or joins an existing one. This method is
     * used to bootstrap collaborating clients of a single "group chat".
     */
    RemoteSession establishSharedSession(String name) throws RemoteException;

    /**
     * Processes message on a remote server, returning the result of such processing.
     */
    Object processMessage(ClientMessage message) throws RemoteException, Throwable;
}

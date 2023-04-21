package org.apache.http.nio.reactor;

import java.net.SocketAddress;

/**
 * ConnectingIOReactor represents an I/O reactor capable of establishing 
 * connections to remote hosts.
 * 
 *
 * @version $Revision: 744545 $
 *
 * @since 4.0
 */
public interface ConnectingIOReactor extends IOReactor {

    /**
     * Requests a connection to a remote host.
     * <p>
     * Opening a connection to a remote host usually tends to be a time 
     * consuming process and may take a while to complete. One can monitor and 
     * control the process of session initialization by means of the 
     * {@link SessionRequest} interface.
     * <p>
     * There are several parameters one can use to exert a greater control over 
     * the process of session initialization:
     * <p>
     * A non-null local socket address parameter can be used to bind the socket 
     * to a specific local address.
     * <p>
     * An attachment object can added to the new session's context upon 
     * initialization. This object can be used to pass an initial processing 
     * state to the protocol handler.
     * <p>
     * It is often desirable to be able to react to the completion of a session 
     * request asynchronously without having to wait for it, blocking the 
     * current thread of execution. One can optionally provide an implementation 
     * {@link SessionRequestCallback} instance to get notified of events related 
     * to session requests, such as request completion, cancellation, failure or 
     * timeout.
     * 
     * @param remoteAddress the socket address of the remote host.
     * @param localAddress the local socket address. Can be <code>null</code>,
     *    in which can the default local address and a random port will be used.
     * @param attachment the attachment object. Can be <code>null</code>.
     * @param callback interface. Can be <code>null</code>.
     * @return session request object.
     */
    SessionRequest connect(SocketAddress remoteAddress, SocketAddress localAddress, Object attachment, SessionRequestCallback callback);
}

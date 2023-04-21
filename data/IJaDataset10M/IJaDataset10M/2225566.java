package org.exolab.jms.net.rmi;

import java.rmi.RemoteException;
import java.rmi.AccessException;
import java.rmi.server.RemoteObject;
import java.security.Principal;
import org.exolab.jms.net.connector.ManagedConnectionAcceptorListener;
import org.exolab.jms.net.connector.Authenticator;
import org.exolab.jms.net.connector.ResourceException;
import org.exolab.jms.net.uri.URIHelper;

/**
 * A factory for {@link RMIInvoker} instances.
 *
 * @author <a href="mailto:tma@netspace.net.au">Tim Anderson</a>
 * @version $Revision: 1.2 $ $Date: 2005/04/17 14:01:30 $
 */
public class RMIInvokerFactoryImpl extends RemoteObject implements RMIInvokerFactory {

    /**
     * The connection authenticator.
     */
    private final Authenticator _authenticator;

    /**
     * The connection acceptor that constructed this.
     */
    private final RMIManagedConnectionAcceptor _acceptor;

    /**
     * The listener to delegate accepted connections to.
     */
    private final ManagedConnectionAcceptorListener _listener;

    /**
     * Construct a new <code>RMIInvokerFactoryImpl</code>.
     *
     * @param authenticator the connection authenticator
     * @param acceptor the connection acceptor that constructed this
     * @param listener the listsner to delegate new connections to
     */
    public RMIInvokerFactoryImpl(Authenticator authenticator, RMIManagedConnectionAcceptor acceptor, ManagedConnectionAcceptorListener listener) {
        _authenticator = authenticator;
        _acceptor = acceptor;
        _listener = listener;
    }

    /**
     * Creates an authenticated invoker.
     *
     * @param principal the security principal
     * @param client    the invoker for performing invocations back to the
     *                  client
     * @param uri       the URI representing the client
     * @return an authenticated invoker
     * @throws RemoteException for any remote error
     */
    public RMIInvoker createInvoker(Principal principal, RMIInvoker client, String uri) throws RemoteException {
        try {
            if (!_authenticator.authenticate(principal)) {
                throw new AccessException("Failed to authenticate");
            }
        } catch (ResourceException exception) {
            throw new AccessException("Failed to authenticate", exception);
        }
        RMIInvokerImpl invoker = new RMIInvokerImpl();
        RMIManagedConnection connection = new RMIManagedConnection(principal, invoker, _acceptor.getURI(), client, URIHelper.parse(uri));
        _listener.accepted(_acceptor, connection);
        return invoker;
    }
}

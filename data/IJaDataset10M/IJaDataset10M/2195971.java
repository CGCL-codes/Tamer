package net.sf.asyncobjects.management.samples.echo;

import net.sf.asyncobjects.AsyncObject;
import net.sf.asyncobjects.Promise;
import net.sf.asyncobjects.net.ASocket;

/**
 * Network service.
 * 
 * @author cap
 * 
 */
public interface ANetworkService extends AsyncObject {

    /**
	 * Set service context. This method is called before first connection is
	 * passed to {@link #handleConnection(ASocket)} method. If this method fail,
	 * the service is considered to be uninitialized.
	 * 
	 * @param context
	 *            a context of network service
	 * @return when it is safe to call handleConnection() method on the service.
	 *         I
	 */
    Promise setContext(ANeworkServiceContext context);

    /**
	 * Handle newtork connection
	 * 
	 * @param socket
	 *            a socket
	 * @return when connection stopped to be handled. A failure is logged and
	 *         ignored by service runtime.
	 */
    Promise handleConnection(ASocket socket);

    /**
	 * Unset service context. This method is called before stopping to using
	 * this object as handler for network connections. is passed to
	 * {@link #handleConnection(ASocket)} method.
	 * 
	 * @param context
	 *            a context of network service
	 * @return when it is safe to call handleConnection() method on the service.
	 */
    Promise unsetContext(ANeworkServiceContext context);
}

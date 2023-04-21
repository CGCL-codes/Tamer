package org.apache.xmlrpc.server;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcHandler;

/** Maps from a handler name to a handler object.
 * @since 1.2
 */
public interface XmlRpcHandlerMapping {

    /** Return the handler for the specified handler name.
	 * @param handlerName The name of the handler to retrieve.
	 * @return Object The desired handler. Never null, an exception
	 * is thrown if no such handler is available.
	 * @throws XmlRpcNoSuchHandlerException The handler is not available.
	 * @throws XmlRpcException An internal error occurred.
	 */
    public XmlRpcHandler getHandler(String handlerName) throws XmlRpcNoSuchHandlerException, XmlRpcException;
}

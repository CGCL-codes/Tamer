package org.apache.xmlrpc.test;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.server.XmlRpcServer;

/** This interface allows to perform a unit test with various
 * transports. Basically, the implementation creates the client,
 * including the transport, and the server, if required.
 */
public interface ClientProvider {

    /** Returns the clients default configuration.
	 * @return The clients configuration.
	 * @throws Exception Creating the configuration failed.
	 */
    XmlRpcClientConfigImpl getConfig() throws Exception;

    /** Returns a new client instance.
	 * @return A client being used for performing the test.
	 */
    XmlRpcClient getClient();

    /** Returns the providers server instance.
     * @return A server instance, which is being used for performing the test.
	 */
    XmlRpcServer getServer();

    /** Performs a shutdown of the server.
     */
    void shutdown();
}

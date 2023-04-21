package org.palo.api;

import java.io.InputStream;
import java.util.Properties;

/**
 * <code>ConnectionFactory</code>
 * 
 * <p>An instance of <code>ConnectionFactory</code> is obtained with the 
 * {@link #getInstance()} method. Subsequently a connection to a PALO server
 * can be created by invoking the
 * {@link #newConnection(String, String, String, String)} method.
 * </p>
 * 
 * <p>Example:
 * <pre>
        Connection c = ConnectionFactory.getInstance().newConnection(
            &quot;localhost&quot;,
            &quot;1234&quot;,
            &quot;user&quot;,
            &quot;pass&quot;);
            
         // use the connection here
         c.disconnect();
 * </pre>
 * </p>
 *
 * @author Stepan Rutz
 * @version $Id: ConnectionFactory.java,v 1.20 2008/04/04 10:28:41 ArndHouben Exp $
 * 
 * @see org.palo.api.PaloAPIException
 */
public abstract class ConnectionFactory {

    private static ConnectionFactory instance;

    static {
        try {
            Properties props = new Properties(System.getProperties());
            InputStream propsStream = ConnectionFactory.class.getResourceAsStream("/paloapi.properties");
            if (propsStream != null) {
                props.load(propsStream);
                if (props.containsKey("wpalo")) System.setProperty("wpalo", props.getProperty("wpalo"));
                if (props.containsKey("xmla_ignoreVariableCubes")) System.setProperty("xmla_ignoreVariableCubes", props.getProperty("xmla_ignoreVariableCubes"));
                Properties sysProps = System.getProperties();
                for (Object key : props.keySet()) {
                    if (!sysProps.containsKey(key)) {
                        sysProps.put(key, props.get(key));
                    }
                }
                System.setProperties(sysProps);
            }
            instance = (ConnectionFactory) Class.forName("org.palo.api.impl.ConnectionFactoryImpl").newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ConnectionFactory getInstance() {
        return instance;
    }

    /**
     * Creates a new {@link Connection} which is connected to the specified
     * palo-server. The default connection is of type <code>HTTP</code> and no
	 * load on demand is activated. To
     * define the connection type and to use load on demand please call
     * {@link #newConnection(String, String, String, String, boolean, int)}
     * 
     * @param server the server to connect to.
     * @param service the service to use (corresponds to port numbers given as a string)
     * @param user the username to use for authentication
     * @param pass the password to use for authentication
     * @return the palo-server connection upon success
     * @throws PaloAPIException thrown if connecting failed.
     * @deprecated please use {@link #newConnection(ConnectionConfiguration)
     */
    public abstract Connection newConnection(String server, String service, String user, String pass);

    /**
     * Creates a new {@link Connection} which is connected to the specified
     * palo-server using specified type (legacy of HTTP). Load on demand can
	 * be used. In this case the API tries to load only the information which
	 * is currently required.
     *
     * @param server the server to connect to.
     * @param service the service to use (corresponds to port numbers given as a string)
     * @param user the username to use for authentication
     * @param pass the password to use for authentication
	 * @param doLoadOnDemand activate load on demand
     * @param type palo server type to be used. Please use one of the defined
     * constants {@link Connection#TYPE_LEGACY} or {@link Connection#TYPE_HTTP}
     * @return the palo-server connection upon success
     * @throws PaloAPIException thrown if connecting failed.
     * @deprecated please use {@link #newConnection(ConnectionConfiguration)
     */
    public abstract Connection newConnection(String server, String service, String user, String pass, boolean doLoadOnDemand, int type);

    /**
     * Creates a new {@link ConnectionConfiguration} instance. Only the name
     * of the palo server host and its service are set. All other fields
     * have their default values.
     * @param host host which runs the palo server
     * @param service the service which handles palo requests
     * @return new {@link ConnectionConfiguration} instance
     */
    public abstract ConnectionConfiguration getConfiguration(String host, String service);

    /**
     * Creates a new {@link ConnectionConfiguration} instance with the specified
     * settings for host, service, user and password.
     * @param host host which runs the palo server
     * @param service the service which handles palo requests
     * @param user the login name
     * @param password the login password
     * @return {@link ConnectionConfiguration} instance
     */
    public abstract ConnectionConfiguration getConfiguration(String host, String service, String user, String password);

    /**
     * Creates a new {@link Connection} using the connection settings from the
     * given {@link ConnectionConfiguration}
     * @param cfg {@link ConnectionConfiguration} containg connection settings
     * @return the palo-server connection upon success
     */
    public abstract Connection newConnection(ConnectionConfiguration cfg);
}

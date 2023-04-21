package org.exolab.jms.net.orb;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import org.exolab.jms.net.connector.Authenticator;

/**
 * Factory for {@link ORB} instances.
 *
 * @author <a href="mailto:tma@netspace.net.au">Tim Anderson</a>
 * @version $Revision: 1.2 $ $Date: 2005/05/03 13:45:58 $
 */
public final class ORBFactory {

    /**
     * Prevent construction of factory
     */
    private ORBFactory() {
    }

    /**
     * Construct a new <code>ORB</code>.
     *
     * @return a new <code>ORB</code>
     * @throws RemoteException for any error
     */
    public static ORB createORB() throws RemoteException {
        return new DefaultORB();
    }

    /**
     * Construct a new <code>ORB</code>.
     *
     * @param defaultURI the default URI for exported objects
     * @return a new <code>ORB</code>
     * @throws RemoteException for any error
     */
    public static ORB createORB(String defaultURI) throws RemoteException {
        Map properties = new HashMap();
        properties.put(ORB.PROVIDER_URI, defaultURI);
        return createORB(properties);
    }

    /**
     * Construct a new <code>ORB</code>.
     *
     * @param properties    configuration properties. May be <code>null</code>
     * @return a new <code>ORB</code>
     * @throws RemoteException for any error
     */
    public static ORB createORB(Map properties) throws RemoteException {
        return new DefaultORB(properties);
    }

    /**
     * Construct a new <code>ORB</code>.
     *
     * @param authenticator the connection authenticator
     * @param defaultURI    the default URI for exported objects
     * @return a new <code>ORB</code>
     * @throws RemoteException for any error
     */
    public static ORB createORB(Authenticator authenticator, String defaultURI) throws RemoteException {
        Map properties = new HashMap();
        properties.put(ORB.PROVIDER_URI, defaultURI);
        return createORB(authenticator, properties);
    }

    /**
     * Construct a new <code>ORB</code>.
     *
     * @param authenticator the connection authenticator
     * @param properties    configuration properties. May be <code>null</code>
     * @return a new <code>ORB</code>
     * @throws RemoteException for any error
     */
    public static ORB createORB(Authenticator authenticator, Map properties) throws RemoteException {
        ORB orb = new DefaultORB(authenticator, properties);
        return orb;
    }
}

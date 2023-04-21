package uk.ac.dl.dp.coreutil.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Collections;
import java.net.URL;
import java.util.Properties;
import javax.ejb.EJBHome;
import javax.ejb.EJBLocalHome;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.sql.DataSource;
import javax.mail.Session;
import org.apache.log4j.Logger;
import javax.persistence.EntityManager;

/**
 *
 * @author gjd37
 * @version
 */
public class CachingServiceLocator {

    private InitialContext ic;

    private Map cache;

    private boolean remote = false;

    private static Logger log = Logger.getLogger(CachingServiceLocator.class);

    private static CachingServiceLocator me;

    static {
        try {
            me = new CachingServiceLocator();
        } catch (NamingException se) {
            throw new RuntimeException(se);
        }
    }

    private CachingServiceLocator() throws NamingException {
        ic = new InitialContext();
        cache = Collections.synchronizedMap(new HashMap());
    }

    public static CachingServiceLocator getInstance() {
        return me;
    }

    private Object lookupImpl(String jndiName) throws NamingException {
        log.debug("Looking up: " + jndiName);
        Object cachedObj = cache.get(jndiName);
        if (cachedObj == null) {
            log.debug("Not in cache, doing a jndi lookup.");
            cachedObj = ic.lookup(jndiName);
            cache.put(jndiName, cachedObj);
        }
        return cachedObj;
    }

    public <T> Object lookup(String jndiHomeName) throws NamingException {
        if (remote) jndiHomeName += "Remote";
        return (Object) lookupImpl(jndiHomeName);
    }

    public <T> Queue lookupQueue(String jndiHomeName) throws NamingException {
        if (remote) jndiHomeName += "Remote";
        return (Queue) lookupImpl(jndiHomeName);
    }

    public void put(EntityManager em) {
        log.trace("inserting entityManager");
        cache.put("entityManager", em);
    }

    public EntityManager getEntityManager() {
        log.trace("Looking up entityManager");
        return (EntityManager) cache.get("entityManager");
    }

    /**
     * will get the ejb Local home factory. If this ejb home factory has already been
     * clients need to cast to the type of EJBHome they desire
     *
     * @return the EJB Home corresponding to the homeName
     */
    public EJBLocalHome getLocalHome(String jndiHomeName) throws NamingException {
        return (EJBLocalHome) lookupImpl(jndiHomeName);
    }

    /**
     * will get the ejb Remote home factory. If this ejb home factory has already been
     * clients need to cast to the type of EJBHome they desire
     *
     * @return the EJB Home corresponding to the homeName
     */
    public EJBHome getRemoteHome(String jndiHomeName, Class className) throws NamingException {
        Object objref = lookupImpl(jndiHomeName);
        return (EJBHome) PortableRemoteObject.narrow(objref, className);
    }

    /**
     * This method helps in obtaining the topic factory
     * @return the factory for the factory to get topic connections from
     */
    public ConnectionFactory getConnectionFactory(String connFactoryName) throws NamingException {
        return (ConnectionFactory) lookupImpl(connFactoryName);
    }

    /**
     * This method obtains the topc itself for a caller
     * @return the Topic Destination to send messages to
     */
    public Destination getDestination(String destName) throws NamingException {
        return (Destination) lookupImpl(destName);
    }

    /**
     * This method obtains the datasource
     * @return the DataSource corresponding to the name parameter
     */
    public DataSource getDataSource(String dataSourceName) throws NamingException {
        return (DataSource) lookupImpl(dataSourceName);
    }

    /**
     * This method obtains the mail session
     * @return the Session corresponding to the name parameter
     */
    public Session getSession(String sessionName) throws NamingException {
        return (Session) lookupImpl(sessionName);
    }

    /**
     * @return the URL value corresponding
     * to the env entry name.
     */
    public URL getUrl(String envName) throws NamingException {
        return (URL) lookupImpl(envName);
    }

    /**
     * @return the boolean value corresponding
     * to the env entry such as SEND_CONFIRMATION_MAIL property.
     */
    public boolean getBoolean(String envName) throws NamingException {
        Boolean bool = (Boolean) lookupImpl(envName);
        return bool.booleanValue();
    }

    /**
     * @return the String value corresponding
     * to the env entry name.
     */
    public String getString(String envName) throws NamingException {
        return (String) lookupImpl(envName);
    }
}

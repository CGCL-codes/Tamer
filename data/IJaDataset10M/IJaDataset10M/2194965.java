package edu.ucdavis.genomics.metabolomics.binbase.server.ejb.compounds;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import javax.management.MBeanServer;
import javax.management.MBeanServerInvocationHandler;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import org.apache.log4j.Logger;
import org.jboss.cache.CacheException;
import org.jboss.mx.util.MBeanServerLocator;
import edu.ucdavis.genomics.metabolomics.binbase.bci.Configurator;
import edu.ucdavis.genomics.metabolomics.binbase.bci.authentification.AuthentificationException;
import edu.ucdavis.genomics.metabolomics.binbase.bci.cache.Cache;
import edu.ucdavis.genomics.metabolomics.binbase.bci.server.jmx.ServiceJMXMBean;
import edu.ucdavis.genomics.metabolomics.exception.BinBaseException;
import edu.ucdavis.genomics.metabolomics.util.database.ConnectionFactory;
import edu.ucdavis.genomics.metabolomics.util.database.SimpleConnectionFactory;
import edu.ucdavis.genomics.metabolomics.util.database.WrappedConnection;

public class AbstractService {

    protected QueryHelper helper = new QueryHelper();

    private ServiceJMXMBean myServiceBean;

    private Logger logger = Logger.getLogger(getClass());

    public AbstractService() {
        super();
    }

    protected void closeConnection(Connection connection) {
        if (connection instanceof WrappedConnection) {
            WrappedConnection c = (WrappedConnection) connection;
            try {
                c.getConnection().close();
            } catch (SQLException e) {
                logger.warn(e.getMessage(), e);
            }
        }
    }

    /**
	 * gives us a connection for the specified username
	 */
    public Connection getConnection(String userName) throws BinBaseException {
        userName = userName.toLowerCase();
        try {
            ConnectionFactory factory = ConnectionFactory.createFactory();
            Properties p = Configurator.getDatabaseService().createProperties();
            p.setProperty(SimpleConnectionFactory.KEY_USERNAME_PROPERTIE, userName);
            factory.setProperties(p);
            logger.debug("done with factory setup!");
            Connection c = factory.getConnection();
            return c;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new BinBaseException(e);
        }
    }

    protected void initialize() throws BinBaseException {
        try {
            if (edu.ucdavis.genomics.metabolomics.binbase.bci.Configurator.getImportService().isDisableServices()) {
                throw new AuthentificationException("sorry all BinBase Service are disabled right now!");
            }
            MBeanServer server = MBeanServerLocator.locate();
            myServiceBean = (ServiceJMXMBean) MBeanServerInvocationHandler.newProxyInstance(server, new ObjectName("binbase:service=Import"), ServiceJMXMBean.class, false);
        } catch (BinBaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BinBaseException(e);
        }
    }

    /**
	 * is this key in the cache
	 * 
	 * @param key
	 * @return
	 * @throws MalformedObjectNameException
	 */
    protected boolean isContainedInCache(String key) throws BinBaseException {
        return getCache().contains(key);
    }

    /**
	 * loads the data from cache
	 * 
	 * @param key
	 * @return
	 * @throws MalformedObjectNameException
	 * @throws CacheException
	 */
    protected Serializable loadFromCache(String key) throws BinBaseException {
        return (Serializable) getCache().get(key);
    }

    /**
	 * puts it into the cache
	 * 
	 * @param key
	 * @param value
	 * @throws MalformedObjectNameException
	 * @throws CacheException
	 */
    protected void storeInCache(String key, Serializable value) throws BinBaseException {
        getCache().put(key, value);
    }

    /**
	 * get the cache
	 * 
	 * @return
	 * @throws MalformedObjectNameException
	 */
    protected Cache getCache() throws BinBaseException {
        try {
            return Configurator.getCacheService().getCache();
        } catch (Exception e) {
            throw new BinBaseException(e);
        }
    }
}

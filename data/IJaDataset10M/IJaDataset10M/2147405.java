package com.sun.j2ee.blueprints.servicelocator.web;

import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJBHome;
import javax.ejb.EJBLocalHome;
import javax.jms.ConnectionFactory;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import javax.sql.DataSource;
import javax.transaction.UserTransaction;
import javax.xml.rpc.Service;
import com.sun.j2ee.blueprints.servicelocator.ServiceLocatorException;

/**
 * This class is an implementation of the Service Locator pattern. It is
 * used to looukup resources such as EJBHomes, JMS Destinations, etc.
 * This implementation uses the "singleton" strategy and also the "caching"
 * strategy.
 * This implementation is intended to be used on the web tier and
 * not on the ejb tier.
 */
public final class ServiceLocator {

    private InitialContext ic;

    private final Map cache = Collections.synchronizedMap(new HashMap());

    private static ServiceLocator instance = new ServiceLocator();

    public static ServiceLocator getInstance() {
        return instance;
    }

    private ServiceLocator() throws ServiceLocatorException {
        try {
            ic = new InitialContext();
        } catch (Exception e) {
            throw new ServiceLocatorException(e);
        }
    }

    /**
     * will get the ejb Local home factory. If this ejb home factory has already been
     * clients need to cast to the type of EJBHome they desire
     *
     * @return the EJB Home corresponding to the homeName
     */
    public EJBLocalHome getLocalHome(final String jndiHomeName) throws ServiceLocatorException {
        EJBLocalHome home = (EJBLocalHome) cache.get(jndiHomeName);
        if (home == null) {
            try {
                home = (EJBLocalHome) ic.lookup(jndiHomeName);
                cache.put(jndiHomeName, home);
            } catch (Exception e) {
                throw new ServiceLocatorException(e);
            }
        }
        return home;
    }

    /**
     * will get the ejb Remote home factory. If this ejb home factory has already been
     * clients need to cast to the type of EJBHome they desire
     *
     * @return the EJB Home corresponding to the homeName
     */
    public EJBHome getRemoteHome(final String jndiHomeName, final Class className) throws ServiceLocatorException {
        EJBHome home = (EJBHome) cache.get(jndiHomeName);
        if (home == null) {
            try {
                final Object objref = ic.lookup(jndiHomeName);
                final Object obj = PortableRemoteObject.narrow(objref, className);
                home = (EJBHome) obj;
                cache.put(jndiHomeName, home);
            } catch (Exception e) {
                throw new ServiceLocatorException(e);
            }
        }
        return home;
    }

    /**
     * @return the factory for the factory to get queue connections from
     */
    public ConnectionFactory getJMSConnectionFactory(final String jmsConnFactoryName) throws ServiceLocatorException {
        ConnectionFactory factory = (ConnectionFactory) cache.get(jmsConnFactoryName);
        if (factory == null) {
            try {
                factory = (ConnectionFactory) ic.lookup(jmsConnFactoryName);
                cache.put(jmsConnFactoryName, factory);
            } catch (Exception e) {
                throw new ServiceLocatorException(e);
            }
        }
        return factory;
    }

    /**
     * @return the Queue Destination to send messages to
     */
    public javax.jms.Destination getJMSDestination(final String destName) throws ServiceLocatorException {
        javax.jms.Destination dest = (javax.jms.Destination) cache.get(destName);
        if (dest == null) {
            try {
                dest = (javax.jms.Destination) ic.lookup(destName);
                cache.put(destName, dest);
            } catch (Exception e) {
                throw new ServiceLocatorException(e);
            }
        }
        return dest;
    }

    /**
     * This method obtains the datasource itself for a caller
     * @return the DataSource corresponding to the name parameter
     */
    public DataSource getDataSource(final String dataSourceName) throws ServiceLocatorException {
        DataSource dataSource = (DataSource) cache.get(dataSourceName);
        if (dataSource == null) {
            try {
                dataSource = (DataSource) ic.lookup(dataSourceName);
                cache.put(dataSourceName, dataSource);
            } catch (Exception e) {
                throw new ServiceLocatorException(e);
            }
        }
        return dataSource;
    }

    /**
     * This method obtains the UserTransaction itself for a caller
     * @return the UserTransaction corresponding to the name parameter
     */
    public UserTransaction getUserTransaction(final String utName) throws ServiceLocatorException {
        try {
            return (UserTransaction) ic.lookup(utName);
        } catch (Exception e) {
            throw new ServiceLocatorException(e);
        }
    }

    /**
     * @return the URL value corresponding
     * to the env entry name.
     */
    public URL getUrl(final String envName) throws ServiceLocatorException {
        try {
            return (URL) ic.lookup(envName);
        } catch (Exception e) {
            throw new ServiceLocatorException(e);
        }
    }

    /**
     * @return the boolean value corresponding
     * to the env entry such as SEND_CONFIRMATION_MAIL property.
     */
    public boolean getBoolean(final String envName) throws ServiceLocatorException {
        try {
            return ((Boolean) ic.lookup(envName)).booleanValue();
        } catch (Exception e) {
            throw new ServiceLocatorException(e);
        }
    }

    /**
     * @return the String value corresponding
     * to the env entry name.
     */
    public String getString(final String envName) throws ServiceLocatorException {
        try {
            return (String) ic.lookup(envName);
        } catch (Exception e) {
            throw new ServiceLocatorException(e);
        }
    }

    public java.rmi.Remote getPort(final String envName, Class portInterface) throws ServiceLocatorException {
        try {
            Service service = (Service) ic.lookup(envName);
            return service.getPort(portInterface);
        } catch (Exception e) {
            throw new ServiceLocatorException(e);
        }
    }
}

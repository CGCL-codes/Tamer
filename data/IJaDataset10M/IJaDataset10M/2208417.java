package onepoint.persistence.hibernate;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * This class function as a cache for Hibernate configurations and session factories.
 *
 * @author calin.pavel
 */
public class OpHibernateCache {

    private Map<String, Configuration> configurations = new HashMap<String, Configuration>();

    private Map<String, SessionFactory> sessionFactories = new HashMap<String, SessionFactory>();

    private static OpHibernateCache instance;

    /**
    * Returns a singleton instance of <code>OpHibernateCache</code>.
    *
    * @return instance
    */
    public static OpHibernateCache getInstance() {
        if (instance == null) {
            instance = new OpHibernateCache();
        }
        return instance;
    }

    /**
    * Add configurations to cache.
    *
    * @param configurationName name/key for the configuration to be added.
    * @param configuration     configuration to add.
    */
    public void addConfiguration(String configurationName, Configuration configuration) {
        configurations.put(configurationName, configuration);
    }

    /**
    * Returns configuration mapped with a given name or NULL if none can be found.
    *
    * @param configurationName configuration name
    * @return configuration mapped to that name or NULL if none can be found.
    */
    public Configuration getConfiguration(String configurationName) {
        return configurations.get(configurationName);
    }

    /**
    * Add a session factory to cache.
    *
    * @param configurationName name/key for the session factory to be added.
    * @param sessionFactory    the session factory to add.
    */
    public void addSessionFactory(String configurationName, SessionFactory sessionFactory) {
        sessionFactories.put(configurationName, sessionFactory);
    }

    /**
    * Returns the session factory mapped with a given name or NULL if none can be found.
    *
    * @param configurationName the session factory name
    * @return the session factory mapped to that name or NULL if none can be found.
    */
    public SessionFactory getSessionFactory(String configurationName) {
        return sessionFactories.get(configurationName);
    }

    /**
    * 
    * @return 
    * @pre
    * @post
    */
    public Iterator<String> getConfigurationNames() {
        return configurations.keySet().iterator();
    }
}

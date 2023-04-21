package org.openlogbooks.hibernate;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.EmptyInterceptor;
import org.hibernate.Interceptor;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

/**
 * Basic Hibernate helper class for Hibernate configuration and startup.
 * <p>
 * Uses a static initializer to read startup options and initialize
 * <tt>Configuration</tt> and <tt>SessionFactory</tt>.
 * <p>
 * This class also tries to figure out if JNDI binding of the
 * <tt>SessionFactory</tt> is used, otherwise it falls back to a global static
 * variable (Singleton). If you use this helper class to obtain a
 * <tt>SessionFactory</tt> in your code, you are shielded from these deployment
 * differences.
 * <p>
 * Another advantage of this class is access to the <tt>Configuration</tt>
 * object that was used to build the current <tt>SessionFactory</tt>. You can
 * access mapping metadata programmatically with this API, and even change it
 * and rebuild the <tt>SessionFactory</tt>.
 * <p>
 * If you want to assign a global interceptor, set its fully qualified class
 * name with the system (or hibernate.properties/hibernate.cfg.xml) property
 * <tt>hibernate.util.interceptor_class</tt>. It will be loaded and instantiated
 * on static initialization of HibernateUtil; it has to have a no-argument
 * constructor. You can call <tt>HibernateUtil.getInterceptor()</tt> if you need
 * to provide settings before using the interceptor.
 * <p>
 * Note: This class supports annotations by default, hence needs JDK 5.0 and the
 * Hibernate Annotations library on the classpath. Change the single commented
 * line in the source to make it compile and run on older JDKs with XML mapping
 * files only.
 * <p>
 * Note: This class supports only one data store. Support for several
 * <tt>SessionFactory</tt> instances can be easily added (through a static
 * <tt>Map</tt>, for example). You could then lookup a <tt>SessionFactory</tt>
 * by its name.
 * 
 * @author christian@hibernate.org
 */
public class HibernateUtil {

    private static Log log = LogFactory.getLog(HibernateUtil.class);

    private static final String INTERCEPTOR_CLASS = "hibernate.util.interceptor_class";

    private static Configuration configuration;

    private static SessionFactory sessionFactory;

    static {
        try {
            configuration = new AnnotationConfiguration();
            configuration.setEntityResolver(new ImportFromClasspathEntityResolver());
            configuration.configure();
            setInterceptor(configuration, null);
            if (configuration.getProperty(Environment.SESSION_FACTORY_NAME) != null) {
                configuration.buildSessionFactory();
            } else {
                sessionFactory = configuration.buildSessionFactory();
            }
        } catch (Throwable ex) {
            log.error("Building SessionFactory failed.", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
	 * Returns the original Hibernate configuration.
	 * 
	 * @return Configuration
	 */
    public static Configuration getConfiguration() {
        return configuration;
    }

    /**
	 * Returns the global SessionFactory.
	 * 
	 * @return SessionFactory
	 */
    public static SessionFactory getSessionFactory() {
        SessionFactory sf = null;
        String sfName = configuration.getProperty(Environment.SESSION_FACTORY_NAME);
        if (sfName != null) {
            log.debug("Looking up SessionFactory in JNDI.");
            try {
                sf = (SessionFactory) new InitialContext().lookup(sfName);
            } catch (NamingException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            sf = sessionFactory;
        }
        if (sf == null) throw new IllegalStateException("SessionFactory not available.");
        return sf;
    }

    /**
	 * Closes the current SessionFactory and releases all resources.
	 * <p>
	 * The only other method that can be called on HibernateUtil after this one
	 * is rebuildSessionFactory(Configuration).
	 */
    public static void shutdown() {
        log.debug("Shutting down Hibernate.");
        getSessionFactory().close();
        configuration = null;
        sessionFactory = null;
    }

    /**
	 * Rebuild the SessionFactory with the static Configuration.
	 * <p>
	 * This method also closes the old SessionFactory before, if still open.
	 * Note that this method should only be used with static SessionFactory
	 * management, not with JNDI or any other external registry.
	 */
    public static void rebuildSessionFactory() {
        log.debug("Using current Configuration for rebuild.");
        rebuildSessionFactory(configuration);
    }

    /**
	 * Rebuild the SessionFactory with the given Hibernate Configuration.
	 * <p>
	 * HibernateUtil does not configure() the given Configuration object, it
	 * directly calls buildSessionFactory(). This method also closes the old
	 * SessionFactory before, if still open.
	 * 
	 * @param cfg
	 */
    public static void rebuildSessionFactory(Configuration cfg) {
        log.debug("Rebuilding the SessionFactory from given Configuration.");
        synchronized (sessionFactory) {
            if (sessionFactory != null && !sessionFactory.isClosed()) sessionFactory.close();
            if (cfg.getProperty(Environment.SESSION_FACTORY_NAME) != null) cfg.buildSessionFactory(); else sessionFactory = cfg.buildSessionFactory();
            configuration = cfg;
        }
    }

    /**
	 * Register a Hibernate interceptor with the current SessionFactory.
	 * <p>
	 * Every Session opened is opened with this interceptor after registration.
	 * Has no effect if the current Session of the thread is already open,
	 * effective on next close()/getCurrentSession().
	 * <p>
	 * Attention: This method effectively restarts Hibernate. If you need an
	 * interceptor active on static startup of HibernateUtil, set the
	 * <tt>hibernateutil.interceptor</tt> system property to its fully qualified
	 * class name.
	 */
    public static SessionFactory registerInterceptorAndRebuild(Interceptor interceptor) {
        log.debug("Setting new global Hibernate interceptor and restarting.");
        setInterceptor(configuration, interceptor);
        rebuildSessionFactory();
        return getSessionFactory();
    }

    public static Interceptor getInterceptor() {
        return configuration.getInterceptor();
    }

    /**
	 * Resets global interceptor to default state.
	 */
    public static void resetInterceptor() {
        log.debug("Resetting global interceptor to configuration setting");
        setInterceptor(configuration, null);
    }

    /**
	 * Either sets the given interceptor on the configuration or looks it up
	 * from configuration if null.
	 */
    private static void setInterceptor(Configuration configuration, Interceptor interceptor) {
        String interceptorName = configuration.getProperty(INTERCEPTOR_CLASS);
        if (interceptor == null && interceptorName != null) {
            try {
                Class<?> interceptorClass = HibernateUtil.class.getClassLoader().loadClass(interceptorName);
                interceptor = (Interceptor) interceptorClass.newInstance();
            } catch (Exception ex) {
                throw new RuntimeException("Could not configure interceptor: " + interceptorName, ex);
            }
        }
        if (interceptor != null) {
            configuration.setInterceptor(interceptor);
        } else {
            configuration.setInterceptor(EmptyInterceptor.INSTANCE);
        }
    }
}

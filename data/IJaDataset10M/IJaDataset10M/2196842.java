package org.jcvi.glk.session;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

/**
 * The <code>SessionServiceCleanupHandler</code> is a {@link Thread} which was specifically 
 * designed to be supplied to {@link Runtime#addShutdownHook(Thread)} to handle cleanup on a
 * {@link SessionService} object.  In most cases, <code>SessionService</code>s can simply be 
 * left for garbage collecting to clean up, however, it is a little nicer to clean them up
 * specifically.  Using this handler can also be used as a safe way of removing the boring code
 * of tearing down a <code>SessionService</code> after an application is finished using it.
 * <p>
 * Because this handler is implemented as a {@link Thread}, both the standard 
 * <code>Thread</code> and {@link Runnable} APIs may be used to invoke this handler outside of
 * its intended use as a shutdown hook.
 * 
 * @author jsitz@jcvi.org
 */
public class SessionServiceShutdownHandler extends Thread {

    /**
     * Registers a <code>SessionServiceCleanupHandler</code> which will shut down the supplied
     * {@link SessionService}.
     * 
     * @param service The {@link SessionService} to shut down.
     * @param logger The Log4J {@link Logger} to report status to, or <code>null</code> if
     * no reporting is desired.
     * @see #SessionServiceCleanupHandler(SessionService, Logger)
     */
    public static void addShutdownHandler(SessionService service, Logger logger) {
        Runtime.getRuntime().addShutdownHook(new SessionServiceShutdownHandler(service, logger));
    }

    /**
     * Registers a <code>SessionServiceCleanupHandler</code> which will shut down the supplied
     * {@link SessionService}.
     * 
     * @param service The {@link SessionService} to shut down.
     * @see #SessionServiceCleanupHandler(SessionService)
     */
    public static void addShutdownHandler(SessionService service) {
        Runtime.getRuntime().addShutdownHook(new SessionServiceShutdownHandler(service));
    }

    /** The {@link SessionService} to shut down. */
    private SessionService service;

    /** The {@link Logger} to log messages to, if any. */
    private Logger logger;

    /**
     * Creates a new handler which will clean up the supplied {@link SessionService}.  Status
     * messages will be delivered to the provided {@link Logger} if it is not <code>null</code>.
     * 
     * @param service The {@link SessionService} to shut down.
     * @param logger The Log4J {@link Logger} to report status to, or <code>null</code> if
     * no reporting is desired.
     */
    public SessionServiceShutdownHandler(SessionService service, Logger logger) {
        super();
        this.service = service;
        this.logger = logger;
    }

    /**
     * Creates a new handler which will clean up the supplied {@link SessionService}.
     * 
     * @param service The {@link SessionService} to shut down.
     */
    public SessionServiceShutdownHandler(SessionService service) {
        this(service, null);
    }

    @Override
    public void run() {
        SessionFactory factory = this.service.getSessionFactory();
        if (factory.isClosed()) {
            if (this.logger != null) {
                this.logger.debug("Avoided shutdown on previously closed SessionService: " + this.getServiceName());
            }
            return;
        }
        if (this.logger != null) {
            this.logger.debug("Shutting down SessionService: " + this.getServiceName());
        }
        try {
            factory.close();
            if (this.logger != null) {
                this.logger.debug("SessionService has been shut down: " + this.getServiceName());
            }
        } catch (final HibernateException e) {
            if (this.logger != null) {
                this.logger.error("Error while shutting down SessionService: " + this.getServiceName(), e);
            }
        }
    }

    private String getServiceName() {
        Configuration config = this.service.getConfiguration();
        String datasource = config.getProperty(Environment.DATASOURCE);
        if (datasource != null && datasource.length() > 0) return datasource;
        return config.getProperty(Environment.URL);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((logger == null) ? 0 : logger.hashCode());
        result = prime * result + ((service == null) ? 0 : service.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object thatObj) {
        if (this == thatObj) return true;
        if (thatObj == null) return false;
        if (getClass() != thatObj.getClass()) return false;
        SessionServiceShutdownHandler that = (SessionServiceShutdownHandler) thatObj;
        if (this.logger == null) {
            if (that.logger != null) return false;
        } else if (!this.logger.equals(that.logger)) return false;
        if (this.service == null) {
            if (that.service != null) return false;
        } else if (!this.service.equals(that.service)) return false;
        return true;
    }
}

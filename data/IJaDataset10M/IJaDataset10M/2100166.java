package org.nexopenframework.util.sql;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hsqldb.DatabaseManager;
import org.hsqldb.Server;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * <p>NexTReT Open Framework</p>
 * 
 * <p> Utility class for dealing with tests, starts and shutdowns a HSQL Database. Avoids integration 
 * and dependency of  a given database providing a way to start and stop a embedded database.</p>
 * 
 * @author <a href="mailto:fme@nextret.net">Francesc Xavier Magdaleno</a>
 * @version 1.0
 * @since 1.0
 * @see Server#start()
 * @see Server#stop()
 * @see #destroy()
 */
public class HSQLServer implements InitializingBean, DisposableBean {

    /** {@link org.apache.commons.logging} logging facility  */
    private Log logger = LogFactory.getLog(this.getClass());

    /**HSQL Database server*/
    private Server server;

    /**
	 * Default silent.
	 */
    private boolean silent = true;

    /**
	 * Default trace.
	 */
    private boolean trace = false;

    /**
	 * Default no_system_exit New embedded support in 1.7
	 */
    private boolean noSystemExit = true;

    /**
	 * Default database name r: <code>nexopen</code>.
	 */
    private String name = "nexopen";

    /**
	 * Default port r: <code>1701</code>.
	 */
    private int port = 1701;

    /**
	 * @return
	 */
    public String getDatabaseName() {
        return name;
    }

    public void setDatabaseName(String name) {
        this.name = name;
    }

    public boolean isNoSystemExit() {
        return noSystemExit;
    }

    public void setNoSystemExit(boolean noSystemExit) {
        this.noSystemExit = noSystemExit;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isSilent() {
        return silent;
    }

    public void setSilent(boolean silent) {
        this.silent = silent;
    }

    public boolean isTrace() {
        return trace;
    }

    public void setTrace(boolean trace) {
        this.trace = trace;
    }

    /**
	 * 
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
    public void afterPropertiesSet() {
        server = new Server();
        server.setPort(this.port);
        server.setTrace(this.trace);
        server.setNoSystemExit(this.noSystemExit);
        server.setDatabaseName(0, this.name);
        this.setSilent(silent);
        server.start();
        if (logger.isInfoEnabled()) {
            logger.info("Started HSQLDB State :: " + server.getStateDescriptor());
        }
    }

    /**
	 * <p>closes the HSQL database</p>
	 * 
	 * @see DatabaseManager#closeDatabases(int)
	 */
    public void destroy() {
        try {
            if (logger.isInfoEnabled()) {
                logger.info("Shutdwon timer ...");
            }
            DatabaseManager.getTimer().shutDown();
            if (logger.isInfoEnabled()) {
                logger.info("closing databases ...");
            }
            DatabaseManager.closeDatabases(-1);
        } catch (RuntimeException e) {
            if (logger.isInfoEnabled()) {
                logger.info("Exception arised closing HSQLDB [" + e + "] :: " + e.getMessage(), e);
            }
        } catch (Throwable e) {
            if (logger.isInfoEnabled()) {
                logger.info("Unexpected Exception arised closing HSQLDB [" + e + "] :: " + e.getMessage(), e);
            }
        } finally {
            try {
                server.shutdown();
            } catch (Throwable e) {
                if (logger.isInfoEnabled()) {
                    logger.info("Unexpected Exception arised closing databases in HSQLDB [" + e + "] :: " + e.getMessage());
                }
            }
        }
    }
}

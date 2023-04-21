package spring.kickstart.db.hsqldb;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hsqldb.DatabaseManager;
import org.hsqldb.ServerConfiguration;
import org.hsqldb.Database;
import org.hsqldb.persist.HsqlProperties;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import java.util.Properties;

/**
 * Bean that will start an instance of an HSQL database.  This class is primarily intended
 * to be used in demo applications.  It allows for a self contained distribution including
 * a database instance.
 *
 * This is an example of a bean configuration:
 *
 * <pre>
 *     &lt;bean id="dataBase" class="spring.kickstart.db.hsqldb.ServerBean" scope="singleton" lazy-init="false"&gt;
 *         &lt;property name="serverProperties"&gt;
 *             &lt;props&gt;
 *                 &lt;prop key="server.port"&gt;9101&lt;/prop&gt;
 *                 &lt;prop key="server.database.0"&gt;webapps/myapp/db/test&lt;/prop&gt;
 *                 &lt;prop key="server.dbname.0"&gt;test&lt;/prop&gt;
 *             &lt;/props&gt;
 *         &lt;/property&gt;
 *     &lt;/bean&gt;
 * </pre>
 *
 *
 * @author Thomas Risberg
 * @see org.hsqldb.Server
 */
public class ServerBean implements InitializingBean, DisposableBean {

    /**
     * Commons Logging instance.
     */
    private static final Log log = LogFactory.getLog(ServerBean.class);

    /**
     * Properties used to customize instance.
     */
    private Properties serverProperties;

    /**
     * The actual server instance.
     */
    private org.hsqldb.Server server;

    public Properties getServerProperties() {
        return serverProperties;
    }

    public void setServerProperties(Properties serverProperties) {
        this.serverProperties = serverProperties;
    }

    public void afterPropertiesSet() throws Exception {
        HsqlProperties configProps = serverProperties != null ? new HsqlProperties(serverProperties) : new HsqlProperties();
        ServerConfiguration.translateDefaultDatabaseProperty(configProps);
        server = new org.hsqldb.Server();
        server.setRestartOnShutdown(false);
        server.setNoSystemExit(true);
        server.setProperties(configProps);
        log.info("HSQL Server Startup sequence initiated");
        server.start();
        String portMsg = "port " + server.getPort();
        log.info("HSQL Server listening on " + portMsg);
    }

    public void destroy() {
        log.info("HSQL Server Shutdown sequence initiated");
        server.signalCloseAllServerConnections();
        server.stop();
        DatabaseManager.closeDatabases(Database.CLOSEMODE_NORMAL);
        log.info("HSQL Server Shutdown completed");
        server = null;
    }
}

package net.taylor.liquibase;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URL;
import java.sql.Connection;
import java.util.Enumeration;
import liquibase.FileOpener;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.ResourceLoader;
import org.jboss.seam.log.Log;

/**
 * NOTE: Control order of start up components by name sort order.
 * 
 * @author jgilbert01
 */
@Scope(ScopeType.APPLICATION)
@AutoCreate
@Install(precedence = Install.BUILT_IN)
public class SeamLiquibase {

    @Logger
    protected Log log;

    private String changeLog = "dbchangelog.xml";

    private Connection connection;

    private String hostExcludes;

    private String hostIncludes;

    private String contexts;

    private String defaultSchema;

    private boolean failOnError = true;

    @Observer("org.jboss.seam.postInitialization")
    public void init() throws Exception {
        if (!shouldRun()) {
            return;
        }
        log.info("Starting SeamLiquibase...");
        Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(connection);
        Liquibase liquibase = new Liquibase(getChangeLog(), new SeamResourceOpener(), database);
        liquibase.update(getContexts());
    }

    protected boolean shouldRun() throws Exception {
        String shouldRun = System.getProperty(Liquibase.SHOULD_RUN_SYSTEM_PROPERTY);
        if (shouldRun != null && !Boolean.valueOf(shouldRun)) {
            log.info("Conversion not run because system property: {0}, was set to false", Liquibase.SHOULD_RUN_SYSTEM_PROPERTY);
            return false;
        }
        String hostName = getHostName();
        if (getHostExcludes() != null && getHostExcludes().contains(hostName)) {
            log.info("Conversion not run on: {0}, due to excludes: {1}", hostName, getHostExcludes());
            return false;
        }
        if (getHostIncludes() != null && !getHostIncludes().contains(hostName)) {
            log.info("Conversion not run on: {0}, due to includes: {1}", hostName, getHostIncludes());
            return false;
        }
        return true;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public String getChangeLog() {
        return changeLog;
    }

    public void setChangeLog(String changeLog) {
        this.changeLog = changeLog;
    }

    public String getDefaultSchema() {
        return defaultSchema;
    }

    public void setDefaultSchema(String defaultSchema) {
        this.defaultSchema = defaultSchema;
    }

    public String getHostExcludes() {
        return hostExcludes;
    }

    public void setHostExcludes(String hostExcludes) {
        this.hostExcludes = hostExcludes;
    }

    public String getHostIncludes() {
        return hostIncludes;
    }

    public void setHostIncludes(String hostIncludes) {
        this.hostIncludes = hostIncludes;
    }

    public String getContexts() {
        return contexts;
    }

    public void setContexts(String contexts) {
        this.contexts = contexts;
    }

    public boolean isFailOnError() {
        return failOnError;
    }

    public void setFailOnError(boolean failOnError) {
        this.failOnError = failOnError;
    }

    public class SeamResourceOpener implements FileOpener {

        public InputStream getResourceAsStream(String fileName) throws IOException {
            return ResourceLoader.instance().getResourceAsStream(fileName);
        }

        public Enumeration<URL> getResources(String packageName) throws IOException {
            return null;
        }

        public ClassLoader toClassLoader() {
            return ResourceLoader.instance().getClass().getClassLoader();
        }
    }

    protected String getHostName() throws Exception {
        return InetAddress.getLocalHost().getHostName();
    }
}

package org.jboss.jaxr.juddi;

import org.apache.ws.scout.registry.ConnectionFactoryImpl;
import org.jboss.system.ServiceMBeanSupport;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.xml.registry.ConnectionFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * A JBoss MBean for the JUDDI Service
 * MBean for the JUDDI open source project integrated as part of the JAXR requirements
 * for J2EE 1.4 compliance
 *
 * @author <mailto:Anil.Saldhana@jboss.org>Anil Saldhana
 *         extends="org.jboss.system.ServiceMBean"
 * @since Nov 8, 2004
 */
public class JUDDIService extends ServiceMBeanSupport implements JUDDIServiceMBean {

    protected boolean dropOnStart = false;

    protected boolean createOnStart = false;

    protected boolean dropOnStop = false;

    protected boolean loadNAICS = false;

    protected boolean loadUNSPSC = false;

    protected DataSource datasource = null;

    protected String datasourceurl = null;

    protected String registryOperator = null;

    protected String bindJaxr = null;

    protected boolean shouldBindJaxr = true;

    protected boolean dropDB = false;

    private boolean jndiAlreadyBound = false;

    protected Connection getConnection() throws SQLException, NamingException {
        try {
            if (datasource == null) {
                InitialContext ctx = new InitialContext();
                Object obj = ctx.lookup(datasourceurl);
                log.debug(obj.getClass().getName());
                datasource = (DataSource) obj;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e);
        }
        return datasource.getConnection();
    }

    protected void runDrop() throws SQLException, IOException {
        log.debug("JUDDIService: Inside runDrop");
        locateAndRunScript("juddi_drop_db.ddl");
        log.debug("JUDDIService: Exit runDrop");
    }

    protected void runCreate() throws SQLException, IOException {
        log.debug("JUDDIService: Inside runCreate");
        locateAndRunScript("juddi_create_db.ddl");
        locateAndRunScript("juddi_data.ddl");
    }

    private void locateAndRunScript(String name) throws SQLException, IOException {
        log.debug("JUDDIService: Inside locateScript");
        InputStream input = getClass().getClassLoader().getResourceAsStream("META-INF/ddl/" + name);
        if (input != null) {
            try {
                runScript(input);
            } finally {
                input.close();
            }
        }
    }

    protected void runScript(InputStream stream) throws SQLException, IOException {
        boolean firstError = true;
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        Connection connection = null;
        try {
            connection = this.getConnection();
            if (connection != null) log.debug("Obtained the Connection");
            Statement statement = connection.createStatement();
            try {
                String nextStatement = "";
                String nextLine;
                while ((nextLine = reader.readLine()) != null) {
                    log.debug("Statement Obtained=" + nextLine);
                    nextLine = nextLine.trim();
                    if (nextLine.indexOf("--") != -1) continue;
                    int semicolon = nextLine.indexOf(";");
                    if (semicolon != -1) {
                        nextStatement += nextLine.substring(0, semicolon);
                        try {
                            log.debug("Statement to execute:" + nextStatement);
                            statement.execute(nextStatement);
                        } catch (SQLException e) {
                            String err = "Could not execute a statement of juddi script::";
                            if (firstError) {
                                log.debug(err + e.getLocalizedMessage() + " " + nextStatement);
                                log.debug("Your settings are:dropOnStart =" + dropOnStart + ";createOnStart =" + createOnStart);
                                log.debug("dropOnStop = " + dropOnStop);
                                firstError = false;
                            }
                        }
                        nextStatement = nextLine.substring(semicolon + 1);
                    } else {
                        nextStatement += nextLine;
                    }
                }
                if (!nextStatement.equals("")) {
                    try {
                        log.debug("Statement to execute:" + nextStatement);
                        statement.execute(nextStatement);
                    } catch (SQLException e) {
                        log.debug("Could not execute last statement of a juddi init script: " + e.getLocalizedMessage());
                        log.debug("Your settings are:dropOnStart =" + dropOnStart + ";createOnStart =" + createOnStart);
                        log.debug("dropOnStop = " + dropOnStop);
                    }
                }
            } finally {
                if (statement != null) statement.close();
            }
        } catch (NamingException nm) {
            log.error("Looks like DataSource was tried with the wrong JNDI name");
            log.error(nm);
        } finally {
            if (connection != null) connection.close();
        }
    }

    /**
     * starts the jUDDI service
     */
    public void startService() throws Exception {
        log.debug("JUDDIService: Inside startService with dropOnStart=" + dropOnStart + " createOnStart=" + createOnStart);
        if (shouldBindJaxr && !jndiAlreadyBound) {
            bindJAXRConnectionFactory();
        }
        if (dropOnStart) {
            runDrop();
        }
        if (createOnStart) {
            runCreate();
        }
    }

    /**
     * stop the service
     */
    public void stopService() throws Exception {
        log.debug("JUDDIService: Inside stopService with dropOnStop=" + dropOnStop);
        if (dropOnStop) {
            runDrop();
        }
        unBindJAXRConnectionFactory();
    }

    /**
     * @return boolean
     */
    public boolean isDropOnStart() {
        return dropOnStart;
    }

    /**
     * Sets the dropOnStart.
     *
     * @param dropOnStart The dropOnStart to set
     */
    public void setDropOnStart(boolean dropOnStart) {
        this.dropOnStart = dropOnStart;
    }

    /**
     * @return boolean
     */
    public boolean isDropOnStop() {
        return dropOnStop;
    }

    /**
     * Sets the dropOnStop.
     *
     * @param dropOnStop The dropOnStop to set
     */
    public void setDropOnStop(boolean dropOnStop) {
        this.dropOnStop = dropOnStop;
    }

    /**
     * @return boolean
     */
    public boolean isCreateOnStart() {
        return createOnStart;
    }

    /**
     * Sets the createOnStart.
     *
     * @param createOnStart The createOnStart to set
     */
    public void setCreateOnStart(boolean createOnStart) {
        this.createOnStart = createOnStart;
    }

    /**
     * @return String
     */
    public String getDataSource() {
        return datasourceurl;
    }

    /**
     * Sets the Datasource Url.
     *
     * @param ds The datasourceurl to set
     */
    public void setDataSourceUrl(String ds) {
        this.datasourceurl = ds;
    }

    /**
     * @return String
     */
    public String getRegistryOperator() {
        return registryOperator;
    }

    /**
     * Sets the RegistryOperator.
     *
     * @param ro The datasourceurl to set
     */
    public void setRegistryOperator(String ro) {
        this.registryOperator = ro;
    }

    /**
     * gets the JAXR ConnectionFactory.
     */
    public String getBindJaxr() {
        return bindJaxr;
    }

    /**
     * Sets the JAXR ConnectionFactory.
     *
     * @param str The context to bind the Jaxr factory to set
     */
    public void setBindJaxr(String str) {
        this.bindJaxr = str;
        if (this.shouldBindJaxr) {
            bindJAXRConnectionFactory();
        }
    }

    /**
     * gets the JAXR ConnectionFactory.
     */
    public boolean getShouldBindJaxr() {
        return shouldBindJaxr;
    }

    /**
     * Sets the JAXR ConnectionFactory.
     *
     * @param str Should a Jaxr Connection Factory bound
     */
    public void setShouldBindJaxr(boolean str) {
        this.shouldBindJaxr = str;
        if (shouldBindJaxr) {
            bindJAXRConnectionFactory();
        }
    }

    /**
     * gets the JAXR ConnectionFactory.
     */
    public boolean getDropDB() {
        return dropDB;
    }

    /**
     * Sets the JAXR ConnectionFactory.
     *
     * @param b Should a Jaxr Connection Factory bound
     */
    public void setDropDB(boolean b) {
        this.dropDB = b;
        try {
            if (datasource != null) this.runDrop();
        } catch (Exception e) {
            log.error(e.toString());
        }
    }

    private void bindJAXRConnectionFactory() {
        if (this.bindJaxr == null || jndiAlreadyBound) return;
        InitialContext ctx = null;
        try {
            ctx = new InitialContext();
        } catch (NamingException e) {
            log.error("JNDI InitialContext Failed");
            e.printStackTrace();
        }
        ConnectionFactory factory = (ConnectionFactory) ConnectionFactoryImpl.newInstance();
        try {
            ctx.rebind(bindJaxr, factory);
            jndiAlreadyBound = true;
        } catch (NamingException e) {
            log.error("JNDI Bind Failed:" + bindJaxr);
            e.printStackTrace();
        }
    }

    private void unBindJAXRConnectionFactory() {
        if (this.bindJaxr == null || jndiAlreadyBound) return;
        InitialContext ctx = null;
        try {
            ctx = new InitialContext();
        } catch (NamingException e) {
            log.error("JNDI InitialContext Failed");
            e.printStackTrace();
        }
        try {
            ctx.unbind(bindJaxr);
        } catch (NamingException e) {
        }
    }
}

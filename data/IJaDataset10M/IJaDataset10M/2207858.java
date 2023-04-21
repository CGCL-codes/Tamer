package org.hl7.test;

import com.apelon.apelonserver.client.*;
import com.apelon.common.log4j.LogConfigLoader;
import com.apelon.common.sql.ConnectionParams;
import junit.framework.TestCase;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.*;

/**
 * This is the base class for all client API tests.
 * <p>This loads the properties from CTSQueryTest.properties file which should be
 * placed in the working directory. To use CTSQueryTest.properties file from any
 * location, the system property "props.file" should be set to the file path
 * of CTSQueryTest.properties.
 * <p>The following system properties can be used:
 * <ol>
 * <li>sc.type : ServerConnection Type; Options - jdbc and securesocket; Default is jdbc
 * <li>db.type : For JDBC Server Connection type; Options - oracle and sql2k; Default is oracle
 * <li>username : Overrides the username from the properties file
 * <li>password : Overrides the password fron the properties file
 * </ol>
 * <p>Examples:
 * <ol>
 * <li>
 * To run the test on sql2k with properties file junit\sql2k\CTSQueryTest.properites,
 * use these VM parameters
 * <pre>
 * -Dprops.file=junit\sql2k\CTSQueryTest.properites -Ddb.type=sql2k
 * </pre>
 * <li>
 * To run the test using SecureSocket connection with default properties file,
 * use these VM parameters
 * <pre>
 * -Dsc.type=securesocket
 * </pre>
 * <li>
 * To run the test using SecureSocket connection with default properties file, but different
 * user information, use these VM parameters
 * <pre>
 * -Dsc.type=securesocket -Dusername=&lt;dtsuser&gt; -Dpassword=&lt;password&gt;
 * </pre>
 */
public class CTSQueryTest extends TestCase {

    protected static Logger logger = Logger.getLogger("org.hl7.test");

    static {
        Enumeration loggerEnum = LogManager.getLogManager().getLoggerNames();
        while (loggerEnum.hasMoreElements()) {
            String loggerName = (String) loggerEnum.nextElement();
            System.out.println("Logger:  " + loggerName);
        }
        try {
            logConfigLoader().loadDefault();
        } catch (ClassNotFoundException e) {
            System.err.print("Unable to initialize log4j logging.");
        }
    }

    protected ServerConnection sc;

    protected Properties props;

    protected String dbType;

    protected String jdbcDriver;

    protected String username;

    protected String password;

    protected String hostname;

    protected String instance;

    protected String port;

    public CTSQueryTest(String s) {
        super(s);
    }

    public void setUp() {
        try {
            setUpProps();
            String scType = System.getProperty("sc.type", "jdbc");
            if (scType.equalsIgnoreCase("jdbc")) {
                dbType = System.getProperty("db.type", "oracle");
                jdbcDriver = props.getProperty(dbType + ".jdbc.driver");
                username = props.getProperty(dbType + ".username");
                password = props.getProperty(dbType + ".password");
                hostname = props.getProperty(dbType + ".hostname");
                instance = props.getProperty(dbType + ".instance");
                port = props.getProperty(dbType + ".port");
                username = System.getProperty("username", username);
                password = System.getProperty("password", password);
                ConnectionParams cps = new ConnectionParams(dbType, username, password, hostname, port, instance, jdbcDriver);
                sc = new ServerConnectionJDBC(cps);
            } else if (scType.equalsIgnoreCase("SecureSocket")) {
                username = props.getProperty("ss.username");
                password = props.getProperty("ss.password");
                hostname = props.getProperty("ss.hostname");
                port = props.getProperty("ss.port");
                username = System.getProperty("username", username);
                password = System.getProperty("password", password);
                sc = new ServerConnectionSecureSocket(hostname, Integer.parseInt(port), username, password);
            } else if (scType.equalsIgnoreCase("Socket")) {
                hostname = props.getProperty("ss.hostname");
                port = props.getProperty("ss.port");
                sc = new ServerConnectionSocket(hostname, Integer.parseInt(port));
            } else {
                fail("Invalid Server Connection Type :" + scType);
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public static LogConfigLoader logConfigLoader() throws ClassNotFoundException {
        return new LogConfigLoader(".", "ctsquerylog.xml", Class.forName("org.hl7.test.CTSQueryTest"));
    }

    private void setUpProps() {
        String filePath = System.getProperty("props.file", "CTSQueryTest.properties");
        InputStream is = null;
        try {
            is = getClass().getResourceAsStream("..\\..\\test\\" + filePath);
            props = new Properties();
            props.load(is);
            is.close();
        } catch (IOException ioe) {
            fail("Properties could not be loaded from file " + filePath + "." + System.getProperty("line.separator") + ioe.getMessage());
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    logger.log(Level.WARNING, "Properties file could not be closed.", e);
                }
            }
        }
    }

    public void tearDown() {
        try {
            sc.close();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}

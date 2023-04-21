package com.risertech.xdav.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.jetty.plus.jndi.Resource;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.xml.XmlConfiguration;
import org.hsqldb.jdbc.jdbcDataSource;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public abstract class BaseTest {

    public static final String ICS_NORMAL_P_1PM = "Normal_Pacific_1pm.ics";

    public static final String ICS_NORMAL_P_1PM_UID = "EC93F93F-61CB-4404-8F10-8849012F3F5A";

    public static final String ICS_NORMAL_P_815AM = "Normal_Pacific_815am.ics";

    public static final String ICS_DAILY_NY_5PM = "Daily_NY_5pm.ics";

    public static final String ICS_DAILY_NY_5PM_UID = "DE916949-731D-4DAE-BA93-48A38B2B2030";

    public static final String ICS_DAILY_NY_5PM_SUMMARY = "Daily_NY_5pm";

    public static final String VCF_ARLEN_JOHNSON = "Arlen Johnson.vcf";

    protected static final String HOST = System.getProperty("jetty.host", "localhost");

    protected static final int PORT = Integer.getInteger("jetty.port", 8081).intValue();

    protected static final String USER = "caluser1";

    protected static final String PASSWORD = "bedework";

    private static final boolean embedded = !Boolean.getBoolean("external");

    private static final String jetty_default = new File("jetty-7.0.0.M2").toURI().getPath();

    private static final String jetty_home = System.getProperty("jetty.home", jetty_default);

    private static Server server;

    static {
        if (embedded) {
            jdbcDataSource calDB = new jdbcDataSource();
            calDB.setDatabase("jdbc:hsqldb:mem:calDB");
            calDB.setUser("sa");
            calDB.setPassword("");
            try {
                setupDB(calDB.getConnection(), new File(jetty_home + "schema.sql"));
                Class<?> clazz = createRestoreClassloader().loadClass("org.bedework.dumprestore.restore.Restore");
                Method method = clazz.getMethod("main", String[].class);
                String[] arguments = new String[] { "-appname", "dumpres", "-f", jetty_home + "initbedework-sparse.xml", "-initSyspars" };
                method.invoke(null, new Object[] { arguments });
            } catch (Throwable t) {
                t.printStackTrace();
                throw new RuntimeException(t);
            }
        }
    }

    private static ClassLoader createRestoreClassloader() throws Exception {
        List<URL> urls = new ArrayList<URL>();
        urls.add(new File(jetty_home + "dumpres/classes").toURL());
        File file = new File(jetty_home + "dumpres/lib");
        for (File jar : file.listFiles()) {
            urls.add(jar.toURL());
        }
        return new URLClassLoader(urls.toArray(new URL[0]), BaseTest.class.getClassLoader());
    }

    @BeforeClass
    public static void setupServer() throws Exception {
        if (embedded) {
            setupJetty();
        }
    }

    public static void setupJetty() throws Exception {
        System.setProperty("jetty.home", jetty_home);
        server = new Server(PORT);
        XmlConfiguration configuration = new XmlConfiguration(new FileInputStream(jetty_home + "etc/jetty.xml"));
        configuration.configure(server);
        jdbcDataSource calDB = new jdbcDataSource();
        calDB.setDatabase("jdbc:hsqldb:mem:calDB");
        calDB.setUser("sa");
        calDB.setPassword("");
        new Resource("jdbc/calDB", calDB);
        server.start();
    }

    /**
     * Executes the SQL statements found in "file".<br>
     * This method breaks the provided SQL file in executable statements
     * on:<ul>
     * <li>semicolon on the end of the line, like in:<br>
     *     <code>SELECT * FROM table<br>...<br>ORDER BY
id<b>;</b></code><br>
     *     The semicolon will be skipped (causes error in JDBC), but
it's
     *     needed by the command line clients;</li>
     * <li>a slash alone on a line, like in:<br>
     *     <code>CREATE TRIGGER ... <br>...<br>BEGIN<br>...<br>END;
     *     &#47;* hack to hide the semicolon *&#47;
<br><b>&#47;</b></code><br>
     *     Note the SQL comment needed to hide the semicolon at the end
of the
     *     line (which is part of the SQL statement) from being
interpreted as
     *     statement separator.
     * </ul>
     */
    private static void setupDB(Connection connection, File file) throws IOException, SQLException {
        System.out.println("Reading from file " + file.getAbsolutePath());
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuffer sqlBuf = new StringBuffer();
        String line;
        boolean statementReady = false;
        int count = 0;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.equals("--/exe/--")) {
                sqlBuf.append(' ');
                statementReady = true;
            } else if (line.equals("/")) {
                sqlBuf.append(' ');
                statementReady = true;
            } else if (line.startsWith("--") || line.length() == 0) {
                continue;
            } else if (line.endsWith(";")) {
                sqlBuf.append(' ');
                sqlBuf.append(line.substring(0, line.length() - 1));
                statementReady = true;
            } else {
                sqlBuf.append(' ');
                sqlBuf.append(line);
                statementReady = false;
            }
            if (statementReady) {
                if (sqlBuf.length() == 0) continue;
                try {
                    connection.createStatement().execute(sqlBuf.toString());
                } catch (Throwable t) {
                    System.out.println(t.getMessage());
                }
                count++;
                sqlBuf.setLength(0);
            }
        }
        System.out.println("" + count + " statements processed");
        System.out.println("Import done sucessfully");
    }

    @AfterClass
    public static void shutdownJetty() throws Exception {
        if (server != null) {
            server.stop();
            server.destroy();
        }
    }

    protected URI getBaseURI() {
        try {
            return new URI("/" + getServiceName() + "/user/" + USER + "/");
        } catch (URISyntaxException e) {
            throw new RuntimeException();
        }
    }

    protected abstract String getServiceName();

    protected static class Timer {

        private long start;

        private String description;

        public Timer() {
        }

        public void start(String description) {
            start = System.currentTimeMillis();
            this.description = description;
        }

        public void stop() {
            long end = System.currentTimeMillis();
            System.out.println(description + " took " + (((double) (end - start)) / 1000d) + " seconds");
        }
    }
}

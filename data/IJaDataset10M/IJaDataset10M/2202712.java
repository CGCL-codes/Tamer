package org.h2.test.osgi;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import org.apache.felix.framework.*;
import org.apache.felix.*;
import org.apache.felix.framework.cache.BundleCache;
import org.apache.felix.framework.util.StringMap;
import org.h2.tools.Server;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.testng.annotations.*;

/**
 *
 * @author ep
 */
public class ConsoleServiceTest {

    private boolean stopDbServer = false;

    private String dbServerPort = "9084";

    private Server server;

    private Felix m_felix;

    @BeforeClass
    public void setUp() throws Exception {
        Map configMap = new StringMap(false);
        configMap.put(Constants.FRAMEWORK_SYSTEMPACKAGES, "org.osgi.framework; version=1.3.0," + "org.osgi.service.packageadmin; version=1.2.0," + "org.osgi.service.startlevel; version=1.0.0," + "org.osgi.service.url; version=1.0.0," + "javax.naming.spi, javax.naming," + "javax.transaction.xa," + "javax.net.ssl, javax.net, javax.sql");
        configMap.put(BundleCache.CACHE_PROFILE_PROP, "test");
        File file = File.createTempFile("osgi", "testng");
        file.delete();
        file.mkdir();
        file.deleteOnExit();
        configMap.put(BundleCache.CACHE_PROFILE_DIR_PROP, file.getPath());
        m_felix = new Felix(configMap, new ArrayList());
        m_felix.start();
    }

    @AfterClass
    public void tearDown() throws Exception {
        m_felix.stop();
    }

    @Test
    public void testSomethingWithFelix() throws BundleException, InterruptedException {
        BundleContext bundleContext = m_felix.getBundleContext();
        bundleContext.installBundle("file:///Applications/Development/felix/ipojo/org.apache.felix.ipojo-1.0.0.jar").start();
        bundleContext.installBundle("file:///Users/ep/Documents/workspace/h2osgi-console-service/target/ConsoleService-0.0.3-h2-1.1.105.jar").start();
        Thread.sleep(1000 * 60 * 2);
        Bundle[] bundles = bundleContext.getBundles();
        for (Bundle bundle : bundles) {
            assert bundle.getState() == Bundle.ACTIVE : "Bundle " + bundle.getSymbolicName() + " is not started.";
        }
    }

    private void startDbServer() throws Exception {
        server = Server.createTcpServer(new String[] { "-tcpPort", dbServerPort });
        server.start();
        System.out.println("You can access the database remotely now, using the URL:");
        System.out.println("jdbc:h2:tcp://localhost:" + dbServerPort + "/~/xlocation (user: sa, password: sa)");
        Class.forName("org.h2.Driver");
        String connStringServer = "jdbc:h2:tcp://localhost:" + dbServerPort + "/~/.h2/buysellrent/mixed/buysellrent";
        String connStringMem = "jdbc:h2:~/.h2/buysellrent/mixed/buysellrent";
        String connString = connStringMem;
        System.out.println("Connecting to: " + connString);
        Connection conn = DriverManager.getConnection(connString, "sa", "");
        java.sql.Statement stat = conn.createStatement();
        stat.execute("DROP TABLE TIMER IF EXISTS");
        stat.execute("CREATE TABLE TIMER(ID INT PRIMARY KEY, TIME VARCHAR)");
        System.out.println("Execute this a few times: SELECT TIME FROM TIMER");
        System.out.println("To stop this application (and the server), run: DROP TABLE TIMER");
        try {
            stat.execute("MERGE INTO TIMER VALUES(0, NOW())");
            int i = 0;
            while (!stopDbServer) {
                stat.execute("insert INTO TIMER VALUES(" + i + ", NOW())");
                Thread.sleep(1000);
                i++;
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.toString());
        }
        conn.close();
    }
}

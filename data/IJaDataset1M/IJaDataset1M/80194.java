package de.schwarzrot.data.access.jdbc;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import junit.framework.TestSuite;
import de.schwarzrot.app.config.DSConfig;
import de.schwarzrot.jar.JarExtensionHandler;
import de.schwarzrot.test.TestBase;

public class JDBCDynTestBase extends TestBase {

    public JDBCDynTestBase(String testCase) {
        super(testCase);
    }

    public final DSConfig getConfig() {
        return cfg;
    }

    public final void setConfig(DSConfig config) {
        cfg = config;
    }

    @Override
    public void setUp() {
        JarExtensionHandler jeh = new JarExtensionHandler();
        cfg = createConfig();
        jeh.investigateArchive(cfg.getDrvLibPath());
        Thread.currentThread().setContextClassLoader(jeh.getClassLoader());
        try {
            System.out.println("load driver class: " + cfg.getDrvClassName());
            jdbcDriverClass = jeh.getClassLoader().loadClass(cfg.getDrvClassName());
            jdbcDriver = (Driver) jdbcDriverClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testConnection() throws Exception {
        Connection conn = connect();
        assertNotNull("failed to connect to database!", conn);
        checkConnection(conn);
    }

    protected void checkConnection(Connection conn) throws SQLException {
        conn.setAutoCommit(false);
        DatabaseMetaData dmd = conn.getMetaData();
        ResultSet rs = dmd.getCatalogs();
        while (rs.next()) {
            System.out.println("catalog: " + rs.getString("TABLE_CAT"));
        }
        conn.close();
    }

    protected Connection connect() throws Exception {
        Properties props = new Properties();
        props.setProperty("user", cfg.getDsUser());
        props.setProperty("password", cfg.getDsPassword());
        System.err.println("used url: " + cfg.getDrvUrl());
        return jdbcDriver.connect(cfg.getDrvUrl(), props);
    }

    protected DSConfig createConfig() {
        DSConfig cfg = new DSConfig();
        cfg.setDrvLibPath(new File(System.getProperty("jdbc.driver.archive")));
        cfg.setDrvClassName(System.getProperty("jdbc.driver.class"));
        cfg.setDrvUrl(System.getProperty("jdbc.url"));
        cfg.setDsUser(System.getProperty("jdbc.user.name"));
        cfg.setDsPassword(System.getProperty("jdbc.pass"));
        return cfg;
    }

    public static TestSuite suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new JDBCDynTestBase("testConnection"));
        return suite;
    }

    private DSConfig cfg;

    private Driver jdbcDriver;

    private Class<?> jdbcDriverClass;
}

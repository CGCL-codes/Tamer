package org.nexopenframework.persistence.hibernate3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.spi.NamingManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.cfg.Environment;
import org.hsqldb.jdbcDriver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.nexopenframework.mock.database.HSQLServer;
import org.nexopenframework.persistence.hibernate3.profiling.P6SpyConnectionProvider;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import com.p6spy.engine.logging.P6LogConnection;

public class P6SpyConnectionProviderTest {

    private static final Log log = LogFactory.getLog(P6SpyConnectionProviderTest.class);

    private final HSQLServer server = new HSQLServer();

    @Test
    public void testP6Spy() throws SQLException {
        final P6SpyConnectionProvider cp = new P6SpyConnectionProvider();
        final Properties props = new Properties();
        props.setProperty(Environment.DATASOURCE, "java:comp/env/jdbc/nexopenDS");
        cp.configure(props);
        final Connection conn = cp.getConnection();
        assertNotNull(conn);
        assertTrue(conn instanceof P6LogConnection);
        final DatabaseMetaData dbmd = conn.getMetaData();
        log.info("Database product name :: " + dbmd.getDatabaseProductName());
        conn.close();
    }

    @After
    public void tearDown() {
        server.destroy();
    }

    @Before
    public void setUp() throws SQLException, NamingException {
        DriverManager.registerDriver(new jdbcDriver());
        if (!NamingManager.hasInitialContextFactoryBuilder()) {
            NamingManager.setInitialContextFactoryBuilder(new SimpleNamingContextBuilder());
        }
        server.setDatabaseName("nexopen");
        server.setPort(9989);
        server.afterPropertiesSet();
        final DriverManagerDataSource dmds = new DriverManagerDataSource();
        dmds.setDriverClassName("org.hsqldb.jdbcDriver");
        dmds.setUrl("jdbc:hsqldb:hsql://localhost:9989/nexopen");
        dmds.setUsername("sa");
        dmds.setPassword("");
        new InitialContext().bind("java:comp/env/jdbc/nexopenDS", dmds);
    }
}

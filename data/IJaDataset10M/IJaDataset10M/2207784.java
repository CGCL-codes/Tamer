package org.jcvi.glk.dbunit;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.log4j.xml.DOMConfigurator;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.CompositeOperation;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.jcvi.glk.AbstractGLKTest;
import org.jcvi.glk.GLK;
import org.jcvi.glk.helpers.DefaultHibernateHelper;
import org.jcvi.glk.helpers.GLKHelper;
import org.jcvi.glk.helpers.HibernateHelper;
import org.jcvi.glk.session.DatabaseConfig;
import org.jcvi.glk.session.SessionManager;
import org.jcvi.glk.session.SybaseDatabaseConfig;

/**
 *
 *
 * @author jsitz
 * @author dkatzel
 */
public class DBUnitAbstractGLKTest extends AbstractGLKTest {

    protected static CompositeOperation CLEAN_OPERATION = new CompositeOperation(new GLKCleanOperation(true), new SybaseIdentityInsertOperation(DatabaseOperation.INSERT));

    protected static String BASELINE_XML = "test/org/jcvi/glk/configurations/baseline.xml";

    protected static DatabaseConfig dbConfig = new SybaseDatabaseConfig("TEST", "flim");

    protected static GLKHelper helper;

    protected static HibernateHelper dbHelper;

    /**
     * Creates a new <code>InitSandbox</code>.
     *
     */
    public DBUnitAbstractGLKTest() {
        super();
    }

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        URL logConfig = AbstractGLKTest.class.getClassLoader().getResource("log4j-test.xml");
        DOMConfigurator.configure(logConfig);
        SessionManager.getImplementation().setDatabaseConfig(new SybaseDatabaseConfig("TEST", "flim"));
        SessionManager.getImplementation().setAuthentication("flim", "flimflam");
        SessionManager.getImplementation().useSQLEcho(true);
        DBUnitAbstractGLKTest.helper = GLK.getCurrentHelper();
        DBUnitAbstractGLKTest.dbHelper = new DefaultHibernateHelper(SessionManager.newSession());
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    protected GLKHelper getHelper() {
        return DBUnitAbstractGLKTest.helper;
    }

    protected HibernateHelper getDbHelper() {
        return DBUnitAbstractGLKTest.dbHelper;
    }

    protected static void runCleanOperation() throws Exception, DatabaseUnitException, SQLException {
        final IDatabaseConnection conn = getConnection();
        final IDataSet data = getDataSet();
        try {
            getCleanOperation().execute(conn, data);
        } finally {
            conn.close();
        }
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    protected static IDatabaseConnection getConnection() throws Exception {
        Class.forName(dbConfig.getDriver());
        final String connURL = dbConfig.getConnectionString();
        System.out.println("Harvesting data from: " + connURL);
        Connection conn = DriverManager.getConnection(connURL, "flim", "flimflam");
        return new DatabaseConnection(conn);
    }

    protected static IDataSet getDataSet() throws Exception {
        System.out.println("loading baseline=" + BASELINE_XML);
        return new FlatXmlDataSet(ClassLoader.getSystemResourceAsStream(BASELINE_XML), ClassLoader.getSystemResourceAsStream(BASELINE_XML + ".dtd"));
    }

    protected static DatabaseOperation getCleanOperation() throws Exception {
        return CLEAN_OPERATION;
    }
}

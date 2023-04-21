package net.mlw;

import java.sql.Connection;
import javax.sql.DataSource;
import junit.framework.TestCase;
import net.mlw.util.sql.ScriptRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * This test case has support for the following:
 * <ol>
 * <li>JDBC support that executes scripts before and after the test is run.
 * <li>
 * </ol>
 * 
 * com.bah.common.BaseTestCase
 * 
 * @author Matthew L. Wilson, Andrej Zachar
 * @version $Revision: 1.5 $ $Date: 2006/03/28 17:06:49 $
 */
public class BaseTestCase extends TestCase {

    protected static ApplicationContext context;

    /** Holds: "Does this test need data setup?" */
    private boolean usePersistence = false;

    /** The name of the test method. */
    private String method = null;

    private Connection conn;

    /**
     * Creates a new instance of SQLBaseTestCase
     * 
     * @param name
     *            Name of the test.
     */
    public BaseTestCase(String name) {
        this(name, true);
    }

    /**
     * Creates a new instance of SQLBaseTestCase
     * 
     * @param name
     *            Name of the test.
     * @param persistence
     *            Does this test need data setup?
     */
    public BaseTestCase(String name, boolean persistence) {
        super(name);
        this.method = name;
        this.usePersistence = persistence;
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() throws Exception {
        if (context == null) {
            context = new ClassPathXmlApplicationContext("applicationContext.xml");
        }
        DataSource dataSource = (DataSource) context.getBean("myDataSource");
        conn = dataSource.getConnection();
        if (usePersistence) {
            createTables();
            createData();
            ScriptRunner.executeScript(conn, ("." + getClass().getName() + "(setUp)").replace('.', '/') + ".sql");
            ScriptRunner.executeScript(conn, ("." + getClass().getName() + "-" + method + "(setUp)").replace('.', '/') + ".sql");
        }
    }

    /**
     * @throws Exception
     */
    private void createTables() throws Exception {
        ScriptRunner.executeScript(getConnection(), "/net/mlw/data/CreateTestTable.sql");
    }

    /**
     * @throws Exception
     */
    private void createData() throws Exception {
        ScriptRunner.executeScript(getConnection(), "/net/mlw/data/CreateTestData.sql");
    }

    /**
     * @throws Exception
     */
    private void destroyData() throws Exception {
        ScriptRunner.executeScript(getConnection(), "/net/mlw/data/DestroyTestData.sql");
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    public void tearDown() throws Exception {
        if (usePersistence) {
            destroyData();
        }
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
        context = null;
    }

    /**
     * Gets the connection.
     * 
     * @return A connectoin to a datasource.
     * @throws Exception
     *             If the connection cannot be establised.
     */
    public Connection getConnection() throws Exception {
        return conn;
    }

    /**
     * @return Returns the context.
     */
    public static ApplicationContext getContext() {
        return context;
    }
}

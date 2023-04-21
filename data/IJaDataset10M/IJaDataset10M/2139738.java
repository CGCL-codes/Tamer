package nl.ou.netlogo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import nl.ou.netlogo.testsupport.Database;
import nl.ou.netlogo.testsupport.DatabaseHelper;
import nl.ou.netlogo.testsupport.HeadlessTest;

/**
 * Tests for the sql:get-rowcount reporter for all {@link Database} values.
 * <p>
 * Note that update related tests are only tested with UPDATE statements,
 * testing the same using INSERT and DELETE would be testing the JDBC driver.
 * </p>
 * 
 * @author Mark Rotteveel
 */
@RunWith(Parameterized.class)
public class GetRowCountTest extends HeadlessTest {

    private static final String TABLE_PREFIX = "TEST";

    protected String tableName;

    private Database db;

    public GetRowCountTest(Database db) {
        this.db = db;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getParameters() {
        List<Object[]> parameters = new ArrayList<Object[]>();
        for (Database db : Database.values()) {
            parameters.add(new Object[] { db });
        }
        return parameters;
    }

    @Before
    public void createTable() throws ClassNotFoundException {
        tableName = TABLE_PREFIX + Calendar.getInstance().getTimeInMillis();
        try {
            DatabaseHelper.executeUpdate(db, "CREATE TABLE " + tableName + "( " + "ID INTEGER PRIMARY KEY, " + "CHAR_FIELD CHAR(25), " + "INT_FIELD INTEGER, " + "VARCHAR_FIELD VARCHAR(200) " + ")");
        } catch (SQLException e) {
            throw new IllegalStateException(msg("Unable to setup database"), e);
        }
        String query = "INSERT INTO " + tableName + "(ID, CHAR_FIELD, INT_FIELD, VARCHAR_FIELD) VALUES (%d, '%s', %d, '%s')";
        try {
            DatabaseHelper.executeUpdate(db, String.format(query, 1, "CHAR-content", 1234, "VARCHAR-content"));
        } catch (SQLException e) {
            throw new IllegalStateException(msg("Unable to setup data"), e);
        }
    }

    /**
     * Test if sql:get-rowcount works after executing a SELECT statement using
     * sql:exec-direct.
     * <p>
     * Expected: returned rowcount is 0 when performing SELECTS.
     * </p>
     * 
     * @throws Exception
     *             For any exceptions during testing
     */
    @Test
    public void testSelect_ExecDirect_rowCount() throws Exception {
        workspace.open("init-sql.nlogo");
        workspace.command(db.getConnectCommand());
        workspace.command("sql:exec-direct \"SELECT * FROM " + tableName + "\"");
        Object rowCount = workspace.report("sql:get-rowcount");
        assertEquals(msg("Unexpected rowcount"), Double.valueOf(-1), rowCount);
    }

    /**
     * Test if sql:get-rowcount works after executing a SELECT statement using
     * sql:exec-query.
     * <p>
     * Expected: returned rowcount is -1 when performing SELECTS.
     * </p>
     * 
     * @throws Exception
     *             For any exceptions during testing
     */
    @Test
    public void testSelect_ExecQuery_rowCount() throws Exception {
        workspace.open("init-sql.nlogo");
        workspace.command(db.getConnectCommand());
        workspace.command("sql:exec-query \"SELECT * FROM " + tableName + "\" []");
        Object rowCount = workspace.report("sql:get-rowcount");
        assertEquals(msg("Unexpected rowcount"), Double.valueOf(-1), rowCount);
    }

    /**
     * Test if sql:get-rowcount works after executing an UPDATE statement using
     * sql:exec-direct.
     * <p>
     * Expected: returned rowcount is 1 when performing UPDATE on single row.
     * </p>
     * 
     * @throws Exception
     *             For any exceptions during testing
     */
    @Test
    public void testUpdate_ExecDirect_rowCount() throws Exception {
        workspace.open("init-sql.nlogo");
        workspace.command(db.getConnectCommand());
        workspace.command("sql:exec-direct \"UPDATE " + tableName + " SET INT_FIELD = 2345 WHERE ID = 1\"");
        Object rowCount = workspace.report("sql:get-rowcount");
        assertEquals(msg("Unexpected rowcount"), Double.valueOf(1), rowCount);
    }

    /**
     * Test if sql:get-rowcount works after executing an UPDATE statement using
     * sql:exec-update.
     * <p>
     * Expected: returned rowcount is 1 when performing UPDATE on single row.
     * </p>
     * 
     * @throws Exception
     *             For any exceptions during testing
     */
    @Test
    public void testUpdate_ExecUpdate_rowCount() throws Exception {
        workspace.open("init-sql.nlogo");
        workspace.command(db.getConnectCommand());
        workspace.command("sql:exec-update \"UPDATE " + tableName + " SET INT_FIELD = 2345 WHERE ID = 1\" []");
        Object rowCount = workspace.report("sql:get-rowcount");
        assertEquals(msg("Unexpected rowcount"), Double.valueOf(1), rowCount);
    }

    /**
     * Test if sql:get-rowcount works after executing an UPDATE statement using
     * sql:exec-update, when connected with pooling with autodisconnect enabled.
     * <p>
     * Expected: returned rowcount is 1 when performing UPDATE on single row.
     * </p>
     * 
     * @throws Exception
     *             For any exceptions during testing
     */
    @Test
    public void testUpdate_autodisconnect_rowCount() throws Exception {
        workspace.open("init-sql.nlogo");
        workspace.command(db.getPoolConfigurationCommand());
        workspace.command("sql:exec-update \"UPDATE " + tableName + " SET INT_FIELD = 2345 WHERE ID = ?\" [1]");
        boolean isConnected = (Boolean) workspace.report("sql:debug-is-connected?");
        assertFalse(msg("Expected to be autodisconnected"), isConnected);
        Object rowCount = workspace.report("sql:get-rowcount");
        assertEquals(msg("Unexpected rowcount"), Double.valueOf(1), rowCount);
    }

    /**
     * Test if sql:get-rowcount returns -1.0 if executed without a connection.
     * <p>
     * Expected: return value is -1 (of type Double).
     * </p>
     * 
     * @throws Exception
     *             For any exceptions during testing
     */
    @Test
    public void testGetRowCount_noConnection() throws Exception {
        workspace.open("init-sql.nlogo");
        Object result = workspace.report("sql:get-rowcount");
        assertEquals(msg("Unexpected rowcount"), Double.valueOf(-1), result);
    }

    /**
     * Test if sql:get-rowcount returns -1 if a connection is created with
     * sql:connect, but no statement was executed.
     * <p>
     * Expected: Returned rowcount is -1
     * </p>
     * 
     * @throws Exception
     *             For any exceptions during testing
     */
    @Test
    public void testGetRowCount_connect_noStatement() throws Exception {
        workspace.open("init-sql.nlogo");
        workspace.command(db.getConnectCommand());
        Object rowCount = (Double) workspace.report("sql:get-rowcount");
        assertEquals(msg("Unexpected rowcount without statement"), Double.valueOf(-1), rowCount);
    }

    /**
     * Test if sql:get-rowcount returns -1 if executed with connectionpooling,
     * without a statement.
     * <p>
     * Expected: Returned rowcount is -1
     * </p>
     * 
     * @throws Exception
     *             For any exceptions during testing
     */
    @Test
    public void testGetRowCount_connectionPool_noStatement() throws Exception {
        workspace.open("init-sql.nlogo");
        workspace.command(db.getPoolConfigurationCommand());
        Object result = workspace.report("sql:get-rowcount");
        assertEquals(msg("Unexpected rowcount"), Double.valueOf(-1), result);
    }

    @After
    public void dropTable() {
        try {
            DatabaseHelper.executeUpdate(db, "DROP TABLE " + tableName);
        } catch (SQLException e) {
            throw new IllegalStateException(msg("Unable to drop table " + tableName), e);
        }
    }

    private String msg(String message) {
        return message + " (" + db.name() + ")";
    }
}

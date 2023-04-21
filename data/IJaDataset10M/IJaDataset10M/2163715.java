package org.test.MyMockConnection;

import org.tigr.cloe.model.facade.datastoreFacade.authentication.TDBUserCredentials;
import org.tigr.cloe.model.facade.datastoreFacade.authentication.UserCredentials;
import org.tigr.cloe.model.facade.datastoreFacade.datastore.DatabaseDataStore;
import org.tigr.cloe.model.facade.datastoreFacade.datastore.TigrSybaseDatabaseDataStore;
import org.tigr.common.AbstractApplication;
import org.tigr.common.Application;
import org.tigr.seq.tdb.TDBConnection;
import com.mockrunner.jdbc.BasicJDBCTestCaseAdapter;
import com.mockrunner.mock.jdbc.MockResultSet;
import com.mockrunner.jdbc.StatementResultSetHandler;
import java.sql.*;

/**
 * @author dkatzel
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MockTDBConnectionTest extends BasicJDBCTestCaseAdapter {

    MockTDBConnection mockConn;

    String connectionString;

    private StatementResultSetHandler statementHandler;

    public MockTDBConnectionTest(String name) {
        super(name);
        statementHandler = getJDBCMockObjectFactory().getMockConnection().getStatementResultSetHandler();
    }

    public void setUp() {
        Application.setApplication(new AbstractApplication());
        TDBUserCredentials cred = new TDBUserCredentials("fakeaccess", "access", null);
        UserCredentials.setUserCredentials(cred);
        DatabaseDataStore dataStore = new TigrSybaseDatabaseDataStore("MockServerName", "MockDescription", "MockHostName", 1234, null, null, null, null, null);
        Application.setDatastore(dataStore);
        connectionString = ((DatabaseDataStore) dataStore).getConnectionString();
        mockConn = new MockTDBConnection(dataStore, (TDBUserCredentials) UserCredentials.getUserCredentials(), getJDBCMockObjectFactory().getMockConnection());
    }

    /**
	 * Test access to mock db using access.
	 *
	 */
    public void testFakeAccessUserConnection() {
        assertTrue(mockConn != null);
        assertEquals(UserCredentials.getUserName(), "fakeaccess");
        DatabaseDataStore dbStore = (DatabaseDataStore) Application.getDatastore();
        assertEquals(dbStore.getConnectionString(), connectionString);
    }

    public void testQuery() throws SQLException {
        TDBConnection tdbCon = TDBConnection.getInstance();
        Connection con = tdbCon.getConnection();
        MockResultSet result = statementHandler.createResultSet();
        result.addColumn("id", new String[] { "8675309" });
        statementHandler.prepareResultSet("select s.id from dak..sequence s where s.seq_name =fake_sequence_name", result);
        String query = "select s.id from dak..sequence s where s.seq_name =fake_sequence_name";
        Statement statement = con.createStatement();
        ResultSet rs = statement.executeQuery(query.toString());
        int counter = 0;
        while (rs.next()) {
            counter++;
            assertEquals(rs.getInt(1), 8675309);
        }
        assertEquals(counter, 1);
        rs.close();
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(MockTDBConnectionTest.class);
    }
}

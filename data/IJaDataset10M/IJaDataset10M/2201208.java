package davidlauzon.activerecord.connection;

import java.sql.SQLException;
import davidlauzon.activerecord.connection.ConnectionAdapter;
import davidlauzon.activerecord.connection.MysqlAdapter;

/**
 * java -cp bin:lib/junit-4.7.jar:lib/mysql-connector-java-5.1.16-bin.jar org.junit.runner.JUnitCore  test.newcommerce.db.connection.MysqlConnectionAdapterTest
 */
public class MysqlConnectionAdapterTest extends ConnectionAdapterTest {

    /*********************************************************************************************
     * VARIABLES
     *********************************************************************************************/
    private MysqlAdapter _adapter = null;

    /*********************************************************************************************
     * TEST SETUP
     *********************************************************************************************/
    protected void setUp() {
        super.setUp();
        _createTableSQL = "CREATE TABLE IF NOT EXISTS people (" + "id INTEGER NOT NULL AUTO_INCREMENT," + "name VARCHAR(45) DEFAULT NULL," + "size FLOAT DEFAULT NULL," + "age INT DEFAULT NULL, " + "PRIMARY KEY (id) )";
    }

    /*********************************************************************************************
     * TEST METHODS
     *********************************************************************************************/
    public void testExecutePrep() throws SQLException {
        defaultTestExecutePrep();
    }

    public void testExecuteInsert() throws SQLException {
        defaultTestExecuteInsert();
    }

    public void testExecuteQuery() throws SQLException {
        defaultTestExecuteQuery();
    }

    public void testExecuteUpdate() throws SQLException {
        defaultTestExecuteUpdate();
    }

    /*********************************************************************************************
     * INTERNAL METHODS
     *********************************************************************************************/
    @Override
    protected ConnectionAdapter getAdapter() {
        try {
            if (_adapter == null) _adapter = new MysqlAdapter("test");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return _adapter;
    }
}

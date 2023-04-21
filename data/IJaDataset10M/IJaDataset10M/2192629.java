package android.database.sqlite;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import android.test.suitebuilder.annotation.MediumTest;

/**
 * Minimal test for JDBC driver
 */
public class SQLiteJDBCDriverTest extends AbstractJDBCDriverTest {

    private File dbFile;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        dbFile = File.createTempFile("sqliteTestDB", null);
    }

    @Override
    protected void tearDown() throws Exception {
        if (dbFile != null) {
            dbFile.delete();
        }
        super.tearDown();
    }

    @Override
    protected String getConnectionURL() {
        return "jdbc:sqlite:/" + dbFile;
    }

    @Override
    protected File getDbFile() {
        return dbFile;
    }

    @Override
    protected String getJDBCDriverClassName() {
        return "SQLite.JDBCDriver";
    }

    @MediumTest
    public void test_connection3() throws Exception {
        PreparedStatement prst = null;
        Statement st = null;
        Connection conn = null;
        try {
            Class.forName("SQLite.JDBCDriver").newInstance();
            if (dbFile.exists()) {
                dbFile.delete();
            }
            conn = DriverManager.getConnection("jdbc:sqlite:/" + dbFile.getPath());
            assertNotNull(conn);
            st = conn.createStatement();
            String sql = "CREATE TABLE zoo (ZID INTEGER NOT NULL, family VARCHAR (20) NOT NULL, name VARCHAR (20) NOT NULL, PRIMARY KEY(ZID) )";
            st.executeUpdate(sql);
            String update = "update zoo set family = ? where name = ?;";
            prst = conn.prepareStatement(update);
            prst.setString(1, "cat");
            prst.setString(2, "Yasha");
            prst.executeUpdate();
            try {
                prst = conn.prepareStatement("");
                prst.execute();
                fail("SQLException is not thrown");
            } catch (SQLException e) {
            }
            try {
                conn.prepareStatement(null);
                fail("NPE is not thrown");
            } catch (Exception e) {
            }
            try {
                st = conn.createStatement();
                st.execute("drop table if exists zoo");
            } catch (SQLException e) {
                fail("Couldn't drop table: " + e.getMessage());
            } finally {
                try {
                    st.close();
                    conn.close();
                } catch (SQLException ee) {
                }
            }
        } finally {
            try {
                if (prst != null) {
                    prst.close();
                }
                if (st != null) {
                    st.close();
                }
            } catch (SQLException ee) {
            }
        }
    }
}

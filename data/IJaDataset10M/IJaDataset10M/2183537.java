package net.sourceforge.jdbclogger.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Catalin Kormos (latest modification by $Author: catalean $)
 * @version $Revision: 121 $ $Date: 2007-09-17 16:39:03 -0400 (Mon, 17 Sep 2007) $
 */
public abstract class AbstractJdbcLoggerDemo {

    private static Log log = LogFactory.getLog("JdbcLoggerDemo");

    public void executeJdbcStatements() {
        Connection conn = null;
        try {
            conn = getConnection();
            executeCreateTestTableStatement(conn);
            executeJdbcInsertStatements(conn);
            executeRepeatedBatchStatements(conn);
        } catch (Exception exc) {
            log.error("Error getting database connection", exc);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException exc) {
                    log.error("Error while closing database connection", exc);
                }
            }
        }
    }

    /**
	 * @param conn
	 * @throws SQLException
	 */
    public void executeJdbcInsertStatements(Connection conn) throws SQLException {
        log.info("Executing test JDBC inserts...");
        PreparedStatement ps = conn.prepareStatement("insert into TEST_TABLE values (?, ?, ?, ?);");
        byte[] b = { 1, 2, 3 };
        for (int i = 1; i < 7; i++) {
            ps.setInt(1, i);
            ps.setString(2, "text " + String.valueOf(i));
            ps.setTimestamp(3, new Timestamp(Calendar.getInstance().getTimeInMillis()));
            ps.setBytes(4, b);
            ps.addBatch();
        }
        ps.executeBatch();
        conn.commit();
        log.info("Test JDBC inserts successfuly executed.");
    }

    /**
	 * @param conn
	 * @throws SQLException 
	 */
    public void executeRepeatedBatchStatements(Connection conn) throws SQLException {
        log.info("Executing test repeated batch JDBC inserts...");
        PreparedStatement ps = conn.prepareStatement("insert into TEST_TABLE values (?, ?, ?, ?);");
        byte[] b = { 1, 2, 3 };
        int id = 1;
        for (int batchCount = 0; batchCount < 2; batchCount++) {
            log.info("Execute new batch");
            for (int i = 1; i < 7; i++) {
                ps.setInt(1, id);
                ps.setString(2, "text " + String.valueOf(id));
                ps.setTimestamp(3, new Timestamp(Calendar.getInstance().getTimeInMillis()));
                ps.setBytes(4, b);
                ps.addBatch();
                id += 1;
            }
            ps.executeBatch();
        }
        conn.commit();
        log.info("Repeated batch JDBC inserts successfuly executed.");
    }

    /**
	 * @param conn
	 * @throws SQLException 
	 */
    public void executeCreateTestTableStatement(Connection conn) throws SQLException {
        Statement st = conn.createStatement();
        st.execute("create table TEST_TABLE (id int, text varchar(255), createdTs timestamp, content Object)");
        conn.commit();
    }

    /**
	 * @return
	 * @throws SQLException
	 */
    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:hsqldb:mem:test", "sa", "");
    }
}

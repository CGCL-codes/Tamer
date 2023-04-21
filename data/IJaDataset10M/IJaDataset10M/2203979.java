package aurora.testcase.database;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

public class DummyDataSource implements DataSource {

    public Connection getConnection() throws SQLException {
        return new DummyConnection();
    }

    public Connection getConnection(String username, String password) throws SQLException {
        return new DummyConnection();
    }

    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    public void setLogWriter(PrintWriter out) throws SQLException {
    }

    public void setLoginTimeout(int seconds) throws SQLException {
    }

    public boolean isWrapperFor(Class arg0) throws SQLException {
        return false;
    }

    public Object unwrap(Class arg0) throws SQLException {
        return null;
    }
}

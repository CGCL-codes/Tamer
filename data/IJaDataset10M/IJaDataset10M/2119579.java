package library.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.sql.DataSource;
import library.enums.Library;
import library.utils._Properties;
import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceFactory;
import org.hibernate.SessionFactory;

public class OraclePooledDatabase extends AbstractLibraryDatabase {

    protected String databaseName;

    protected Map<String, SessionFactory> factories;

    protected String password;

    protected int port;

    protected AbstractLibraryDatabase.Process process;

    private Properties props;

    protected DataSource source;

    protected String user;

    {
        props = new _Properties("/database-connection.properties");
        databaseName = props.getProperty("databaseName");
        password = props.getProperty("password");
        port = Integer.parseInt(props.getProperty("port"));
        user = props.getProperty("user");
        PoolDataSource pds = PoolDataSourceFactory.getPoolDataSource();
        try {
            pds.setConnectionFactoryClassName("oracle.jdbc.pool.OracleDataSource");
            pds.setDatabaseName(databaseName);
            pds.setPassword(password);
            pds.setPortNumber(port);
            pds.setUser(user);
            pds.setInitialPoolSize(5);
            pds.setMinPoolSize(5);
            pds.setMaxConnectionReuseCount(100);
            pds.setMaxStatements(50);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        source = pds;
        process = new AbstractLibraryDatabase.Process();
        factories = new HashMap<String, SessionFactory>();
    }

    @Override
    protected Connection getConnection(Library library) throws SQLException {
        Connection conn = source.getConnection();
        Statement stmt = conn.createStatement();
        stmt.execute("ALTER SESSION SET CURRENT_SCHEMA = " + struct.getDatabaseName(library));
        stmt.close();
        conn.setAutoCommit(true);
        return conn;
    }

    @Override
    protected Properties getDatabaseStructureProperties() {
        return new _Properties("/database-structure_oracle.properties");
    }

    @Override
    protected Library getLibrary(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rset = stmt.executeQuery("SELECT SYS_CONTEXT('USERENV'," + "'SESSION_SCHEMA') FROM DUAL");
        if (rset.next()) {
            String lib = rset.getString("search_path");
            Library library = struct.getLibrary(lib);
            return library;
        } else {
            throw new SQLException("Error while getting library: " + "no schema fetched!");
        }
    }

    @Override
    protected Process getProcessSingleton() {
        return process;
    }

    @Override
    protected Map<String, SessionFactory> getSessionFactories() {
        return factories;
    }

    @Override
    protected void setLibrary(Library library, Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        String command = "ALTER SESSION SET current_schema = " + struct.getDatabaseName(library);
        stmt.execute(command);
        stmt.close();
    }
}

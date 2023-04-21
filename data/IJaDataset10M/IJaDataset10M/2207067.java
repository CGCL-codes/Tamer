package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import database.DatabaseType;
import database.connection.AbastractConnectionFactory;

/**
 * 
 * @author noniko
 */
public class MyDBConnection {

    private Connection myConnection;

    /** Creates a new instance of MyDBConnection */
    public MyDBConnection() {
    }

    public void init() throws SQLException {
        String dbType = MarkadProperties.getProperty(MarkadProperties.DBTYPE);
        if (dbType == null) throw new SQLException("I cannot load any connection factory");
        DatabaseType db = DatabaseType.valueOf(dbType);
        AbastractConnectionFactory f = AbastractConnectionFactory.getInstance(db);
        if (f == null) throw new SQLException("I cannot load any connection factory");
        try {
            Class.forName(f.getDriverString());
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
        myConnection = DriverManager.getConnection(f.getConnectionString(), f.getProperties());
        myConnection.setAutoCommit(false);
    }

    public static ResultSet executeQuery(String query, Connection con) throws SQLException {
        ResultSet rs = null;
        Statement s = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        rs = s.executeQuery(query);
        return rs;
    }

    public Connection getMyConnection() {
        return myConnection;
    }

    public static int executeNoQuery(String strStatement) throws SQLException {
        MyDBConnection myc = new MyDBConnection();
        myc.init();
        Statement statement = myc.getMyConnection().createStatement();
        try {
            int rows = statement.executeUpdate(strStatement);
            myc.myConnection.commit();
            return rows;
        } catch (SQLException e) {
            myc.myConnection.rollback();
            throw new SQLException("rollback e close effettuato dopo " + e.getMessage());
        } finally {
            myc.close();
        }
    }

    public void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (Exception e) {
            }
        }
    }

    public void close(java.sql.Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (Exception e) {
            }
        }
    }

    public void close() {
        if (myConnection != null) {
            try {
                myConnection.close();
            } catch (Exception e) {
            }
        }
    }
}

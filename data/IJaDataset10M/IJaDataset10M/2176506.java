package jtq.implementation.sqlServer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import jtq.core.ADatabase;
import jtq.core.SqlError;
import jtq.helper.Str;

public class SqlServerDatabase extends ADatabase {

    private final String mConnectionString;

    public SqlServerDatabase(String pDatabaseName, String pConnectionString, String pDriverName) {
        super(DatabaseEnum.SqlServer, pDatabaseName);
        if (Str.isNullOrEmpty(pDriverName)) throw new IllegalArgumentException("pDriverName can not be null or empty");
        if (Str.isNullOrEmpty(pConnectionString)) throw new IllegalArgumentException("pConnectionString can not be null or empty");
        try {
            Class.forName(pDriverName);
        } catch (ClassNotFoundException e) {
            throw new Error("ClassNotFoundException caught while loading JDBC driver", e);
        }
        mConnectionString = pConnectionString;
    }

    @Override
    public Connection getNewConnection() throws SqlError {
        try {
            return DriverManager.getConnection(mConnectionString);
        } catch (SQLException e) {
            throw new SqlError(e);
        }
    }
}

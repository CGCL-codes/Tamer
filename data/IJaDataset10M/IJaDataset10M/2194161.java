package org.dolmen.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import javax.sql.DataSource;

/**
 * Utility class methods related to DB management
 * 
 * @since 0.0.1
 * @author <a href="mailto:straahd@users.sourceforge.net">Laurent Lecomte
 * 
 */
public final class DBUtils {

    public static final boolean isSupportsTransactions(Connection aConnection) throws SQLException {
        DatabaseMetaData metadata = aConnection.getMetaData();
        return metadata.supportsTransactions();
    }

    public static final boolean isSupportsTransactions(DataSource aDataSource) throws SQLException {
        return isSupportsTransactions(aDataSource.getConnection());
    }

    private DBUtils() {
    }
}

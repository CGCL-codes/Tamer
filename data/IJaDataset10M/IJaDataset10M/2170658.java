package org.objectstyle.cayenne.dba.ingres;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import org.objectstyle.cayenne.dba.DbAdapter;
import org.objectstyle.cayenne.dba.DbAdapterFactory;

/**
 * Detects Ingres database from JDBC metadata.
 * 
 * @since 1.2
 * @author Andrus Adamchik
 */
public class IngresSniffer implements DbAdapterFactory {

    public DbAdapter createAdapter(DatabaseMetaData md) throws SQLException {
        String dbName = md.getDatabaseProductName();
        return dbName != null && dbName.toUpperCase().indexOf("INGRES") >= 0 ? new IngresAdapter() : null;
    }
}

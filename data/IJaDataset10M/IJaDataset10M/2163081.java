package net.sourceforge.dbtoolbox.extractor.mysql;

import java.sql.Connection;
import java.sql.SQLException;
import net.sourceforge.dbtoolbox.extractor.FunctionColumnMDExtractor;
import net.sourceforge.dbtoolbox.extractor.GenericDatabaseMDExtractor;
import net.sourceforge.dbtoolbox.extractor.MDExtractor;
import net.sourceforge.dbtoolbox.extractor.ProcedureColumnMDExtractor;
import net.sourceforge.dbtoolbox.model.CallableColumnMD;

/**
 * MySQL database extractor.
 * Workaround NullPointerException in
 * <code>
 * java.lang.NullPointerException
 *   at com.mysql.jdbc.DatabaseMetaData.getProcedureOrFunctionColumns(DatabaseMetaData.java:4090)
 *   at com.mysql.jdbc.DatabaseMetaData.getProcedureColumns(DatabaseMetaData.java:4056)
 * </code>
 */
public class MySQLDatabaseMDExtractor extends GenericDatabaseMDExtractor {

    public MySQLDatabaseMDExtractor(Connection connection) {
        super(connection);
    }

    @Override
    protected MDExtractor<CallableColumnMD> createFunctionColumnExtractor() throws SQLException {
        return new FunctionColumnMDExtractor(this, "%", "%");
    }

    @Override
    protected MDExtractor<CallableColumnMD> createProcedureColumnExtractor() throws SQLException {
        return new ProcedureColumnMDExtractor(this, "%", "%");
    }
}

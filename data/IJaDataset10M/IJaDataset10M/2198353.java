package org.jcvi.glk.dbunit;

import java.sql.Connection;
import java.sql.SQLException;
import org.jcvi.glk.dbunit.GLKCleanOperation;

public class CTMCleanOperation extends GLKCleanOperation {

    private static String[] TABLES = new String[] { "ctm_reference_attribute", "ctm_task_attribute", "ctm_attribute_type", "ctm_reference_history", "ctm_task_history", "ctm_task", "ctm_user", "ctm_access", "ctm_reference", "ctm_reference_status", "ctm_task_status", "ctm_task_type" };

    public CTMCleanOperation(boolean useTruncate) {
        super(useTruncate);
    }

    @Override
    public void clean(Connection conn) throws SQLException {
        super.clean(conn);
        cleanJustCtmTables(conn);
    }

    private void cleanJustCtmTables(final Connection conn) throws SQLException {
        for (String table : TABLES) {
            cleanTable(conn, table);
        }
    }
}

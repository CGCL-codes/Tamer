package com.acuityph.commons.jdbc.metadata;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 * DatabaseMetaDataHelper.
 *
 * @author Alistair A. Israel
 * @since 0.1
 */
public class DatabaseMetaDataHelper extends MetaDataHelper {

    private static final Logger logger = Logger.getLogger(DatabaseMetaDataHelper.class.getName());

    /**
     *
     */
    private static final String[] TABLES_AND_VIEWS_TYPE = new String[] { "TABLE", "VIEW" };

    /**
     *
     */
    private static final String TABLE_NAME = "TABLE_NAME";

    /**
     * @param dataSource
     *        {@link DataSource}
     */
    public DatabaseMetaDataHelper(final DataSource dataSource) {
        super(dataSource);
    }

    private List<TableMetaDataHelper> tables;

    /**
     * @return List < TableMetaDataHelper >
     * @throws SQLException
     *         on exception
     */
    public final List<TableMetaDataHelper> listTables() throws SQLException {
        if (tables == null) {
            tables = new ArrayList<TableMetaDataHelper>();
            final Connection conn = getDataSource().getConnection();
            try {
                final DatabaseMetaData dmd = conn.getMetaData();
                final ResultSet rs = dmd.getTables(null, null, null, TABLES_AND_VIEWS_TYPE);
                try {
                    while (rs.next()) {
                        final String tableName = rs.getString(TABLE_NAME);
                        logger.finest("Got table \"" + tableName + "\"");
                        final TableMetaDataHelper tableMetaData = new TableMetaDataHelper(getDataSource(), tableName);
                        tableMap.put(tableName, tableMetaData);
                        tables.add(tableMetaData);
                    }
                } finally {
                    rs.close();
                }
            } finally {
                conn.close();
            }
        }
        return tables;
    }

    private final Map<String, TableMetaDataHelper> tableMap = new HashMap<String, TableMetaDataHelper>();

    /**
     * @param tableName
     *        The table name to look for.
     * @return {@link TableMetaDataHelper}
     */
    public final TableMetaDataHelper getTableMetaData(final String tableName) {
        final TableMetaDataHelper result;
        if (tableMap.containsKey(tableName)) {
            result = tableMap.get(tableName);
        } else {
            result = new TableMetaDataHelper(getDataSource(), tableName);
            tableMap.put(tableName, result);
        }
        return result;
    }
}

package com.moonspider.dbmap;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author David Brown initial version
 * @author Sam Pullara added use of schema
 */
public class DBTable {

    private String name;

    private Map<String, DBColumn> cols = new TreeMap<String, DBColumn>();

    private List<DBKey> keys = new ArrayList<DBKey>();

    private List<DBKey> fkeys = new ArrayList<DBKey>();

    private boolean isJoinTable = false;

    public DBTable(String name, String schema, DatabaseMetaData meta) throws SQLException {
        this.name = name;
        ResultSet rs;
        List<String> uniqueColumns = getUniqueColumnNames(name, schema, meta);
        rs = meta.getColumns(null, schema, name, null);
        try {
            while (rs.next()) {
                String colName = rs.getString("COLUMN_NAME");
                boolean nullAllowed = rs.getString("IS_NULLABLE").equals("YES");
                int sqlType = rs.getInt("DATA_TYPE");
                int colSize = rs.getInt("COLUMN_SIZE");
                DBColumn dbcol = new DBColumn(colName, sqlType, nullAllowed, colSize, uniqueColumns.contains(colName));
                cols.put(colName, dbcol);
            }
        } finally {
            rs.close();
        }
        rs = meta.getPrimaryKeys(null, schema, name);
        try {
            while (rs.next()) {
                String colName = rs.getString("COLUMN_NAME");
                cols.get(colName).setPrimKey(true);
            }
        } finally {
            rs.close();
        }
        rs = meta.getExportedKeys(null, schema, name);
        try {
            while (rs.next()) {
                keys.add(new DBKey(rs));
            }
        } finally {
            rs.close();
        }
        rs = meta.getImportedKeys(null, schema, name);
        try {
            while (rs.next()) {
                fkeys.add(new DBKey(rs));
            }
        } finally {
            rs.close();
        }
        if (cols.size() == fkeys.size()) {
            isJoinTable = true;
        }
        Collections.sort(keys, DBKey.COMP);
        Collections.sort(fkeys, DBKey.COMP);
    }

    public String getName() {
        return name;
    }

    public boolean isJoinTable() {
        return isJoinTable;
    }

    public List<DBKey> getKeys() {
        return keys;
    }

    public List<DBKey> getFkeys() {
        return fkeys;
    }

    public Collection<DBColumn> getColumns() {
        return cols.values();
    }

    public DBColumn getColumn(String column) {
        return cols.get(column);
    }

    public String toString() {
        return getName();
    }

    /**
     * retrieve column names that are considered to be <i>singularly</i>
     * unique by the DB.  This excludes columns that have a <i>compound</i>
     * unique constraint.  For example, if the table has a constraint:
     * <pre>
     * ALTER TABLE project ADD CONSTRAINT repositoryId_and_name_unique UNIQUE(repository_id, name);
     * </pre>
     * Then neither repository_id nor name are considered unique (at least
     * just considering the above constraint).
     */
    private static List<String> getUniqueColumnNames(String table, String schema, DatabaseMetaData meta) throws SQLException {
        List<String> ret = new ArrayList<String>();
        Map<String, String> indicesToCol = new HashMap<String, String>();
        ResultSet rs = meta.getIndexInfo(null, schema, table, true, true);
        try {
            while (rs.next()) {
                String col = rs.getString("COLUMN_NAME");
                ret.add(col);
                String ind = rs.getString("INDEX_NAME");
                if (ind != null) {
                    String compoundOther = indicesToCol.get(ind);
                    if (compoundOther != null) {
                        ret.remove(compoundOther);
                        ret.remove(col);
                    }
                    indicesToCol.put(ind, col);
                }
            }
        } finally {
            rs.close();
        }
        return ret;
    }
}

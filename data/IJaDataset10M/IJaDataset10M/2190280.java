package com.restsql.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author L
 */
public class DerbyForeignKeyInfo extends GenericForeignKeyInfo implements ForeignKeyInfo {

    ArrayList ar_table1 = new ArrayList();

    ArrayList ar_table2 = new ArrayList();

    ArrayList ar_column1 = new ArrayList();

    ArrayList ar_column2 = new ArrayList();

    public DerbyForeignKeyInfo(Database db) throws SQLException {
        ResultSet rs_tables = db.getDatabaseMetaData().getTables(null, null, "%", new String[] { "TABLE" });
        int i = 0;
        while (rs_tables.next()) {
            String table_name = rs_tables.getString("TABLE_NAME");
            ResultSet rs = db.getDatabaseMetaData().getImportedKeys(null, db.getSchemaName(), table_name);
            ArrayList keyList = new ArrayList();
            while (rs.next()) {
                final String key = rs.getString("FK_NAME") + rs.getString("KEY_SEQ");
                index.put(key, i);
                ar_table1.add(rs.getString("FKTABLE_NAME"));
                ar_column1.add(rs.getString("FKCOLUMN_NAME"));
                ar_table2.add(rs.getString("PKTABLE_NAME"));
                ar_column2.add(rs.getString("PKCOLUMN_NAME"));
                if (relations.containsKey(rs.getString("FKTABLE_NAME") + "-" + rs.getString("PKTABLE_NAME"))) {
                    relations.get(rs.getString("FKTABLE_NAME") + "-" + rs.getString("PKTABLE_NAME")).add(key);
                } else {
                    keyList.add(key);
                    relations.put(rs.getString("FKTABLE_NAME") + "-" + rs.getString("PKTABLE_NAME"), keyList);
                }
                i++;
            }
            rs.close();
        }
        rs_tables.close();
        final int size = ar_table1.size();
        if (size > 0) {
            table1 = (String[]) ar_table1.toArray(new String[size]);
            column1 = (String[]) ar_column1.toArray(new String[size]);
            table2 = (String[]) ar_table2.toArray(new String[size]);
            column2 = (String[]) ar_column2.toArray(new String[size]);
        }
    }
}

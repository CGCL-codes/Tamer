package aml.ramava.db.utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Vector;
import aml.ramava.db.DatabaseCreation;

public class PrintDbStructure {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        getApplicationTableNames();
        getColumnNamesForTable("TOPICS");
    }

    public static Vector getApplicationTableNames() {
        Vector result = new Vector();
        try {
            Connection con = DatabaseCreation.getActiveConnection();
            DatabaseMetaData dbMeta = con.getMetaData();
            ResultSet rs = dbMeta.getTables("APP", null, null, new String[] { "TABLE" });
            ResultSetMetaData rsMeta = rs.getMetaData();
            for (int i = 0; i < rsMeta.getColumnCount(); i++) {
                System.out.print(rsMeta.getColumnName(i + 1) + "\t");
            }
            System.out.println();
            while (rs.next()) {
                for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                    System.out.print(rs.getString(i + 1) + "\t");
                }
                System.out.println();
                result.add(rs.getString("TABLE_NAME"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static ResultSet getColumnNamesForTable(String tableName) {
        Vector result = new Vector();
        try {
            Connection con = DatabaseCreation.getActiveConnection();
            DatabaseMetaData dbMeta = con.getMetaData();
            ResultSet rs = dbMeta.getColumns(null, null, tableName, null);
            return rs;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}

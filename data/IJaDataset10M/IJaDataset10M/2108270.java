package Cosmo.dbms.dbAccess;

import java.util.Vector;
import Cosmo.util.Constants;

/**
 * A class provides methods to generate SQL statement based on common parameters.
 * <P>
 * @author Ting Al Fu
 */
public class SQLUtil {

    public SQLUtil() {
    }

    public static String createTable(String tableName, Vector column, String keys) {
        String sql = "CREATE TABLE ";
        sql += tableName;
        sql += " ( ";
        for (int i = 0; i < column.size(); i++) {
            sql += (String) column.elementAt(i);
            sql += ", ";
        }
        sql += keys;
        sql += " )";
        return sql;
    }

    public static String delete(String tableName, String where) {
        String sql = "DELETE FROM ";
        sql += tableName;
        if (where.length() > 0) {
            sql += " WHERE ";
            sql += where;
        } else {
        }
        return sql;
    }

    public static String delete(String tableName) {
        String sql = "DELETE  * FROM ";
        sql += tableName;
        return sql;
    }

    public static String count(String tableName, String where) {
        String sql = "SELECT COUNT(*) FROM ";
        sql += tableName;
        if (where.length() > 0) {
            sql += " WHERE ";
            sql += where;
        }
        return sql;
    }

    public static String update(String tableName, Vector columns, Vector values, String where) {
        String sql = "UPDATE ";
        sql += tableName;
        sql += " SET ";
        String column = (String) columns.elementAt(0);
        String value = (String) values.elementAt(0);
        sql += column;
        sql += "=";
        sql += value;
        for (int i = 1; i < columns.size(); i++) {
            sql += ", ";
            column = (String) columns.elementAt(i);
            value = (String) values.elementAt(i);
            sql += column;
            sql += "=";
            sql += value;
        }
        sql += " WHERE ";
        sql += where;
        Constants.iLog.LogInfoLine("UPDATE SQLLLLLLLLLLL :  " + sql);
        return sql;
    }

    public static String foreignKeys(String local, String remote) {
        String sql = "FOREIGN KEY (";
        sql += local;
        sql += ") REFERENCES ";
        sql += remote;
        sql += " ON DELETE CASCADE ";
        return sql;
    }

    public static String insert(String tableName, Vector values) {
        String sql = "INSERT INTO ";
        sql += tableName;
        sql += " VALUES (";
        for (int i = 0; i < values.size() - 1; i++) {
            sql += (String) values.elementAt(i);
            sql += ", ";
        }
        sql += (String) values.elementAt(values.size() - 1);
        sql += " )";
        Constants.iLog.LogInfoLine(sql);
        return sql;
    }

    public static String dropTable(String tableName) {
        String sql = "DROP TABLE " + tableName + " CASCADE CONSTRAINTS";
        return sql;
    }

    public static String query(Vector select, Vector from, String where, String other) {
        String sql = "SELECT ";
        for (int i = 0; i < select.size() - 1; i++) {
            sql += (String) select.elementAt(i);
            sql += ", ";
        }
        sql += (String) select.elementAt(select.size() - 1);
        sql += " FROM ";
        for (int i = 0; i < from.size() - 1; i++) {
            sql += (String) from.elementAt(i);
            sql += ", ";
        }
        sql += (String) from.elementAt(from.size() - 1);
        if (where.length() > 0) {
            sql += " WHERE ";
            sql += where;
        }
        if (other.length() > 0) {
            sql += " ";
            sql += other;
        }
        return sql;
    }

    public static String toString(String input) {
        String sql = "\'" + input + "\'";
        return sql;
    }

    public static String MAX(String columnName) {
        return " MAX(" + columnName + ") ";
    }
}

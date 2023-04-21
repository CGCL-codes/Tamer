package proj5labbd;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * A DBInterface tem como propósito prover uma comunicação mais fácil entre o SQL automatizando
 * algumas operações e evitando o uso das bibliotecas JDBC na interface gráfica.
 */
public class DBInterface {

    protected DBConnector conn;

    public enum DBQuery {

        VIEW, REMOVE, UPDATE, INSERT, JOIN
    }

    public DBInterface(DBConnector conn) {
        this.conn = conn;
    }

    public String queryFactory(String[] table, String[] fields, Object[] param, DBQuery opt) {
        String query = new String("");
        if (opt == DBQuery.VIEW) {
            query += "SELECT ";
            if (fields == null || fields.length == 0) {
                query += "* ";
            } else {
                for (int i = 0; i < fields.length - 1; i++) {
                    query += fields[i] + ", ";
                }
                query += fields[fields.length - 1] + " ";
            }
            query += "FROM " + table[0];
            if ((param != null && param.length != 0)) {
                query += " WHERE ";
                for (int i = 0; i < param.length - 1; i++) {
                    query += param[i] + " AND ";
                }
                query += param[param.length - 1];
            }
        }
        if (opt == DBQuery.INSERT) {
            query += "INSERT INTO " + table[0] + "(";
            for (int i = 0; i < fields.length - 1; i++) {
                query += fields[i] + ", ";
            }
            query += fields[fields.length - 1] + ") VALUES (";
            for (int i = 0; i < param.length - 1; i++) {
                query += param[i] + ", ";
            }
            query += param[fields.length - 1] + ")";
        }
        if (opt == DBQuery.REMOVE) {
            query += "DELETE FROM " + table[0];
            if ((param != null && param.length != 0)) {
                query += " WHERE ";
                for (int i = 0; i < param.length - 1; i++) {
                    query += param[i] + " AND ";
                }
                query += param[param.length - 1];
            }
        }
        if (opt == DBQuery.JOIN) {
            query += "SELECT DISTINCT ";
            if (fields == null || fields.length == 0) {
                query += "* ";
            } else {
                for (int i = 0; i < fields.length - 1; i++) {
                    query += fields[i] + ", ";
                }
                query += fields[fields.length - 1] + " ";
            }
            query += "FROM ";
            for (int i = 0; i < table.length - 1; i++) {
                query += table[i] + ", ";
            }
            query += table[table.length - 1];
            if ((param != null && param.length != 0)) {
                query += " WHERE ";
                for (int i = 0; i < param.length - 1; i++) {
                    query += param[i] + " AND ";
                }
                query += param[param.length - 1];
            }
        }
        if (opt == DBQuery.UPDATE) {
            if (fields != null && fields.length != 0) {
                query += "UPDATE " + table[0] + " SET";
                for (int i = 0; i < fields.length - 1; i++) {
                    query += " " + fields[i] + ", ";
                }
                query += " " + fields[fields.length - 1];
                if (param != null && param.length != 0) {
                    query += " WHERE ";
                    for (int i = 0; i < param.length - 1; i++) query += param[i] + " AND ";
                    query += param[param.length - 1];
                }
            }
        }
        return query;
    }

    public void update(String[] fields, String[] param, String[] table) throws SQLException {
        Statement stmt = conn.getConn().createStatement();
        String query = queryFactory(table, fields, param, DBQuery.UPDATE);
        ResultSet rs = stmt.executeQuery(query);
    }

    public String[][] view(String[] fields, String[] param, String[] table) throws SQLException {
        Statement stmt = conn.getConn().createStatement();
        String query = new String();
        if (table.length == 1) query = queryFactory(table, fields, param, DBQuery.VIEW); else if (table.length > 1) query = queryFactory(table, fields, param, DBQuery.JOIN);
        ResultSet rs = stmt.executeQuery(query);
        ResultSetMetaData meta = rs.getMetaData();
        int col = meta.getColumnCount();
        ArrayList<String[]> s = new ArrayList<String[]>();
        int i = 0;
        while (rs.next()) {
            s.add(new String[col]);
            for (int j = 1; j <= col; j++) {
                s.get(i)[j - 1] = rs.getString(j);
            }
            i++;
        }
        String[][] sArray = new String[s.size()][col];
        s.toArray(sArray);
        return sArray;
    }

    public void insert(String[] fields, String[] param, String[] table) throws SQLException {
        Statement stmt = conn.getConn().createStatement();
        String query = queryFactory(table, fields, param, DBQuery.INSERT);
        ResultSet rs = stmt.executeQuery(query);
    }

    public void remove(String[] param, String[] table) throws SQLException {
        Statement stmt = conn.getConn().createStatement();
        String query = queryFactory(table, null, param, DBQuery.REMOVE);
        ResultSet rs = stmt.executeQuery(query);
    }

    public Connection getConn() {
        return conn.getConn();
    }

    public String[][] execJoin(String query) throws SQLException {
        Statement stmt = conn.getConn().createStatement();
        ResultSet rs = stmt.executeQuery(query);
        ResultSetMetaData meta = rs.getMetaData();
        int col = meta.getColumnCount();
        ArrayList<String[]> s = new ArrayList<String[]>();
        int i = 0;
        while (rs.next()) {
            s.add(new String[col]);
            for (int j = 1; j <= col; j++) {
                s.get(i)[j - 1] = rs.getString(j);
            }
            i++;
        }
        String[][] sArray = new String[s.size()][col];
        s.toArray(sArray);
        return sArray;
    }

    public String[][] rsToStringArray(ResultSet rs) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();
        int col = meta.getColumnCount();
        ArrayList<String[]> s = new ArrayList<String[]>();
        int i = 0;
        while (rs.next()) {
            s.add(new String[col]);
            for (int j = 1; j <= col; j++) {
                s.get(i)[j - 1] = rs.getString(j);
            }
            i++;
        }
        String[][] sArray = new String[s.size()][col];
        s.toArray(sArray);
        return sArray;
    }
}

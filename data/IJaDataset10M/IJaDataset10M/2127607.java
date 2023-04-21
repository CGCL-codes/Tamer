package fi.kaila.suku.util;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;
import java.util.Vector;

/**
 * Auxiliary used by import from Suku 2004.
 * 
 * @author Kalle
 */
public class NameArray implements Array {

    @Override
    public void free() throws SQLException {
        v.clear();
    }

    private Vector<String> v = null;

    /**
	 * initialize class.
	 */
    public NameArray() {
        v = new Vector<String>();
    }

    /**
	 * append to container vector.
	 * 
	 * @param member
	 *            the member
	 */
    public void append(String member) {
        v.add(member);
    }

    @Override
    public Object getArray() throws SQLException {
        return null;
    }

    @Override
    public Object getArray(Map<String, Class<?>> arg0) throws SQLException {
        return null;
    }

    @Override
    public Object getArray(long arg0, int arg1) throws SQLException {
        return null;
    }

    @Override
    public Object getArray(long arg0, int arg1, Map<String, Class<?>> arg2) throws SQLException {
        return null;
    }

    @Override
    public int getBaseType() throws SQLException {
        return Types.VARCHAR;
    }

    @Override
    public String getBaseTypeName() throws SQLException {
        return "varchar";
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        return null;
    }

    @Override
    public ResultSet getResultSet(Map<String, Class<?>> arg0) throws SQLException {
        return null;
    }

    @Override
    public ResultSet getResultSet(long arg0, int arg1) throws SQLException {
        return null;
    }

    @Override
    public ResultSet getResultSet(long arg0, int arg1, Map<String, Class<?>> arg2) throws SQLException {
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int i = 0; i < v.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append("\"");
            sb.append(toSqlString(v.get(i)));
            sb.append("\"");
        }
        sb.append("}");
        return sb.toString();
    }

    private String toSqlString(String text) {
        if (text == null) return null;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            switch(c) {
                case '\\':
                case '"':
                case '\'':
                    sb.append('\\');
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }
}

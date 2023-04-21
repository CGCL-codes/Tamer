package org.commons.database.util.jquerycreator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utilit&agrave; di uso comune per lavorare con API JDBC.
 * 
 * @author Marco Speranza
 */
public final class JdbcUtils {

    /**
     * Chiude il <code>ResultSet</code>, se non &egrave; <code>null</code>.
     * 
     * @param rs
     *            Il <code>ResultSet</code> da chiudere
     * 
     * @throws SQLException
     *             the SQL exception
     */
    public static void close(final ResultSet rs) throws SQLException {
        if (rs != null) {
            rs.close();
        }
    }

    /**
     * Chiude lo <code>Statement</code>, se non &egrave; <code>null</code>.
     * 
     * @param stmt
     *            Lo <code>Statement</code> da chiudere
     * 
     * @throws SQLException
     *             the SQL exception
     */
    public static void close(final Statement stmt) throws SQLException {
        if (stmt != null) {
            stmt.close();
        }
    }

    /**
     * Chiude il <code>ResultSet</code> e lo <code>Statement</code> se non sono
     * <code>null</code>.
     * 
     * @param rs
     *            Il <code>java.sql.ResultSet</code> da chiudere
     * @param stmt
     *            Il <code>java.sql.Statement</code> da chiudere
     * 
     * @throws SQLException
     *             the SQL exception
     */
    public static void close(final ResultSet rs, final Statement stmt) throws SQLException {
        close(rs);
        close(stmt);
    }
}

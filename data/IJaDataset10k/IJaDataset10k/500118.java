package org.gbif.checklistbank.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PgSqlUtils {

    private static final Logger log = LoggerFactory.getLogger(PgSqlUtils.class);

    public static String addIndexSql(String table, String... attributes) {
        List<String> attrs = new ArrayList<String>();
        for (String a : attributes) {
            attrs.add(buildIndexAttributeName(a));
        }
        return "CREATE INDEX " + buildIndexName(table, attributes) + " ON " + table + " USING btree (" + StringUtils.join(attrs, ",") + ")";
    }

    public static String analyzeSql(@Nullable String table) {
        return "VACUUM ANALYZE " + StringUtils.trimToEmpty(table);
    }

    public static String deferConstraints() {
        return "SET CONSTRAINTS ALL DEFERRED";
    }

    private static String buildIndexAttributeName(String attr) {
        attr = attr.replaceAll("\\s", "");
        if (attr.contains("(")) {
            return attr.replace("(", "(\"").replace(")", "\")");
        } else {
            return "\"" + attr + "\"";
        }
    }

    public static String buildIndexName(String table, String... attributes) {
        String idxName;
        if (table.contains(".")) {
            idxName = StringUtils.substringAfter(table, ".");
        } else {
            idxName = table;
        }
        table = table.replaceAll("\\s", "");
        List<String> attrs = new ArrayList<String>();
        for (String a : attributes) {
            attrs.add(a.replaceAll("\\s", "").replaceAll("[().]", "_"));
        }
        return idxName + "_" + StringUtils.join(attrs, "_").replaceAll("_+", "_") + "_idx";
    }

    public static String disableTrigger(String table) {
        return "ALTER TABLE " + table + " disable trigger all";
    }

    public static String dropSchemaSql(String schema) {
        return "drop schema IF EXISTS " + schema + " cascade";
    }

    public static void dropTable(String table, Connection conn) throws SQLException {
        Statement st = null;
        try {
            st = conn.createStatement();
            st.execute("drop table IF EXISTS " + table);
            conn.commit();
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
            } catch (SQLException e) {
                log.error("Error dropping table " + table, e);
            }
        }
    }

    public static String dropTableSql(String table) {
        return "drop table IF EXISTS " + table;
    }

    public static String enableTrigger(String table) {
        return "ALTER TABLE " + table + " enable trigger all";
    }

    public static Boolean getBoolean(ResultSet rs, String column) throws SQLException {
        boolean val = rs.getBoolean(column);
        if (rs.wasNull()) {
            return null;
        }
        return val;
    }

    public static Byte getByte(ResultSet rs, String column) throws SQLException {
        byte val = rs.getByte(column);
        if (rs.wasNull()) {
            return null;
        }
        return val;
    }

    public static Integer getGeneratedIntegerKey(PreparedStatement ps) throws SQLException {
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            return rs.getInt(1);
        }
        throw new IllegalArgumentException("Insert returned no generated key");
    }

    public static Integer getInteger(ResultSet rs, String column) throws SQLException {
        int val = rs.getInt(column);
        if (rs.wasNull()) {
            return null;
        }
        return val;
    }

    public static Long getLong(ResultSet rs, String column) throws SQLException {
        long val = rs.getLong(column);
        if (rs.wasNull()) {
            return null;
        }
        return val;
    }

    public static String removeIndexSql(String table, String... attributes) {
        return "DROP INDEX IF EXISTS " + buildIndexName(table, attributes);
    }

    public static String setSequenceValueSql(String sequenceName, long value) {
        return "SELECT setval('" + sequenceName + "', " + value + ")";
    }

    public static void truncateTable(String table, Connection conn) throws SQLException {
        Statement st = null;
        try {
            st = conn.createStatement();
            st.execute("Truncate table " + table);
            conn.commit();
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
            } catch (SQLException e) {
                log.error("Error truncating table " + table, e);
            }
        }
    }
}

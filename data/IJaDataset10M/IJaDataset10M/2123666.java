package jk.spider.core.storage.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;
import jk.spider.util.SpiderUtil;
import jk.spider.util.config.PropertySet;

public class DAOHelp {

    protected static final Logger log = Logger.getLogger(DAOHelp.class);

    public static final String DRIVER = "driver";

    public static final String USER = "user";

    public static final String PASSWORD = "password";

    public static final String URL = "url";

    protected Connection connection;

    protected DataSource dateSource;

    protected BasicDataSource ds;

    public DAOHelp(PropertySet props) {
        connection = this.getConnection(props);
    }

    public DAOHelp(String dbUrl) {
        connection = this.getConnection(dbUrl);
    }

    public String format(boolean bool) {
        return bool ? "1" : "0";
    }

    public String format(int i) {
        return "" + i;
    }

    public String format(String str) {
        return "'" + this.formatSQL(str) + "'";
    }

    public String formatSQL(String str) {
        if (!SpiderUtil.isStringNull(str)) return str.replaceAll("'", "''"); else return str;
    }

    public Connection getConnection() {
        return this.connection;
    }

    public DataSource getDateSource() {
        return this.dateSource;
    }

    public Connection getConnection(PropertySet props) {
        Connection connection = null;
        try {
            dateSource = this.setupDataSource(props);
            connection = dateSource.getConnection();
        } catch (SQLException e) {
            log.error("SQL Exception during JDBC Connect", e);
            throw new RuntimeException("SQL Exception during JDBC Connect");
        }
        return connection;
    }

    public Connection getConnection(String dbUrl) {
        Connection connection = null;
        try {
            dateSource = this.setupDataSource(dbUrl);
            connection = dateSource.getConnection();
        } catch (SQLException e) {
            log.error("SQL Exception during JDBC Connect", e);
            throw new RuntimeException("SQL Exception during JDBC Connect");
        }
        return connection;
    }

    public int execSql(String sql) {
        PreparedStatement stmt = null;
        int rows = 0;
        try {
            stmt = connection.prepareStatement(sql);
            rows = stmt.executeUpdate();
            safeClose(stmt);
        } catch (SQLException e) {
            log.warn("DAOHelpImpl update SQLException -> ", e);
            this.rollBack(connection);
        } catch (NullPointerException e) {
            log.warn("DAOHelpImpl update NullPointerException -> ", e);
        } finally {
            safeClose(stmt);
        }
        return rows;
    }

    public void safeClose(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                log.error("error closing resultset", e);
            }
        }
    }

    public void safeClose(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                log.error("error closing resultset", e);
            }
        }
    }

    protected void fillStatement(PreparedStatement stmt, Object[] params) throws SQLException {
        if (params == null) {
            return;
        }
        for (int i = 0; i < params.length; i++) {
            try {
                if (params[i] != null) {
                    stmt.setObject(i + 1, params[i]);
                } else {
                    stmt.setNull(i + 1, Types.VARCHAR);
                }
            } catch (NumberFormatException e) {
                stmt.setObject(i + 1, 0);
            }
        }
    }

    private void rollBack(Connection conn) {
        log.debug("rollback...");
        try {
            conn.rollback();
        } catch (SQLException e) {
            log.warn("DAOHelpImpl rollBack -> ", e);
        }
    }

    private DataSource setupDataSource(String dbUrl) {
        ds = new BasicDataSource();
        String driverClassName = "org.hsqldb.jdbcDriver";
        String url = dbUrl;
        String username = "sa";
        String password = "";
        ds.setDriverClassName(driverClassName);
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setUrl(url);
        return ds;
    }

    private DataSource setupDataSource(PropertySet props) {
        ds = new BasicDataSource();
        String driverClassName = props.getString(DRIVER, "");
        String url = props.getString(URL, "");
        String username = props.getString(USER, "sa");
        String password = props.getString(PASSWORD, "");
        ds.setDriverClassName(driverClassName);
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setUrl(url);
        return ds;
    }

    public void shutdown() {
        try {
            if (connection != null) connection.close();
            if (ds != null) ds.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

package com.globalretailtech.data;

import java.util.Properties;
import java.util.Vector;
import java.sql.*;
import com.globalretailtech.util.ShareProperties;
import com.globalretailtech.util.sql.*;
import org.apache.log4j.Logger;

/**
 * DBContext implements a simple 2-tier
 * database api, adequate for a POS.
 *
 * @author  Quentin Olson
 */
public class DBContext {

    static Logger logger = Logger.getLogger(DBContext.class);

    private CommonDBContext dbctxt;

    private Connection conn;

    private String driver;

    private String connect;

    private String user;

    private String password;

    private SQLException exception;

    public static final String[] tableList = new String[] { "BO_MAP", "BO_TYPE", "BO_VERSION", "BU", "CURRENCY", "CURRENCY_CODE", "CURRENCY_DENOMINATION", "CUSTOMER", "DEPARTMENT", "DIALOG", "DIALOG_EVENT", "EMPLOYEE", "EMPLOYEE_TIMESHEET", "HELP", "INPUT_FILTER", "INPUT_FILTER_FIELD", "ITEM", "ITEM_LINK", "MEDIA", "MENU_ROOT", "PLU", "POS", "POS_BALANCE_DETAIL", "POS_CLOSE", "POS_CLOSE_DETAIL", "POS_CONFIG", "POS_KEY", "POS_LITERAL", "POS_PARAM", "POS_PROFILE", "POS_PROFILE_EVENT", "POS_SESSION", "POS_TOTAL", "POS_TRANS_UPLOAD", "PROMOTION", "PROMOTION_MAP", "REASON_CODE", "REASON_CODE_CATEGORY", "SITE", "SITE_DATA_STORE", "SUB", "SUB_MAP", "SUB_MENU", "TAX", "TAX_GROUP", "TOTAL", "TRANS", "TRANS_BANK", "TRANS_CUSTOMER", "TRANS_ITEM", "TRANS_PROMOTION", "TRANS_TAX", "TRANS_TENDER", "TRANS_TIME_PUNCH", "TRANS_TOTAL", "LOOKUPALT_BARCODE" };

    public String getDriver() {
        return driver;
    }

    public void setDriver(String value) {
        driver = value;
    }

    public String getConnect() {
        return connect;
    }

    public void setConnect(String value) {
        connect = value;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String value) {
        user = value;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String value) {
        password = value;
    }

    public SQLException getException() {
        return exception;
    }

    public void setException(SQLException value) {
        logger.warn("SQL Exeption " + value.toString(), value);
        exception = value;
    }

    public boolean init() {
        return init(this.getClass().getName());
    }

    public boolean init(String propertyname) {
        ShareProperties p = new ShareProperties(propertyname);
        if (p.Found()) {
            init(p);
        } else {
            logger.fatal("Database properties not found, " + propertyname);
            return false;
        }
        return makeConnection(driver, connect, user, password);
    }

    public void init(Properties p) {
        driver = p.getProperty("Driver", "interbase.interclient.Driver");
        connect = p.getProperty("ConnectPrefix") + p.getProperty("ConnectHost") + p.getProperty("ConnectFile");
        user = p.getProperty("User", "sysdba");
        password = p.getProperty("Password", "masterkey");
    }

    /** makes connection with prevously set driver/connect/user/password */
    public boolean makeConnection() {
        return makeConnection(driver, connect, user, password);
    }

    public boolean makeConnection(String d, String c, String u, String p) {
        setDriver(d);
        setConnect(c);
        setUser(u);
        setPassword(p);
        logger.info("Connect : " + driver + " : " + connect + " : " + user + " : " + password);
        try {
            Class.forName(driver);
        } catch (java.lang.ClassNotFoundException e) {
            logger.fatal(e.toString(), e);
            return false;
        }
        try {
            conn = DriverManager.getConnection(connect, user, password);
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            logger.fatal(e.toString(), e);
            return false;
        }
        if (getDriver().indexOf("postgres") != -1) {
            dbctxt = new PostgresDBContext();
        } else if (getDriver().indexOf("interbase") != -1) {
            dbctxt = new InterbaseDBContext();
        } else if (getDriver().indexOf("hsqldb") != -1) {
            dbctxt = new HsqlBContext();
        } else {
            return false;
        }
        return true;
    }

    public void Close() {
        try {
            conn.close();
        } catch (SQLException e) {
        }
    }

    public void commit() throws SQLException {
        try {
            conn.commit();
        } catch (SQLException e) {
            logger.fatal(e.toString(), e);
            throw e;
        }
    }

    private Vector fetchGeneric(DBRecord rec, String fs, boolean withRelations) {
        logger.debug(fs + " " + rec.getClass());
        try {
            Statement stmt = conn.createStatement();
            ResultSet rset = stmt.executeQuery(fs);
            Vector v = new Vector();
            while (rset.next()) {
                DBRecord d = rec.copy();
                d.populate(rset);
                if (withRelations) d.relations();
                v.add(d);
            }
            return v;
        } catch (SQLException e) {
            logger.fatal("Exception in fetch : " + e.toString(), e);
            return null;
        }
    }

    public Vector fetch(DBRecord rec, String fs) {
        return fetchGeneric(rec, fs, true);
    }

    public Vector fetchNoRelations(DBRecord rec, String fs) {
        return fetchGeneric(rec, fs, false);
    }

    private ResultSet executeGeneric(SQLCall sql, boolean withResult) throws UnknownSQLCall, SQLException {
        return executeGeneric(dbctxt.generateCall(sql), withResult);
    }

    private ResultSet executeGeneric(String sql, boolean withResult) throws SQLException {
        logger.debug(sql);
        try {
            Statement stmt = conn.createStatement();
            if (stmt.execute(sql) && withResult) {
                ResultSet rset = stmt.getResultSet();
                return rset;
            }
            logger.info("affected:" + stmt.getUpdateCount());
        } catch (SQLException e) {
            logger.fatal("Exception in executeWithResult : " + e.toString(), e);
            logger.fatal("Exception statement : " + sql);
            throw e;
        }
        return null;
    }

    public void execute(SQLCall sql) throws SQLException, UnknownSQLCall {
        executeGeneric(sql, false);
    }

    public void execute(String sql) throws SQLException {
        executeGeneric(sql, false);
    }

    public ResultSet executeWithResult(SQLCall sql) throws UnknownSQLCall {
        try {
            return executeGeneric(sql, true);
        } catch (SQLException e) {
            return null;
        }
    }

    public ResultSet executeWithResult(String sql) {
        try {
            return executeGeneric(sql, true);
        } catch (SQLException e) {
            return null;
        }
    }

    public Connection getConn() {
        return conn;
    }

    public static String getClassNameForType(int type) {
        if (type == Types.BIGINT) return "java.lang.Long"; else if (type == Types.BIT) return "java.lang.Boolean"; else if (type == Types.BOOLEAN) return "java.lang.Boolean"; else if (type == Types.CHAR) return "java.lang.Char"; else if (type == Types.DATE) return "java.sql.Timestamp"; else if (type == Types.TIMESTAMP) return "java.sql.Timestamp"; else if (type == Types.DOUBLE) return "java.lang.Double"; else if (type == Types.FLOAT) return "java.lang.Float"; else if (type == Types.NUMERIC) return "java.lang.Float"; else if (type == Types.INTEGER) return "java.lang.Integer"; else if (type == Types.LONGVARCHAR) return "java.lang.String"; else if (type == Types.REAL) return "java.lang.Float"; else if (type == Types.SMALLINT) return "java.lang.Integer"; else if (type == Types.TIME) return "java.sql.Timestamp"; else if (type == Types.VARCHAR) return "java.lang.String"; else {
            throw new IllegalArgumentException("Unknown SQL Type:" + type);
        }
    }
}

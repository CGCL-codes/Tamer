package org.fantasy.common.db.pool;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import org.olap4j.OlapConnection;
import org.olap4j.OlapDatabaseMetaData;
import org.olap4j.OlapException;
import org.olap4j.OlapStatement;
import org.olap4j.PreparedOlapStatement;
import org.olap4j.Scenario;
import org.olap4j.mdx.parser.MdxParserFactory;
import org.olap4j.metadata.Catalog;
import org.olap4j.metadata.NamedList;
import org.olap4j.metadata.Schema;

/**
 * OlapConnection代理，代理close()方法。
 * 
 * @author 王文成
 * @version 1.0
 * @since 2011-3-24
 */
public class OlapConnectionProxy implements OlapConnection {

    /**
	 * OlapConnection数据库连接
	 */
    private OlapConnection conn;

    /**
	 * 数据库配置
	 */
    private ConnectionManager manager;

    public OlapConnectionProxy(ConnectionManager manager, Connection conn) {
        this.conn = (OlapConnection) conn;
        this.manager = manager;
    }

    /**
	 * 重写Close方法
	 */
    public void close() throws SQLException {
        manager.freeConnection(conn);
    }

    public Scenario createScenario() {
        return conn.createScenario();
    }

    public OlapStatement createStatement() throws OlapException {
        return conn.createStatement();
    }

    public List<String> getAvailableRoleNames() {
        return conn.getAvailableRoleNames();
    }

    public NamedList<Catalog> getCatalogs() {
        return conn.getCatalogs();
    }

    public Locale getLocale() {
        return conn.getLocale();
    }

    public OlapDatabaseMetaData getMetaData() throws OlapException {
        return conn.getMetaData();
    }

    public MdxParserFactory getParserFactory() {
        return conn.getParserFactory();
    }

    public String getRoleName() {
        return conn.getRoleName();
    }

    public Scenario getScenario() {
        return conn.getScenario();
    }

    public Schema getSchema() throws OlapException {
        return conn.getSchema();
    }

    public PreparedOlapStatement prepareOlapStatement(String mdx) throws OlapException {
        return conn.prepareOlapStatement(mdx);
    }

    public void setLocale(Locale locale) {
        conn.setLocale(locale);
    }

    public void setRoleName(String roleName) throws OlapException {
        conn.setRoleName(roleName);
    }

    public void setScenario(Scenario scenario) {
        conn.setScenario(scenario);
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return conn.isWrapperFor(iface);
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        return conn.unwrap(iface);
    }

    public void clearWarnings() throws SQLException {
        conn.clearWarnings();
    }

    public void commit() throws SQLException {
        conn.commit();
    }

    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        return conn.createArrayOf(typeName, elements);
    }

    public Blob createBlob() throws SQLException {
        return conn.createBlob();
    }

    public Clob createClob() throws SQLException {
        return conn.createClob();
    }

    public NClob createNClob() throws SQLException {
        return conn.createNClob();
    }

    public SQLXML createSQLXML() throws SQLException {
        return conn.createSQLXML();
    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return conn.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        return conn.createStatement(resultSetType, resultSetConcurrency);
    }

    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        return conn.createStruct(typeName, attributes);
    }

    public boolean getAutoCommit() throws SQLException {
        return conn.getAutoCommit();
    }

    public String getCatalog() throws SQLException {
        return conn.getCatalog();
    }

    public Properties getClientInfo() throws SQLException {
        return conn.getClientInfo();
    }

    public String getClientInfo(String name) throws SQLException {
        return conn.getClientInfo(name);
    }

    public int getHoldability() throws SQLException {
        return conn.getHoldability();
    }

    public int getTransactionIsolation() throws SQLException {
        return conn.getTransactionIsolation();
    }

    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return conn.getTypeMap();
    }

    public SQLWarning getWarnings() throws SQLException {
        return conn.getWarnings();
    }

    public boolean isClosed() throws SQLException {
        return conn.isClosed();
    }

    public boolean isReadOnly() throws SQLException {
        return conn.isReadOnly();
    }

    public boolean isValid(int timeout) throws SQLException {
        return conn.isValid(timeout);
    }

    public String nativeSQL(String sql) throws SQLException {
        return conn.nativeSQL(sql);
    }

    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return conn.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return conn.prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    public CallableStatement prepareCall(String sql) throws SQLException {
        return conn.prepareCall(sql);
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return conn.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return conn.prepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        return conn.prepareStatement(sql, autoGeneratedKeys);
    }

    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        return conn.prepareStatement(sql, columnIndexes);
    }

    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        return conn.prepareStatement(sql, columnNames);
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return conn.prepareStatement(sql);
    }

    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        conn.releaseSavepoint(savepoint);
    }

    public void rollback() throws SQLException {
        conn.rollback();
    }

    public void rollback(Savepoint savepoint) throws SQLException {
        conn.rollback(savepoint);
    }

    public void setAutoCommit(boolean autoCommit) throws SQLException {
        conn.setAutoCommit(autoCommit);
    }

    public void setCatalog(String catalog) throws SQLException {
        conn.setCatalog(catalog);
    }

    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        conn.setClientInfo(properties);
    }

    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        conn.setClientInfo(name, value);
    }

    public void setHoldability(int holdability) throws SQLException {
        conn.setHoldability(holdability);
    }

    public void setReadOnly(boolean readOnly) throws SQLException {
        conn.setReadOnly(readOnly);
    }

    public Savepoint setSavepoint() throws SQLException {
        return conn.setSavepoint();
    }

    public Savepoint setSavepoint(String name) throws SQLException {
        return conn.setSavepoint(name);
    }

    public void setTransactionIsolation(int level) throws SQLException {
        conn.setTransactionIsolation(level);
    }

    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        conn.setTypeMap(map);
    }
}

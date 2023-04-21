package com.oneline.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.apache.log4j.Logger;

public abstract class ReadBase<T> {

    protected Connection con = null;

    protected Statement stmt = null;

    protected PreparedStatement prepareStmt = null;

    protected ResultSet rs = null;

    private static final Logger LOG = Logger.getLogger(ReadBase.class);

    /**
	 * Returns the appropriate results for the given sql statement.
	 * The data is stored in generic Object.class instance 
	 * @param sqlStmt
	 * @return List of records
	 * @throws SQLException
	 */
    public List<T> execute(String sqlStmt) throws SQLException {
        try {
            this.con = PoolFactory.getDefaultPool().getConnection();
            this.prepare(sqlStmt);
            return this.populate();
        } catch (SQLException ex) {
            LOG.fatal(ex);
            throw (ex);
        } finally {
            this.release();
        }
    }

    /**
	 * This is with prepare statement.
	 * @param sqlStmt
	 * @param clazz
	 * @return
	 * @throws SQLException
	 */
    public List<T> execute(String sqlStmt, Object[] columns) throws SQLException {
        try {
            this.con = PoolFactory.getDefaultPool().getConnection();
            this.prepare(sqlStmt, columns);
            return this.populate();
        } catch (SQLException ex) {
            LOG.fatal(ex);
            throw (ex);
        } finally {
            this.release();
        }
    }

    /**
	 * This is with prepare statement.
	 * @param sqlStmt
	 * @param clazz
	 * @return
	 * @throws SQLException
	 */
    public List<T> execute(String sqlStmt, List<Object> columns) throws SQLException {
        try {
            this.con = PoolFactory.getDefaultPool().getConnection();
            this.prepare(sqlStmt, columns);
            return this.populate();
        } catch (SQLException ex) {
            LOG.fatal(ex);
            throw (ex);
        } finally {
            this.release();
        }
    }

    /**
	 * This is with prepare statement to get a record by unique key.
	 * @param sqlStmt
	 * @param clazz
	 * @return
	 * @throws SQLException
	 */
    public T selectByPrimaryKey(String sqlStmt, Object id) throws SQLException {
        try {
            this.con = PoolFactory.getDefaultPool().getConnection();
            this.prepareForPrimaryKey(sqlStmt, id);
            return this.getFirstRow();
        } catch (SQLException ex) {
            LOG.fatal(ex);
            throw (ex);
        } finally {
            this.release();
        }
    }

    public T selectByUniqueKey(String sqlStmt, Object[] columns) throws SQLException {
        try {
            this.con = PoolFactory.getDefaultPool().getConnection();
            this.prepare(sqlStmt, columns);
            return this.getFirstRow();
        } catch (SQLException ex) {
            LOG.fatal(ex);
            throw (ex);
        } finally {
            this.release();
        }
    }

    protected void prepare(String sqlStmt) throws SQLException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Sql=" + sqlStmt);
        }
        this.stmt = this.con.createStatement();
        this.rs = this.stmt.executeQuery(sqlStmt);
    }

    protected void prepareForPrimaryKey(String sqlStmt, Object id) throws SQLException {
        this.prepareStmt = this.con.prepareStatement(sqlStmt);
        this.prepareStmt.setObject(1, id);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Sql statement\n " + sqlStmt + "\n id=" + id);
        }
        this.rs = this.prepareStmt.executeQuery();
    }

    protected void prepare(String sqlStmt, Object[] columns) throws SQLException {
        this.prepareStmt = this.con.prepareStatement(sqlStmt);
        for (int i = 1; i <= columns.length; i++) {
            this.prepareStmt.setObject(i, columns[i - 1]);
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Sql statement\n " + sqlStmt);
            for (int i = 1; i <= columns.length; i++) LOG.debug("\nParam=" + columns[i - 1]);
        }
        this.rs = this.prepareStmt.executeQuery();
    }

    protected void prepare(String sqlStmt, List<Object> columns) throws SQLException {
        this.prepareStmt = this.con.prepareStatement(sqlStmt);
        int colsT = columns.size();
        for (int i = 1; i <= colsT; i++) {
            this.prepareStmt.setObject(i, columns.get(i - 1));
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Sql statement\n " + sqlStmt);
            for (int i = 1; i <= colsT; i++) LOG.debug("\nParam=" + columns.get(i - 1));
        }
        this.rs = this.prepareStmt.executeQuery();
    }

    protected void release() {
        if (this.rs != null) {
            try {
                this.rs.close();
                this.rs = null;
            } catch (SQLException ex) {
                LOG.fatal(ex);
            }
        }
        if (this.prepareStmt != null) {
            try {
                this.prepareStmt.close();
                this.prepareStmt = null;
            } catch (SQLException ex) {
                LOG.fatal(ex);
            }
        }
        if (this.stmt != null) {
            try {
                this.stmt.close();
                this.stmt = null;
            } catch (SQLException ex) {
                LOG.fatal(ex);
            }
        }
        if (this.con != null) {
            try {
                this.con.close();
                this.con = null;
            } catch (SQLException ex) {
                LOG.fatal(ex);
            }
        }
    }

    protected abstract List<T> populate() throws SQLException;

    protected abstract T getFirstRow() throws SQLException;
}

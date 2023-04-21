package org.architecture.common.db;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.architecture.common.data.Page;
import org.architecture.common.db.paging.PagingSQLHandler;
import org.architecture.common.exception.DatabaseException;

/**
 * 쿼리 실행자
 * 
 * @author love
 * 
 */
public class QueryProcessorImpl extends JDBCProcessor implements QueryProcessor {

    private static final Log log = LogFactory.getLog(QueryProcessorImpl.class);

    public Page selectPage(String sql, Object[] params, int start, int size) throws DatabaseException {
        Page page = null;
        Connection conn = null;
        try {
            conn = super.getConnection();
            PagingSQLHandler pagingSQL = super.getPagingSQLHandler();
            long count = ((java.math.BigDecimal) this.selectObject(conn, pagingSQL.generateCountSQL(sql), params)).longValue();
            sql = pagingSQL.generatePagingSQL(sql);
            params = pagingSQL.createParameter(params, start, size);
            super.loggingSQL(sql, params);
            if (params != null) {
                List result = (List) getQueryRunner().query(conn, sql, super.getMapListResultHandler(), params);
                page = new Page(count, result, start, size);
            } else {
                List result = (List) getQueryRunner().query(conn, sql, super.getMapListResultHandler());
                page = new Page(count, result, start, size);
            }
        } catch (Exception ex) {
            String msg = "QueryProcessor [selectPage] execute failed : " + ex.getMessage();
            log.error(msg);
            throw new DatabaseException(msg, ex);
        } finally {
            super.closeConnection(conn);
        }
        return page;
    }

    public Page selectPage(Query query, Object[] params, int start, int size) throws DatabaseException {
        return selectPage(super.getSQL(query), params, start, size);
    }

    public Page selectPage(Connection conn, String sql, Object[] params, int start, int size) throws DatabaseException {
        Page page = null;
        try {
            PagingSQLHandler pagingSQL = super.getPagingSQLHandler();
            long count = ((java.math.BigDecimal) this.selectObject(conn, pagingSQL.generateCountSQL(sql), params)).longValue();
            sql = pagingSQL.generatePagingSQL(sql);
            params = pagingSQL.createParameter(params, start, size);
            super.loggingSQL(sql, params);
            if (params != null) {
                List result = (List) getQueryRunner().query(conn, sql, super.getMapListResultHandler(), params);
                page = new Page(count, result, start, size);
            } else {
                List result = (List) getQueryRunner().query(conn, sql, super.getMapListResultHandler());
                page = new Page(count, result, start, size);
            }
        } catch (Exception ex) {
            String msg = "QueryProcessor [selectPage] execute failed : " + ex.getMessage();
            log.error(msg);
            throw new DatabaseException(msg, ex);
        } finally {
        }
        return page;
    }

    public Page selectPage(Connection conn, Query query, Object[] params, int start, int size) throws DatabaseException {
        return selectPage(conn, super.getSQL(query), params, start, size);
    }

    public List selectList(String sql, Object[] params) throws DatabaseException {
        List result = null;
        Connection conn = null;
        try {
            conn = super.getConnection();
            super.loggingSQL(sql, params);
            if (params != null) {
                result = (List) getQueryRunner().query(conn, sql, super.getMapListResultHandler(), params);
            } else {
                result = (List) getQueryRunner().query(conn, sql, super.getMapListResultHandler());
            }
        } catch (Exception ex) {
            String msg = "QueryProcessor [selectList] execute failed : " + ex.getMessage();
            log.error(msg);
            throw new DatabaseException(msg, ex);
        } finally {
            super.closeConnection(conn);
        }
        return result;
    }

    public List selectList(Query query, Object[] params) throws DatabaseException {
        return selectList(super.getSQL(query), params);
    }

    public List selectList(Connection conn, String sql, Object[] params) throws DatabaseException {
        List result = null;
        try {
            super.loggingSQL(sql, params);
            if (params != null) {
                result = (List) getQueryRunner().query(conn, sql, super.getMapListResultHandler(), params);
            } else {
                result = (List) getQueryRunner().query(conn, sql, super.getMapListResultHandler());
            }
        } catch (Exception ex) {
            String msg = "QueryProcessor [selectList] execute failed : " + ex.getMessage();
            log.error(msg);
            throw new DatabaseException(msg, ex);
        } finally {
        }
        return result;
    }

    public List selectList(Connection conn, Query query, Object[] params) throws DatabaseException {
        return selectList(conn, super.getSQL(query), params);
    }

    public Map selectMap(String sql, Object[] params) throws DatabaseException {
        Map result = null;
        Connection conn = null;
        try {
            conn = super.getConnection();
            super.loggingSQL(sql, params);
            result = (Map) getQueryRunner().query(conn, sql, super.getMapResultHandler(), params);
        } catch (Exception ex) {
            String msg = "QueryProcessor [selectMap] execute failed : " + ex.getMessage();
            log.error(msg);
            throw new DatabaseException(msg, ex);
        } finally {
            super.closeConnection(conn);
        }
        return result;
    }

    public Map selectMap(Query query, Object[] params) throws DatabaseException {
        ;
        return selectMap(super.getSQL(query), params);
    }

    public Map selectMap(Connection conn, String sql, Object[] params) throws DatabaseException {
        Map result = null;
        try {
            super.loggingSQL(sql, params);
            result = (Map) getQueryRunner().query(conn, sql, super.getMapResultHandler(), params);
        } catch (Exception ex) {
            String msg = "QueryProcessor [selectMap] execute failed : " + ex.getMessage();
            log.error(msg);
            throw new DatabaseException(msg, ex);
        } finally {
        }
        return result;
    }

    public Map selectMap(Connection conn, Query query, Object[] params) throws DatabaseException {
        return selectMap(conn, super.getSQL(query), params);
    }

    public Object selectObject(String sql, Object[] params) throws DatabaseException {
        Object result = null;
        Connection conn = null;
        try {
            conn = super.getConnection();
            super.loggingSQL(sql, params);
            result = (Object) getQueryRunner().query(conn, sql, super.getObjectResultHandler(), params);
        } catch (Exception ex) {
            String msg = "QueryProcessor [selectObject] execute failed : " + ex.getMessage();
            log.error(msg);
            throw new DatabaseException(msg, ex);
        } finally {
            super.closeConnection(conn);
        }
        return result;
    }

    public Object selectObject(Query query, Object[] params) throws DatabaseException {
        return selectMap(super.getSQL(query), params);
    }

    public Object selectObject(Connection conn, String sql, Object[] params) throws DatabaseException {
        Object result = null;
        try {
            super.loggingSQL(sql, params);
            result = (Object) getQueryRunner().query(conn, sql, super.getObjectResultHandler(), params);
        } catch (Exception ex) {
            String msg = "QueryProcessor [selectObject] execute failed : " + ex.getMessage();
            log.error(msg);
            throw new DatabaseException(msg, ex);
        } finally {
        }
        return result;
    }

    public Object selectObject(Connection conn, Query query, Object[] params) throws DatabaseException {
        return selectObject(conn, super.getSQL(query), params);
    }

    public int update(String sql, Object[] params) throws DatabaseException {
        int result = 0;
        Connection conn = null;
        try {
            conn = super.getConnection();
            super.loggingSQL(sql, params);
            result = getQueryRunner().update(conn, sql, params);
        } catch (Exception ex) {
            String msg = "QueryProcessor [update] execute failed : " + ex.getMessage();
            log.error(msg);
            throw new DatabaseException(msg, ex);
        } finally {
            super.closeConnection(conn);
        }
        return result;
    }

    public int update(Query query, Object[] params) throws DatabaseException {
        return update(super.getSQL(query), params);
    }

    public int update(Connection conn, String sql, Object[] params) throws DatabaseException {
        int result = 0;
        try {
            super.loggingSQL(sql, params);
            result = getQueryRunner().update(conn, sql, params);
        } catch (Exception ex) {
            String msg = "QueryProcessor [update] execute failed : " + ex.getMessage();
            log.error(msg);
            throw new DatabaseException(msg, ex);
        } finally {
        }
        return result;
    }

    public int update(Connection conn, Query query, Object[] params) throws DatabaseException {
        return update(conn, super.getSQL(query), params);
    }

    public int[] batch(String sql, Object[][] params) throws DatabaseException {
        int[] result = null;
        Connection conn = null;
        try {
            conn = super.getConnection();
            super.loggingSQL(sql, params);
            result = getQueryRunner().batch(conn, sql, params);
        } catch (Exception ex) {
            String msg = "QueryProcessor [batch] execute failed : " + ex.getMessage();
            log.error(msg);
            throw new DatabaseException(msg, ex);
        } finally {
            super.closeConnection(conn);
        }
        return result;
    }

    public int[] batch(Query query, Object[][] params) throws DatabaseException {
        return batch(super.getSQL(query), params);
    }

    public int[] batch(Connection conn, String sql, Object[][] params) throws DatabaseException {
        int[] result;
        try {
            super.loggingSQL(sql, params);
            result = getQueryRunner().batch(conn, sql, params);
        } catch (Exception ex) {
            String msg = "QueryProcessor [batch] execute failed : " + ex.getMessage();
            log.error(msg);
            throw new DatabaseException(msg, ex);
        } finally {
        }
        return result;
    }

    public int[] batch(Connection conn, Query query, Object[][] params) throws DatabaseException {
        return batch(conn, super.getSQL(query), params);
    }
}

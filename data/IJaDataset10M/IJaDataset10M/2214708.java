package com.shenming.sms.dc.sql2java;

import java.sql.*;

/**
 * Handles database calls for the sm_vw_account_pay_sum table.
 */
public class SmVwAccountPaySumManager {

    /**
     * Column pay_sum of type Types.DECIMAL mapped to Long.
     */
    public static final int ID_PAY_SUM = 0;

    public static final int TYPE_PAY_SUM = Types.DECIMAL;

    public static final String NAME_PAY_SUM = "pay_sum";

    /**
     * Column user_id of type Types.DECIMAL mapped to Long.
     */
    public static final int ID_USER_ID = 1;

    public static final int TYPE_USER_ID = Types.DECIMAL;

    public static final String NAME_USER_ID = "user_id";

    private static final String TABLE_NAME = "sm_vw_account_pay_sum";

    /**
     * Create an array of type string containing all the fields of the sm_vw_account_pay_sum table.
     */
    private static final String[] FIELD_NAMES = { "sm_vw_account_pay_sum.pay_sum", "sm_vw_account_pay_sum.user_id" };

    /**
     * Field that contains the comma separated fields of the sm_vw_account_pay_sum table.
     */
    private static final String ALL_FIELDS = "sm_vw_account_pay_sum.pay_sum" + ",sm_vw_account_pay_sum.user_id";

    private static SmVwAccountPaySumManager singleton = new SmVwAccountPaySumManager();

    /**
     * Get the SmVwAccountPaySumManager singleton.
     *
     * @return SmVwAccountPaySumManager
     */
    public static synchronized SmVwAccountPaySumManager getInstance() {
        return singleton;
    }

    /**
     * Sets your own SmVwAccountPaySumManager instance.
     <br>
     * This is optional, by default we provide it for you.
     */
    public static synchronized void setInstance(SmVwAccountPaySumManager instance) {
        singleton = instance;
    }

    /**
     * Creates a new SmVwAccountPaySumBean instance.
     *
     * @return the new SmVwAccountPaySumBean
     */
    public SmVwAccountPaySumBean createSmVwAccountPaySumBean() {
        return new SmVwAccountPaySumBean();
    }

    public SmVwAccountPaySumBean[] loadAll() throws SQLException {
        return loadAll(1, -1);
    }

    public SmVwAccountPaySumBean[] loadAll(int startRow, int numRows) throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;
        try {
            c = getConnection();
            ps = c.prepareStatement("SELECT " + ALL_FIELDS + " FROM sm_vw_account_pay_sum", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            return loadByPreparedStatement(ps, null, startRow, numRows);
        } finally {
            getManager().close(ps);
            freeConnection(c);
        }
    }

    public SmVwAccountPaySumBean[] loadByWhere(String where) throws SQLException {
        return loadByWhere(where, null);
    }

    public SmVwAccountPaySumBean[] loadByWhere(String where, int[] fieldList) throws SQLException {
        return loadByWhere(where, null, 1, -1);
    }

    public SmVwAccountPaySumBean[] loadByWhere(String where, int[] fieldList, int startRow, int numRows) throws SQLException {
        String sql = null;
        if (fieldList == null) sql = "select " + ALL_FIELDS + " from sm_vw_account_pay_sum " + where; else {
            StringBuffer buff = new StringBuffer(128);
            buff.append("select ");
            for (int i = 0; i < fieldList.length; i++) {
                if (i != 0) buff.append(",");
                buff.append(FIELD_NAMES[fieldList[i]]);
            }
            buff.append(" from sm_vw_account_pay_sum ");
            buff.append(where);
            sql = buff.toString();
            buff = null;
        }
        Connection c = null;
        Statement pStatement = null;
        ResultSet rs = null;
        java.util.ArrayList v = null;
        try {
            c = getConnection();
            pStatement = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = pStatement.executeQuery(sql);
            v = new java.util.ArrayList();
            int count = 0;
            if (rs.absolute(startRow) && numRows != 0) {
                do {
                    if (fieldList == null) v.add(decodeRow(rs)); else v.add(decodeRow(rs, fieldList));
                    count++;
                } while ((count < numRows || numRows < 0) && rs.next());
            }
            return (SmVwAccountPaySumBean[]) v.toArray(new SmVwAccountPaySumBean[0]);
        } finally {
            if (v != null) {
                v.clear();
            }
            getManager().close(pStatement, rs);
            freeConnection(c);
        }
    }

    /**
     * Deletes all rows from sm_vw_account_pay_sum table.
     * @return the number of deleted rows.
     */
    public int deleteAll() throws SQLException {
        return deleteByWhere("");
    }

    /**
     * Deletes rows from the sm_vw_account_pay_sum table using a 'where' clause.
     * It is up to you to pass the 'WHERE' in your where clausis.
     * <br>Attention, if 'WHERE' is omitted it will delete all records.
     *
     * @param where the sql 'where' clause
     * @return the number of deleted rows
     */
    public int deleteByWhere(String where) throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;
        try {
            c = getConnection();
            String delByWhereSQL = "DELETE FROM sm_vw_account_pay_sum " + where;
            ps = c.prepareStatement(delByWhereSQL);
            return ps.executeUpdate();
        } finally {
            getManager().close(ps);
            freeConnection(c);
        }
    }

    public SmVwAccountPaySumBean save(SmVwAccountPaySumBean pObject) throws SQLException {
        if (!pObject.isModified()) {
            return pObject;
        }
        Connection c = null;
        PreparedStatement ps = null;
        StringBuffer _sql = null;
        try {
            c = getConnection();
            if (pObject.isNew()) {
                beforeInsert(pObject);
                int _dirtyCount = 0;
                _sql = new StringBuffer("INSERT into sm_vw_account_pay_sum (");
                if (pObject.isPaySumModified()) {
                    if (_dirtyCount > 0) {
                        _sql.append(",");
                    }
                    _sql.append("pay_sum");
                    _dirtyCount++;
                }
                if (pObject.isUserIdModified()) {
                    if (_dirtyCount > 0) {
                        _sql.append(",");
                    }
                    _sql.append("user_id");
                    _dirtyCount++;
                }
                _sql.append(") values (");
                if (_dirtyCount > 0) {
                    _sql.append("?");
                    for (int i = 1; i < _dirtyCount; i++) {
                        _sql.append(",?");
                    }
                }
                _sql.append(")");
                ps = c.prepareStatement(_sql.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                _dirtyCount = 0;
                if (pObject.isPaySumModified()) {
                    Manager.setLong(ps, ++_dirtyCount, pObject.getPaySum());
                }
                if (pObject.isUserIdModified()) {
                    Manager.setLong(ps, ++_dirtyCount, pObject.getUserId());
                }
                ps.executeUpdate();
                pObject.isNew(false);
                pObject.resetIsModified();
                afterInsert(pObject);
            } else {
                beforeUpdate(pObject);
                _sql = new StringBuffer("UPDATE sm_vw_account_pay_sum SET ");
                boolean useComma = false;
                if (pObject.isPaySumModified()) {
                    if (useComma) {
                        _sql.append(",");
                    } else {
                        useComma = true;
                    }
                    _sql.append("pay_sum").append("=?");
                }
                if (pObject.isUserIdModified()) {
                    if (useComma) {
                        _sql.append(",");
                    } else {
                        useComma = true;
                    }
                    _sql.append("user_id").append("=?");
                }
                _sql.append("");
                ps = c.prepareStatement(_sql.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                int _dirtyCount = 0;
                if (pObject.isPaySumModified()) {
                    Manager.setLong(ps, ++_dirtyCount, pObject.getPaySum());
                }
                if (pObject.isUserIdModified()) {
                    Manager.setLong(ps, ++_dirtyCount, pObject.getUserId());
                }
                if (_dirtyCount == 0) {
                    return pObject;
                }
                ps.executeUpdate();
                pObject.resetIsModified();
                afterUpdate(pObject);
            }
            return pObject;
        } finally {
            getManager().close(ps);
            freeConnection(c);
        }
    }

    public SmVwAccountPaySumBean[] save(SmVwAccountPaySumBean[] pObjects) throws SQLException {
        for (int iIndex = 0; iIndex < pObjects.length; iIndex++) {
            save(pObjects[iIndex]);
        }
        return pObjects;
    }

    public SmVwAccountPaySumBean loadUniqueUsingTemplate(SmVwAccountPaySumBean pObject) throws SQLException {
        SmVwAccountPaySumBean[] pReturn = loadUsingTemplate(pObject);
        if (pReturn.length == 0) return null;
        if (pReturn.length > 1) throw new SQLException("More than one element !!");
        return pReturn[0];
    }

    public SmVwAccountPaySumBean[] loadUsingTemplate(SmVwAccountPaySumBean pObject) throws SQLException {
        return loadUsingTemplate(pObject, 1, -1);
    }

    public SmVwAccountPaySumBean[] loadUsingTemplate(SmVwAccountPaySumBean pObject, int startRow, int numRows) throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;
        StringBuffer where = new StringBuffer("");
        StringBuffer _sql = new StringBuffer("SELECT " + ALL_FIELDS + " from sm_vw_account_pay_sum WHERE ");
        StringBuffer _sqlWhere = new StringBuffer("");
        try {
            int _dirtyCount = 0;
            if (pObject.isPaySumModified()) {
                _dirtyCount++;
                _sqlWhere.append((_sqlWhere.length() == 0) ? " " : " AND ").append("pay_sum= ?");
            }
            if (pObject.isUserIdModified()) {
                _dirtyCount++;
                _sqlWhere.append((_sqlWhere.length() == 0) ? " " : " AND ").append("user_id= ?");
            }
            if (_dirtyCount == 0) {
                throw new SQLException("The pObject to look for is invalid : not initialized !");
            }
            _sql.append(_sqlWhere);
            c = getConnection();
            ps = c.prepareStatement(_sql.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            _dirtyCount = 0;
            if (pObject.isPaySumModified()) {
                Manager.setLong(ps, ++_dirtyCount, pObject.getPaySum());
            }
            if (pObject.isUserIdModified()) {
                Manager.setLong(ps, ++_dirtyCount, pObject.getUserId());
            }
            ps.executeQuery();
            return loadByPreparedStatement(ps, null, startRow, numRows);
        } finally {
            getManager().close(ps);
            freeConnection(c);
        }
    }

    public int deleteUsingTemplate(SmVwAccountPaySumBean pObject) throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;
        StringBuffer sql = null;
        try {
            sql = new StringBuffer("DELETE FROM sm_vw_account_pay_sum WHERE ");
            int _dirtyAnd = 0;
            if (pObject.isPaySumInitialized()) {
                if (_dirtyAnd > 0) sql.append(" AND ");
                sql.append("pay_sum").append("=?");
                _dirtyAnd++;
            }
            if (pObject.isUserIdInitialized()) {
                if (_dirtyAnd > 0) sql.append(" AND ");
                sql.append("user_id").append("=?");
                _dirtyAnd++;
            }
            c = getConnection();
            ps = c.prepareStatement(sql.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            int _dirtyCount = 0;
            if (pObject.isPaySumInitialized()) {
                Manager.setLong(ps, ++_dirtyCount, pObject.getPaySum());
            }
            if (pObject.isUserIdInitialized()) {
                Manager.setLong(ps, ++_dirtyCount, pObject.getUserId());
            }
            int _rows = ps.executeUpdate();
            return _rows;
        } finally {
            getManager().close(ps);
            freeConnection(c);
        }
    }

    public int countAll() throws SQLException {
        return countWhere("");
    }

    /**
     * Retrieves the number of rows of the table sm_vw_account_pay_sum with a 'where' clause.
     * It is up to you to pass the 'WHERE' in your where clausis.
     *
     * @param where the restriction clause
     * @return the number of rows returned
     */
    public int countWhere(String where) throws SQLException {
        String sql = "select count(*) as MCOUNT from sm_vw_account_pay_sum " + where;
        Connection c = null;
        Statement pStatement = null;
        ResultSet rs = null;
        try {
            int iReturn = -1;
            c = getConnection();
            pStatement = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = pStatement.executeQuery(sql);
            if (rs.next()) {
                iReturn = rs.getInt("MCOUNT");
            }
            if (iReturn != -1) return iReturn;
        } finally {
            getManager().close(pStatement, rs);
            freeConnection(c);
        }
        throw new SQLException("Error in countWhere");
    }

    int countByPreparedStatement(PreparedStatement ps) throws SQLException {
        ResultSet rs = null;
        try {
            int iReturn = -1;
            rs = ps.executeQuery();
            if (rs.next()) iReturn = rs.getInt("MCOUNT");
            if (iReturn != -1) return iReturn;
        } finally {
            getManager().close(rs);
        }
        throw new SQLException("Error in countByPreparedStatement");
    }

    public int countUsingTemplate(SmVwAccountPaySumBean pObject) throws SQLException {
        StringBuffer where = new StringBuffer("");
        Connection c = null;
        PreparedStatement ps = null;
        StringBuffer _sql = null;
        StringBuffer _sqlWhere = null;
        try {
            _sql = new StringBuffer("SELECT count(*) as MCOUNT  from sm_vw_account_pay_sum WHERE ");
            _sqlWhere = new StringBuffer("");
            int _dirtyCount = 0;
            if (pObject.isPaySumModified()) {
                _dirtyCount++;
                _sqlWhere.append((_sqlWhere.length() == 0) ? " " : " AND ").append("pay_sum= ?");
            }
            if (pObject.isUserIdModified()) {
                _dirtyCount++;
                _sqlWhere.append((_sqlWhere.length() == 0) ? " " : " AND ").append("user_id= ?");
            }
            if (_dirtyCount == 0) throw new SQLException("The pObject to look is unvalid : not initialized !");
            _sql.append(_sqlWhere);
            c = getConnection();
            ps = c.prepareStatement(_sql.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            _dirtyCount = 0;
            if (pObject.isPaySumModified()) {
                Manager.setLong(ps, ++_dirtyCount, pObject.getPaySum());
            }
            if (pObject.isUserIdModified()) {
                Manager.setLong(ps, ++_dirtyCount, pObject.getUserId());
            }
            return countByPreparedStatement(ps);
        } finally {
            getManager().close(ps);
            freeConnection(c);
        }
    }

    public SmVwAccountPaySumBean decodeRow(ResultSet rs) throws SQLException {
        SmVwAccountPaySumBean pObject = createSmVwAccountPaySumBean();
        pObject.setPaySum(Manager.getLong(rs, 1));
        pObject.setUserId(Manager.getLong(rs, 2));
        pObject.isNew(false);
        pObject.resetIsModified();
        return pObject;
    }

    public SmVwAccountPaySumBean decodeRow(ResultSet rs, int[] fieldList) throws SQLException {
        SmVwAccountPaySumBean pObject = createSmVwAccountPaySumBean();
        int pos = 0;
        for (int i = 0; i < fieldList.length; i++) {
            switch(fieldList[i]) {
                case ID_PAY_SUM:
                    ++pos;
                    pObject.setPaySum(Manager.getLong(rs, pos));
                    break;
                case ID_USER_ID:
                    ++pos;
                    pObject.setUserId(Manager.getLong(rs, pos));
                    break;
            }
        }
        pObject.isNew(false);
        pObject.resetIsModified();
        return pObject;
    }

    public SmVwAccountPaySumBean[] loadByPreparedStatement(PreparedStatement ps) throws SQLException {
        return loadByPreparedStatement(ps, null);
    }

    /**
     * Loads all the elements using a prepared statement specifying a list of fields to be retrieved.
     *
     * @param ps the PreparedStatement to be used
     * @param fieldList table of the field's associated constants
     * @return an array of SmVwAccountPaySumBean
     */
    public SmVwAccountPaySumBean[] loadByPreparedStatement(PreparedStatement ps, int[] fieldList) throws SQLException {
        return loadByPreparedStatement(ps, fieldList, 1, -1);
    }

    public SmVwAccountPaySumBean[] loadByPreparedStatement(PreparedStatement ps, int[] fieldList, int startRow, int numRows) throws SQLException {
        ResultSet rs = null;
        java.util.ArrayList v = null;
        try {
            rs = ps.executeQuery();
            v = new java.util.ArrayList();
            int count = 0;
            if (rs.absolute(startRow) && numRows != 0) {
                do {
                    if (fieldList == null) v.add(decodeRow(rs)); else v.add(decodeRow(rs, fieldList));
                    count++;
                } while ((count < numRows || numRows < 0) && rs.next());
            }
            return (SmVwAccountPaySumBean[]) v.toArray(new SmVwAccountPaySumBean[0]);
        } finally {
            if (v != null) {
                v.clear();
                v = null;
            }
            getManager().close(rs);
        }
    }

    private SmVwAccountPaySumListener listener = null;

    public void registerListener(SmVwAccountPaySumListener listener) {
        this.listener = listener;
    }

    void beforeInsert(SmVwAccountPaySumBean pObject) throws SQLException {
        if (listener != null) listener.beforeInsert(pObject);
    }

    void afterInsert(SmVwAccountPaySumBean pObject) throws SQLException {
        if (listener != null) listener.afterInsert(pObject);
    }

    void beforeUpdate(SmVwAccountPaySumBean pObject) throws SQLException {
        if (listener != null) listener.beforeUpdate(pObject);
    }

    void afterUpdate(SmVwAccountPaySumBean pObject) throws SQLException {
        if (listener != null) listener.afterUpdate(pObject);
    }

    Manager getManager() {
        return Manager.getInstance();
    }

    /**
     * Frees the connection.
     *
     * @param c the connection to release
     */
    void freeConnection(Connection c) {
        getManager().releaseConnection(c);
    }

    /**
     * Gets the connection.
     */
    Connection getConnection() throws SQLException {
        return getManager().getConnection();
    }
}

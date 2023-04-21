package org.apache.harmony.sql.internal.rowset;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.RowSetInternal;
import javax.sql.RowSetWriter;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.spi.SyncProviderException;
import javax.sql.rowset.spi.SyncResolver;
import org.apache.harmony.sql.internal.nls.Messages;

public class CachedRowSetWriter implements RowSetWriter {

    private CachedRowSet originalRowSet;

    private CachedRowSetImpl currentRowSet;

    private Connection originalConnection;

    private String tableName;

    private String[] colNames;

    private int columnCount;

    private SyncResolverImpl resolver;

    private SyncProviderException syncException;

    public void setConnection(Connection conn) {
        originalConnection = conn;
    }

    public Connection getConnection() {
        return originalConnection;
    }

    public SyncProviderException getSyncException() {
        return syncException;
    }

    public boolean writeData(RowSetInternal theRowSet) throws SQLException {
        init(theRowSet);
        currentRowSet.beforeFirst();
        originalRowSet.beforeFirst();
        resolver = null;
        while (currentRowSet.next()) {
            if (currentRowSet.rowInserted()) {
                try {
                    insertCurrentRow();
                } catch (SyncProviderException e) {
                    addConflict(SyncResolver.INSERT_ROW_CONFLICT);
                }
            } else if (currentRowSet.rowDeleted()) {
                if (isConflictExistForCurrentRow()) {
                    addConflict(SyncResolver.DELETE_ROW_CONFLICT);
                } else {
                    deleteCurrentRow();
                }
            } else if (currentRowSet.rowUpdated()) {
                if (isConflictExistForCurrentRow()) {
                    addConflict(SyncResolver.UPDATE_ROW_CONFLICT);
                } else {
                    try {
                        updateCurrentRow();
                    } catch (SyncProviderException e) {
                        addConflict(SyncResolver.UPDATE_ROW_CONFLICT);
                    }
                }
            }
        }
        if (resolver != null) {
            syncException = new SyncProviderException(resolver);
            return false;
        }
        return true;
    }

    private void addConflict(int status) throws SQLException {
        if (resolver == null) {
            resolver = new SyncResolverImpl(currentRowSet.getMetaData());
        }
        resolver.addConflictRow(new CachedRow(new Object[columnCount]), currentRowSet.getRow(), status);
    }

    /**
     * Insert the RowSet's current row to DB
     * 
     * @throws SQLException
     */
    private void insertCurrentRow() throws SQLException {
        StringBuilder insertSQL = new StringBuilder("INSERT INTO ");
        insertSQL.append(tableName);
        insertSQL.append("(");
        StringBuilder insertColNames = new StringBuilder();
        StringBuilder insertPlaceholder = new StringBuilder();
        Object[] insertColValues = new Object[columnCount];
        int updateCount = 0;
        for (int i = 1; i <= columnCount; i++) {
            if (currentRowSet.columnUpdated(i)) {
                insertColNames.append(colNames[i - 1]);
                insertColNames.append(",");
                insertPlaceholder.append("?,");
                insertColValues[updateCount] = currentRowSet.getObject(i);
                updateCount++;
            }
        }
        if (updateCount == 0) {
            return;
        }
        insertSQL.append(subStringN(insertColNames.toString(), 1));
        insertSQL.append(") values (");
        insertSQL.append(subStringN(insertPlaceholder.toString(), 1));
        insertSQL.append(")");
        PreparedStatement preSt = getConnection().prepareStatement(insertSQL.toString());
        try {
            for (int i = 0; i < updateCount; i++) {
                preSt.setObject(i + 1, insertColValues[i]);
            }
            preSt.executeUpdate();
        } catch (SQLException e) {
            throw new SyncProviderException(Messages.getString("rowset.29"));
        } finally {
            try {
                preSt.close();
            } catch (SQLException e) {
            }
        }
    }

    /**
     * Delete the current row from DB
     * 
     * @throws SQLException
     */
    private void deleteCurrentRow() throws SQLException {
        StringBuilder deleteSQL = new StringBuilder("DELETE FROM ");
        deleteSQL.append(tableName);
        deleteSQL.append(" WHERE ");
        deleteSQL.append(generateQueryCondition());
        PreparedStatement preSt = getConnection().prepareStatement(deleteSQL.toString());
        try {
            fillParamInPreStatement(preSt, 1);
            preSt.executeUpdate();
        } finally {
            try {
                preSt.close();
            } catch (SQLException e) {
            }
        }
    }

    /**
     * Update the current row to DB
     * 
     * @throws SQLException
     */
    private void updateCurrentRow() throws SQLException {
        StringBuilder updateSQL = new StringBuilder("UPDATE ");
        updateSQL.append(tableName);
        updateSQL.append(" SET ");
        StringBuilder updateCols = new StringBuilder();
        Object[] updateColValues = new Object[columnCount];
        int[] updateColIndexs = new int[columnCount];
        int updateCount = 0;
        for (int i = 1; i <= columnCount; i++) {
            if (currentRowSet.columnUpdated(i)) {
                updateCols.append(colNames[i - 1]);
                updateCols.append(" = ?, ");
                updateColValues[updateCount] = currentRowSet.getObject(i);
                updateColIndexs[updateCount] = i;
                updateCount++;
            }
        }
        if (updateCount == 0) {
            return;
        }
        updateSQL.append(subStringN(updateCols.toString(), 2));
        updateSQL.append(" WHERE ");
        updateSQL.append(generateQueryCondition());
        PreparedStatement preSt = getConnection().prepareStatement(updateSQL.toString());
        try {
            for (int i = 0; i < updateCount; i++) {
                if (updateColValues[i] == null) {
                    preSt.setNull(i + 1, currentRowSet.getMetaData().getColumnType(updateColIndexs[i]));
                } else {
                    preSt.setObject(i + 1, updateColValues[i]);
                }
            }
            fillParamInPreStatement(preSt, updateCount + 1);
            preSt.executeUpdate();
        } catch (SQLException e) {
            throw new SyncProviderException(Messages.getString("rowset.5"));
        } finally {
            try {
                preSt.close();
            } catch (SQLException e) {
            }
        }
    }

    private void init(RowSetInternal theRowSet) throws SQLException {
        currentRowSet = (CachedRowSetImpl) theRowSet;
        originalRowSet = (CachedRowSet) currentRowSet.getOriginal();
        String schema = currentRowSet.getMetaData().getSchemaName(1);
        if (schema == null || schema.length() == 0) {
            tableName = currentRowSet.getTableName();
        } else {
            tableName = schema + "." + currentRowSet.getTableName();
        }
        columnCount = currentRowSet.getMetaData().getColumnCount();
        colNames = new String[columnCount];
        for (int i = 1; i <= columnCount; i++) {
            colNames[i - 1] = currentRowSet.getMetaData().getColumnName(i);
        }
    }

    /**
     * Compare the current row's data between database and CachedRowSet to check
     * whether it has been changed in database.
     * 
     * @return if conflict exists, return true; else, return false
     * @throws SQLException
     */
    private boolean isConflictExistForCurrentRow() throws SQLException {
        boolean isExist = true;
        originalRowSet.absolute(currentRowSet.getRow());
        StringBuilder querySQL = new StringBuilder("SELECT COUNT(*) FROM ");
        querySQL.append(tableName);
        querySQL.append(" WHERE ");
        querySQL.append(generateQueryCondition());
        PreparedStatement preSt = getConnection().prepareStatement(querySQL.toString());
        ResultSet queryRs = null;
        try {
            fillParamInPreStatement(preSt, 1);
            queryRs = preSt.executeQuery();
            if (queryRs.next()) {
                if (queryRs.getInt(1) == 1) {
                    isExist = false;
                }
            }
        } finally {
            try {
                if (queryRs != null) {
                    queryRs.close();
                }
                preSt.close();
            } catch (SQLException e) {
            }
        }
        return isExist;
    }

    /**
     * Generate the query condition after the keyword "WHERE" in SQL. Expression
     * likes as: COLUMN1 = ? AND COLUMN2 = ?
     * 
     * @return the SQL query expression
     */
    private String generateQueryCondition() throws SQLException {
        StringBuilder queryCondtion = new StringBuilder();
        for (int i = 0; i < colNames.length; i++) {
            if (originalRowSet.getObject(i + 1) == null) {
                queryCondtion.append(colNames[i]);
                queryCondtion.append(" is null ");
            } else {
                queryCondtion.append(colNames[i]);
                queryCondtion.append(" = ? ");
            }
            if (i != colNames.length - 1) {
                queryCondtion.append(" and ");
            }
        }
        return queryCondtion.toString();
    }

    /**
     * Fill all the parameters in PreparedStatement
     * 
     * @param preSt
     *            PreparedStatement
     * @param fromIndex
     *            It must be greater than 0
     * @throws SQLException
     */
    private void fillParamInPreStatement(PreparedStatement preSt, int fromIndex) throws SQLException {
        int notNullCount = fromIndex;
        for (int i = 1; i <= columnCount; i++) {
            if (originalRowSet.getObject(i) != null) {
                preSt.setObject(notNullCount, originalRowSet.getObject(i));
                notNullCount++;
            }
        }
    }

    private String subStringN(String input, int n) {
        return input.substring(0, input.length() - n);
    }
}

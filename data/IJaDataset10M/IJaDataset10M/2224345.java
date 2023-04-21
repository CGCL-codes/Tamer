package com.completex.objective.components.persistency.policy.impl;

import com.completex.objective.components.log.Log;
import com.completex.objective.components.persistency.MetaColumn;
import com.completex.objective.components.persistency.Persistency;
import com.completex.objective.components.persistency.Record;
import com.completex.objective.components.persistency.transact.Transaction;
import java.sql.SQLException;

/**
 * @author Gennady Krizhevsky
 */
public class MSSqlServerDatabasePolicyImpl extends AbstractDatabasePolicy {

    public static final String DB_MS_SQL_SERVER = "mssqlserver";

    public String getName() {
        return DB_MS_SQL_SERVER;
    }

    public boolean supportsSubqueries() {
        return true;
    }

    public boolean supportsAutoincrement() {
        return true;
    }

    public boolean supportsDirectBlobInsert() {
        return true;
    }

    public int getLimitLocation() {
        return LIMIT_IN_SELECT;
    }

    /**
     selectClause
     : SELECT (ALL | DISTINCT)? (TOP Integer (PERCENT)? (WITH TIES)? )? selectList
     */
    public String getLimitSql(int offset, int rowCount) {
        return " TOP " + rowCount + " ";
    }

    public String connectionActiveSql() {
        return "SELECT 1";
    }

    public String nowSql() {
        return " getdate() ";
    }

    public String getLockHint(boolean lock, String tableName) {
        if (lock) {
            return tableName + " WITH (updlock, rowlock)";
        } else {
            return tableName;
        }
    }

    public void handlePostInsert(Transaction transaction, Persistency persistency, Record record, Log logger) throws SQLException {
        populateLastInsertedId(transaction, "SELECT SCOPE_IDENTITY()", record, logger);
    }

    protected boolean isAutoIncrement(MetaColumn column) {
        return column.isAutoIncrement();
    }

    public String charLengthSql(String expression) {
        return " LEN(" + expression + ") ";
    }
}

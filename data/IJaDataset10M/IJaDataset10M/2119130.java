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
public class SybaseDatabasePolicyImpl extends AbstractDatabasePolicy {

    public static final String DB_SYBASE = "sybase";

    public String getName() {
        return DB_SYBASE;
    }

    public boolean supportsSubqueries() {
        return true;
    }

    public boolean supportsAutoincrement() {
        return true;
    }

    public boolean supportsClob() {
        return false;
    }

    public boolean supportsBlob() {
        return false;
    }

    public int getLimitLocation() {
        return LIMIT_IN_SELECT;
    }

    public String connectionActiveSql() {
        return "SELECT 1 FROM DUMMY";
    }

    public String nowSql() {
        return "SELECT getdate()";
    }

    public String getLockHint(boolean lock, String tableName) {
        if (lock) {
            return tableName + " holdlock ";
        } else {
            return tableName;
        }
    }

    public void handlePostInsert(Transaction transaction, Persistency persistency, Record record, Log logger) throws SQLException {
        populateLastInsertedId(transaction, "select @@identity", record, logger);
    }

    protected boolean isAutoIncrement(MetaColumn column) {
        return column.isAutoIncrement();
    }

    public String charLengthSql(String expression) {
        return " char_length(" + expression + ") ";
    }
}

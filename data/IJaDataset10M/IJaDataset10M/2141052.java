package dbtools.queries;

import dbtools.DBResultSet;
import dbtools.DBTransaction;
import dbtools.structures.DBCond;
import dbtools.structures.DBField;
import dbtools.structures.DBTable;

public class FromClause {

    private DBTable[] tables;

    private DBField[] fields;

    private DBTransaction transaction;

    public FromClause(DBTransaction transaction, DBTable[] tables, DBField[] fields) {
        this.tables = tables;
        this.fields = fields;
        this.transaction = transaction;
    }

    public WhereClause where(DBCond... conditions) {
        return new WhereClause(transaction, tables, fields, conditions);
    }

    public DBResultSet exec() {
        return transaction.execSelect(tables, fields, null, null);
    }
}

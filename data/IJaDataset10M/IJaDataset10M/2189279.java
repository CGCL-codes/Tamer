package com.healthmarketscience.sqlbuilder;

import java.io.IOException;
import com.healthmarketscience.common.util.AppendableExt;

/**
 * Base of a query which generates an INSERT statement.  Keeps track of the
 * table and column names.
 *
 * @author James Ahlborn
 */
abstract class BaseInsertQuery<ThisType extends BaseInsertQuery<ThisType>> extends Query<ThisType> {

    private SqlObject _table;

    protected SqlObjectList<SqlObject> _columns = SqlObjectList.create();

    /** @param tableStr name of the table into which to insert the values. */
    public BaseInsertQuery(SqlObject tableStr) {
        _table = tableStr;
    }

    @Override
    protected void collectSchemaObjects(ValidationContext vContext) {
        super.collectSchemaObjects(vContext);
        _table.collectSchemaObjects(vContext);
        _columns.collectSchemaObjects(vContext);
    }

    /**
   * Appends the prefix "INSERT INTO (&lt;columns&gt;)" to the given
   * AppendableExt.
   */
    protected void appendPrefixTo(AppendableExt app) throws IOException {
        app.append("INSERT INTO ").append(_table).append(" (").append(_columns).append(") ");
    }
}

package davidlauzon.activerecord.manager;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import android.util.Log;
import davidlauzon.activerecord.ActiveRecord;
import davidlauzon.activerecord.nodes.AliasableColumnIF;
import davidlauzon.activerecord.nodes.AliasableTableIF;
import davidlauzon.activerecord.nodes.Column;
import davidlauzon.activerecord.nodes.Count;
import davidlauzon.activerecord.nodes.Function;
import davidlauzon.activerecord.nodes.SelectStatement;
import davidlauzon.activerecord.nodes.SortableColumnIF;
import davidlauzon.activerecord.nodes.SqlStatement;
import davidlauzon.activerecord.nodes.WhereClause;
import davidlauzon.activerecord.nodes.field.Field;
import davidlauzon.activerecord.recordset.RecordSet;

public class SelectManager extends StatementManager {

    /*********************************************************************************************
     * VARIABLES
     *********************************************************************************************/
    private SelectStatement _statement;

    private List<AliasableTableIF> _selectTables;

    private List<AliasableTableIF> _joinTables;

    private AliasableColumnIF[] _selectColumns;

    private List<AliasableColumnIF> _groupColumns;

    private List<SortableColumnIF> _orderColumns;

    private boolean _count;

    private boolean _distinct;

    private WhereClause _whereClause;

    private WhereClause _havingClause;

    private String _whereCondition;

    private List<Object> _whereArgs;

    private int _limit;

    private long _offset;

    private Function _aggregateFunction;

    private Class<ActiveRecord> _recordClass;

    /*********************************************************************************************
     * PUBLIC METHODS
     *********************************************************************************************/
    public SelectManager(Class<ActiveRecord> klass) {
        super();
        _recordClass = klass;
    }

    public void reset() {
        super.reset();
        _count = false;
        _distinct = false;
        _selectColumns = null;
        _selectTables = new Vector<AliasableTableIF>();
        _groupColumns = new Vector<AliasableColumnIF>();
        _orderColumns = new Vector<SortableColumnIF>();
        _whereClause = new WhereClause(null, null);
        _whereCondition = null;
        _whereArgs = new Vector<Object>();
        _limit = -1;
        _offset = -1;
    }

    public SelectManager distinct() {
        _distinct = true;
        return this;
    }

    public SelectManager columns(AliasableColumnIF[] columns) {
        _selectColumns = columns;
        return this;
    }

    public SelectManager columns(List<AliasableColumnIF> columns) {
        _selectColumns = aliasableColumnsToArray(columns);
        return this;
    }

    public SelectManager from(AliasableTableIF table) {
        if (_selectTables.size() > 0) {
            _selectTables.remove(0);
        }
        addTable(table);
        setConnection(table.getConnection());
        setSqlSerializer(table.getConnection().getSerializer());
        return this;
    }

    public SelectManager addTable(AliasableTableIF table) {
        _selectTables.add(table);
        return this;
    }

    public SelectManager where(String condition) {
        _whereCondition = condition;
        return this;
    }

    public SelectManager _(Object unsafeValue) {
        _whereArgs.add(unsafeValue);
        return this;
    }

    public SelectManager group(AliasableColumnIF column) {
        _groupColumns.add(column);
        return this;
    }

    public SelectManager order(SortableColumnIF column) {
        _orderColumns.add(column);
        return this;
    }

    public SelectManager limit(int limit) {
        _limit = limit;
        return this;
    }

    public SelectManager offset(long offset) {
        _offset = offset;
        return this;
    }

    @Override
    protected SqlStatement buildStatement() {
        _whereClause = buildWhereClause(_whereCondition, _whereArgs);
        _statement = new SelectStatement();
        _statement.setSelectTables(tablesToArray(_selectTables));
        _statement.setCount(_count);
        _statement.setDistinct(_distinct);
        _statement.setAggregateFunction(_aggregateFunction);
        _statement.setSelectColumns(_selectColumns);
        _statement.setWhereClause(_whereClause);
        _statement.setGroupColumns(aliasableColumnsToArray(_groupColumns));
        _statement.setOrderColumns(sortableColumnsToArray(_orderColumns));
        _statement.setLimit(_limit);
        _statement.setOffset(_offset);
        return _statement;
    }

    public RecordSet query() throws SQLException {
        String sql = this.toSql();
        RecordSet set = getConnection().executeQuery(sql);
        set.setFieldList(getFieldListForRecordSet());
        set.setRecordClass(_recordClass);
        return set;
    }

    public long count() throws SQLException {
        _aggregateFunction = new Count();
        return getConnection().executeQueryForLong(this.toSql());
    }

    private List<Field> getFieldListForRecordSet() {
        List<Field> allFields = new Vector<Field>();
        Field[] fields;
        AliasableColumnIF column;
        AliasableColumnIF[] columns;
        int i;
        if (isStarColumn()) {
            List<AliasableTableIF> tables = getAllTablesInRecordSet();
            for (AliasableTableIF table : tables) {
                fields = table.fields();
                for (i = 0; i < fields.length; i++) {
                    allFields.add(fields[i]);
                }
            }
        } else {
            columns = _selectColumns;
            for (i = 0; i < columns.length; i++) {
                column = columns[i];
                if (column instanceof Field) allFields.add(((Field) column)); else if (column instanceof Column) allFields.add(((Column) column).source()); else throw new IllegalArgumentException("Unsupported class: " + column.getClass().getName());
            }
        }
        return allFields;
    }

    private boolean isStarColumn() {
        return (_selectColumns == null || _selectColumns.length == 0);
    }

    private List<AliasableTableIF> getAllTablesInRecordSet() {
        return _selectTables;
    }

    private AliasableTableIF[] tablesToArray(List<AliasableTableIF> tables) {
        return (AliasableTableIF[]) tables.toArray(new AliasableTableIF[tables.size()]);
    }

    private AliasableColumnIF[] aliasableColumnsToArray(List<AliasableColumnIF> columns) {
        return (AliasableColumnIF[]) columns.toArray(new AliasableColumnIF[columns.size()]);
    }

    private SortableColumnIF[] sortableColumnsToArray(List<SortableColumnIF> columns) {
        return (SortableColumnIF[]) columns.toArray(new SortableColumnIF[columns.size()]);
    }
}

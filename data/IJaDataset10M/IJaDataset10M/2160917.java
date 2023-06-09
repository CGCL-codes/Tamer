package org.hsqldb;

import org.hsqldb.HsqlNameManager.HsqlName;
import org.hsqldb.HsqlNameManager.SimpleName;
import org.hsqldb.error.Error;
import org.hsqldb.error.ErrorCode;
import org.hsqldb.lib.ArrayListIdentity;
import org.hsqldb.lib.HsqlList;
import org.hsqldb.lib.OrderedHashSet;
import org.hsqldb.lib.Set;
import org.hsqldb.store.ValuePool;
import org.hsqldb.types.Type;

/**
 * Implementation of column, variable, parameter, etc. access operations.
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 2.0.1
 * @since 1.9.0
 */
public class ExpressionColumn extends Expression {

    public static final ExpressionColumn[] emptyArray = new ExpressionColumn[] {};

    ColumnSchema column;

    String schema;

    String tableName;

    String columnName;

    RangeVariable rangeVariable;

    NumberSequence sequence;

    boolean isWritable;

    boolean isParam;

    boolean strictReference;

    /**
     * Creates a OpTypes.COLUMN expression
     */
    ExpressionColumn(String schema, String table, String column, boolean strictReference) {
        super(OpTypes.COLUMN);
        this.schema = schema;
        this.tableName = table;
        this.columnName = column;
        this.strictReference = strictReference;
    }

    ExpressionColumn(ColumnSchema column) {
        super(OpTypes.COLUMN);
        this.column = column;
        this.dataType = column.getDataType();
        columnName = column.getName().name;
    }

    ExpressionColumn(RangeVariable rangeVar, int index) {
        super(OpTypes.COLUMN);
        columnIndex = index;
        setAutoAttributesAsColumn(rangeVar, columnIndex);
    }

    /**
     * Creates a temporary OpTypes.COLUMN expression
     */
    ExpressionColumn(Expression e, int colIndex, int rangePosition) {
        super(OpTypes.SIMPLE_COLUMN);
        dataType = e.dataType;
        columnIndex = colIndex;
        alias = e.alias;
        this.rangePosition = rangePosition;
    }

    ExpressionColumn() {
        super(OpTypes.ASTERISK);
    }

    ExpressionColumn(int type) {
        super(type);
        if (type == OpTypes.DYNAMIC_PARAM) {
            isParam = true;
        }
    }

    ExpressionColumn(Expression[] nodes, String name) {
        super(OpTypes.COALESCE);
        this.nodes = nodes;
        this.columnName = name;
    }

    /**
     * Creates an OpCodes.ASTERISK expression
     */
    ExpressionColumn(String schema, String table) {
        super(OpTypes.MULTICOLUMN);
        this.schema = schema;
        tableName = table;
    }

    /**
     * Creates a OpTypes.SEQUENCE expression
     */
    ExpressionColumn(NumberSequence sequence, int opType) {
        super(opType);
        this.sequence = sequence;
        dataType = sequence.getDataType();
    }

    void setAutoAttributesAsColumn(RangeVariable range, int i) {
        columnIndex = i;
        column = range.getColumn(i);
        dataType = column.getDataType();
        columnName = range.getColumnAlias(i);
        tableName = range.getTableAlias();
        rangeVariable = range;
        rangeVariable.addColumn(columnIndex);
    }

    void setAttributesAsColumn(RangeVariable range, int i) {
        columnIndex = i;
        column = range.getColumn(i);
        dataType = column.getDataType();
        rangeVariable = range;
        if (range.rangeType == RangeVariable.TABLE_RANGE) {
            rangeVariable.addColumn(columnIndex);
        }
    }

    void setAttributesAsColumn(ColumnSchema column, boolean isWritable) {
        this.column = column;
        dataType = column.getDataType();
        this.isWritable = isWritable;
    }

    SimpleName getSimpleName() {
        if (alias != null) {
            return alias;
        }
        if (column != null) {
            return column.getName();
        }
        if (opType == OpTypes.COALESCE) {
            return nodes[LEFT].getSimpleName();
        }
        return null;
    }

    String getAlias() {
        if (alias != null) {
            return alias.name;
        }
        if (opType == OpTypes.COLUMN) {
            return columnName;
        }
        if (opType == OpTypes.COALESCE) {
            return columnName;
        }
        return "";
    }

    public String getBaseColumnName() {
        if (opType == OpTypes.COLUMN && rangeVariable != null) {
            return rangeVariable.getTable().getColumn(columnIndex).getName().name;
        }
        return null;
    }

    public HsqlName getBaseColumnHsqlName() {
        return column.getName();
    }

    void collectObjectNames(Set set) {
        switch(opType) {
            case OpTypes.SEQUENCE:
                HsqlName name = sequence.getName();
                set.add(name);
                return;
            case OpTypes.MULTICOLUMN:
            case OpTypes.DYNAMIC_PARAM:
            case OpTypes.ASTERISK:
            case OpTypes.SIMPLE_COLUMN:
            case OpTypes.COALESCE:
                break;
            case OpTypes.PARAMETER:
            case OpTypes.VARIABLE:
                break;
            case OpTypes.COLUMN:
                set.add(column.getName());
                if (column.getName().parent != null) {
                    set.add(column.getName().parent);
                }
                return;
        }
    }

    String getColumnName() {
        if (opType == OpTypes.COLUMN && column != null) {
            return column.getName().name;
        }
        return getAlias();
    }

    ColumnSchema getColumn() {
        return column;
    }

    String getSchemaName() {
        return schema;
    }

    RangeVariable getRangeVariable() {
        return rangeVariable;
    }

    public HsqlList resolveColumnReferences(Session session, RangeVariable[] rangeVarArray, int rangeCount, HsqlList unresolvedSet, boolean acceptsSequences) {
        switch(opType) {
            case OpTypes.SEQUENCE:
                if (!acceptsSequences) {
                    throw Error.error(ErrorCode.X_42598);
                }
                break;
            case OpTypes.MULTICOLUMN:
            case OpTypes.DYNAMIC_PARAM:
            case OpTypes.ASTERISK:
            case OpTypes.SIMPLE_COLUMN:
            case OpTypes.COALESCE:
                break;
            case OpTypes.COLUMN:
            case OpTypes.PARAMETER:
            case OpTypes.VARIABLE:
                {
                    boolean resolved = false;
                    boolean tableQualified = tableName != null;
                    if (rangeVariable != null) {
                        return unresolvedSet;
                    }
                    for (int i = 0; i < rangeCount; i++) {
                        RangeVariable rangeVar = rangeVarArray[i];
                        if (rangeVar == null) {
                            continue;
                        }
                        if (resolved) {
                            if (resolvesDuplicateColumnReference(rangeVar)) {
                                if (strictReference) {
                                    String message = getColumnName();
                                    if (alias != null) {
                                        StringBuffer sb = new StringBuffer(message);
                                        sb.append(' ').append(Tokens.T_AS).append(' ').append(alias.getStatementName());
                                        message = sb.toString();
                                    }
                                    throw Error.error(ErrorCode.X_42580, message);
                                }
                            }
                        } else {
                            if (resolveColumnReference(rangeVar)) {
                                if (tableQualified) {
                                    return unresolvedSet;
                                }
                                resolved = true;
                                continue;
                            }
                        }
                    }
                    if (resolved) {
                        return unresolvedSet;
                    }
                    if (session.database.sqlSyntaxOra) {
                        if (acceptsSequences && tableName != null) {
                            if (Tokens.T_CURRVAL.equals(columnName)) {
                                NumberSequence seq = session.database.schemaManager.getSequence(tableName, session.getSchemaName(schema), false);
                                if (seq != null) {
                                    opType = OpTypes.SEQUENCE_CURRENT;
                                    dataType = seq.getDataType();
                                    sequence = seq;
                                    schema = null;
                                    tableName = null;
                                    columnName = null;
                                }
                            } else if (Tokens.T_NEXTVAL.equals(columnName)) {
                                NumberSequence seq = session.database.schemaManager.getSequence(tableName, session.getSchemaName(schema), false);
                                if (seq != null) {
                                    opType = OpTypes.SEQUENCE;
                                    dataType = seq.getDataType();
                                    sequence = seq;
                                    schema = null;
                                    tableName = null;
                                    columnName = null;
                                }
                            }
                        }
                        if (tableName == null && Tokens.T_ROWNUM.equals(columnName)) {
                            opType = OpTypes.ROWNUM;
                            dataType = Type.SQL_INTEGER;
                            columnName = null;
                        }
                    }
                    if (unresolvedSet == null) {
                        unresolvedSet = new ArrayListIdentity();
                    }
                    unresolvedSet.add(this);
                }
        }
        return unresolvedSet;
    }

    private boolean resolveColumnReference(RangeVariable rangeVar) {
        if (tableName == null) {
            Expression e = rangeVar.getColumnExpression(columnName);
            if (e != null) {
                opType = e.opType;
                nodes = e.nodes;
                dataType = e.dataType;
                return true;
            }
            switch(rangeVar.rangeType) {
                case RangeVariable.PARAMETER_RANGE:
                case RangeVariable.VARIALBE_RANGE:
                    int colIndex = rangeVar.findColumn(columnName);
                    if (colIndex == -1) {
                        return false;
                    }
                    ColumnSchema column = rangeVar.getColumn(colIndex);
                    if (column.getParameterMode() == SchemaObject.ParameterModes.PARAM_OUT) {
                        return false;
                    } else {
                        opType = rangeVar.rangeType == RangeVariable.VARIALBE_RANGE ? OpTypes.VARIABLE : OpTypes.PARAMETER;
                        setAttributesAsColumn(rangeVar, colIndex);
                        return true;
                    }
            }
        }
        int colIndex = rangeVar.findColumn(this);
        if (colIndex == -1) {
            return false;
        }
        if (rangeVar.rangeType == RangeVariable.TRANSITION_RANGE) {
            opType = OpTypes.TRANSITION_VARIABLE;
        }
        setAttributesAsColumn(rangeVar, colIndex);
        return true;
    }

    boolean resolvesDuplicateColumnReference(RangeVariable rangeVar) {
        if (tableName == null) {
            Expression e = rangeVar.getColumnExpression(columnName);
            if (e != null) {
                return false;
            }
            switch(rangeVar.rangeType) {
                case RangeVariable.PARAMETER_RANGE:
                case RangeVariable.VARIALBE_RANGE:
                    int colIndex = rangeVar.findColumn(columnName);
                    if (colIndex == -1) {
                        return false;
                    }
                    ColumnSchema column = rangeVar.getColumn(colIndex);
                    if (column.getParameterMode() == SchemaObject.ParameterModes.PARAM_OUT) {
                        return false;
                    } else {
                        return true;
                    }
            }
        }
        int colIndex = rangeVar.findColumn(this);
        if (colIndex == -1) {
            return false;
        }
        return true;
    }

    public void resolveTypes(Session session, Expression parent) {
        switch(opType) {
            case OpTypes.DEFAULT:
                if (parent != null && parent.opType != OpTypes.ROW) {
                    throw Error.error(ErrorCode.X_42544);
                }
                break;
            case OpTypes.COALESCE:
                {
                    Type type = null;
                    for (int i = 0; i < nodes.length; i++) {
                        type = Type.getAggregateType(nodes[i].dataType, type);
                    }
                    dataType = type;
                    break;
                }
        }
    }

    public Object getValue(Session session) {
        switch(opType) {
            case OpTypes.DEFAULT:
                return null;
            case OpTypes.VARIABLE:
                {
                    return session.sessionContext.routineVariables[columnIndex];
                }
            case OpTypes.PARAMETER:
                {
                    return session.sessionContext.routineArguments[columnIndex];
                }
            case OpTypes.TRANSITION_VARIABLE:
                {
                    return session.sessionContext.triggerArguments[rangeVariable.rangePosition][columnIndex];
                }
            case OpTypes.COLUMN:
                {
                    Object value = session.sessionContext.rangeIterators[rangeVariable.rangePosition].getCurrent(columnIndex);
                    if (dataType != column.dataType) {
                        value = dataType.convertToType(session, value, column.dataType);
                    }
                    return value;
                }
            case OpTypes.SIMPLE_COLUMN:
                {
                    Object value = session.sessionContext.rangeIterators[rangePosition].getCurrent(columnIndex);
                    return value;
                }
            case OpTypes.COALESCE:
                {
                    Object value = null;
                    for (int i = 0; i < nodes.length; i++) {
                        value = nodes[i].getValue(session, dataType);
                        if (value != null) {
                            return value;
                        }
                    }
                    return value;
                }
            case OpTypes.DYNAMIC_PARAM:
                {
                    return session.sessionContext.dynamicArguments[parameterIndex];
                }
            case OpTypes.SEQUENCE:
                {
                    return session.sessionData.getSequenceValue(sequence);
                }
            case OpTypes.SEQUENCE_CURRENT:
                {
                    return session.sessionData.getSequenceCurrent(sequence);
                }
            case OpTypes.ROWNUM:
                {
                    return ValuePool.getInt(session.sessionContext.rownum);
                }
            case OpTypes.ASTERISK:
            case OpTypes.MULTICOLUMN:
            default:
                throw Error.runtimeError(ErrorCode.U_S0500, "ExpressionColumn");
        }
    }

    public String getSQL() {
        switch(opType) {
            case OpTypes.DEFAULT:
                return Tokens.T_DEFAULT;
            case OpTypes.DYNAMIC_PARAM:
                return Tokens.T_QUESTION;
            case OpTypes.ASTERISK:
                return "*";
            case OpTypes.COALESCE:
                return alias.getStatementName();
            case OpTypes.VARIABLE:
            case OpTypes.PARAMETER:
            case OpTypes.COLUMN:
                {
                    if (column == null) {
                        if (alias != null) {
                            return alias.getStatementName();
                        } else {
                            return columnName;
                        }
                    }
                    if (rangeVariable.tableAlias == null) {
                        return column.getName().getSchemaQualifiedStatementName();
                    } else {
                        StringBuffer sb = new StringBuffer();
                        sb.append(rangeVariable.tableAlias.getStatementName());
                        sb.append('.');
                        sb.append(column.getName().statementName);
                        return sb.toString();
                    }
                }
            case OpTypes.MULTICOLUMN:
                {
                    if (nodes.length == 0) {
                        return "*";
                    }
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < nodes.length; i++) {
                        Expression e = nodes[i];
                        if (i > 0) {
                            sb.append(',');
                        }
                        String s = e.getSQL();
                        sb.append(s);
                    }
                    return sb.toString();
                }
            default:
                throw Error.runtimeError(ErrorCode.U_S0500, "ExpressionColumn");
        }
    }

    protected String describe(Session session, int blanks) {
        StringBuffer sb = new StringBuffer(64);
        sb.append('\n');
        for (int i = 0; i < blanks; i++) {
            sb.append(' ');
        }
        switch(opType) {
            case OpTypes.DEFAULT:
                sb.append(Tokens.T_DEFAULT);
                break;
            case OpTypes.ASTERISK:
                sb.append("OpTypes.ASTERISK ");
                break;
            case OpTypes.VARIABLE:
                sb.append("VARIABLE: ");
                sb.append(column.getName().name);
                break;
            case OpTypes.PARAMETER:
                sb.append(Tokens.T_PARAMETER).append(": ");
                sb.append(column.getName().name);
                break;
            case OpTypes.COALESCE:
                sb.append(Tokens.T_COLUMN).append(": ");
                sb.append(columnName);
                if (alias != null) {
                    sb.append(" AS ").append(alias.name);
                }
                break;
            case OpTypes.COLUMN:
                sb.append(Tokens.T_COLUMN).append(": ");
                sb.append(column.getName().getSchemaQualifiedStatementName());
                if (alias != null) {
                    sb.append(" AS ").append(alias.name);
                }
                break;
            case OpTypes.DYNAMIC_PARAM:
                sb.append("DYNAMIC PARAM: ");
                sb.append(", TYPE = ").append(dataType.getNameString());
                break;
            case OpTypes.SEQUENCE:
                sb.append(Tokens.T_SEQUENCE).append(": ");
                sb.append(sequence.getName().name);
                break;
            case OpTypes.MULTICOLUMN:
        }
        return sb.toString();
    }

    /**
     * Returns the table name used in query
     *
     * @return table name
     */
    String getTableName() {
        if (opType == OpTypes.MULTICOLUMN) {
            return tableName;
        }
        if (opType == OpTypes.COLUMN) {
            if (rangeVariable == null) {
                return tableName;
            } else {
                return rangeVariable.getTable().getName().name;
            }
        }
        return "";
    }

    static void checkColumnsResolved(HsqlList set) {
        if (set != null && !set.isEmpty()) {
            StringBuffer sb = new StringBuffer();
            Expression e = (Expression) set.get(0);
            if (e instanceof ExpressionColumn) {
                ExpressionColumn c = (ExpressionColumn) e;
                if (c.schema != null) {
                    sb.append(c.schema + '.');
                }
                if (c.tableName != null) {
                    sb.append(c.tableName + '.');
                }
                throw Error.error(ErrorCode.X_42501, sb.toString() + c.getColumnName());
            } else {
                OrderedHashSet newSet = new OrderedHashSet();
                e.collectAllExpressions(newSet, Expression.columnExpressionSet, Expression.emptyExpressionSet);
                checkColumnsResolved(newSet);
                throw Error.error(ErrorCode.X_42501);
            }
        }
    }

    public OrderedHashSet getUnkeyedColumns(OrderedHashSet unresolvedSet) {
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] == null) {
                continue;
            }
            unresolvedSet = nodes[i].getUnkeyedColumns(unresolvedSet);
        }
        if (opType == OpTypes.COLUMN && !rangeVariable.hasKeyedColumnInGroupBy) {
            if (unresolvedSet == null) {
                unresolvedSet = new OrderedHashSet();
            }
            unresolvedSet.add(this);
        }
        return unresolvedSet;
    }

    /**
     * collects all range variables in expression tree
     */
    void collectRangeVariables(RangeVariable[] rangeVariables, Set set) {
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] != null) {
                nodes[i].collectRangeVariables(rangeVariables, set);
            }
        }
        if (rangeVariable != null) {
            for (int i = 0; i < rangeVariables.length; i++) {
                if (rangeVariables[i] == rangeVariable) {
                    set.add(rangeVariable);
                    break;
                }
            }
        }
    }

    Expression replaceAliasInOrderBy(Expression[] columns, int length) {
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] == null) {
                continue;
            }
            nodes[i] = nodes[i].replaceAliasInOrderBy(columns, length);
        }
        switch(opType) {
            case OpTypes.COALESCE:
            case OpTypes.COLUMN:
                {
                    for (int i = 0; i < length; i++) {
                        SimpleName aliasName = columns[i].alias;
                        String alias = aliasName == null ? null : aliasName.name;
                        if (schema == null && tableName == null && columnName.equals(alias)) {
                            return columns[i];
                        }
                    }
                    for (int i = 0; i < length; i++) {
                        if (columns[i] instanceof ExpressionColumn) {
                            if (this.equals(columns[i])) {
                                return columns[i];
                            }
                            if (tableName == null && schema == null && columnName.equals(((ExpressionColumn) columns[i]).columnName)) {
                                return columns[i];
                            }
                        }
                    }
                }
            default:
        }
        return this;
    }

    Expression replaceColumnReferences(RangeVariable range, Expression[] list) {
        if (opType == OpTypes.COLUMN && rangeVariable == range) {
            return list[columnIndex];
        }
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] == null) {
                continue;
            }
            nodes[i] = nodes[i].replaceColumnReferences(range, list);
        }
        return this;
    }

    int findMatchingRangeVariableIndex(RangeVariable[] rangeVarArray) {
        for (int i = 0; i < rangeVarArray.length; i++) {
            RangeVariable rangeVar = rangeVarArray[i];
            if (rangeVar.resolvesTableName(this)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * return true if given RangeVariable is used in expression tree
     */
    boolean hasReference(RangeVariable range) {
        if (range == rangeVariable) {
            return true;
        }
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] != null) {
                if (nodes[i].hasReference(range)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * SIMPLE_COLUMN expressions can be of different Java types
     */
    public boolean equals(Expression other) {
        if (other == this) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (opType != other.opType) {
            return false;
        }
        switch(opType) {
            case OpTypes.SIMPLE_COLUMN:
                return this.columnIndex == other.columnIndex;
            case OpTypes.COALESCE:
                return nodes == other.nodes;
            case OpTypes.VARIABLE:
            case OpTypes.PARAMETER:
            case OpTypes.COLUMN:
                return column == other.getColumn() && rangeVariable == other.getRangeVariable();
            default:
                return false;
        }
    }

    void replaceRangeVariables(RangeVariable[] ranges, RangeVariable[] newRanges) {
        for (int i = 0; i < nodes.length; i++) {
            nodes[i].replaceRangeVariables(ranges, newRanges);
        }
        for (int i = 0; i < ranges.length; i++) {
            if (rangeVariable == ranges[i]) {
                rangeVariable = newRanges[i];
                break;
            }
        }
    }

    void resetColumnReferences() {
        rangeVariable = null;
        columnIndex = -1;
    }

    public boolean isIndexable(RangeVariable range) {
        if (opType == OpTypes.COLUMN) {
            return rangeVariable == range;
        }
        return false;
    }

    public boolean isUnresolvedParam() {
        return isParam && dataType == null;
    }

    boolean isDynamicParam() {
        return isParam;
    }
}

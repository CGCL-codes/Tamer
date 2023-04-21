package org.hsqldb;

import org.hsqldb.error.Error;
import org.hsqldb.error.ErrorCode;
import org.hsqldb.index.Index;
import org.hsqldb.lib.ArrayUtil;
import org.hsqldb.lib.HsqlArrayList;
import org.hsqldb.types.Type;

public final class SortAndSlice {

    static final SortAndSlice noSort = new SortAndSlice();

    public int[] sortOrder;

    public boolean[] sortDescending;

    public boolean[] sortNullsLast;

    boolean sortUnion;

    HsqlArrayList exprList = new HsqlArrayList();

    Expression limitCondition;

    int columnCount;

    boolean hasNullsLast;

    public boolean skipSort = false;

    public boolean skipFullResult = false;

    int[] columnIndexes;

    public Index index;

    public boolean isGenerated;

    SortAndSlice() {
    }

    public HsqlArrayList getExpressionList() {
        return exprList;
    }

    public boolean hasOrder() {
        return exprList.size() != 0;
    }

    public boolean hasLimit() {
        return limitCondition != null;
    }

    public int getOrderLength() {
        return exprList.size();
    }

    public void addOrderExpression(Expression e) {
        exprList.add(e);
    }

    public void addLimitCondition(Expression expression) {
        limitCondition = expression;
    }

    public void prepareSingleColumn(int colIndex) {
        sortOrder = new int[1];
        sortDescending = new boolean[1];
        sortNullsLast = new boolean[1];
        sortOrder[0] = colIndex;
    }

    public void prepare(int degree) {
        columnCount = exprList.size();
        if (columnCount == 0) {
            return;
        }
        sortOrder = new int[columnCount + degree];
        sortDescending = new boolean[columnCount + degree];
        sortNullsLast = new boolean[columnCount + degree];
        ArrayUtil.fillSequence(sortOrder);
        for (int i = 0; i < columnCount; i++) {
            ExpressionOrderBy sort = (ExpressionOrderBy) exprList.get(i);
            sortDescending[i] = sort.isDescending();
            sortNullsLast[i] = sort.isNullsLast();
            hasNullsLast |= sortNullsLast[i];
        }
    }

    public void prepare(QuerySpecification select) {
        columnCount = exprList.size();
        if (columnCount == 0) {
            return;
        }
        sortOrder = new int[columnCount];
        sortDescending = new boolean[columnCount];
        sortNullsLast = new boolean[columnCount];
        for (int i = 0; i < columnCount; i++) {
            ExpressionOrderBy sort = (ExpressionOrderBy) exprList.get(i);
            if (sort.getLeftNode().queryTableColumnIndex == -1) {
                sortOrder[i] = select.indexStartOrderBy + i;
            } else {
                sortOrder[i] = sort.getLeftNode().queryTableColumnIndex;
            }
            sortDescending[i] = sort.isDescending();
            sortNullsLast[i] = sort.isNullsLast();
            hasNullsLast |= sortNullsLast[i];
        }
        if (select == null || hasNullsLast) {
            return;
        }
        if (select.isDistinctSelect || select.isGrouped || select.isAggregated) {
            return;
        }
        int[] colIndexes = new int[columnCount];
        for (int i = 0; i < columnCount; i++) {
            Expression e = ((Expression) exprList.get(i)).getLeftNode();
            if (e.getType() != OpTypes.COLUMN) {
                return;
            }
            if (((ExpressionColumn) e).getRangeVariable() != select.rangeVariables[0]) {
                return;
            }
            colIndexes[i] = e.columnIndex;
        }
        this.columnIndexes = colIndexes;
    }

    void setSortRange(QuerySpecification select) {
        if (isGenerated) {
            return;
        }
        if (columnCount == 0) {
            if (limitCondition == null) {
                return;
            }
            if (select.isDistinctSelect || select.isGrouped || select.isAggregated) {
                return;
            }
            skipFullResult = true;
            return;
        }
        for (int i = 0; i < columnCount; i++) {
            ExpressionOrderBy sort = (ExpressionOrderBy) exprList.get(i);
            Type dataType = sort.getLeftNode().getDataType();
            if (dataType.isArrayType() || dataType.isLobType()) {
                throw Error.error(ErrorCode.X_42534);
            }
        }
        if (columnIndexes == null) {
            return;
        }
        int[] colIndexes;
        Index rangeIndex = select.rangeVariables[0].getSortIndex();
        if (rangeIndex == null) {
            return;
        }
        colIndexes = rangeIndex.getColumns();
        int count = ArrayUtil.countTrueElements(sortDescending);
        boolean allDescending = count == columnCount;
        if (!allDescending && count > 0) {
            return;
        }
        if (!select.rangeVariables[0].hasIndexCondition()) {
            Table table = select.rangeVariables[0].getTable();
            Index index = table.getFullIndexForColumns(columnIndexes);
            if (index != null) {
                if (select.rangeVariables[0].setSortIndex(index, allDescending)) {
                    skipSort = true;
                    skipFullResult = true;
                }
            }
        } else if (ArrayUtil.haveEqualArrays(columnIndexes, colIndexes, columnIndexes.length)) {
            if (allDescending) {
                boolean reversed = select.rangeVariables[0].reverseOrder();
                if (!reversed) {
                    return;
                }
            }
            skipSort = true;
            skipFullResult = true;
        }
    }

    public boolean prepareSpecial(Session session, QuerySpecification select) {
        Expression e = select.exprColumns[select.indexStartAggregates];
        int opType = e.getType();
        e = e.getLeftNode();
        if (e.getType() != OpTypes.COLUMN) {
            return false;
        }
        if (((ExpressionColumn) e).getRangeVariable() != select.rangeVariables[0]) {
            return false;
        }
        Index rangeIndex = select.rangeVariables[0].getSortIndex();
        if (rangeIndex == null) {
            return false;
        }
        int[] colIndexes = rangeIndex.getColumns();
        if (select.rangeVariables[0].hasIndexCondition()) {
            if (colIndexes[0] != ((ExpressionColumn) e).getColumnIndex()) {
                return false;
            }
            if (opType == OpTypes.MAX) {
                select.rangeVariables[0].reverseOrder();
            }
        } else {
            Table table = select.rangeVariables[0].getTable();
            Index index = table.getIndexForColumn(session, ((ExpressionColumn) e).getColumnIndex());
            if (index == null) {
                return false;
            }
            if (!select.rangeVariables[0].setSortIndex(index, opType == OpTypes.MAX)) {
                return false;
            }
        }
        columnCount = 1;
        sortOrder = new int[columnCount];
        sortDescending = new boolean[columnCount];
        sortNullsLast = new boolean[columnCount];
        columnIndexes = new int[columnCount];
        columnIndexes[0] = e.columnIndex;
        skipSort = true;
        skipFullResult = true;
        return true;
    }

    public int getLimitStart(Session session) {
        if (limitCondition != null) {
            Integer limit = (Integer) limitCondition.getLeftNode().getValue(session);
            if (limit != null) {
                return limit.intValue();
            }
        }
        return 0;
    }

    public int getLimitCount(Session session, int rowCount) {
        int limitCount = 0;
        if (limitCondition != null) {
            Integer limit = (Integer) limitCondition.getRightNode().getValue(session);
            if (limit != null) {
                limitCount = limit.intValue();
            }
        }
        if (rowCount != 0 && (limitCount == 0 || rowCount < limitCount)) {
            limitCount = rowCount;
        }
        return limitCount;
    }

    public void setIndex(Session session, TableBase table) {
        try {
            index = table.createAndAddIndexStructure(session, null, sortOrder, sortDescending, sortNullsLast, false, false, false);
        } catch (Throwable t) {
            throw Error.runtimeError(ErrorCode.U_S0500, "SortAndSlice");
        }
    }
}

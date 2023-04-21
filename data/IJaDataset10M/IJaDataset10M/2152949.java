package com.objectsql.statement.query;

import com.objectsql.statement.column.SelectColumn;
import com.objectsql.statement.groupby.GroupBy;
import com.objectsql.statement.join.JoinTable;
import com.objectsql.statement.order.OrderBy;
import com.objectsql.statement.query.filter.Where;
import com.objectsql.statement.update.AbstractEmptyParametersStatementFragement;
import com.sun.org.apache.xml.internal.utils.ObjectPool;
import static com.objectsql.utility.SqlUtil.*;
import java.util.*;

public abstract class AbstractQueryStructure extends AbstractQuery {

    private Where where;

    private OrderBy orderBy;

    private GroupBy groupBy;

    private List<JoinTable> joinDefinitions = new ArrayList<JoinTable>();

    private List<SelectColumn> selectColumns = new ArrayList<SelectColumn>();

    private String tableAlias;

    private boolean distinct = false;

    protected abstract List<SelectColumn> getAllClassColumns();

    protected abstract String getTableName();

    public Query addSelectColumn(SelectColumn... selectColumns) {
        this.selectColumns.addAll(Arrays.asList(selectColumns));
        for (SelectColumn selectColumn : selectColumns) {
            ((AbstractEmptyParametersStatementFragement) selectColumn).setOwnerQuery(this);
        }
        return this;
    }

    protected String getQuery() {
        List<SelectColumn> columnsToSelect = defineSelectColumns();
        procesarAliasTablas(columnsToSelect);
        StringBuffer queryStr = new StringBuffer();
        queryStr.append(getSqlWriter().getSelectSyntax() + " " + ((distinct == true ? getSqlWriter().getDistinctSyntax() + " " : "")) + getSelectColumnStringWithAlias(columnsToSelect.toArray(new SelectColumn[columnsToSelect.size()])) + " " + getSqlWriter().getFromSyntax() + " " + getTableWithAlias(getTableName(), tableAlias));
        for (JoinTable joinDefinition : this.joinDefinitions) {
            queryStr.append(" " + joinDefinition.sql());
        }
        return queryStr.toString();
    }

    private void procesarAliasTablas(List<SelectColumn> columnsSelected) {
        Set<String> tableAliasUsed = new HashSet<String>();
        if (this.tableAlias == null) {
            tableAlias = getTableAlias(getTableName());
        }
        tableAliasUsed.add(tableAlias);
        for (SelectColumn selectColumn : columnsSelected) {
            selectColumn.setTableAlias(this.tableAlias);
        }
        for (JoinTable joinTable : this.joinDefinitions) {
            String joinTableAlias = null;
            if (joinTable.getJoinTableAlias() == null) {
                joinTableAlias = getTableAlias(joinTable.getJoinTable(), tableAliasUsed);
            } else {
                joinTableAlias = joinTable.getJoinTableAlias();
            }
            tableAliasUsed.add(joinTableAlias);
            joinTable.setJoinTableAlias(joinTableAlias);
            if (!joinTable.hasJoinedDefinitionAsociated()) {
                joinTable.setJoinedTableAlias(tableAlias);
            }
        }
        Where where = getWhere();
        if (where != null) {
            where.setTableAlias(tableAlias);
            where.setOwnerQuery(this);
        }
    }

    public String sql() {
        StringBuffer finalQuery = new StringBuffer(getQuery());
        addQueryFragment(getWhere(), finalQuery);
        addQueryFragment(groupBy, finalQuery);
        addQueryFragment(orderBy, finalQuery);
        return finalQuery.toString();
    }

    protected List<SelectColumn> defineSelectColumns() {
        List<SelectColumn> selectedColumns = new ArrayList<SelectColumn>();
        if (this.selectColumns.isEmpty()) {
            selectedColumns.addAll(getAllClassColumns());
        } else {
            selectedColumns.addAll(this.selectColumns);
        }
        for (JoinTable joinTable : this.joinDefinitions) {
            selectedColumns.addAll(Arrays.asList(joinTable.getSelectColumn()));
        }
        return selectedColumns;
    }

    public void setOrderBy(OrderBy orderBy) {
        this.orderBy = orderBy;
    }

    public void setGroupBy(GroupBy groupBy) {
        this.groupBy = groupBy;
    }

    public void addJoinTable(JoinTable joinDefinition) {
        this.joinDefinitions.add(joinDefinition);
    }

    public Where getWhere() {
        return where;
    }

    public void setWhere(Where where) {
        this.where = where;
    }

    public Object[] parameterValues() {
        List<Object> parameters = new ArrayList<Object>();
        for (SelectColumn selectColumn : this.selectColumns) {
            parameters.addAll(Arrays.asList(selectColumn.parameterValues()));
        }
        parameters.addAll(getWhere() != null ? Arrays.asList(getWhere().parameterValues()) : Collections.emptyList());
        parameters.addAll(getGroupBy() != null ? Arrays.asList(getGroupBy().parameterValues()) : Collections.emptyList());
        return parameters.toArray();
    }

    protected GroupBy getGroupBy() {
        return groupBy;
    }

    public void setPrimaryTableAlias(String alias) {
        this.tableAlias = alias;
    }

    public void useDistinct() {
        this.distinct = true;
    }

    public void unuseDistinct() {
        this.distinct = false;
    }
}

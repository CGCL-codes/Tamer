package org.basegen.base.persistence.query;

import java.util.List;

/**
 * Subquery Expression
 */
public class SubqueryExpression implements Expression {

    /**
     * Holds left value property
     */
    private String lValue;

    /**
     * Holds operation property
     */
    private String operation;

    /**
     * Holds subquery column property
     */
    private String subqueryColumn;

    /**
     * Holds subquery class property
     */
    private Class subqueryClass;

    /**
     * Holds filter property
     */
    private Filter filter;

    /**
     * @return Returns the filter.
     */
    public Filter getFilter() {
        return filter;
    }

    /**
     * @param newFilter The filter to set.
     */
    public void setFilter(Filter newFilter) {
        this.filter = newFilter;
    }

    /**
     * @return Returns the lValue.
     */
    public String getLValue() {
        return lValue;
    }

    /**
     * @param value The lValue to set.
     */
    public void setLValue(String value) {
        lValue = value;
    }

    /**
     * @return Returns the operation.
     */
    public String getOperation() {
        return operation;
    }

    /**
     * @param newOperation The operation to set.
     */
    public void setOperation(String newOperation) {
        this.operation = newOperation;
    }

    /**
     * @return Returns the subqueryClass.
     */
    public Class getSubqueryClass() {
        return subqueryClass;
    }

    /**
     * @param newSubqueryClass The subqueryClass to set.
     */
    public void setSubqueryClass(Class newSubqueryClass) {
        this.subqueryClass = newSubqueryClass;
    }

    /**
     * @return Returns the subqueryColumn.
     */
    public String getSubqueryColumn() {
        return subqueryColumn;
    }

    /**
     * @param newSubqueryColumn The subqueryColumn to set.
     */
    public void setSubqueryColumn(String newSubqueryColumn) {
        this.subqueryColumn = newSubqueryColumn;
    }

    /**
     * Mant�m o valor da propriedade groupBy.
     */
    private GroupBy groupBy;

    /**
     * "Getter" para a propriedade groupBy.
     * 
     * @return Valor para a propriedade groupBy.
     */
    public GroupBy getGroupBy() {
        return this.groupBy;
    }

    /**
     * "Setter" para a propriedade groupBy.
     * 
     * @param newGroupBy Novo valor para a propriedade groupBy.
     */
    public void setGroupBy(GroupBy newGroupBy) {
        this.groupBy = newGroupBy;
    }

    /**
     * Default constructor
     */
    public SubqueryExpression() {
        this(null, null, null, null, null, null);
    }

    /**
     * Constructor with several parameters
     * @param newLValue new left value
     * @param newOperator new operator
     * @param newSubqueryColumn new subquery column
     * @param newSubqueryClass new subquery class
     * @param newFilter new filter 
     * @param newGroupBy new group by
     */
    public SubqueryExpression(String newLValue, String newOperator, String newSubqueryColumn, Class newSubqueryClass, Filter newFilter, GroupBy newGroupBy) {
        this.lValue = newLValue;
        this.operation = newOperator;
        this.subqueryColumn = newSubqueryColumn;
        this.subqueryClass = newSubqueryClass;
        this.filter = newFilter;
        this.groupBy = newGroupBy;
    }

    /**
     * To hql function
     * @return string
     */
    public String toHql() {
        String whereStr = "";
        if (!getFilter().toHql().trim().equals("")) {
            whereStr = " WHERE ";
        }
        String alias = Character.toLowerCase(getSubqueryClass().getSimpleName().charAt(0)) + getSubqueryClass().getSimpleName().substring(1);
        String groupByStr = "";
        if (getGroupBy() != null) {
            groupByStr = getGroupBy().toHql(alias);
        }
        return "( " + getLValue() + " " + getOperation() + " ( SELECT " + getSubqueryColumn() + " FROM " + getSubqueryClass().getName() + " " + alias + " " + whereStr + " " + getFilter().toHql() + " " + groupByStr + ") )";
    }

    /**
     * Returns values
     * @param values values
     * @return list
     */
    public List getValues(List values) {
        List result = values;
        if (getFilter().getExpression() != null) {
            result = getFilter().getExpression().getValues(values);
        }
        return result;
    }
}

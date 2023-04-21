package com.objectsql.statement.query.filter;

import com.objectsql.statement.column.SelectColumn;

/**
 * Objecto que representa un filtro 'in' de una query de la base de datos.
 *
 * @author plagreca
 */
public class FilterIn extends AbstractFilterByValues {

    public FilterIn(SelectColumn column, Object... values) {
        super(column, values);
    }

    protected String getOperator() {
        return getSqlWriter().getInOperator();
    }
}

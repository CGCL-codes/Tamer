package com.objectsql.statement.query;

import com.objectsql.statement.query.filter.Where;
import java.util.Collections;
import java.util.List;

public class QueryFree extends AbstractQueryString implements Query {

    private List<Object> parameters = Collections.EMPTY_LIST;

    private String query;

    public QueryFree(String query, List<Object> parameters) {
        this.query = query;
        this.parameters = parameters;
    }

    public QueryFree(String query) {
        this.query = query;
    }

    @Override
    protected String getQuery() {
        return query;
    }

    public Where getWhere() {
        return null;
    }

    public Object[] parameterValues() {
        return parameters.toArray();
    }
}

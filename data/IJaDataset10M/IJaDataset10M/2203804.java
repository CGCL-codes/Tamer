package com.toremo.frontline.services;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class QueryBuilder {

    private final StringBuffer buffer = new StringBuffer();

    private final List<Object> parameters = new ArrayList<Object>();

    private boolean thereAreFilters;

    public QueryBuilder filter(String expression, Object... params) {
        if (thereAreFilters) {
            buffer.append(" and ");
        } else {
            buffer.append(" where ");
            thereAreFilters = true;
        }
        buffer.append(expression);
        for (Object param : params) {
            parameters.add(param);
        }
        return this;
    }

    public QueryBuilder add(String expression) {
        buffer.append(expression);
        return this;
    }

    public WhenPredicate when(boolean condition) {
        return new WhenPredicate(this, condition);
    }

    public Query build(EntityManager em) {
        Query q = em.createQuery(buffer.toString());
        int i = 1;
        for (Object param : parameters) {
            q.setParameter(i++, param);
        }
        return q;
    }

    public static class WhenPredicate {

        private QueryBuilder builder;

        private boolean condition;

        public WhenPredicate(QueryBuilder builder, boolean condition) {
            this.builder = builder;
            this.condition = condition;
        }

        public QueryBuilder filter(String expression, Object... params) {
            if (condition) {
                builder.filter(expression, params);
            }
            return builder;
        }
    }
}

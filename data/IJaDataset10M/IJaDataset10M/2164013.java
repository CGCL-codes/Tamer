package com.miranteinfo.seam.hibernate.criterion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Query;
import org.hibernate.Session;

public class HQL {

    private Session session;

    private List<String> select;

    private List<String> from;

    private List<String> where;

    private Map<String, Object> params;

    private List<String> group;

    private List<String> order;

    private Integer firstResult;

    private Integer maxResults;

    public HQL(Session session) {
        this.session = session;
        this.select = new ArrayList<String>();
        this.from = new ArrayList<String>();
        this.where = new ArrayList<String>();
        this.params = new HashMap<String, Object>();
        this.group = new ArrayList<String>();
        this.order = new ArrayList<String>();
    }

    public void addToSelect(String select) {
        this.select.add(select);
    }

    public void addToFrom(String from) {
        this.from.add(from);
    }

    public void addToWhere(String where) {
        this.where.add(where);
    }

    public void addParam(String paramName, Object paramValue) {
        this.params.put(paramName, paramValue);
    }

    public void addToGroup(String group) {
        this.group.add(group);
    }

    public void addToOrder(String order) {
        this.order.add(order);
    }

    public void setFirstResult(Integer firstResult) {
        this.firstResult = firstResult;
    }

    public void setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> list(Class<T> resultType) {
        return (List<T>) getQuery().list();
    }

    @SuppressWarnings("unchecked")
    public <T> T uniqueResult(Class<T> resultType) {
        return (T) getQuery().uniqueResult();
    }

    public Object uniqueResult() {
        return getQuery().uniqueResult();
    }

    @SuppressWarnings("rawtypes")
    private Query getQuery() {
        StringBuilder hql = new StringBuilder();
        if (!select.isEmpty()) append(hql, "select", select, ",");
        append(hql, "from", from);
        if (!where.isEmpty()) append(hql, "where", where, "and");
        if (!group.isEmpty()) append(hql, "group by", group, ",");
        if (!order.isEmpty()) append(hql, "order by", order, ",");
        Query query = session.createQuery(hql.toString());
        for (String paramName : params.keySet()) {
            Object param = params.get(paramName);
            if (param instanceof Collection) {
                query.setParameterList(paramName, (Collection) params.get(paramName));
            } else if (param instanceof Object[]) {
                query.setParameterList(paramName, (Object[]) params.get(paramName));
            } else {
                query.setParameter(paramName, params.get(paramName));
            }
        }
        if (firstResult != null) query.setFirstResult(firstResult);
        if (maxResults != null) query.setMaxResults(maxResults);
        return query;
    }

    private void append(StringBuilder sb, String part, List<?> values, String separator) {
        append(sb, part);
        for (int i = 0; i < values.size(); i++) {
            append(sb, values.get(i));
            if (i != (values.size() - 1)) append(sb, separator);
            sb.append(" ");
        }
    }

    private void append(StringBuilder sb, String part, List<?> values) {
        append(sb, part);
        for (Object value : values) append(sb, value);
    }

    private void append(StringBuilder sb, Object value) {
        sb.append(value);
        sb.append(" ");
    }
}

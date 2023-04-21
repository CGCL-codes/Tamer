package com.sitescape.team.dao.util;

import java.util.List;
import java.util.ArrayList;

/**
 * @author Janet McCann
 * Keep controls for a simple equality query.
 */
public class FilterControls implements Cloneable {

    private List filterNames = new ArrayList();

    private List filterValues = new ArrayList();

    private OrderBy orderBy;

    public FilterControls() {
    }

    public FilterControls(String name, Object value) {
        filterNames.add(name);
        filterValues.add(value);
    }

    public FilterControls(String[] filterNames, Object[] filterValues) {
        addNames(filterNames);
        addValues(filterValues);
    }

    public FilterControls(String[] filterNames, Object[] filterValues, OrderBy filterOrder) {
        addNames(filterNames);
        addValues(filterValues);
        this.orderBy = filterOrder;
    }

    public void add(String name, Object value) {
        filterNames.add(name);
        filterValues.add(value);
    }

    public List getFilterNames() {
        return this.filterNames;
    }

    private void addNames(String[] filterNames) {
        for (int i = 0; i < filterNames.length; ++i) {
            this.filterNames.add(filterNames[i]);
        }
    }

    private void addValues(Object[] filterValues) {
        for (int i = 0; i < filterValues.length; ++i) {
            this.filterValues.add(filterValues[i]);
        }
    }

    public void appendFilter(String alias, StringBuffer filter) {
        int count = filterNames.size();
        if (count > 0) {
            filter.append(" where ");
            filter.append(getWhereString(alias));
        }
        if (orderBy != null) filter.append(" order by " + orderBy.getOrderByClause(alias));
    }

    public String getOrderBy(String alias) {
        if (orderBy != null) return " order by " + orderBy.getOrderByClause(alias);
        return null;
    }

    public String getFilterString(String alias) {
        StringBuffer filter = new StringBuffer();
        appendFilter(alias, filter);
        return filter.toString();
    }

    public String getWhereString(String alias) {
        StringBuffer where = new StringBuffer();
        int count = filterNames.size();
        String name;
        for (int i = 0; i < count; ++i) {
            if (i > 0) where.append(" and ");
            name = (String) filterNames.get(i);
            int pos = name.lastIndexOf('(');
            if (pos == -1) where.append(alias + "." + name + "=? "); else {
                ++pos;
                where.append(name.substring(0, pos) + alias + "." + name.substring(pos, name.length()) + "=? ");
            }
        }
        return where.toString();
    }

    public List getFilterValues() {
        return this.filterValues;
    }

    public OrderBy getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(OrderBy orderBy) {
        this.orderBy = orderBy;
    }
}

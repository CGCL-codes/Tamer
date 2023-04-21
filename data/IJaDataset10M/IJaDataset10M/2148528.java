package net.ontopia.persistence.query.sql;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.ontopia.utils.StringUtils;

/**
 * INTERNAL: Represents an abstract SQL query. Holds SQL query
 * information in a platform independent form.
 */
public class SQLQuery {

    protected List select = new ArrayList();

    protected boolean distinct = false;

    protected int limit = -1;

    protected int offset = -1;

    protected SQLExpressionIF filter;

    protected List orderby;

    public SQLQuery() {
    }

    public boolean isSetQuery() {
        return (getFilter() instanceof SQLSetOperation);
    }

    public boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public List getSelect() {
        return select;
    }

    public void addSelect(SQLValueIF value) {
        select.add(value);
    }

    public void addSelect(SQLAggregateIF aggregate) {
        select.add(aggregate);
    }

    public int getWidth() {
        int width = 0;
        int length = select.size();
        for (int i = 0; i < length; i++) {
            Object value = select.get(i);
            SQLValueIF sqlvalue;
            if (value instanceof SQLAggregateIF) sqlvalue = ((SQLAggregateIF) value).getValue(); else sqlvalue = (SQLValueIF) value;
            width = width + sqlvalue.getArity();
        }
        return width;
    }

    public List getOrderBy() {
        if (orderby == null) return Collections.EMPTY_LIST; else return orderby;
    }

    public void addOrderBy(SQLOrderBy sob) {
        if (orderby == null) orderby = new ArrayList();
        orderby.add(sob);
    }

    public void addAscending(SQLValueIF value) {
        if (orderby == null) orderby = new ArrayList();
        orderby.add(new SQLOrderBy(value, SQLOrderBy.ASCENDING));
    }

    public void addDescending(SQLValueIF value) {
        if (orderby == null) orderby = new ArrayList();
        orderby.add(new SQLOrderBy(value, SQLOrderBy.DESCENDING));
    }

    public void addAscending(SQLAggregateIF aggregate) {
        if (orderby == null) orderby = new ArrayList();
        orderby.add(new SQLOrderBy(aggregate, SQLOrderBy.ASCENDING));
    }

    public void addDescending(SQLAggregateIF aggregate) {
        if (orderby == null) orderby = new ArrayList();
        orderby.add(new SQLOrderBy(aggregate, SQLOrderBy.DESCENDING));
    }

    public SQLExpressionIF getFilter() {
        return filter;
    }

    public void setFilter(SQLExpressionIF filter) {
        this.filter = filter;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("select ");
        if (getDistinct()) sb.append("distinct ");
        if (select == null || select.isEmpty()) sb.append("*"); else StringUtils.join(select, ", ", sb);
        if (getFilter() != null) {
            sb.append(" from ");
            sb.append(getFilter());
        }
        List _orderby = getOrderBy();
        if (!_orderby.isEmpty()) {
            sb.append(" order by ");
            StringUtils.join(_orderby, ", ", sb);
        }
        return sb.toString();
    }
}

package com.completex.objective.components.persistency.core.impl.query;

import com.completex.objective.components.persistency.Mappable;
import com.completex.objective.components.persistency.OdalRuntimePersistencyException;
import com.completex.objective.components.persistency.Query;
import com.completex.objective.components.persistency.SelectQueryDefinition;
import com.completex.objective.components.persistency.policy.DatabasePolicy;
import com.completex.objective.components.persistency.core.Union;
import com.completex.objective.components.persistency.core.UnionMode;
import com.completex.objective.util.ArrayUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Union implementation
 * 
 * @author Gennady Krizhevsky
 */
public class UnionImpl implements Union, com.completex.objective.components.persistency.Cloneable, Serializable, Mappable {

    private List unions;

    private List orderBy;

    private boolean compiled;

    public UnionImpl() {
    }

    public UnionImpl(Map map) {
        fromMap(map);
    }

    protected List lazyUnions() {
        if (unions == null) {
            unions = new ArrayList();
        }
        return unions;
    }

    public int unionSize() {
        return unions == null ? 0 : unions.size();
    }

    public boolean hasEntries() {
        return unionSize() > 0;
    }

    public Union union(SelectQueryDefinition query) {
        return union(query, UnionMode.UNION);
    }

    public Union unionAll(SelectQueryDefinition query) {
        return union(query, UnionMode.UNION_ALL);
    }

    public Union union(SelectQueryDefinition query, UnionMode union) {
        lazyUnions().add(new UnionEntry(query, union));
        return this;
    }

    public Union addToOrderBy(String columnExpression) {
        return addToOrderBy(columnExpression, null);
    }

    public Union addToOrderBy(String columnExpression, Query.OrderDirection direction) {
        ensureImmutable();
        if (columnExpression == null || columnExpression.length() == 0) {
            return this;
        }
        if (direction == Query.ORDER_DESC) {
            columnExpression = columnExpression + Query.DESC;
        }
        if (orderBy == null) {
            orderBy = new ArrayList();
        }
        orderBy.add(columnExpression);
        return this;
    }

    public Union setOrderBy(String[] orderBy) {
        ensureImmutable();
        this.orderBy = ArrayUtil.asList(orderBy);
        return this;
    }

    public Union setOrderBy(List orderBy) {
        ensureImmutable();
        this.orderBy = orderBy;
        return this;
    }

    public String[] getOrderBy() {
        return (String[]) (orderBy == null ? null : orderBy.toArray(new String[orderBy.size()]));
    }

    public boolean hasOrderBy() {
        return orderBy != null && !orderBy.isEmpty();
    }

    public boolean isCompiled() {
        return compiled;
    }

    private void ensureImmutable() {
        if (compiled) {
            throw new IllegalArgumentException("Cannot modify compiled Union");
        }
    }

    public synchronized Union decompile() {
        compiled = false;
        return this;
    }

    public synchronized Union compile(DatabasePolicy databasePolicy) {
        if (!compiled) {
            compiled = true;
        }
        return this;
    }

    public UnionEntry getUnionEntry(int index) {
        return (UnionEntry) unions.get(index);
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new OdalRuntimePersistencyException("Cannot clone union", e);
        }
    }

    public Map toMap() {
        return null;
    }

    public void fromMap(Map map) {
    }
}

package org.middleheaven.domain.criteria;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.middleheaven.domain.criteria.projection.Projection;
import org.middleheaven.domain.criteria.projection.ProjectionOperator;
import org.middleheaven.util.QualifiedName;
import org.middleheaven.util.criteria.AbstractCriteria;
import org.middleheaven.util.criteria.Criterion;
import org.middleheaven.util.criteria.LogicCriterion;
import org.middleheaven.util.criteria.OrderingCriterion;

public abstract class AbstractEntityCriteria<T> extends AbstractCriteria<T> implements EntityCriteria<T> {

    private Class<T> targetClass;

    private Class<T> fromClass;

    private boolean keyOnly = false;

    private boolean countOnly = false;

    private boolean distinct;

    private int count = -1;

    private int start = -1;

    private List<OrderingCriterion> ordering = new LinkedList<OrderingCriterion>();

    private Projection aggregation;

    private List<QualifiedName> resultFields = new LinkedList<QualifiedName>();

    AbstractEntityCriteria(Class<T> targetClass) {
        this.targetClass = targetClass;
    }

    protected AbstractEntityCriteria(AbstractEntityCriteria<T> other) {
        this.targetClass = other.targetClass;
        this.keyOnly = other.keyOnly;
        this.distinct = other.distinct;
        this.count = other.count;
        this.start = other.start;
        this.setRestrictions((LogicCriterion) other.constraints().clone());
        this.ordering = new LinkedList<OrderingCriterion>(other.ordering);
        this.resultFields = new LinkedList<QualifiedName>(other.resultFields);
    }

    public List<OrderingCriterion> ordering() {
        return ordering;
    }

    public Class<T> getTargetClass() {
        return targetClass;
    }

    public Class<T> getFromClass() {
        return fromClass;
    }

    public EntityCriteria<T> add(Criterion criterion) {
        return (EntityCriteria<T>) super.add(criterion);
    }

    public boolean isDistinct() {
        return distinct;
    }

    public EntityCriteria<T> setDistinct(boolean distinct) {
        this.distinct = distinct;
        return this;
    }

    public int getCount() {
        return count;
    }

    public int getStart() {
        return start;
    }

    public Projection projection() {
        return aggregation;
    }

    public EntityCriteria<T> setRange(int count) {
        this.count = count;
        return this;
    }

    public EntityCriteria<T> setRange(int start, int count) {
        this.start = start;
        this.count = count;
        return this;
    }

    public void setKeyOnly(boolean keyOnly) {
        this.keyOnly = keyOnly;
    }

    public final boolean isKeyOnly() {
        return this.keyOnly;
    }

    @Override
    public EntityCriteria<T> add(OrderingCriterion order) {
        this.ordering.add(order);
        return this;
    }

    @Override
    public Collection<QualifiedName> resultFields() {
        return resultFields;
    }

    public void add(ProjectionOperator operator) {
        this.aggregation.add(operator);
    }

    @Override
    public boolean isCountOnly() {
        return countOnly;
    }

    @Override
    public void setCountOnly(boolean countOnly) {
        this.countOnly = countOnly;
    }
}

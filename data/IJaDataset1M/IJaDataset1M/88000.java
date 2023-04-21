package com.amazon.carbonado.rel;

/**
 * Represents a node in a query execution plan.
 *
 * @author Brian S O'Neill
 */
public interface Plan {

    <R, P> R accept(PlanVisitor<R, P> visitor, P param);

    Relation relation();

    Cardinality cardinality();

    Ordering ordering();
}

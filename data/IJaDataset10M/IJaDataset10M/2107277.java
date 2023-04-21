package uk.org.ogsadai.tuple.sort;

import uk.org.ogsadai.expression.ExpressionEvaluationException;
import uk.org.ogsadai.expression.arithmetic.ExecutableFunctionExpression;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.serialise.SerialisableFunction;

/**
 * A group tuple based on a set of group-by columns.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class GroupTuple extends ComparableTuple {

    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2008";

    /** Aggregate functions. */
    private ExecutableFunctionExpression[] mAggregates;

    /**
     * Creates a new group tuple.
     * 
     * @param tuple
     *            content
     * @param columns
     *            columns to compare
     */
    public GroupTuple(Tuple tuple, int[] columns) {
        super(tuple, columns);
    }

    /**
     * Constructor.
     * 
     * @param values
     *            values of the grouping columns
     * @param columns
     *            indexes of the grouping columns
     * @param aggregates
     *            aggregate functions
     */
    public GroupTuple(Object[] values, int[] columns, ExecutableFunctionExpression[] aggregates) {
        super(values);
        mAggregates = aggregates;
    }

    /**
     * Creates a new group tuple.
     * 
     * @param tuple
     *            content
     * @param columns
     *            columns to compare
     * @param aggregates
     *            aggregate functions
     */
    public GroupTuple(Tuple tuple, int[] columns, ExecutableFunctionExpression[] aggregates) {
        super(tuple, columns);
        mAggregates = aggregates;
        evaluate(tuple);
    }

    /**
     * Sets the aggregate functions.
     * 
     * @param aggregates
     *            aggregate functions
     */
    public void setAggregates(ExecutableFunctionExpression[] aggregates) {
        mAggregates = aggregates;
        evaluate(mTuple);
    }

    /**
     * Adds a new tuple to the group by evaluating the aggregate functions.
     * 
     * @param tuple
     *            tuple to add
     */
    public void evaluate(Tuple tuple) {
        try {
            for (ExecutableFunctionExpression function : mAggregates) {
                function.evaluate(tuple);
            }
        } catch (ExpressionEvaluationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public Tuple getTuple() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the values of the grouping columns.
     * 
     * @return values
     */
    public Object[] getGroupColumns() {
        return mValues;
    }

    /**
     * Returns the aggregate functions.
     * 
     * @return aggregates
     */
    public ExecutableFunctionExpression[] getAggregates() {
        return mAggregates;
    }

    /**
     * Merges this group tuple with the given group tuple.
     * 
     * @param group
     *            group to merge
     */
    public void mergeGroups(GroupTuple group) {
        if (group.mAggregates != null) {
            for (int i = 0; i < mAggregates.length; i++) {
                SerialisableFunction aggregate = (SerialisableFunction) mAggregates[i].getExecutable();
                aggregate.merge((SerialisableFunction) group.mAggregates[i].getExecutable());
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append("Values [");
        if (mValues != null) {
            for (int i = 0; i < mValues.length - 1; i++) {
                result.append(mValues[i].toString());
                result.append(", ");
            }
            result.append(mValues[mValues.length - 1].toString());
        }
        result.append("], Aggregates [");
        if (mAggregates != null) {
            for (int i = 0; i < mAggregates.length - 1; i++) {
                result.append(mAggregates[i]);
                result.append(", ");
            }
            result.append(mAggregates[mAggregates.length - 1]);
        }
        result.append("]");
        return result.toString();
    }
}

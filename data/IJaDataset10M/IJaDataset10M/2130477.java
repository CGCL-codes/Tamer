package org.middleheaven.persistance.criteria.building;

import org.middleheaven.persistance.criteria.DataSetConstraint;
import org.middleheaven.persistance.criteria.LogicConstraint;
import org.middleheaven.util.criteria.CriterionOperator;

/**
 * 
 */
public abstract class AbstractComparisonOperatorBuilders<SELF, TERMBUILDER> implements ComparisonOperatorBuilders<SELF, TERMBUILDER> {

    DataSetCriteriaBuilder builder;

    private boolean negated = false;

    ColumnValueLocator leftSideValueLocator;

    LogicConstraint logic;

    TERMBUILDER termBuilder;

    public <T extends LogicConstraintHolder> AbstractComparisonOperatorBuilders(ColumnValueLocator valueLocator, DataSetCriteriaBuilder builder, TERMBUILDER termBuilder, LogicConstraint logic) {
        this.leftSideValueLocator = valueLocator;
        this.builder = builder;
        this.logic = logic;
        this.termBuilder = termBuilder;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public final SELF not() {
        negated = !negated;
        return (SELF) this;
    }

    private RestrictionValueCaptureBuilder<TERMBUILDER> operator(CriterionOperator operator) {
        if (this.negated) {
            negated = false;
            operator = operator.negate();
        }
        return new GenericRestrictionValueCaptureBuilder<TERMBUILDER>(this, operator);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public RestrictionValueCaptureBuilder<TERMBUILDER> gt() {
        return operator(CriterionOperator.GREATER_THAN);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public RestrictionValueCaptureBuilder<TERMBUILDER> lt() {
        return operator(CriterionOperator.LESS_THAN);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public RestrictionValueCaptureBuilder<TERMBUILDER> ge() {
        return operator(CriterionOperator.GREATER_THAN_OR_EQUAL);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public RestrictionValueCaptureBuilder<TERMBUILDER> le() {
        return operator(CriterionOperator.LESS_THAN_OR_EQUAL);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public RestrictionValueCaptureBuilder<TERMBUILDER> eq() {
        return operator(CriterionOperator.EQUAL);
    }

    /**
	 * @param columnValueConstraint
	 */
    public void addConstraint(ColumnValueConstraint constraint) {
        this.logic.addConstraint(constraint);
    }
}

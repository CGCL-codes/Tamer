package net.sourceforge.jeval;

import net.sourceforge.jeval.operator.Operator;

/**
 * Represents the next operator in the expression to process.
 */
class NextOperator {

    private Operator operator = null;

    private int index = -1;

    /**
	 * Create a new NextOperator from an operator and index.
	 * 
	 * @param operator
	 *            The operator this object represents.
	 * @param index
	 *            The index of the operator in the expression.
	 */
    public NextOperator(final Operator operator, final int index) {
        this.operator = operator;
        this.index = index;
    }

    /**
	 * Returns the operator for this object.
	 * 
	 * @return The operator represented by this object.
	 */
    public Operator getOperator() {
        return operator;
    }

    /**
	 * Returns the index for this object.
	 * 
	 * @return The index of the operator in the expression.
	 */
    public int getIndex() {
        return index;
    }
}

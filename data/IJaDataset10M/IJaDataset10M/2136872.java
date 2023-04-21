package org.palo.api.subsets.filter.settings;

import org.palo.api.subsets.Subset2;

/**
 * <code>DataCriteria</code>
 * <p>
 * A data criteria consists of one or two operators and their operands. Data
 * criteria are used by a data filter to bound the subset elements by their 
 * value. With a data criteria expressions like <code>take all elements > 400</code>
 * are possible. 
 * </p>
 *
 * @author ArndHouben
 * @version $Id: DataCriteria.java,v 1.5 2008/05/15 08:14:41 ArndHouben Exp $
 **/
public class DataCriteria {

    public static final String LESSER = "<";

    public static final String LESSER_EQUAL = "<=";

    public static final String GREATER = ">";

    public static final String GREATER_EQUAL = ">=";

    public static final String NOT_EQUAL = "<>";

    public static final String EQUAL = "=";

    public static final String[] ALL_OPERATORS = new String[] { LESSER, LESSER_EQUAL, GREATER, GREATER_EQUAL, NOT_EQUAL, EQUAL };

    private int operator1;

    private int operator2;

    private StringParameter operand1;

    private StringParameter operand2;

    private Subset2 subset;

    /**
	 * Creates a new <code>DataCriteria</code> instance with the given operator
	 * and operand
	 * @param operator one of the defined operator constants
	 * @param operand an operand value 
	 */
    public DataCriteria(String operator, String operand) {
        this.operator1 = getIndex(operator);
        this.operand1 = new StringParameter();
        this.operand2 = new StringParameter();
        this.operand1.setValue(operand);
    }

    /**
	 * Checks if this criteria has an additional operator and operand
	 * @return <code>true</code> if this criteria has a second expression,
	 * <code>false</code> otherwise
	 */
    public final boolean hasSecondOperator() {
        return operand2 != null && !operand2.equals("");
    }

    /**
	 * Returns the first operator
	 * @return the first operator
	 */
    public final String getFirstOperator() {
        return ALL_OPERATORS[operator1];
    }

    /**
	 * Returns the index of the first operator inside the predefined operator
	 * constants array
	 * @return first operator index
	 */
    public final int getFirstOperatorIndex() {
        return operator1;
    }

    /**
	 * Sets the operator to use as first operator
	 * @param operator1 the new first operator
	 */
    public final void setFirstOperator(String operator1) {
        this.operator1 = getIndex(operator1);
        markDirty();
    }

    /**
	 * Sets the first operator by specifying its index in the predefined
	 * operator constants array
	 * @param index the new first operator
	 */
    public final void setFirstOperator(int index) {
        this.operator1 = index;
        markDirty();
    }

    /**
	 * Returns the second operator
	 * @return the second operator
	 */
    public final String getSecondOperator() {
        return ALL_OPERATORS[operator2];
    }

    /**
	 * Returns the index of the second operator inside the predefined operator
	 * constants array
	 * @return second operator index
	 */
    public final int getSecondOperatorIndex() {
        return operator2;
    }

    /**
	 * Sets the operator to use as second operator
	 * @param operator2 the new second operator
	 */
    public final void setSecondOperator(String operator2) {
        this.operator2 = getIndex(operator2);
        markDirty();
    }

    /**
	 * Sets the second operator by specifying its index in the predefined 
	 * operator constants array
	 * @param index the new second operator
	 */
    public final void setSecondOperator(int index) {
        this.operator2 = index;
        markDirty();
    }

    public final StringParameter getFirstOperand() {
        return operand1;
    }

    /**
	 * Sets the first operand
	 * @param operand1 the new first operand
	 */
    public final void setFirstOperand(String operand1) {
        this.operand1.setValue(operand1);
    }

    public final void setFirstOperand(StringParameter operand1) {
        this.operand1 = operand1;
        this.operand1.bind(subset);
        markDirty();
    }

    public final StringParameter getSecondOperand() {
        return operand2;
    }

    /**
	 * Sets the second operand
	 * @param operand2 the new second operand
	 */
    public final void setSecondOperand(String operand2) {
        this.operand2.setValue(operand2);
    }

    public final void setSecondOperand(StringParameter operand2) {
        this.operand2 = operand2;
        this.operand2.bind(subset);
        markDirty();
    }

    /**
	 * <p>Binds this instance to the given {@link Subset2}</p>
	 * <b>NOTE: PLEASE DON'T USE! INTERNAL METHOD </b>
	 * @param subset 
	 */
    public final void bind(Subset2 subset) {
        this.subset = subset;
        operand1.bind(subset);
        operand2.bind(subset);
        markDirty();
    }

    /**
	 * <p>Releases this instance from a previously binded {@link Subset2}</p>
	 * <b>NOTE: PLEASE DON'T USE! INTERNAL METHOD </b>
	 */
    public final void unbind() {
        subset = null;
        operand1.unbind();
        operand2.unbind();
    }

    /**
	 * Creates a deep copy of this data criteria instance.
	 * @return
	 */
    final DataCriteria copy() {
        DataCriteria copy = new DataCriteria(ALL_OPERATORS[operator1], operand1.getValue());
        copy.operator2 = operator2;
        copy.operand2 = operand2;
        return copy;
    }

    private final int getIndex(String operator) {
        for (int i = 0; i < ALL_OPERATORS.length; ++i) {
            if (ALL_OPERATORS[i].equals(operator)) return i;
        }
        throw new RuntimeException("Illegal operator!");
    }

    private final void markDirty() {
        if (subset != null) subset.modified();
    }
}

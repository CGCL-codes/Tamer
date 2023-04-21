package csheets.core.formula.lang;

import csheets.core.Value;

/**
 * A relational equal operator.
 * @author Einar Pehrson
 */
public class Equal extends RelationalOperator {

    /** The unique version identifier used for serialization */
    private static final long serialVersionUID = 6599944970820617562L;

    /**
	 * Creates a new relational equal operator.
	 */
    public Equal() {
    }

    public <T> boolean compare(Comparable<T> left, T right) {
        return left.compareTo(right) == 0;
    }

    public String getIdentifier() {
        return "=";
    }

    public Value.Type getOperandValueType() {
        return Value.Type.NUMERIC;
    }

    public String toString() {
        return getIdentifier();
    }
}

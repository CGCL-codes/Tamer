package org.norecess.citkit.tir.expressions;

import java.util.HashMap;
import java.util.Map;
import org.norecess.citkit.tir.ExpressionTIR;
import org.norecess.citkit.tir.IPosition;
import org.norecess.citkit.types.HobbesType;
import org.norecess.citkit.visitors.ExpressionTIRVisitor;

/**
 * The TIR for an operator expression. This covers most types of
 * expressions---comparative, additive, multiplicative, etc. The
 * {@link OperatorETIR.Operator} enumerate type supplies values for the possible
 * operators.
 * 
 * @author Jeremy D. Frens
 * 
 */
public class OperatorETIR implements IOperatorETIR {

    /**
	 * Represents an operator like addition or multiplication. See the
	 * enumerated constants for possible values.
	 * 
	 * <p>
	 * Each operator has punctuation associated with it (e.g., {@link #ADD} has
	 * <code>"+"</code> associated with it). {@link #toString()} still returns
	 * the raw name (e.g., <code>"ADD"</code> ); use {@link #getPunctuation()}
	 * to get the punctuation. {@link #convertPunctuation(String)} can be used
	 * to turn punctuation (like <code>"+"</code>) into an
	 * {@link OperatorETIR.Operator}.
	 * 
	 * @author Jeremy D. Frens
	 */
    public enum Operator implements IOperator {

        ADD("+"), SUBTRACT("-"), MULTIPLY("*"), DIVIDE("/"), MODULUS("%"), EQUALS("="), NOT_EQUALS("<>"), LESS_THAN("<"), LESS_EQUALS("<="), GREATER_EQUALS(">="), GREATER_THAN(">"), AND("&"), OR("|");

        private String myPunctuation;

        private Operator(String punctuation) {
            myPunctuation = punctuation;
        }

        /**
		 * Gets the standard punctuation for the operator.
		 */
        public String getPunctuation() {
            return myPunctuation;
        }

        private static final Map<String, Operator> PUNCTUATION;

        static {
            PUNCTUATION = new HashMap<String, Operator>();
            PUNCTUATION.put("+", ADD);
            PUNCTUATION.put("-", SUBTRACT);
            PUNCTUATION.put("/", DIVIDE);
            PUNCTUATION.put("*", MULTIPLY);
            PUNCTUATION.put("%", MODULUS);
            PUNCTUATION.put("=", EQUALS);
            PUNCTUATION.put("<>", NOT_EQUALS);
            PUNCTUATION.put("<", LESS_THAN);
            PUNCTUATION.put("<=", LESS_EQUALS);
            PUNCTUATION.put(">=", GREATER_EQUALS);
            PUNCTUATION.put(">", GREATER_THAN);
            PUNCTUATION.put("&", AND);
            PUNCTUATION.put("|", OR);
        }

        /**
		 * Converts the standard C/C++/Java punctuation strings into operators.
		 * For example, <code>"+"</code> becomes an <code>ADD</code>.
		 * 
		 * @param punctuation
		 *            the punctuation for the operator.
		 * @return the appropriate {@link Operator}.
		 */
        public static Operator convertPunctuation(String punctuation) {
            return PUNCTUATION.get(punctuation);
        }
    }

    /**
	 * The expression on the left of the operator.
	 */
    private final ExpressionTIR myLeft;

    /**
	 * The operator.
	 */
    private final IOperator myOperator;

    /**
	 * The expression on the right of the operator.
	 */
    private final ExpressionTIR myRight;

    /**
	 * The position of code that lead to the TIR.
	 */
    private final IPosition myPosition;

    /**
	 * Constructs an operator expression.
	 * 
	 * @param position
	 *            the position of the operator expression in the source code.
	 * @param left
	 *            the expression to the left of the operator.
	 * @param operator
	 *            the operator (see the constants of this class).
	 * @param right
	 *            the expression to the right of the operator.
	 */
    public OperatorETIR(IPosition position, ExpressionTIR left, IOperator operator, ExpressionTIR right) {
        myPosition = position;
        myLeft = left;
        myOperator = operator;
        myRight = right;
    }

    /**
	 * Basic constructor.
	 * 
	 * @param left
	 *            the left subexpression.
	 * @param operator
	 *            the operator.
	 * @param right
	 *            the right subexpression.
	 */
    public OperatorETIR(ExpressionTIR left, IOperator operator, ExpressionTIR right) {
        this(null, left, operator, right);
    }

    /**
	 * Returns the left expression.
	 * 
	 * @return the left expression.
	 */
    public ExpressionTIR getLeftExpression() {
        return myLeft;
    }

    /**
	 * Returns the left expression.
	 * 
	 * @return the left expression.
	 */
    public ExpressionTIR getLeft() {
        return myLeft;
    }

    /**
	 * Returns the operator.
	 * 
	 * @return the operator.
	 */
    public IOperator getOperator() {
        return myOperator;
    }

    /**
	 * Returns the right expression.
	 * 
	 * @return the right expression.
	 */
    public ExpressionTIR getRightExpression() {
        return myRight;
    }

    /**
	 * Returns the right expression.
	 * 
	 * @return the right expression.
	 */
    public ExpressionTIR getRight() {
        return myRight;
    }

    /**
	 * Currently, this method returns the type of the left subtree. This is an
	 * error if the right subtree is more general.
	 */
    public HobbesType getType() {
        return myLeft.getType();
    }

    public <T> T accept(ExpressionTIRVisitor<T> visitor) {
        return visitor.visitOperatorETIR(this);
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof OperatorETIR) && equals((OperatorETIR) o);
    }

    private boolean equals(OperatorETIR o) {
        return getLeft().equals(o.getLeft()) && (getOperator() == o.getOperator()) && getRightExpression().equals(o.getRightExpression());
    }

    @Override
    public int hashCode() {
        return getLeft().hashCode() * getOperator().hashCode() * getRightExpression().hashCode();
    }

    @Override
    public String toString() {
        return "(" + getLeft() + getOperator().getPunctuation() + getRightExpression() + ")";
    }

    /**
	 * Retrieves the position of the code that generated the TIR.
	 * 
	 * @return the position of the code in the source code.
	 */
    public IPosition getPosition() {
        return myPosition;
    }
}

package jdice.expression.lib;

import java.io.PrintStream;
import jdice.expression.AbstractBooleanExpression;
import jdice.expression.EvaluationContext;
import jdice.expression.Expression;
import jdice.expression.OperatorPrecedence;
import jdice.expression.Scope;
import jdice.values.Value;

/**
 * Comparison expressions.
 * @author phatonin
 *
 */
public class Compare extends AbstractBooleanExpression {

    /** Comparison operators. */
    public static enum Operator {

        /** == */
        EQ {

            @Override
            public boolean evaluate(int compare) {
                return compare == 0;
            }

            @Override
            public String toString() {
                return "==";
            }
        }
        , /** != */
        NE {

            @Override
            public boolean evaluate(int compare) {
                return compare != 0;
            }

            @Override
            public String toString() {
                return "!=";
            }
        }
        , /** < */
        LT {

            @Override
            public boolean evaluate(int compare) {
                return compare < 0;
            }

            @Override
            public String toString() {
                return "<";
            }
        }
        , /** > */
        GT {

            @Override
            public boolean evaluate(int compare) {
                return compare > 0;
            }

            @Override
            public String toString() {
                return ">";
            }
        }
        , /** <= */
        LE {

            @Override
            public boolean evaluate(int compare) {
                return compare <= 0;
            }

            @Override
            public String toString() {
                return "<=";
            }
        }
        , /** >= */
        GE {

            @Override
            public boolean evaluate(int compare) {
                return compare >= 0;
            }

            @Override
            public String toString() {
                return ">=";
            }
        }
        ;

        /**
		 * Returns true iff the specified comparison result is compatible with this operator.
		 * @param compare
		 * @return
		 */
        abstract boolean evaluate(int compare);
    }

    /** Comparison operator. */
    public final Operator operator;

    /** Left operand. */
    public final Expression left;

    /** Right operand. */
    public final Expression right;

    /**
	 * Constructs a comparison expression with the specified operator and operands.
	 * @param operator
	 * @param left
	 * @param right
	 */
    public Compare(Operator operator, Expression left, Expression right) {
        super();
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean evaluateBoolean(EvaluationContext ctx, Scope scope) {
        Value l = left.evaluate(ctx, scope);
        return operator.evaluate(l.compareTo(right.evaluate(ctx, scope)));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((left == null) ? 0 : left.hashCode());
        result = prime * result + ((operator == null) ? 0 : operator.hashCode());
        result = prime * result + ((right == null) ? 0 : right.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Compare other = (Compare) obj;
        if (left == null) {
            if (other.left != null) return false;
        } else if (!left.equals(other.left)) return false;
        if (operator == null) {
            if (other.operator != null) return false;
        } else if (!operator.equals(other.operator)) return false;
        if (right == null) {
            if (other.right != null) return false;
        } else if (!right.equals(other.right)) return false;
        return true;
    }

    @Override
    public void tree(PrintStream out, int depth) {
        tree(out, depth, operator.toString(), left, right);
    }

    @Override
    public OperatorPrecedence getOperatorPrecedence() {
        return OperatorPrecedence.COMPARE;
    }

    @Override
    public void toString(StringBuilder sb) {
        binaryOperatorString(sb, operator.toString(), left, right);
    }

    @Override
    public boolean isAssociative() {
        return false;
    }
}

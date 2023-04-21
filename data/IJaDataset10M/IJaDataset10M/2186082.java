package com.ibm.tuningfork.infra.stream.expression.operations;

import com.ibm.tuningfork.infra.stream.core.Stream;
import com.ibm.tuningfork.infra.stream.expression.base.BooleanExpression;
import com.ibm.tuningfork.infra.stream.expression.base.Expression;
import com.ibm.tuningfork.infra.stream.expression.base.MissingValueException;
import com.ibm.tuningfork.infra.stream.expression.base.StreamContext;
import com.ibm.tuningfork.infra.stream.expression.base.StreamExpression;
import com.ibm.tuningfork.infra.stream.expression.literals.StreamLiteral;

/**
 * The ternary conditional operation (?:) with a stream result.
 */
public class StreamTernaryConditional extends StreamExpression {

    /**
     * The condition expression (comes before the ? punctuator)
     */
    private BooleanExpression condition;

    /**
     * The operand between the ? and :
     */
    private StreamExpression left;

    /**
     * The operand after the :
     */
    private StreamExpression right;

    /**
     * Make a new ternary conditional.  The left and right tuples must be exactly the same type unless one of them is StreamLiteral.MISSING
     *   which is always null
     * @param condition the condition
     * @param left the operand between the ? and :
     * @param right the operand after the :
     */
    public StreamTernaryConditional(BooleanExpression condition, StreamExpression left, StreamExpression right) {
        super(false);
        this.condition = condition;
        this.left = left;
        this.right = right;
        if (left == StreamLiteral.MISSING) {
            setType(right.getType());
        } else if (right == StreamLiteral.MISSING) {
            setType(left.getType());
        } else {
            checkAssertion(left.getType().equals(right.getType()), "Differently typed streams cannot be used in the same conditional expression");
            setType(left.getType());
        }
        mergeContexts(condition, left, right);
    }

    public Stream getStreamValue(StreamContext context) {
        try {
            return condition.getBooleanValue(context) ? left.getStreamValue(context) : right.getStreamValue(context);
        } catch (MissingValueException e) {
            return null;
        }
    }

    public StreamExpression resolve(Expression[] arguments, int depth) {
        BooleanExpression newCondition = condition.resolve(arguments, depth);
        StreamExpression newOperand1 = left.resolve(arguments, depth);
        StreamExpression newOperand2 = right.resolve(arguments, depth);
        if (condition != newCondition || left != newOperand1 || right != newOperand2) {
            return (StreamExpression) new StreamTernaryConditional(newCondition, newOperand1, newOperand2);
        }
        return this;
    }

    public String toString() {
        StringBuilder ans = new StringBuilder("(");
        ans.append(condition).append(" ? ");
        ans.append(left).append(" : ");
        ans.append(right).append(")");
        return ans.toString();
    }
}

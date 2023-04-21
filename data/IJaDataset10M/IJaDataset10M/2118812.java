package uk.ac.lkl.migen.system.ai.analysis.core.cbr.expression;

/**
 * 
 * 
 * @author sergut
 *
 */
public class AdditionBinaryOperator implements AttributeOperator {

    public int eval(AttributeExpression op1, AttributeExpression op2) {
        return op1.eval() + op2.eval();
    }

    public String getOperatorSymbol() {
        return "+";
    }
}

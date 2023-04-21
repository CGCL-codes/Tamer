package jmathlib.core.tokens;

import jmathlib.core.interpreter.GlobalValues;
import jmathlib.core.tokens.numbertokens.DoubleNumberToken;

/**Used to implement addition and subtraction operations within an expression*/
public class AddSubOperatorToken extends BinaryOperatorToken {

    /**Constructor taking the operator and priority
     * @param _operator = the actual operator		   
     */
    public AddSubOperatorToken(char _operator) {
        super(_operator, ADDSUB_PRIORITY);
    }

    /**evaluates the operator
     * @param operands = the operators operands
     * @param globals
     * @return the result of the operation as an OperandToken
     */
    public OperandToken evaluate(Token[] operands, GlobalValues globals) {
        OperandToken result = null;
        OperandToken left = ((OperandToken) operands[0]);
        if (left == null) left = new DoubleNumberToken(0);
        OperandToken right = ((OperandToken) operands[1]);
        if (right == null) right = new DoubleNumberToken(0);
        OperandToken[] ops = { left, right };
        if (value == '+') {
            result = ops[0].add(ops[1]);
        } else {
            result = ops[0].subtract(ops[1]);
        }
        if (result == null) {
            result = new Expression(this, left, right);
        }
        return result;
    }
}

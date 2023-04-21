package calclipse.lib.math.mp.tokens;

import calclipse.lib.math.Complex;
import calclipse.lib.math.MathUtil;
import calclipse.lib.math.mp.OperatorPriorities;
import calclipse.lib.math.rpn.Constant;
import calclipse.lib.math.rpn.Operand;
import calclipse.lib.math.rpn.Operator;
import calclipse.lib.math.rpn.RPNException;
import calclipse.lib.math.rpn.RPNTokenType;
import calclipse.msg.MathMessages;

/**
 * Supports real and complex.
 * @author T. Sommerland
 */
public class ASin extends Operator {

    public ASin() {
        super("asin", RPNTokenType.LUOPERATOR, OperatorPriorities.MEDIUM);
    }

    @Override
    public Operand calculate(final Operand op1, final Operand op2) throws RPNException {
        final Object o = op1.getValue();
        if (o instanceof Number) {
            final Number n = (Number) o;
            return new Constant(Math.asin(n.doubleValue()));
        } else if (o instanceof Complex) {
            final Complex c = (Complex) o;
            return new Constant(MathUtil.asin(c));
        }
        throw new RPNException(MathMessages.getInvalidDataType());
    }
}

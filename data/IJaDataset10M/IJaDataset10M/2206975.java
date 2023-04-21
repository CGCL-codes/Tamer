package calclipse.lib.math.mp.tokens;

import calclipse.lib.math.Fraction;
import calclipse.lib.math.mp.OperatorPriorities;
import calclipse.lib.math.rpn.Constant;
import calclipse.lib.math.rpn.Operand;
import calclipse.lib.math.rpn.Operator;
import calclipse.lib.math.rpn.RPNException;
import calclipse.lib.math.rpn.RPNTokenType;
import calclipse.msg.MathMessages;

/**
 * The denominator of a fraction.
 * @author T. Sommerland
 */
public class Den extends Operator {

    public Den() {
        super("den", RPNTokenType.LUOPERATOR, OperatorPriorities.MEDIUM);
    }

    @Override
    public Operand calculate(final Operand op1, final Operand op2) throws RPNException {
        final Object o = op1.getValue();
        if (o instanceof Fraction) {
            final Fraction f = (Fraction) o;
            return new Constant(f.denominator().doubleValue());
        }
        throw new RPNException(MathMessages.getInvalidDataType());
    }
}

package calclipse.lib.math.mp.tokens;

import calclipse.lib.math.mp.MPConstants;
import calclipse.lib.math.mp.OperatorPriorities;
import calclipse.lib.math.rpn.Operand;
import calclipse.lib.math.rpn.Operator;
import calclipse.lib.math.rpn.RPNException;
import calclipse.lib.math.rpn.RPNTokenType;
import calclipse.msg.MathMessages;

/**
 * Less or equals (<=).
 * Supports comparable objects.
 * @author T. Sommerland
 */
public class LessEquals extends Operator {

    public LessEquals() {
        super("<=", RPNTokenType.BOPERATOR, OperatorPriorities.TEST);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Operand calculate(final Operand op1, final Operand op2) throws RPNException {
        try {
            final Comparable c1 = (Comparable) op1.getValue();
            final Object o2 = op2.getValue();
            if (c1.compareTo(o2) <= 0) {
                return MPConstants.TRUE;
            } else {
                return MPConstants.FALSE;
            }
        } catch (final ClassCastException ex) {
            throw new RPNException(MathMessages.getInvalidDataType(), null, ex);
        }
    }
}

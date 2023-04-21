package calclipse.core.comps.string.tokens;

import calclipse.lib.math.mp.MPConstants;
import calclipse.lib.math.mp.OperatorPriorities;
import calclipse.lib.math.rpn.Operand;
import calclipse.lib.math.rpn.Operator;
import calclipse.lib.math.rpn.RPNException;
import calclipse.lib.math.rpn.RPNTokenType;

public class EndsWith extends Operator {

    public EndsWith() {
        super("ends_with", RPNTokenType.BOPERATOR, OperatorPriorities.MEDIUM);
    }

    @Override
    public Operand calculate(final Operand op1, final Operand op2) throws RPNException {
        final String s1 = op1.getValue().toString();
        final String s2 = op2.getValue().toString();
        if (s1.endsWith(s2)) {
            return MPConstants.TRUE;
        }
        return MPConstants.FALSE;
    }
}

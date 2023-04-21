package calclipse.mcomp.cntxt.tokens;

import calclipse.lib.math.mp.MPUtil;
import calclipse.lib.math.mp.OperatorPriorities;
import calclipse.lib.math.rpn.Constant;
import calclipse.lib.math.rpn.Operand;
import calclipse.lib.math.rpn.Operator;
import calclipse.lib.math.rpn.RPNException;
import calclipse.lib.math.rpn.RPNTokenType;
import calclipse.msg.MathMessages;

public class Wait extends Operator {

    public Wait() {
        super("wait", RPNTokenType.LUOPERATOR, OperatorPriorities.MEDIUM);
    }

    @Override
    public Operand calculate(final Operand op1, final Operand op2) throws RPNException {
        final long millies = MPUtil.toLong(op1.getValue());
        if (millies < 0) {
            throw new RPNException(MathMessages.getDomainError());
        }
        try {
            Thread.sleep(millies);
        } catch (final InterruptedException ex) {
            throw MPUtil.interrupt(ex);
        }
        return Constant.VOID;
    }
}

package calclipse.mcomp.cntxt.tokens;

import calclipse.lib.math.mp.MPConstants;
import calclipse.lib.math.mp.MPUtil;
import calclipse.lib.math.rpn.Function;
import calclipse.lib.math.rpn.Operand;
import calclipse.lib.math.rpn.RPNException;
import calclipse.lib.math.rpn.ReturnException;

public class Return extends Function {

    public Return() {
        super("return", MPConstants.PAREN_DELIMITATION);
    }

    @Override
    public Operand getValue(final Operand[] args) throws RPNException {
        if (args.length > 1) {
            MPUtil.throwWrongNArgs();
        }
        if (args.length == 1) {
            throw new ReturnException(args[0].getValue());
        }
        throw new ReturnException();
    }
}

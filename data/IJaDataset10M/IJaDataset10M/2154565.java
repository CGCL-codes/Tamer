package calclipse.lib.math.mp.tokens;

import calclipse.lib.math.mp.OperatorPriorities;
import calclipse.lib.math.mtrx.Matrix;
import calclipse.lib.math.mtrx.MatrixUtil;
import calclipse.lib.math.rpn.Constant;
import calclipse.lib.math.rpn.Operand;
import calclipse.lib.math.rpn.Operator;
import calclipse.lib.math.rpn.RPNException;
import calclipse.lib.math.rpn.RPNTokenType;
import calclipse.msg.MathMessages;

/**
 * The Frobenius norm of a matrix.
 * @author T. Sommerland
 */
public class NormF extends Operator {

    public NormF() {
        super("normf", RPNTokenType.LUOPERATOR, OperatorPriorities.MEDIUM);
    }

    @Override
    public Operand calculate(final Operand op1, final Operand op2) throws RPNException {
        final Object o = op1.getValue();
        if (o instanceof Matrix) {
            final Matrix m = (Matrix) o;
            return new Constant(MatrixUtil.normF(m));
        }
        throw new RPNException(MathMessages.getInvalidDataType());
    }
}

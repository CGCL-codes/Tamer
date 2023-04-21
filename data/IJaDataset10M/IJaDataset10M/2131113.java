package net.zeminvaders.lang.ast;

import net.zeminvaders.lang.Interpreter;
import net.zeminvaders.lang.SourcePosition;
import net.zeminvaders.lang.runtime.ZemNumber;
import net.zeminvaders.lang.runtime.ZemObject;

/**
 * Negate (-) operator.
 *
 * @author <a href="mailto:grom@zeminvaders.net">Cameron Zemek</a>
 */
public class NegateOpNode extends UnaryOpNode implements IArithmeticOpNode {

    public NegateOpNode(SourcePosition pos, Node value) {
        super(pos, "-", value);
    }

    @Override
    public ZemObject eval(Interpreter interpreter) {
        ZemNumber operand = getOperand().eval(interpreter).toNumber(getOperand().getPosition());
        return operand.negate();
    }
}

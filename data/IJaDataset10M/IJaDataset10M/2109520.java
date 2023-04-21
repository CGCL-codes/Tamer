package net.zeminvaders.lang.ast;

import net.zeminvaders.lang.Interpreter;
import net.zeminvaders.lang.SourcePosition;
import net.zeminvaders.lang.runtime.ZemBoolean;
import net.zeminvaders.lang.runtime.ZemObject;

/**
 * Boolean not (!) operator.
 *
 * @author <a href="mailto:grom@zeminvaders.net">Cameron Zemek</a>
 */
public class NotOpNode extends UnaryOpNode implements IBooleanOpNode {

    public NotOpNode(SourcePosition pos, Node operand) {
        super(pos, "not", operand);
    }

    @Override
    public ZemObject eval(Interpreter interpreter) {
        ZemBoolean operand = getOperand().eval(interpreter).toBoolean(getOperand().getPosition());
        return operand.not();
    }
}

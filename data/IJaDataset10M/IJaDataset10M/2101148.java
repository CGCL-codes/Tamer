package net.zeminvaders.lang.ast;

import net.zeminvaders.lang.Interpreter;
import net.zeminvaders.lang.SourcePosition;
import net.zeminvaders.lang.runtime.ZemNumber;
import net.zeminvaders.lang.runtime.ZemObject;

/**
 * A number constant.
 *
 * @author <a href="mailto:grom@zeminvaders.net">Cameron Zemek</a>
 */
public class NumberNode extends Node {

    private ZemNumber number;

    public NumberNode(SourcePosition pos, String number) {
        super(pos);
        this.number = new ZemNumber(number);
    }

    @Override
    public String toString() {
        return number.toString();
    }

    @Override
    public ZemObject eval(Interpreter interpreter) {
        return number;
    }
}

package de.grogra.xl.expr;

import de.grogra.xl.compiler.BytecodeWriter;
import de.grogra.xl.vmx.*;
import de.grogra.reflect.*;

public class CharConst extends EvalExpression implements Constant {

    public char value;

    public CharConst() {
        super(Type.CHAR);
    }

    public CharConst(char value) {
        this();
        this.value = value;
    }

    @Override
    protected char evaluateCharImpl(VMXState t) {
        return value;
    }

    @Override
    protected void writeImpl(BytecodeWriter out, boolean discard) {
        if (!discard) {
            out.visiticonst(value);
        }
    }
}

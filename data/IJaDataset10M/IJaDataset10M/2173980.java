package org.apache.harmony.unpack200.bytecode.forms;

import org.apache.harmony.unpack200.SegmentConstantPool;
import org.apache.harmony.unpack200.bytecode.OperandManager;

/**
 * This class implements the byte code form for those bytecodes which have int
 * references (and only int references).
 */
public class IntRefForm extends SingleByteReferenceForm {

    public IntRefForm(int opcode, String name, int[] rewrite) {
        super(opcode, name, rewrite);
    }

    public IntRefForm(int opcode, String name, int[] rewrite, boolean widened) {
        this(opcode, name, rewrite);
        this.widened = widened;
    }

    protected int getOffset(OperandManager operandManager) {
        return operandManager.nextIntRef();
    }

    protected int getPoolID() {
        return SegmentConstantPool.CP_INT;
    }
}

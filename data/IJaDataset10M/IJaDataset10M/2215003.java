package org.apache.harmony.unpack200.bytecode.forms;

import org.apache.harmony.pack200.Pack200Exception;
import org.apache.harmony.unpack200.SegmentConstantPool;
import org.apache.harmony.unpack200.bytecode.ByteCode;
import org.apache.harmony.unpack200.bytecode.ClassFileEntry;
import org.apache.harmony.unpack200.bytecode.OperandManager;

/**
 * This class implements the byte code form for those bytecodes which have class
 * references (and only class references).
 */
public class ClassRefForm extends ReferenceForm {

    protected boolean widened;

    public ClassRefForm(int opcode, String name, int[] rewrite) {
        super(opcode, name, rewrite);
    }

    public ClassRefForm(int opcode, String name, int[] rewrite, boolean widened) {
        this(opcode, name, rewrite);
        this.widened = widened;
    }

    protected void setNestedEntries(ByteCode byteCode, OperandManager operandManager, int offset) throws Pack200Exception {
        if (offset != 0) {
            super.setNestedEntries(byteCode, operandManager, offset - 1);
            return;
        }
        final SegmentConstantPool globalPool = operandManager.globalConstantPool();
        ClassFileEntry[] nested = null;
        nested = new ClassFileEntry[] { globalPool.getClassPoolEntry(operandManager.getCurrentClass()) };
        byteCode.setNested(nested);
        byteCode.setNestedPositions(new int[][] { { 0, 2 } });
    }

    protected int getOffset(OperandManager operandManager) {
        return operandManager.nextClassRef();
    }

    protected int getPoolID() {
        return SegmentConstantPool.CP_CLASS;
    }
}

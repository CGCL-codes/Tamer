package gov.nasa.jpf.concolic.bytecode;

import gov.nasa.jpf.jvm.KernelState;
import gov.nasa.jpf.jvm.StackFrame;
import gov.nasa.jpf.jvm.SystemState;
import gov.nasa.jpf.jvm.ThreadInfo;
import gov.nasa.jpf.jvm.bytecode.Instruction;
import gov.nasa.jpf.symbc.numeric.IntegerExpression;

public class IXOR extends gov.nasa.jpf.jvm.bytecode.IXOR {

    @Override
    public Instruction execute(SystemState ss, KernelState ks, ThreadInfo th) {
        StackFrame sf = th.getTopFrame();
        IntegerExpression sym_v1 = (IntegerExpression) sf.getOperandAttr();
        int v1 = th.pop();
        IntegerExpression sym_v2 = (IntegerExpression) sf.getOperandAttr();
        int v2 = th.pop();
        th.push(v1 ^ v2, false);
        if (sym_v1 != null || sym_v2 != null) {
            throw new UnsupportedOperationException("## Error: SYMBOLIC IXOR not supported");
        }
        return getNext(th);
    }
}

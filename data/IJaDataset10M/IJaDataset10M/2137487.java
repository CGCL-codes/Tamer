package gov.nasa.jpf.concolic.bytecode;

import gov.nasa.jpf.jvm.KernelState;
import gov.nasa.jpf.jvm.StackFrame;
import gov.nasa.jpf.jvm.SystemState;
import gov.nasa.jpf.jvm.ThreadInfo;
import gov.nasa.jpf.jvm.bytecode.Instruction;
import gov.nasa.jpf.symbc.numeric.RealExpression;

public class FCMPL extends gov.nasa.jpf.jvm.bytecode.FCMPL {

    @Override
    public Instruction execute(SystemState ss, KernelState ks, ThreadInfo th) {
        StackFrame sf = th.getTopFrame();
        RealExpression sym_v1 = (RealExpression) sf.getOperandAttr(0);
        RealExpression sym_v2 = (RealExpression) sf.getOperandAttr(1);
        if (sym_v1 == null && sym_v2 == null) {
            return super.execute(ss, ks, th);
        } else {
            throw new UnsupportedOperationException("## Error: SYMBOLIC " + getClass().getSimpleName() + "  not supported");
        }
    }
}

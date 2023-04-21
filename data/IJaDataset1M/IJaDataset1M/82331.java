package gov.nasa.jpf.concolic.bytecode;

import gov.nasa.jpf.jvm.KernelState;
import gov.nasa.jpf.jvm.StackFrame;
import gov.nasa.jpf.jvm.SystemState;
import gov.nasa.jpf.jvm.ThreadInfo;
import gov.nasa.jpf.jvm.Types;
import gov.nasa.jpf.jvm.bytecode.Instruction;
import gov.nasa.jpf.symbc.numeric.RealExpression;

/**
 * Divide 2 doubles
 * ..., value1, value2 => ..., value2/value1
 */
public class DDIV extends gov.nasa.jpf.jvm.bytecode.DDIV {

    public Instruction execute(SystemState ss, KernelState ks, ThreadInfo th) {
        StackFrame sf = th.getTopFrame();
        RealExpression sym_v1 = (RealExpression) sf.getLongOperandAttr();
        double v1 = Types.longToDouble(th.longPop());
        RealExpression sym_v2 = (RealExpression) sf.getLongOperandAttr();
        double v2 = Types.longToDouble(th.longPop());
        double r = v2 / v1;
        th.longPush(Types.doubleToLong(r));
        RealExpression result;
        if (sym_v1 != null) {
            if (sym_v2 != null) result = sym_v2._div(sym_v1); else result = sym_v1._div_reverse(v2);
        } else if (sym_v2 != null) result = sym_v2._div(v1); else result = null;
        sf.setLongOperandAttr(result);
        return getNext(th);
    }
}

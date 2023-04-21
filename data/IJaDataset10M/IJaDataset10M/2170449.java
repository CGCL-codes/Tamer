package gov.nasa.jpf.numeric.bytecode;

import gov.nasa.jpf.jvm.KernelState;
import gov.nasa.jpf.jvm.SystemState;
import gov.nasa.jpf.jvm.ThreadInfo;
import gov.nasa.jpf.jvm.Types;
import gov.nasa.jpf.jvm.bytecode.Instruction;
import gov.nasa.jpf.numeric.NumericUtils;

/**
 * Multiply float
 * ..., value1, value2 => ..., result
 */
public class FMUL extends gov.nasa.jpf.jvm.bytecode.FMUL {

    public Instruction execute(SystemState ss, KernelState ks, ThreadInfo th) {
        float v1 = Types.intToFloat(th.pop());
        float v2 = Types.intToFloat(th.pop());
        float r = v1 * v2;
        if (!NumericUtils.checkNaN(r)) {
            return th.createAndThrowException("java.lang.ArithmeticException", "inexact result: " + v2 + "*" + v1 + "=" + r);
        }
        th.push(Types.floatToInt(r), false);
        return getNext(th);
    }
}

package gov.nasa.jpf.jvm.bytecode;

import gov.nasa.jpf.jvm.KernelState;
import gov.nasa.jpf.jvm.SystemState;
import gov.nasa.jpf.jvm.ThreadInfo;
import gov.nasa.jpf.jvm.Types;
import org.apache.bcel.classfile.ConstantPool;

/**
 * Convert int to double
 * ..., value => ..., result
 */
public class I2D extends Instruction {

    public void setPeer(org.apache.bcel.generic.Instruction i, ConstantPool cp) {
    }

    public Instruction execute(SystemState ss, KernelState ks, ThreadInfo th) {
        int ival = th.pop();
        double dval = (double) ival;
        th.longPush(Types.doubleToLong(dval));
        return getNext(th);
    }

    public int getByteCode() {
        return 0x87;
    }
}

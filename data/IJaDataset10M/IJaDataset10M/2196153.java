package gov.nasa.jpf.jvm.bytecode;

import gov.nasa.jpf.jvm.KernelState;
import gov.nasa.jpf.jvm.SystemState;
import gov.nasa.jpf.jvm.ThreadInfo;
import org.apache.bcel.classfile.ConstantPool;

/**
 * Throw exception or error
 * ..., objectref => objectref
 */
public class ATHROW extends Instruction {

    public void setPeer(org.apache.bcel.generic.Instruction i, ConstantPool cp) {
    }

    public Instruction execute(SystemState ss, KernelState ks, ThreadInfo th) {
        int objref = th.pop();
        if (objref == -1) {
            return th.createAndThrowException("java.lang.NullPointerException");
        }
        return th.throwException(objref);
    }

    public int getByteCode() {
        return 0xBF;
    }
}

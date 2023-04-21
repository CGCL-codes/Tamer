package gov.nasa.jpf.jvm.bytecode;

import gov.nasa.jpf.jvm.KernelState;
import gov.nasa.jpf.jvm.SystemState;
import gov.nasa.jpf.jvm.ThreadInfo;
import org.apache.bcel.classfile.ConstantPool;

/**
 * Jump subroutine
 * ... => ..., address
 */
public class JSR extends Instruction {

    private int target;

    public void setPeer(org.apache.bcel.generic.Instruction i, ConstantPool cp) {
        target = ((org.apache.bcel.generic.JSR) i).getTarget().getPosition();
    }

    public Instruction execute(SystemState ss, KernelState ks, ThreadInfo th) {
        th.push(getNext(th).getPosition(), false);
        return th.getMethod().getInstructionAt(target);
    }

    public int getLength() {
        return 3;
    }

    public int getByteCode() {
        return 0xA8;
    }
}

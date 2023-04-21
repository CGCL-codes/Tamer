package takatuka.verifier.dataObjs;

import takatuka.classreader.dataObjs.MethodInfo;
import takatuka.optimizer.cpGlobalization.logic.util.Oracle;
import takatuka.verifier.dataObjs.attribute.VerificationInstruction;

/**
 * <p>Title: </p>
 * <p>Description:
 *
 * </p>
 * @author Faisal Aslam
 * @version 1.0
 */
public class StackMaxInfo {

    public int maxSize = 0;

    public VerificationInstruction instr = null;

    public String methodStr = null;

    public StackMaxInfo(MethodInfo method, int maxSize, VerificationInstruction instr) {
        this.maxSize = maxSize;
        this.instr = instr;
        this.methodStr = Oracle.getInstanceOf().getMethodOrFieldString(method);
    }

    @Override
    public String toString() {
        String ret = "";
        ret += "Method=" + methodStr + ", Max-Stack=" + maxSize + ", Instruction = " + instr + "\n";
        return ret;
    }
}

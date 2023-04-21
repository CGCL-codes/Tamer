package gov.nasa.jpf.delayed.cp.bytecode;

import gov.nasa.jpf.delayed.cp.CPDelayedStateExtensionClient;
import gov.nasa.jpf.delayed.cp.attr.CPDelayedAttr;
import gov.nasa.jpf.delayed.state.DelayedCG;
import gov.nasa.jpf.jvm.ChoiceGenerator;
import gov.nasa.jpf.jvm.KernelState;
import gov.nasa.jpf.jvm.StackFrame;
import gov.nasa.jpf.jvm.SystemState;
import gov.nasa.jpf.jvm.ThreadInfo;
import gov.nasa.jpf.jvm.bytecode.Instruction;

/**
 * 
 * @author Milos Gligoric (milos.gligoric@gmail.com)
 * @author Tihomir Gvero (tihomir.gvero@gmail.com)
 * 
 */
public class IFGE extends gov.nasa.jpf.jvm.bytecode.IFGE {

    @Override
    public Instruction execute(SystemState ss, KernelState ks, ThreadInfo ti) {
        StackFrame sf = ti.getTopFrame();
        Integer key = (Integer) sf.getOperandAttr();
        if (key == null) {
            return super.execute(ss, ks, ti);
        } else {
            ChoiceGenerator<?> cg;
            CPDelayedAttr wattr = CPDelayedStateExtensionClient.inst().getAttrState(key);
            if (!wattr.isInit() && !ti.isFirstStepInsn()) {
                cg = wattr.createChoiceGenerator();
                ss.setNextChoiceGenerator(cg);
                return this;
            } else {
                if (ti.isFirstStepInsn()) {
                    cg = ss.getChoiceGenerator();
                    assert (cg instanceof DelayedCG);
                    wattr.setValue((Integer) cg.getNextChoice());
                }
                ti.pop();
                ti.push(wattr.getValue(), false);
                return this;
            }
        }
    }
}

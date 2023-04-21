package gov.nasa.jpf.delayed.cp.bytecode;

import gov.nasa.jpf.delayed.cp.CPDelayedStateExtensionClient;
import gov.nasa.jpf.delayed.cp.attr.CPDelayedAttr;
import gov.nasa.jpf.delayed.state.DelayedCG;
import gov.nasa.jpf.jvm.KernelState;
import gov.nasa.jpf.jvm.SystemState;
import gov.nasa.jpf.jvm.ThreadInfo;
import gov.nasa.jpf.jvm.bytecode.Instruction;
import gov.nasa.jpf.jvm.StackFrame;
import gov.nasa.jpf.jvm.ChoiceGenerator;

/**
 * Convert int to short ..., value => ..., result
 * 
 * @author Milos Gligoric (milos.gligoric@gmail.com)
 * @author Tihomir Gvero (tihomir.gvero@gmail.com)
 * 
 */
public class I2S extends gov.nasa.jpf.jvm.bytecode.I2S {

    @Override
    public Instruction execute(SystemState ss, KernelState ks, ThreadInfo ti) {
        StackFrame sf = ti.getTopFrame();
        Integer key = (Integer) sf.getOperandAttr();
        if (key == null) {
            return super.execute(ss, ks, ti);
        } else {
            CPDelayedAttr wattr = CPDelayedStateExtensionClient.inst().getAttrState(key);
            ChoiceGenerator<?> cg;
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
                ti.push((short) wattr.getValue(), false);
                return getNext(ti);
            }
        }
    }
}

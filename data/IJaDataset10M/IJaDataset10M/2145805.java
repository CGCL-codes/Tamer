package gov.nasa.jpf.delayed.cp.bytecode;

import gov.nasa.jpf.delayed.cp.CPDelayedStateExtensionClient;
import gov.nasa.jpf.delayed.cp.attr.CPDelayedAttr;
import gov.nasa.jpf.delayed.state.DelayedCG;
import gov.nasa.jpf.jvm.ChoiceGenerator;
import gov.nasa.jpf.jvm.KernelState;
import gov.nasa.jpf.jvm.SystemState;
import gov.nasa.jpf.jvm.ThreadInfo;
import gov.nasa.jpf.jvm.bytecode.Instruction;
import gov.nasa.jpf.jvm.StackFrame;

/**
 * Here attr1 refers to operand2 and attr2 to operand1
 * 
 * @author Milos Gligoric (milos.gligoric@gmail.com)
 * @author Tihomir Gvero (tihomir.gvero@gmail.com)
 * 
 */
public class IREM extends gov.nasa.jpf.jvm.bytecode.IREM {

    @Override
    public Instruction execute(SystemState ss, KernelState ks, ThreadInfo ti) {
        StackFrame sf = ti.getTopFrame();
        Integer key1 = (Integer) sf.getOperandAttr(1);
        Integer key2 = (Integer) sf.getOperandAttr(0);
        if (key1 == null && key2 == null) {
            return super.execute(ss, ks, ti);
        } else {
            ChoiceGenerator<?> cg;
            if (key1 != null) {
                CPDelayedAttr wattr1 = CPDelayedStateExtensionClient.inst().getAttrState(key1);
                if (!wattr1.isInit() && !ti.isFirstStepInsn()) {
                    cg = wattr1.createChoiceGenerator();
                    ss.setNextChoiceGenerator(cg);
                    return this;
                } else {
                    if (ti.isFirstStepInsn()) {
                        cg = ss.getChoiceGenerator();
                        assert (cg instanceof DelayedCG);
                        wattr1.setValue((Integer) cg.getNextChoice());
                    }
                    int v2 = ti.pop();
                    int v1 = ti.pop();
                    v1 = wattr1.getValue();
                    ti.push(v1, false);
                    ti.push(v2, false);
                    ti.setOperandAttrNoClone(key2);
                    return this;
                }
            }
            if (key2 != null) {
                CPDelayedAttr wattr2 = CPDelayedStateExtensionClient.inst().getAttrState(key2);
                if (!wattr2.isInit() && !ti.isFirstStepInsn()) {
                    cg = wattr2.createChoiceGenerator();
                    ss.setNextChoiceGenerator(cg);
                    return this;
                } else {
                    if (ti.isFirstStepInsn()) {
                        cg = ss.getChoiceGenerator();
                        assert (cg instanceof DelayedCG);
                        wattr2.setValue((Integer) cg.getNextChoice());
                    }
                    ti.pop();
                    ti.push(wattr2.getValue(), false);
                    return this;
                }
            }
            return null;
        }
    }
}

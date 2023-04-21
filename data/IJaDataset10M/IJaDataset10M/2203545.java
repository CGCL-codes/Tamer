package gov.nasa.jpf.jvm.bytecode;

import gov.nasa.jpf.jvm.ChoiceGenerator;
import gov.nasa.jpf.jvm.ElementInfo;
import gov.nasa.jpf.jvm.KernelState;
import gov.nasa.jpf.jvm.StackFrame;
import gov.nasa.jpf.jvm.SystemState;
import gov.nasa.jpf.jvm.ThreadInfo;

/**
 * abstraction for the various return instructions
 */
public abstract class ReturnInstruction extends Instruction {

    protected StackFrame returnFrame;

    protected abstract void storeReturnValue(ThreadInfo th);

    protected abstract void pushReturnValue(ThreadInfo th);

    public abstract Object getReturnValue(ThreadInfo ti);

    public StackFrame getReturnFrame() {
        return returnFrame;
    }

    public void setReturnFrame(StackFrame frame) {
        returnFrame = frame;
    }

    public Object getReturnAttr(ThreadInfo ti) {
        return ti.getOperandAttr();
    }

    public void setReturnAttr(ThreadInfo ti, Object attr) {
        if (attr != null) {
            ti.setOperandAttrNoClone(attr);
        }
    }

    public Instruction execute(SystemState ss, KernelState ks, ThreadInfo ti) {
        if (!ti.isFirstStepInsn()) {
            mi.leave(ti);
            if (mi.isSynchronized()) {
                int objref = ti.getThis();
                ElementInfo ei = ks.da.get(objref);
                ChoiceGenerator<ThreadInfo> cg = ss.getSchedulerFactory().createSyncMethodExitCG(ei, ti);
                if (cg != null) {
                    ss.setNextChoiceGenerator(cg);
                    ti.skipInstructionLogging();
                    return this;
                }
            }
        }
        Object attr = getReturnAttr(ti);
        storeReturnValue(ti);
        returnFrame = ti.getTopFrame();
        if (ti.getStackDepth() == 1) {
            int objref = ti.getThreadObjectRef();
            ElementInfo ei = ks.da.get(objref);
            if (!ei.canLock(ti)) {
                ei.block(ti);
                ChoiceGenerator<ThreadInfo> cg = ss.getSchedulerFactory().createMonitorEnterCG(ei, ti);
                if (cg != null) {
                    ss.setNextChoiceGenerator(cg);
                }
                return this;
            } else {
                ei.lock(ti);
                ei.notifiesAll();
                ei.unlock(ti);
                ti.finish();
                ChoiceGenerator<ThreadInfo> cg = ss.getSchedulerFactory().createThreadTerminateCG(ti);
                if (cg != null) {
                    ss.clearAtomic();
                    ss.setNextChoiceGenerator(cg);
                }
                ti.popFrame();
                return null;
            }
        } else {
            ti.popFrame();
            Instruction nextPC = ti.getReturnFollowOnPC();
            if (nextPC != ti.getPC()) {
                ti.removeArguments(mi);
                pushReturnValue(ti);
                if (attr != null) {
                    setReturnAttr(ti, attr);
                }
            }
            return nextPC;
        }
    }
}

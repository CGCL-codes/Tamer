package gov.nasa.jpf.rtembed.restrictpar.bytecode;

import gov.nasa.jpf.jvm.MethodInfo;
import gov.nasa.jpf.jvm.ThreadInfo;
import gov.nasa.jpf.jvm.ElementInfo;
import gov.nasa.jpf.jvm.KernelState;
import gov.nasa.jpf.jvm.SystemState;
import gov.nasa.jpf.jvm.ChoiceGenerator;
import gov.nasa.jpf.jvm.SchedulerFactory;
import gov.nasa.jpf.jvm.bytecode.Instruction;
import gov.nasa.jpf.rtembed.restrictpar.PlatformSpecificSchedulerFactory;

public class INVOKEINTERFACE extends gov.nasa.jpf.jvm.bytecode.INVOKEINTERFACE {

    public Instruction execute(SystemState ss, KernelState ks, ThreadInfo ti) {
        int objRef = ti.getCalleeThis(getArgSize());
        if (objRef == -1) return ti.createAndThrowException("java.lang.NullPointerException", "calling '" + mname + "' on null object");
        MethodInfo mi = getInvokedMethod(ti, objRef);
        if (mi == null) return ti.createAndThrowException("java.lang.NoSuchMethodException", ti.getClassInfo(objRef).getName() + "." + mname);
        if (mi.isSynchronized()) {
            ChoiceGenerator<?> cg = getSyncCG(objRef, mi, ss, ks, ti);
            if (cg != null) {
                ss.setNextChoiceGenerator(cg);
                return this;
            }
        } else if (isNormalMethod(mi)) {
            ChoiceGenerator<?> cg = getInvokeCG(objRef, ss, ks, ti);
            if (cg != null) {
                ss.setNextChoiceGenerator(cg);
                return this;
            }
        }
        return mi.execute(ti);
    }

    protected ChoiceGenerator<?> getInvokeCG(int objRef, SystemState ss, KernelState ks, ThreadInfo ti) {
        ElementInfo ei = ks.da.get(objRef);
        if (!ti.isFirstStepInsn()) {
            SchedulerFactory sf = ss.getSchedulerFactory();
            if (sf instanceof PlatformSpecificSchedulerFactory) {
                PlatformSpecificSchedulerFactory pssf = (PlatformSpecificSchedulerFactory) sf;
                ChoiceGenerator<ThreadInfo> cg = pssf.createMethodBoundaryCG(ei, ti);
                return cg;
            }
        }
        return null;
    }

    protected boolean isNormalMethod(MethodInfo mi) {
        String className = mi.getClassName();
        if (className.startsWith("java.lang.Thread")) return false;
        String fullMethodName = mi.getBaseName();
        if (fullMethodName.startsWith("java.lang.Object.wait")) return false;
        if (fullMethodName.startsWith("java.lang.Object.notify")) return false;
        return true;
    }
}

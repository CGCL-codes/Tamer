package org.jikesrvm.debug;

import org.jikesrvm.ArchitectureSpecific;
import org.jikesrvm.Callbacks;
import org.jikesrvm.Constants;
import org.jikesrvm.UnimplementedError;
import org.jikesrvm.VM;
import org.jikesrvm.ArchitectureSpecific.CodeArray;
import org.jikesrvm.ArchitectureSpecific.Registers;
import org.jikesrvm.classloader.NormalMethod;
import org.jikesrvm.classloader.RVMClass;
import org.jikesrvm.classloader.RVMMethod;
import org.jikesrvm.compilers.baseline.BaselineCompiledMethod;
import org.jikesrvm.compilers.common.CompiledMethod;
import org.jikesrvm.compilers.common.CompiledMethods;
import org.jikesrvm.compilers.opt.runtimesupport.OptCompiledMethod;
import org.jikesrvm.debug.RVMDebug.EventType;
import org.jikesrvm.jni.JNICompiledMethod;
import org.jikesrvm.runtime.Magic;
import org.jikesrvm.runtime.RuntimeEntrypoints;
import org.jikesrvm.scheduler.RVMThread;
import org.jikesrvm.scheduler.Scheduler;
import org.jikesrvm.util.LinkedListRVM;
import org.jikesrvm.runtime.Magic;
import org.vmmagic.unboxed.Address;
import org.vmmagic.unboxed.Offset;
import org.vmmagic.pragma.Entrypoint;
import org.vmmagic.pragma.BaselineSaveLSRegisters;
import org.vmmagic.pragma.NoOptCompile;

/**
 * A bytecode level Single step implementation.
 */
public class SingleStep {

    private static final boolean DEBUG = false;

    /** Note that the call to this method is asynchronous. */
    @BaselineSaveLSRegisters
    @NoOptCompile
    @Entrypoint
    public static void singleStepHit() {
        final NormalMethod method;
        final int bcIndex;
        VM.disableGC();
        Address ip = Magic.getReturnAddress(Magic.getFramePointer());
        Address fp = Magic.getCallerFramePointer(Magic.getFramePointer());
        int cmid = Magic.getCompiledMethodID(fp);
        CompiledMethod cm = CompiledMethods.getCompiledMethod(cmid);
        if (cm instanceof BaselineCompiledMethod) {
            BaselineCompiledMethod bcm = (BaselineCompiledMethod) cm;
            Offset ip_offset = cm.getInstructionOffset(ip);
            bcIndex = bcm.findBytecodeIndexForInstruction(ip_offset);
            method = (NormalMethod) bcm.getMethod();
        } else {
            bcIndex = -1;
            method = null;
            if (VM.VerifyAssertions) {
                VM._assert(false, "Unexpected caller to the single step hit handler");
            }
        }
        VM.enableGC();
        if (VM.VerifyAssertions) {
            VM._assert(method != null && bcIndex >= 0, "a break point trap from unknown source");
        }
        if (DEBUG) {
            VM.sysWrite("singleStepHit: location found ", bcIndex);
            VM.sysWrite(" in ");
            VM.sysWrite(method.getDeclaringClass().getDescriptor());
            ;
            VM.sysWrite(".");
            VM.sysWrite(method.getName());
            VM.sysWrite(method.getDescriptor());
            VM.sysWriteln("");
        }
        if (Scheduler.getCurrentThread().isSystemThread()) {
            if (JikesRVMJDWP.getVerbose() >= 3) {
                VM.sysWriteln("skipping a system thread's break point hit: bcindex ", bcIndex, method.toString());
            }
        } else {
            try {
                EventNotifier notifier = RVMDebug.getRVMDebug().eventNotifier;
                RVMThread thread = Scheduler.getCurrentThread();
                notifier.notifySingleStep(thread, method, bcIndex);
            } catch (Exception t) {
                t.printStackTrace();
                VM._assert(false, "The break point call back should handle all the exceptions.");
            }
        }
    }

    public static void setSingleStep(RVMThread t) {
        t.singleStep = true;
    }

    public static void clearSingleStep(RVMThread t) {
        t.singleStep = false;
    }
}

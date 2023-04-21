package org.jikesrvm.osr;

import org.jikesrvm.VM;
import org.jikesrvm.VM_Callbacks;
import org.jikesrvm.adaptive.controller.VM_Controller;
import org.jikesrvm.adaptive.controller.VM_ControllerMemory;
import org.jikesrvm.adaptive.controller.VM_ControllerPlan;
import org.jikesrvm.adaptive.recompilation.VM_InvocationCounts;
import org.jikesrvm.adaptive.util.VM_AOSLogging;
import org.jikesrvm.adaptive.util.VM_CompilerAdviceAttribute;
import org.jikesrvm.compilers.common.VM_CompiledMethod;
import org.jikesrvm.compilers.common.VM_CompiledMethods;
import org.jikesrvm.compilers.common.VM_RuntimeCompiler;
import org.jikesrvm.compilers.opt.driver.CompilationPlan;
import org.jikesrvm.compilers.opt.runtimesupport.VM_OptCompiledMethod;

/**
 * Maintain statistic information about on stack replacement events
 */
public class OSR_Profiler implements VM_Callbacks.ExitMonitor {

    private static int invalidations = 0;

    private static boolean registered = false;

    public void notifyExit(int value) {
        VM.sysWriteln("OSR invalidations " + invalidations);
    }

    public static void notifyInvalidation(OSR_ExecutionState state) {
        if (!registered && VM.MeasureCompilation) {
            registered = true;
            VM_Callbacks.addExitMonitor(new OSR_Profiler());
        }
        if (VM.TraceOnStackReplacement || VM.MeasureCompilation) {
            OSR_Profiler.invalidations++;
        }
        while (state.callerState != null) {
            state = state.callerState;
        }
        invalidateState(state);
    }

    private static synchronized void invalidateState(OSR_ExecutionState state) {
        VM_CompiledMethod mostRecentlyCompiledMethod = VM_CompiledMethods.getCompiledMethod(state.cmid);
        if (VM.VerifyAssertions) {
            VM._assert(mostRecentlyCompiledMethod.getMethod() == state.meth);
        }
        if (mostRecentlyCompiledMethod != state.meth.getCurrentCompiledMethod()) {
            return;
        }
        if (!(mostRecentlyCompiledMethod instanceof VM_OptCompiledMethod)) {
            return;
        }
        state.meth.invalidateCompiledMethod(mostRecentlyCompiledMethod);
        if (VM.TraceOnStackReplacement) {
            VM.sysWriteln("OSR " + OSR_Profiler.invalidations + " : " + state.bcIndex + "@" + state.meth);
        }
        boolean recmplsucc = false;
        if (VM_Controller.enabled) {
            CompilationPlan cmplplan = null;
            if ((VM_Controller.options.ENABLE_REPLAY_COMPILE || VM_Controller.options.ENABLE_PRECOMPILE) && VM_CompilerAdviceAttribute.hasAdvice()) {
                VM_CompilerAdviceAttribute attr = VM_CompilerAdviceAttribute.getCompilerAdviceInfo(state.meth);
                if (VM.VerifyAssertions) {
                    VM._assert(attr.getCompiler() == VM_CompiledMethod.OPT);
                }
                if (VM_Controller.options.counters()) {
                    cmplplan = VM_InvocationCounts.createCompilationPlan(state.meth);
                } else {
                    cmplplan = VM_Controller.recompilationStrategy.createCompilationPlan(state.meth, attr.getOptLevel(), null);
                }
            } else {
                VM_ControllerPlan ctrlplan = VM_ControllerMemory.findMatchingPlan(mostRecentlyCompiledMethod);
                if (ctrlplan != null) {
                    cmplplan = ctrlplan.getCompPlan();
                }
            }
            if (cmplplan != null) {
                if (VM.VerifyAssertions) {
                    VM._assert(cmplplan.getMethod() == state.meth);
                }
                boolean savedOsr = cmplplan.options.OSR_GUARDED_INLINING;
                cmplplan.options.OSR_GUARDED_INLINING = false;
                int newcmid = VM_RuntimeCompiler.recompileWithOpt(cmplplan);
                cmplplan.options.OSR_GUARDED_INLINING = savedOsr;
                if (newcmid != -1) {
                    VM_AOSLogging.debug("recompiling state with opt succeeded " + state.cmid);
                    VM_AOSLogging.debug("new cmid " + newcmid);
                    double oldSamples = VM_Controller.methodSamples.getData(state.cmid);
                    VM_Controller.methodSamples.reset(state.cmid);
                    VM_Controller.methodSamples.augmentData(newcmid, oldSamples);
                    recmplsucc = true;
                    if (VM.TraceOnStackReplacement) {
                        VM.sysWriteln("  recompile " + state.meth + " at -O" + cmplplan.options.getOptLevel());
                    }
                }
            }
        }
        if (!recmplsucc) {
            int newcmid = VM_RuntimeCompiler.recompileWithOpt(state.meth);
            if (newcmid == -1) {
                if (VM.TraceOnStackReplacement) {
                    VM.sysWriteln("  opt recompilation failed!");
                }
                state.meth.invalidateCompiledMethod(mostRecentlyCompiledMethod);
            }
        }
        if (VM.TraceOnStackReplacement) {
            VM.sysWriteln("  opt recompilation done!");
        }
    }
}

package com.ibm.JikesRVM.adaptive;

import com.ibm.JikesRVM.*;
import com.ibm.JikesRVM.opt.*;
import com.ibm.JikesRVM.OSR.*;

/**
 * A OSR_ControllerOnStackReplacementPlan is scheduled by VM_ControllerThread,
 * and executed by the VM_RecompilationThread.
 *
 * It has the suspended thread whose activation being replaced, 
 * and a compilation plan.
 *
 * The execution of this plan compiles the method, installs the new
 * code, and reschedule the thread.
 *
 * @author Feng Qian
 */
public class OSR_OnStackReplacementPlan implements VM_Constants {

    private double priority;

    private int CMID;

    private int whereFrom;

    private int tsFromFPoff;

    private int ypTakenFPoff;

    private byte status;

    private VM_Thread suspendedThread;

    private OPT_CompilationPlan compPlan;

    private int timeCreated;

    private int timeInitiated = -1;

    private int timeCompleted = -1;

    private double compilationCPUTime;

    public OSR_OnStackReplacementPlan(VM_Thread thread, OPT_CompilationPlan cp, int cmid, int source, int tsoff, int ypoff, double priority) {
        this.suspendedThread = thread;
        this.compPlan = cp;
        this.CMID = cmid;
        this.whereFrom = source;
        this.tsFromFPoff = tsoff;
        this.ypTakenFPoff = ypoff;
        this.priority = priority;
        this.status = VM_ControllerPlan.UNINITIALIZED;
    }

    public int getTimeInitiated() {
        return timeInitiated;
    }

    public void setTimeInitiated(int t) {
        timeInitiated = t;
    }

    public int getTimeCompleted() {
        return timeCompleted;
    }

    public void setTimeCompleted(int t) {
        timeCompleted = t;
    }

    public void setStatus(byte newStatus) {
        status = newStatus;
    }

    public void execute() {
        if (VM.LogAOSEvents) {
            VM_AOSLogging.logOsrEvent("OSR compiling " + compPlan.method);
        }
        VM_Thread cpThread = VM_Thread.getCurrentThread();
        setTimeInitiated(VM_Controller.controllerClock);
        double compileTime;
        {
            double now = VM_Time.now();
            cpThread.setCPUTotalTime(now - cpThread.getCPUStartTime());
            cpThread.setCPUStartTime(now);
            double start = cpThread.getCPUTotalTime();
            OSR_ExecStateExtractor extractor = null;
            VM_CompiledMethod cm = VM_CompiledMethods.getCompiledMethod(this.CMID);
            boolean invalidate = true;
            if (cm.getCompilerType() == VM_CompiledMethod.BASELINE) {
                extractor = new OSR_BaselineExecStateExtractor();
                invalidate = false;
            } else if (cm.getCompilerType() == VM_CompiledMethod.OPT) {
                extractor = new OSR_OptExecStateExtractor();
            } else {
                if (VM.VerifyAssertions) VM._assert(VM.NOT_REACHED);
                return;
            }
            OSR_ExecutionState state = extractor.extractState(suspendedThread, this.tsFromFPoff, this.ypTakenFPoff, CMID);
            if (invalidate) {
                VM_AOSLogging.debug("Invalidate cmid " + CMID);
                OSR_Profiler.notifyInvalidation(state);
            }
            VM_CompiledMethod newCM = OSR_SpecialCompiler.recompileState(state, invalidate);
            now = VM_Time.now();
            cpThread.setCPUTotalTime(now - cpThread.getCPUStartTime());
            cpThread.setCPUStartTime(now);
            double end = cpThread.getCPUTotalTime();
            compileTime = (end - start) * 1000.0;
            setTimeCompleted(VM_Controller.controllerClock);
            if (newCM == null) {
                setStatus(VM_ControllerPlan.ABORTED_COMPILATION_ERROR);
                if (VM.LogAOSEvents) VM_AOSLogging.logOsrEvent("OSR compilation failed!");
            } else {
                setStatus(VM_ControllerPlan.COMPLETED);
                OSR_CodeInstaller.install(state, newCM);
                if (VM.LogAOSEvents) VM_AOSLogging.logOsrEvent("OSR compilation succeded! " + compPlan.method);
            }
        }
        suspendedThread.resume();
        suspendedThread = null;
        compPlan = null;
        CMID = 0;
    }
}

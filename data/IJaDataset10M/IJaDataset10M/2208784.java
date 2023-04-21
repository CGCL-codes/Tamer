package com.ibm.JikesRVM.adaptive;

import com.ibm.JikesRVM.VM;
import com.ibm.JikesRVM.VM_Thread;
import com.ibm.JikesRVM.classloader.VM_Method;
import com.ibm.JikesRVM.classloader.VM_NormalMethod;
import com.ibm.JikesRVM.VM_CompiledMethod;
import com.ibm.JikesRVM.VM_RuntimeCompiler;
import com.ibm.JikesRVM.opt.*;
import java.io.*;

/**
 * This class provides advice file used by compile replay experiments
 * Right now this class is basically duplicate part of the VM_AOSLogging
 * class.
 *
 * @author Xianglong Huang
 */
public class VM_AOSGenerator {

    private static PrintStream log;

    private static boolean booted = false;

    private static boolean recording = false;

    /**
   * Return whether AOS logging has booted.
   * @return whether AOS logging has booted
   */
    public static boolean booted() {
        return booted;
    }

    /**
   * Called from VM_ControllerThread.run to initialize the logging subsystem
   */
    public static void boot() {
        VM.sysWrite("AOS generation booted\n");
        try {
            log = new PrintStream(new FileOutputStream(VM_Controller.options.COMPILATION_ADVICE_FILE_OUTPUT));
        } catch (IOException e) {
            VM.sysWrite("IOException caught in VM_AOSGenerator.java while trying to create and start log file.\n");
            VM.sysWrite("Please check for file permission problems\n");
        }
        booted = true;
        recording = false;
    }

    /**
   * This method logs the successful completion of an adaptively 
   * selected recompilation
   * @param plan the OPT_Compilation plan being executed.
   */
    public static void reCompilationWithOpt(OPT_CompilationPlan plan) {
        if (!booted) return;
        synchronized (log) {
            log.println(plan.method.getDeclaringClass().getDescriptor() + " " + plan.method.getName() + " " + plan.method.getDescriptor() + " 3 " + plan.options.getOptLevel());
        }
    }

    public static void baseCompilationCompleted(VM_CompiledMethod cm) {
        if (recording || (!booted)) return;
        synchronized (log) {
            recording = true;
            log.println(cm.getMethod().getDeclaringClass().getDescriptor() + " " + cm.getMethod().getName() + " " + cm.getMethod().getDescriptor() + " " + cm.getCompilerType() + " " + "-1");
            recording = false;
        }
    }
}

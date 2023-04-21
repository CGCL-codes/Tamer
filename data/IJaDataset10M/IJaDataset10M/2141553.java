package org.jikesrvm.adaptive;

import org.jikesrvm.VM;
import org.jikesrvm.VM_Thread;
import java.util.Enumeration;

/**
 * This class implements the controller thread.  This entity is the brains of 
 * the adaptive optimization system.  It communicates with the runtime 
 * measurements subsystem to instruct and gather profiling information.  
 * It also talks to the compilation threads to generate 
 *     a) instrumented executables;
 *     b) optimized executables; 
 *     c) static information about a method; or 
 *     d) all of the above.
 *
 *  @author Michael Hind
 *  @author David Grove
 *  @author Stephen Fink
 *  @author Peter Sweeney
 */
public class VM_ControllerThread extends VM_Thread {

    public String toString() {
        return "VM_ControllerThread";
    }

    /**
   * constructor
   * @param sentinel   An object to signal when up and running
   */
    VM_ControllerThread(Object sentinel) {
        this.sentinel = sentinel;
        makeDaemon(true);
    }

    private Object sentinel;

    /**
   * There are several ways in which a dcg organizer might
   * be created; keep track of it once it is created so that
   * we only create one instance of it.
   */
    private VM_DynamicCallGraphOrganizer dcgOrg;

    /**
   * This method is the entry point to the controller, it is called when
   * the controllerThread is created.
   */
    public void run() {
        VM_Controller.controllerThread = this;
        VM_AOSLogging.boot();
        if (VM_Controller.options.ENABLE_ADVICE_GENERATION) VM_AOSGenerator.boot();
        VM_AOSLogging.controllerStarted();
        createProfilers();
        if (!VM_Controller.options.ENABLE_RECOMPILATION) {
            controllerInitDone();
            VM.sysWriteln("AOS: In non-adaptive mode; controller thread exiting.");
            return;
        }
        if ((VM_Controller.options.ENABLE_REPLAY_COMPILE) || (VM_Controller.options.ENABLE_PRECOMPILE)) {
            VM_Controller.options.MAX_OPT_LEVEL = 2;
            if (VM_Controller.options.sampling()) {
                VM_Controller.recompilationStrategy.init();
            } else if (VM_Controller.options.counters()) {
                VM_InvocationCounts.init();
            }
            VM_Controller.osrOrganizer = new OSR_OrganizerThread();
            VM_Controller.osrOrganizer.start();
            createCompilationThread();
            controllerInitDone();
            createOrganizerThreads();
            VM.sysWriteln("AOS: In replay mode; controller thread only runs for OSR inlining.");
            while (true) {
                if (VM_Controller.options.EARLY_EXIT && VM_Controller.options.EARLY_EXIT_TIME < VM_Controller.controllerClock) {
                    VM_Controller.stop();
                }
                Object event = VM_Controller.controllerInputQueue.deleteMin();
                ((OSR_OnStackReplacementEvent) event).process();
            }
        }
        VM_CompilerDNA.init();
        createOrganizerThreads();
        createCompilationThread();
        if (VM_Controller.options.sampling()) {
            VM_Controller.recompilationStrategy.init();
        } else if (VM_Controller.options.counters()) {
            VM_InvocationCounts.init();
        }
        controllerInitDone();
        while (true) {
            if (VM_Controller.options.EARLY_EXIT && VM_Controller.options.EARLY_EXIT_TIME < VM_Controller.controllerClock) {
                VM_Controller.stop();
            }
            Object event = VM_Controller.controllerInputQueue.deleteMin();
            ((VM_ControllerInputEvent) event).process();
        }
    }

    private void controllerInitDone() {
        for (Enumeration<VM_Organizer> e = VM_Controller.organizers.elements(); e.hasMoreElements(); ) {
            VM_Organizer o = e.nextElement();
            o.start();
        }
        try {
            synchronized (sentinel) {
                sentinel.notify();
            }
        } catch (Exception e) {
            e.printStackTrace();
            VM.sysFail("Failed to start up controller subsystem");
        }
    }

    /**
   * Called when the controller thread is about to wait on 
   * VM_Controller.controllerInputQueue
   */
    public void aboutToWait() {
    }

    /**
   * Called when the controller thread is woken after waiting on 
   * VM_Controller.controllerInputQueue
   */
    public void doneWaiting() {
        VM_ControllerMemory.incrementNumAwoken();
    }

    /**
   *  Create the compilationThread and schedule it
   */
    private void createCompilationThread() {
        VM_CompilationThread ct = new VM_CompilationThread();
        VM_Controller.compilationThread = ct;
        ct.start();
    }

    /**
   * Create a dynamic call graph organizer of one doesn't already exist
   */
    private void createDynamicCallGraphOrganizer() {
        if (dcgOrg == null) {
            dcgOrg = new VM_DynamicCallGraphOrganizer(new VM_EdgeListener());
            VM_Controller.organizers.addElement(dcgOrg);
        }
    }

    /**
   * Create profiling entities that are independent of whether or not
   * adaptive recompilation is actually enabled.
   */
    private void createProfilers() {
        VM_AOSOptions opts = VM_Controller.options;
        if (opts.GATHER_PROFILE_DATA) {
            VM_Controller.organizers.addElement(new VM_AccumulatingMethodSampleOrganizer());
            createDynamicCallGraphOrganizer();
        }
    }

    /**
   *  Create the organizerThreads and schedule them
   */
    private void createOrganizerThreads() {
        VM_AOSOptions opts = VM_Controller.options;
        if (opts.sampling()) {
            VM_Controller.methodSamples = new VM_MethodCountData();
            VM_Controller.organizers.addElement(new VM_MethodSampleOrganizer(opts.FILTER_OPT_LEVEL));
            if (opts.ADAPTIVE_INLINING) {
                VM_Organizer decayOrganizer = new VM_DecayOrganizer(new VM_YieldCounterListener(opts.DECAY_FREQUENCY));
                VM_Controller.organizers.addElement(decayOrganizer);
                createDynamicCallGraphOrganizer();
            }
        }
        if ((!VM_Controller.options.ENABLE_REPLAY_COMPILE) && (!VM_Controller.options.ENABLE_PRECOMPILE)) {
            VM_Controller.osrOrganizer = new OSR_OrganizerThread();
            VM_Controller.osrOrganizer.start();
        }
    }

    /**
   * Final report
   */
    public static void report() {
        VM_AOSLogging.controllerCompleted();
    }
}

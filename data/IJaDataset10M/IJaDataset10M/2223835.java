package com.ibm.JikesRVM;

public class VM_JNIStartUp implements VM_SizeConstants, Runnable {

    static boolean trace = false;

    VM_Address argAddress;

    VM_Address externalJNIEnvAddress;

    int pthreadID;

    /**
   * Used for AttachCurrentThread to create a java.lang.Thread and associate 
   * it with an external OS thread
   * @param argAddress  address pointing to a struct parms in C defined in libjni.c
   *                    (requestType, JNIEnv ** and pthreadID)
   */
    public VM_JNIStartUp(VM_Address argAddress1) {
        argAddress = argAddress1;
        externalJNIEnvAddress = VM_Magic.getMemoryAddress(argAddress.add(BYTES_IN_ADDRESS));
        pthreadID = VM_Magic.getMemoryInt(argAddress.add(2 * BYTES_IN_ADDRESS));
        if (trace) System.out.println("VM_JNIStartUp: " + VM.addressAsHexString(argAddress) + ", JNIEnvAddr=" + VM.addressAsHexString(externalJNIEnvAddress) + ", pid=" + pthreadID);
    }

    public String toString() {
        return "VM_JNIStartUp";
    }

    /******************************************************************
   *   The run method is executed as the start up Java thread by
   * AttachCurrentThread.  This sets up the necessary data structures
   * to represent the external pthread by a new native VM_Processor.
   * The sequence is similar to the one in the main method, but with
   * some differences.
   * 
   *
   */
    public void run() {
        if (VM.TraceThreads) VM_Scheduler.trace("VM_JNIStartUp", "run");
        if (trace) {
            System.out.println("VM_JNIStartUp: Java thread " + VM.addressAsHexString(VM_Magic.objectAsAddress(VM_Thread.getCurrentThread())) + " attaching external pthread " + pthreadID + "\n");
            System.out.println("JNIEnv to be placed at " + VM.addressAsHexString(externalJNIEnvAddress));
        }
        VM_Processor currentVP = VM_Processor.getCurrentProcessor();
        VM_Thread.getCurrentThread().processorAffinity = currentVP;
        VM_JNICreateVMFinishThread cleanupThread = new VM_JNICreateVMFinishThread(VM_Thread.getCurrentThread(), externalJNIEnvAddress, currentVP);
        cleanupThread.start(currentVP.readyQueue);
        if (trace) {
            System.out.println("VM_JNIStartUp: AttachCurrentThread waiting for clean up thread");
        }
        while (!cleanupThread.ready) {
            VM_Thread.getCurrentThread().yield();
        }
        VM_Processor nativeVP = VM_Processor.createNativeProcessorForExistingOSThread(VM_Thread.getCurrentThread());
        VM_Thread.getCurrentThread().nativeAffinity = nativeVP;
        VM_JNIEnvironment.JNIFunctionPointers[(VM_Thread.getCurrentThread().getIndex() * 2) + 1] = nativeVP.vpStatusAddress.toInt();
        nativeVP.pthread_id = pthreadID;
        ++nativeVP.threadSwitchingEnabledCount;
        VM_JNIEnvironment myEnv = VM_Thread.getCurrentThread().getJNIEnv();
        myEnv.setFromNative(VM_Constants.STACKFRAME_SENTINEL_FP, nativeVP, VM_Magic.getThreadId());
        VM_Thread.getCurrentThread().returnAffinity = VM_Processor.getCurrentProcessor();
        if (trace) {
            System.out.println("VM_JNIStartUp: attach done, moving thread to native VM_Processor ... ");
        }
        VM_Thread.getCurrentThread().beingDispatched = true;
        cleanupThread.proceedToFinish = true;
        VM_Thread.morph();
        if (trace) {
            System.out.println("VM_JNIStartUp: attached thread terminated, " + Thread.currentThread().getName() + ", " + VM.addressAsHexString(VM_Magic.objectAsAddress(VM_Thread.getCurrentThread())));
        }
        if (VM_Processor.unregisterAttachedProcessor(nativeVP) == 0) VM.sysWrite("ERROR:  VM_Processor for JNI_CreateJavaVM was not registered\n");
        VM_Thread.terminate();
    }

    /******************************************************************
   *   The main method is executed as the start up program by
   * the JNICreateJavaVM call.  This sets up the data structures to 
   * represent the external pthread as a native VM_Processor
   * At the end of the set up sequence, this main Java thread places
   * itself as the active thread of the new native VM_Processor and
   * calls morph() to remove itself from the Java VM_Processor. From 
   * this point on, it is no longer executed but will reappear when
   * the external pthread make a JNI call.
   * @param arg[0] a string "-jni xxxx" where xxxx is the decimal address 
   *               of the JNIEnv pointer
   * @param arg[1] a string "-pid xxxx" where xxxx is the decimal pthread ID
   */
    public static void main(String[] args) {
        VM_Address externalJNIEnv = VM_Address.zero();
        int pthread_id = 0;
        if (trace) {
            System.out.println("VM_JNIStartUp: starting RVM ... ");
        }
        VM.runningAsSubsystem = true;
        VM_JNIEnvironment.boot();
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("-jni ")) {
                externalJNIEnv = VM_Address.fromLong(Long.valueOf(args[i].substring(5)).longValue());
                externalJNIEnv = VM_Address.fromIntZeroExtend(Integer.valueOf(args[i].substring(5)).intValue());
            }
            if (args[i].startsWith("-pid ")) pthread_id = Integer.valueOf(args[i].substring(5)).intValue();
        }
        if (externalJNIEnv.isZero()) {
            System.out.println("VM_JNIStartUp:  ERROR, external JNIEnv required for JNI_CreateJavaVM");
            System.exit(-1);
        } else if (pthread_id == 0) {
            System.out.println("VM_JNIStartUp:  ERROR, external phtread ID required for JNI_CreateJavaVM");
            System.exit(-1);
        }
        if (trace) {
            System.out.println("Main VM_Thread is " + VM.addressAsHexString(VM_Magic.objectAsAddress(VM_Thread.getCurrentThread())));
            System.out.println("env for external pthread is " + VM.addressAsHexString(externalJNIEnv));
            System.out.println("AttachRequest at " + VM.addressAsHexString(VM_Magic.getTocPointer().add(VM_BootRecord.the_boot_record.attachThreadRequestedOffset)));
        }
        VM_Processor currentVP = VM_Processor.getCurrentProcessor();
        VM_Thread.getCurrentThread().processorAffinity = currentVP;
        VM_Thread j = new JNIServiceThread();
        j.start();
        while (VM_Scheduler.attachThreadQueue.isEmpty()) {
            VM_Thread.getCurrentThread().yield();
        }
        if (trace) System.out.println("VM_JNIStartUp: JNIServiceThread started");
        VM_JNICreateVMFinishThread cleanupThread = new VM_JNICreateVMFinishThread(VM_Thread.getCurrentThread(), externalJNIEnv, currentVP);
        cleanupThread.start(currentVP.readyQueue);
        if (trace) System.out.println("VM_JNIStartUp: waiting for clean up thread");
        while (!cleanupThread.ready) {
            VM_Thread.getCurrentThread().yield();
        }
        VM_Processor nativeVP = VM_Thread.getCurrentThread().nativeAffinity;
        if (nativeVP == null) {
            nativeVP = VM_Processor.createNativeProcessorForExistingOSThread(VM_Thread.getCurrentThread());
            VM_Thread.getCurrentThread().nativeAffinity = nativeVP;
            VM_JNIEnvironment.JNIFunctionPointers[(VM_Thread.getCurrentThread().getIndex() * 2) + 1] = nativeVP.vpStatusAddress.toInt();
        }
        nativeVP.pthread_id = pthread_id;
        ++nativeVP.threadSwitchingEnabledCount;
        VM_JNIEnvironment myEnv = VM_Thread.getCurrentThread().getJNIEnv();
        myEnv.setFromNative(VM_Constants.STACKFRAME_SENTINEL_FP, nativeVP, VM_Magic.getThreadId());
        VM_Thread.getCurrentThread().returnAffinity = VM_Processor.getCurrentProcessor();
        VM_BootRecord.the_boot_record.bootCompleted = 1;
        if (trace) System.out.println("VM_JNIStartUp: RVM is ready, main thread moving to native VM_Processor ... ");
        VM_Thread.getCurrentThread().beingDispatched = true;
        cleanupThread.proceedToFinish = true;
        VM_Thread.morph();
        if (trace) {
            System.out.println("VM_JNIStartUp: main Java thread terminated, " + Thread.currentThread().getName() + ", " + VM.addressAsHexString(VM_Magic.objectAsAddress(VM_Thread.getCurrentThread())));
        }
        while ((VM_Scheduler.numActiveThreads - VM_Scheduler.numDaemons) > 1) {
            VM_Thread.yield();
        }
        if (VM_Processor.unregisterAttachedProcessor(nativeVP) == 0) VM.sysWrite("ERROR:  VM_Processor for JNI_CreateJavaVM was not registered\n");
        VM_Thread.terminate();
    }
}

package com.ibm.JikesRVM.memoryManagers.vmInterface;

import com.ibm.JikesRVM.memoryManagers.watson.*;
import com.ibm.JikesRVM.VM;
import com.ibm.JikesRVM.VM_Address;
import com.ibm.JikesRVM.VM_Magic;
import com.ibm.JikesRVM.VM_PragmaInline;
import com.ibm.JikesRVM.VM_PragmaNoInline;
import com.ibm.JikesRVM.VM_PragmaUninterruptible;
import com.ibm.JikesRVM.VM_PragmaLogicallyUninterruptible;
import com.ibm.JikesRVM.VM_Processor;
import com.ibm.JikesRVM.VM_Scheduler;
import com.ibm.JikesRVM.VM_Thread;
import com.ibm.JikesRVM.VM_Memory;
import com.ibm.JikesRVM.VM_Time;
import com.ibm.JikesRVM.VM_Synchronizer;

/**
 * This class manages finalization.  When an object is created
 * if its class has a finalize() method, addElement below is 
 * called, and a VM_FinalizerListElement (see VM_FinalizerListElement)
 * is created for it.  While the object is old, the integer field of 
 * the list element holds its value (this does not keep the object 
 * live during gc.  At the end of gc, the list of VM_FinalizerListElements
 * is scanned for objects which have become garbage.  Those which have
 * are made live again, by setting the ref field of the VM_FLE to the
 * object's address, and the VM_FLE is moved from the live objects list
 * to the to-be-finalized objects list.  A distinguished thread, the
 * Finalizer thread (see FinalizerThread.java) enqueues itself on 
 * the VM_Scheduler finalizerQueue.  At the end of gc, if VM_FLEs have 
 * been added to the to-be-finalized list, and if the VM_Scheduler
 * finalizerQueue is not empty, the finalizer thread is scheduled to be
 * run when gc is completed.
 *
 * @author Dick Attanasio
 * @author Stephen Smith
 */
public class VM_Finalizer {

    private static int INITIAL_SIZE = 16384;

    private static VM_Synchronizer locker = new VM_Synchronizer();

    private static int[] candidate = new int[INITIAL_SIZE];

    private static int candidateEnd;

    private static Object[] live = new Object[INITIAL_SIZE];

    private static int liveStart;

    private static int liveEnd;

    private static final boolean TRACE = false;

    private static final boolean TRACE_DETAIL = false;

    public static final void addCandidate(Object item) throws VM_PragmaNoInline, VM_PragmaUninterruptible {
        synchronized (locker) {
            if (TRACE_DETAIL) VM_Scheduler.trace(" VM_Finalizer: ", " addElement	called, count = ", candidateEnd);
            if (candidateEnd >= candidate.length) {
                VM.sysWriteln("finalizer queue exceeded - increase size or implement dynamic adjustment a la write buffer");
                VM._assert(false);
            }
            candidate[candidateEnd++] = VM_Magic.objectAsAddress(item).toInt();
        }
    }

    private static final void compactCandidates() {
        int leftCursor = 0;
        int rightCursor = candidateEnd - 1;
        while (true) {
            while (leftCursor < rightCursor && candidate[leftCursor] != 0) leftCursor++;
            while (rightCursor > leftCursor && candidate[rightCursor] == 0) rightCursor--;
            if (leftCursor >= rightCursor) break;
            if (VM.VerifyAssertions) VM._assert(candidate[leftCursor] == 0);
            if (VM.VerifyAssertions) VM._assert(candidate[rightCursor] != 0);
            candidate[leftCursor] = candidate[rightCursor];
            candidate[rightCursor] = 0;
        }
        if (candidate[leftCursor] == 0) candidateEnd = leftCursor; else candidateEnd = leftCursor + 1;
    }

    private static void addLive(Object obj) {
        if (liveEnd == live.length) {
            if (liveStart == 0) {
                VM.sysWriteln("finalizer's live queue exceeded - increase size or implement dynamic adjustment a la write buffer");
                VM._assert(false);
            }
            for (int i = liveStart; i < liveEnd; i++) live[i - liveStart] = live[i];
            for (int i = liveEnd - liveStart; i < live.length; i++) live[i] = null;
        }
        live[liveEnd++] = obj;
    }

    /**
   * Called from the mutator thread: return the first object queued 
   * on the finalize list, or null if none
   */
    public static final Object get() throws VM_PragmaUninterruptible {
        if (liveStart == liveEnd) return null;
        Object obj = live[liveStart];
        live[liveStart++] = null;
        if (TRACE_DETAIL) VM_Scheduler.trace(" VM_Finalizer: ", "get returning ", VM_Magic.objectAsAddress(obj));
        return obj;
    }

    /** 
   * Move all finalizable objects to the to-be-finalized queue
   * Called on shutdown
  */
    public static final void finalizeAll() throws VM_PragmaUninterruptible {
        int cursor = 0;
        while (cursor < candidateEnd) {
            VM_Address cand = VM_Address.fromInt(candidate[cursor]);
            candidate[cursor] = 0;
            addLive(VM_Magic.addressAsObject(cand));
            cursor++;
        }
        compactCandidates();
        if (!VM_Scheduler.finalizerQueue.isEmpty()) {
            VM_Thread tt = VM_Scheduler.finalizerQueue.dequeue();
            VM_Processor.getCurrentProcessor().scheduleThread(tt);
        }
    }

    /**
   * Scan the array for objects which have become garbage
   * and move them to the Finalizable class
   */
    public static final int moveToFinalizable() throws VM_PragmaUninterruptible {
        if (TRACE) VM_Scheduler.trace(" VM_Finalizer: ", " move to finalizable ");
        int cursor = 0;
        int newFinalizeCount = 0;
        while (cursor < candidateEnd) {
            VM_Address cand = VM_Address.fromInt(candidate[cursor]);
            if (VM_Allocator.processFinalizerCandidate(cand)) {
                Object obj = VM_Allocator.getLiveObject(VM_Magic.addressAsObject(cand));
                candidate[cursor] = 0;
                addLive(obj);
                newFinalizeCount++;
            } else {
                Object obj = VM_Allocator.getLiveObject(VM_Magic.addressAsObject(cand));
                candidate[cursor] = VM_Magic.objectAsAddress(obj).toInt();
            }
            cursor++;
        }
        compactCandidates();
        return newFinalizeCount;
    }

    static void schedule() throws VM_PragmaUninterruptible {
        if ((countToBeFinalized() > 0) && !VM_Scheduler.finalizerQueue.isEmpty()) {
            VM_Thread t = VM_Scheduler.finalizerQueue.dequeue();
            VM_Processor.getCurrentProcessor().scheduleThread(t);
        }
    }

    static int countHasFinalizer() throws VM_PragmaUninterruptible {
        return candidateEnd;
    }

    static int countToBeFinalized() throws VM_PragmaUninterruptible {
        return liveEnd - liveStart;
    }
}

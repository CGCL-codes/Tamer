package com.ibm.JikesRVM.memoryManagers.vmInterface;

import com.ibm.JikesRVM.memoryManagers.watson.*;
import com.ibm.JikesRVM.VM;
import com.ibm.JikesRVM.VM_Constants;
import com.ibm.JikesRVM.VM_Address;
import com.ibm.JikesRVM.VM_Processor;
import com.ibm.JikesRVM.classloader.VM_Method;
import com.ibm.JikesRVM.VM_CompiledMethod;
import com.ibm.JikesRVM.VM_CompiledMethods;
import com.ibm.JikesRVM.VM_Scheduler;
import com.ibm.JikesRVM.VM_Runtime;
import com.ibm.JikesRVM.VM_Magic;
import com.ibm.JikesRVM.VM_Thread;
import com.ibm.JikesRVM.VM_PragmaUninterruptible;

/**
 * Class that supports scanning thread stacks for references during
 * collections. References are located using GCMapIterators and are
 * inserted into a set of root locations.  Optionally, a set of 
 * interior pointer locations paired with the object is created.
 *
 * @author Stephen Smith
 * @author Perry Cheng
 */
public class ScanStack implements VM_Constants, VM_GCConstants {

    static final boolean VALIDATE_STACK_REFS = true;

    static final boolean DUMP_STACK_FRAMES = false;

    static final boolean DUMP_STACK_REFS = false || DUMP_STACK_FRAMES;

    static final boolean TRACE_STACKS = false || DUMP_STACK_REFS;

    public static void processRoots() throws VM_PragmaUninterruptible {
        VM_CollectorThread collector = VM_Magic.threadAsCollectorThread(VM_Thread.getCurrentThread());
        AddressSet rootVals = collector.rootValues;
        AddressSet rootLocs = collector.rootLocations;
        AddressPairSet interiorLocs = collector.interiorLocations;
        while (!rootVals.isEmpty()) {
            VM_Address rootVal = rootVals.pop();
            VM_Allocator.processPtrValue(rootVal);
        }
        while (!rootLocs.isEmpty()) {
            VM_Address rootLoc = rootLocs.pop();
            VM_Allocator.processPtrField(rootLoc);
        }
        if (interiorLocs != null) {
            while (!interiorLocs.isEmpty()) {
                VM_Address obj = interiorLocs.pop1();
                VM_Address interiorLoc = interiorLocs.pop2();
                VM_Address interior = VM_Magic.getMemoryAddress(interiorLoc);
                VM_Address newObj = VM_Allocator.processPtrValue(obj);
                if (obj.NE(newObj)) {
                    VM_Address newInterior = interior.add(newObj.diff(obj));
                    VM_Magic.setMemoryAddress(interiorLoc, newInterior);
                }
            }
        }
    }

    /**
   * Scans a threads stack during collection to find object references.
   * Locate and save locations containing roots and/or return addresses.
   * Include JNI ative frames.
   * <p>
   *
   * @param t              VM_Thread for the thread whose stack is being scanned
   * @param top_frame      address of stack frame at which to begin the scan
   * @param rootLocations  set to store addresses containing roots
   * @param relocate_code  set to store addresses containing return addresses (if null, skip)
   */
    public static void scanThreadStack(VM_Thread t, VM_Address top_frame, boolean relocate_code) throws VM_PragmaUninterruptible {
        VM_CollectorThread collector = VM_Magic.threadAsCollectorThread(VM_Thread.getCurrentThread());
        AddressSet rootLocations = collector.rootLocations;
        AddressPairSet codeLocations = relocate_code ? collector.interiorLocations : null;
        VM_Address ip, fp, prevFp;
        VM_CompiledMethod compiledMethod;
        if (codeLocations != null && t.hardwareExceptionRegisters.inuse) {
            ip = t.hardwareExceptionRegisters.ip;
            compiledMethod = VM_CompiledMethods.findMethodForInstruction(ip);
            if (VM.VerifyAssertions) VM._assert(compiledMethod != null);
            compiledMethod.setObsolete(false);
            VM_Address code = VM_Magic.objectAsAddress(compiledMethod.getInstructions());
            codeLocations.push(code, t.hardwareExceptionRegisters.getIPLocation());
        }
        VM_GCMapIteratorGroup iteratorGroup = collector.iteratorGroup;
        iteratorGroup.newStackWalk(t);
        if (TRACE_STACKS) {
            VM_Scheduler.trace("VM_ScanStack", "Thread id", t.getIndex());
            t.dump();
        }
        if (!top_frame.isZero()) {
            prevFp = top_frame;
            ip = VM_Magic.getReturnAddress(top_frame);
            fp = VM_Magic.getCallerFramePointer(top_frame);
        } else {
            prevFp = VM_Address.zero();
            ip = t.contextRegisters.getInnermostInstructionAddress();
            fp = t.contextRegisters.getInnermostFramePointer();
        }
        if (TRACE_STACKS) {
            VM.sysWrite("  top_frame = ");
            VM.sysWrite(top_frame);
            VM.sysWrite("\n");
            VM.sysWrite("         ip = ");
            VM.sysWrite(ip);
            VM.sysWrite("\n");
            VM.sysWrite("         fp = ");
            VM.sysWrite(fp);
            VM.sysWrite("\n");
            VM.sysWrite("  registers.ip = ");
            VM.sysWrite(t.contextRegisters.ip);
            VM.sysWrite("\n");
        }
        if (DUMP_STACK_REFS && t.jniEnv != null) t.jniEnv.dumpJniRefsStack();
        if (fp.NE(VM_Address.fromInt(STACKFRAME_SENTINAL_FP))) {
            if (DUMP_STACK_REFS) {
                VM_Scheduler.dumpStack(ip, fp);
                VM.sysWrite("\n");
            }
            while (VM_Magic.getCallerFramePointer(fp).NE(VM_Address.fromInt(STACKFRAME_SENTINAL_FP))) {
                int compiledMethodId = VM_Magic.getCompiledMethodID(fp);
                if (compiledMethodId == VM_Constants.INVISIBLE_METHOD_ID) {
                    if (TRACE_STACKS) VM.sysWrite("\n--- METHOD --- <invisible method>\n");
                    prevFp = fp;
                    ip = VM_Magic.getReturnAddress(fp);
                    fp = VM_Magic.getCallerFramePointer(fp);
                    continue;
                }
                compiledMethod = VM_CompiledMethods.getCompiledMethod(compiledMethodId);
                compiledMethod.setObsolete(false);
                VM_Method method = compiledMethod.getMethod();
                int offset = ip.diff(VM_Magic.objectAsAddress(compiledMethod.getInstructions())).toInt();
                VM_GCMapIterator iterator = iteratorGroup.selectIterator(compiledMethod);
                iterator.setupIterator(compiledMethod, offset, fp);
                if (TRACE_STACKS) {
                    VM_Scheduler.outputMutex.lock();
                    VM.sysWrite("\n--- METHOD --- ");
                    VM.sysWrite(method);
                    VM.sysWrite(" at offset ", offset);
                    VM.sysWrite(".\n");
                    VM_Scheduler.outputMutex.unlock();
                }
                if (DUMP_STACK_FRAMES) dumpStackFrame(fp, prevFp);
                if (DUMP_STACK_REFS) VM.sysWrite("--- Refs Reported By GCMap Iterator ---\n");
                if (false) {
                    VM.sysWrite("--- FRAME DUMP of METHOD ");
                    VM.sysWrite(method);
                    VM.sysWrite(" at offset ");
                    VM.sysWrite(offset);
                    VM.sysWrite(".--- \n");
                    VM.sysWrite(" fp = ");
                    VM.sysWrite(fp);
                    VM.sysWrite(" ip = ");
                    VM.sysWrite(ip);
                    VM.sysWrite("\n");
                    dumpStackFrame(fp, prevFp);
                }
                for (VM_Address refaddr = iterator.getNextReferenceAddress(); !refaddr.isZero(); refaddr = iterator.getNextReferenceAddress()) {
                    if (VM.VerifyAssertions && VALIDATE_STACK_REFS) {
                        VM_Address ref = VM_Magic.getMemoryAddress(refaddr);
                        if (!VM_GCUtil.validRef(ref)) {
                            VM.sysWrite("\nInvalid ref reported while scanning stack\n");
                            VM.sysWrite("--- METHOD --- ");
                            VM.sysWrite(method);
                            VM.sysWrite(" at offset ");
                            VM.sysWrite(offset);
                            VM.sysWrite(".\n");
                            VM.sysWrite(" fp = ");
                            VM.sysWrite(fp);
                            VM.sysWrite(" ip = ");
                            VM.sysWrite(ip);
                            VM.sysWrite("\n");
                            VM.sysWrite(refaddr);
                            VM.sysWrite(":");
                            VM_GCUtil.dumpRef(ref);
                            dumpStackFrame(fp, prevFp);
                            VM.sysWrite("\nDumping stack starting at frame with bad ref:\n");
                            VM_Scheduler.dumpStack(ip, fp);
                            VM_Address top_ip = t.contextRegisters.getInnermostInstructionAddress();
                            VM_Address top_fp = t.contextRegisters.getInnermostFramePointer();
                            VM_Scheduler.dumpStack(top_ip, top_fp);
                            VM.sysFail("\n\nVM_ScanStack: Detected bad GC map; exiting RVM with fatal error");
                        }
                    }
                    if (DUMP_STACK_REFS) {
                        VM_Address ref = VM_Magic.getMemoryAddress(refaddr);
                        VM.sysWrite(refaddr);
                        VM.sysWrite(":");
                        VM_GCUtil.dumpRef(ref);
                    }
                    rootLocations.push(refaddr);
                }
                if (codeLocations != null) {
                    VM_Address code = VM_Magic.objectAsAddress(compiledMethod.getInstructions());
                    if (codeLocations != null && !VM_GCUtil.addrInBootImage(code)) {
                        if (prevFp.isZero()) {
                            codeLocations.push(code, t.contextRegisters.getIPLocation());
                        } else {
                            codeLocations.push(code, VM_Magic.getReturnAddressLocation(prevFp));
                        }
                        iterator.reset();
                        for (VM_Address retaddr = iterator.getNextReturnAddressAddress(); !retaddr.isZero(); retaddr = iterator.getNextReturnAddressAddress()) {
                            codeLocations.push(code, retaddr);
                        }
                    }
                }
                iterator.cleanupPointers();
                if (compiledMethod.getMethod().getDeclaringClass().isBridgeFromNative()) {
                    fp = VM_Runtime.unwindNativeStackFrame(fp);
                    if (TRACE_STACKS) VM.sysWrite("scanStack skipping native C frames\n");
                }
                prevFp = fp;
                ip = VM_Magic.getReturnAddress(fp);
                fp = VM_Magic.getCallerFramePointer(fp);
            }
        }
        VM_GCMapIterator iterator = iteratorGroup.getJniIterator();
        VM_Address refaddr = iterator.getNextReferenceAddress();
        while (!refaddr.isZero()) {
            rootLocations.push(refaddr);
            refaddr = iterator.getNextReferenceAddress();
        }
        if (TRACE_STACKS) VM.sysWrite("--- End Of Stack Scan ---\n");
    }

    static void dumpStackFrame(VM_Address fp, VM_Address prevFp) throws VM_PragmaUninterruptible {
        VM_Address start, end;
        if (prevFp.isZero()) {
            start = fp.sub(20 * WORDSIZE);
            VM.sysWrite("--- 20 words of stack frame with fp = ");
        } else {
            start = prevFp;
            VM.sysWrite("--- stack frame with fp = ");
        }
        VM.sysWrite(fp);
        VM.sysWrite(" ----\n");
        end = fp;
        VM.sysWrite("--- stack frame with fp = ");
        VM.sysWrite(fp);
        VM.sysWrite(" ----\n");
        start = fp;
        end = VM_Magic.getMemoryAddress(fp);
        for (VM_Address loc = start; loc.LE(end); loc = loc.add(WORDSIZE)) {
            VM.sysWrite(loc.diff(start).toInt());
            VM.sysWrite(" ");
            VM.sysWrite(loc);
            VM.sysWrite(" ");
            VM_Address value = VM_Magic.getMemoryAddress(loc);
            VM.sysWrite(value);
            VM.sysWrite(" ");
            if (VM_GCUtil.refInVM(value) && loc.NE(start) && loc.NE(end)) VM_GCUtil.dumpRef(value); else VM.sysWrite("\n");
        }
        VM.sysWrite("\n");
    }
}

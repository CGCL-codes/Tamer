package com.ibm.JikesRVM.memoryManagers.watson;

import com.ibm.JikesRVM.VM_Constants;
import com.ibm.JikesRVM.VM_ProcessorLock;
import com.ibm.JikesRVM.VM_Address;
import com.ibm.JikesRVM.VM_Memory;
import com.ibm.JikesRVM.VM_ObjectModel;
import com.ibm.JikesRVM.VM;
import com.ibm.JikesRVM.VM_Entrypoints;
import com.ibm.JikesRVM.VM_Synchronization;
import com.ibm.JikesRVM.VM_PragmaUninterruptible;

/**
 * A heap that allocates in contiguous free
 * memory by bumping a pointer on each allocation.
 * The pointer bump is down with an atomic
 * fetch and add sequence, so this heap is 
 * multi-processor safe. <p>
 * 
 * For reasonable performance in an MP system,
 * most allocations are done in processor local
 * chunks (VM_Chunk.java) and the contiguous heap
 * is only used to acquire new chunks.
 * 
 * @author Perry Cheng
 * @author Dave Grove
 * @author Stephen Smith
 * 
 * @see VM_Chunk
 * @see com.ibm.JikesRVM.VM_Processor
 */
public final class VM_ContiguousHeap extends VM_Heap implements VM_GCConstants {

    static final int FORWARD = 0;

    static final int BACKWARD = 1;

    /**
   * The current allocation pointer.
   * Always updated with atomic operations!
   */
    private VM_Address current;

    private VM_Address saved;

    private int sense;

    VM_ContiguousHeap(String s) throws VM_PragmaUninterruptible {
        super(s);
        sense = FORWARD;
    }

    int sense() throws VM_PragmaUninterruptible {
        return sense;
    }

    /**
   * Allocate size bytes of raw memory.
   * Size is a multiple of wordsize, and the returned memory must be word aligned
   * 
   * @param size Number of bytes to allocate
   * @return Address of allocated storage
   */
    protected VM_Address allocateZeroedMemory(int size) throws VM_PragmaUninterruptible {
        VM.sysFail("allocateZeroedMemory on VM_Contiguous heap forbidden");
        return VM_Address.zero();
    }

    /**
   * Hook to allow heap to perform post-allocation processing of the object.
   * For example, setting the GC state bits in the object header.
   */
    protected void postAllocationProcessing(Object newObj) throws VM_PragmaUninterruptible {
    }

    /** 
   * Allocate raw memory of size bytes.
   * Important the caller of this function may be responsible 
   * for zeroing memory if required! The allocated memory is
   * intentionally not zeroed here.
   * 
   * @param size the number of bytes to allocate
   * @return the allocate memory or VM_Address.zero() if space is exhausted.
   */
    public VM_Address allocateRawMemory(int size) throws VM_PragmaUninterruptible {
        int offset = VM_Entrypoints.contiguousHeapCurrentField.getOffset();
        if (sense == FORWARD) {
            VM_Address addr = VM_Synchronization.fetchAndAddAddressWithBound(this, offset, size, end);
            if (VM.VerifyAssertions) VM._assert(start.LE(current) && current.LE(end));
            if (!addr.isMax()) return addr;
        } else {
            VM_Address addr = VM_Synchronization.fetchAndSubAddressWithBound(this, offset, size, start);
            if (VM.VerifyAssertions) VM._assert(start.LE(current) && current.LE(end));
            if (!addr.isMax()) return addr.sub(size);
        }
        return VM_Address.zero();
    }

    VM_Address current() throws VM_PragmaUninterruptible {
        return current;
    }

    public void show(boolean newline) throws VM_PragmaUninterruptible {
        super.show(false);
        VM.sysWrite("   cur = ");
        VM.sysWrite(current);
        VM.sysWrite((sense == FORWARD) ? " -->" : " <--");
        VM.sysWrite("    ", usedMemory() / 1024, "  Kb used");
        if (newline) VM.sysWriteln();
    }

    /**
   * All space in the heap is available for allocation again.
   */
    public void reset() throws VM_PragmaUninterruptible {
        if (sense == FORWARD) saved = current = start; else saved = current = end;
    }

    /**
   * All space in the heap is available for allocation again.
   */
    public void setRegion(VM_Address s, VM_Address e) throws VM_PragmaUninterruptible {
        super.setRegion(s, e);
        reset();
    }

    public void setRegion(VM_Address s, VM_Address e, int se) throws VM_PragmaUninterruptible {
        if (VM.VerifyAssertions) VM._assert(se == FORWARD || se == BACKWARD);
        sense = se;
        setRegion(s, e);
    }

    public void setRegion(VM_Address s, VM_Address c, VM_Address e, int se) throws VM_PragmaUninterruptible {
        if (VM.VerifyAssertions) VM._assert(se == FORWARD || se == BACKWARD);
        sense = se;
        setRegion(s, e);
        current = c;
    }

    public void contractRegion() throws VM_PragmaUninterruptible {
        if (sense == FORWARD) {
            end = current;
            setAuxiliary();
        } else {
            start = current;
            setAuxiliary();
        }
    }

    public void extendRegion(VM_Address newBoundary) throws VM_PragmaUninterruptible {
        if (sense == FORWARD) {
            if (VM.VerifyAssertions) VM._assert(newBoundary.GE(end));
            end = newBoundary;
            setAuxiliary();
        } else {
            if (VM.VerifyAssertions) VM._assert(newBoundary.LE(start));
            start = newBoundary;
            setAuxiliary();
        }
    }

    /**
   * Heap is reset at attachment and detachment.
   */
    public void attach(int size) throws VM_PragmaUninterruptible {
        super.attach(size);
        reset();
    }

    public void detach(int size) throws VM_PragmaUninterruptible {
        super.detach();
        reset();
    }

    public int allocatedFromSaved() throws VM_PragmaUninterruptible {
        if (sense == FORWARD) return current.diff(saved).toInt(); else return saved.diff(current).toInt();
    }

    public void recordSaved() throws VM_PragmaUninterruptible {
        saved = current;
    }

    /**
   * Zero the remaining free space in the heap.
   */
    public void zeroFreeSpace() throws VM_PragmaUninterruptible {
        if (sense == FORWARD) VM_Memory.zeroPages(current, end.diff(current).toInt()); else VM_Memory.zeroPages(start, current.diff(start).toInt());
    }

    /**
   * Zero the remaining free space in the heap.
   */
    public void zeroFreeSpaceParallel() throws VM_PragmaUninterruptible {
        if (sense == FORWARD) zeroParallel(current, end); else zeroParallel(start, current);
    }

    /**
   * Round up to page boundary
   */
    public void roundUpPage() throws VM_PragmaUninterruptible {
        current = VM_Memory.roundUpPage(current);
    }

    /**
   * How much free memory is left in the heap?
   */
    public int freeMemory() throws VM_PragmaUninterruptible {
        if (sense == FORWARD) return end.diff(current).toInt(); else return current.diff(start).toInt();
    }

    /**
   * How much memory is used in the heap?
   */
    public int usedMemory() throws VM_PragmaUninterruptible {
        if (sense == FORWARD) return current.diff(start).toInt(); else return end.diff(current).toInt();
    }
}

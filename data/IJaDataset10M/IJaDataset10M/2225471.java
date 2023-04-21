package org.mmtk.plan;

import org.mmtk.vm.VM_Interface;
import com.ibm.JikesRVM.VM_Address;
import com.ibm.JikesRVM.VM_Word;
import com.ibm.JikesRVM.VM_Magic;
import com.ibm.JikesRVM.VM_PragmaInline;
import com.ibm.JikesRVM.VM_PragmaUninterruptible;
import org.mmtk.vm.VM_Interface;

public class RCHeader extends RCBaseHeader {

    /**
   * Perform any required initialization of the GC portion of the header.
   * 
   * @param ref the object ref to the storage to be initialized
   * @param tib the TIB of the instance being created
   * @param size the number of bytes allocated by the GC system for this object.
   */
    public static void initializeHeader(VM_Address ref, Object[] tib, int size) throws VM_PragmaUninterruptible, VM_PragmaInline {
        int initialValue = INCREMENT;
        if (Plan.REF_COUNT_CYCLE_DETECTION && VM_Interface.isAcyclic(tib)) initialValue |= GREEN;
        VM_Magic.setIntAtOffset(ref, RC_HEADER_OFFSET, initialValue);
    }

    /**
   * Perform any required initialization of the GC portion of the header.
   * Called for objects created at boot time.
   * 
   * @param ref the object ref to the storage to be initialized
   * @param tib the TIB of the instance being created
   * @param size the number of bytes allocated by the GC system for
   * this object.
   */
    public static VM_Word getBootTimeAvailableBits(int ref, Object[] tib, int size, VM_Word status) throws VM_PragmaUninterruptible, VM_PragmaInline {
        return status;
    }
}

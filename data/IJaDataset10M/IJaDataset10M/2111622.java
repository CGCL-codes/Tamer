package org.mmtk.utility;

import org.mmtk.vm.TraceInterface;
import com.ibm.JikesRVM.VM_Address;
import com.ibm.JikesRVM.VM_Offset;
import com.ibm.JikesRVM.VM_Word;
import com.ibm.JikesRVM.VM_Uninterruptible;

/**
 * This class specializes SortSharedQueue to sort objects according to
 * their time of death (TOD).
 *
 * @author <a href="http://cs.anu.edu.au/~Steve.Blackburn">Steve Blackburn</a>
 * @version $Revision: 6416 $
 * @date $Date: 2004-02-09 18:57:05 -0500 (Mon, 09 Feb 2004) $
 */
public final class SortTODSharedDeque extends SortSharedDeque implements VM_Uninterruptible {

    public static final String Id = "$Id: SortTODSharedDeque.java 6416 2004-02-09 23:57:05Z steveb-oss $";

    private static final int BYTES_PUSHED = BYTES_IN_ADDRESS * 5;

    private static final int MAX_STACK_SIZE = BYTES_PUSHED * 64;

    private static final VM_Offset INSERTION_SORT_LIMIT = VM_Offset.fromInt(80);

    /**
   * Constructor
   *
   * @param rpa The allocator from which the instance should obtain buffers.
   * @param airty The arity of the data to be enqueued
   */
    public SortTODSharedDeque(RawPageAllocator rpa, int arity) {
        super(rpa, arity);
    }

    /**
   * Return the sorting key for the object passed as a parameter.
   *
   * @param obj The address of the object whose key is wanted
   * @return The value of the sorting key for this object
   */
    protected final VM_Word getKey(VM_Address obj) {
        return TraceInterface.getDeathTime(obj);
    }
}

package com.h9labs.swbem.msvm.memory;

import org.jinterop.dcom.impls.automation.IJIDispatch;
import com.h9labs.swbem.SWbemServices;
import com.h9labs.swbem.msvm.MsvmObject;

/**
 * Represents the memory currently allocated to a virtual system.
 * 
 * @author akutz
 * 
 */
public class MsvmMemory extends MsvmObject {

    /**
     * Initializes a new instance of the MsvmMemory class.
     * 
     * @param objectDispatcher The underlying dispatch object used to
     *        communicate with the server.
     * @param service The service connection.
     */
    public MsvmMemory(IJIDispatch objectDispatcher, SWbemServices service) {
        super(objectDispatcher, service);
    }

    /**
     * Gets a calculated value that represents the total amount of memory
     * divided by the BlockSize.
     * 
     * @return A calculated value that represents the total amount of memory
     *         divided by the BlockSize.
     */
    public long getNumberOfBlocks() {
        return super.getProperty("NumberOfBlocks", Long.class);
    }

    /**
     * Gets the size, in bytes, of the blocks that form the storage extent.
     * 
     * @return The size, in bytes, of the blocks that form the storage extent.
     * @remarks If variable block size, then the maximum block size, in bytes,
     *          should be specified. If the block size is unknown, or if a block
     *          concept is not valid (for example, for aggregate extents,
     *          memory, or logical disks), enter a 1 (one).
     */
    public long getBlockSize() {
        return super.getProperty("BlockSize", Long.class);
    }
}

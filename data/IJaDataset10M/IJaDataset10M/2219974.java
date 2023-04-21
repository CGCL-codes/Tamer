package org.motiv.policy;

/**
 * Enumeration of eviction policies.
 * @author Pavlov Dm
 */
public final class MemoryStoreEvictionPolicy {

    /**
     * LRU - least recently used.
     */
    public static final MemoryStoreEvictionPolicy LRU = new MemoryStoreEvictionPolicy("LRU");

    /**
     * LFU - least frequently used.
     */
    public static final MemoryStoreEvictionPolicy LFU = new MemoryStoreEvictionPolicy("LFU");

    /**
     * FIFO - first in first out, the oldest element by creation time.
     */
    public static final MemoryStoreEvictionPolicy FIFO = new MemoryStoreEvictionPolicy("FIFO");

    private final String myName;

    /**
     * This class should not be subclassed or have instances created.
     * @param policy
     */
    private MemoryStoreEvictionPolicy(String policy) {
        myName = policy;
    }

    /**
     * Converts a string representation of the policy into a policy.
     * @param policy either LRU, LFU or FIFO
     * @return one of the static instances
     */
    public static MemoryStoreEvictionPolicy fromString(String policy) {
        if (policy != null) {
            if (policy.equalsIgnoreCase("LRU")) {
                return LRU;
            } else if (policy.equalsIgnoreCase("LFU")) {
                return LFU;
            } else if (policy.equalsIgnoreCase("FIFO")) {
                return FIFO;
            }
        }
        return LRU;
    }

    /**
	* Get policy name method
	*/
    public String getName() {
        return myName;
    }
}

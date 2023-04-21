package com.db4o.defragment;

/**
 * The ID mapping used internally during a defragmentation run.
 * 
 * @see Defragment
 */
public interface ContextIDMapping {

    /**
	 * Returns a previously registered mapping ID for the given ID if it exists.
	 * If lenient mode is set to true, will provide the mapping ID for the next
	 * smaller original ID a mapping exists for. Otherwise returns 0.
	 * 
	 * @param origID The original ID
	 * @param lenient If true, lenient mode will be used for lookup, strict mode otherwise.
	 * @return The mapping ID for the given original ID or 0, if none has been registered.
	 */
    int mappedID(int origID, boolean lenient);

    /**
	 * Registers a mapping for the given IDs.
	 * 
	 * @param origID The original ID
	 * @param mappedID The ID to be mapped to the original ID.
	 * @param isClassID true if the given original ID specifies a class slot, false otherwise.
	 */
    void mapIDs(int origID, int mappedID, boolean isClassID);

    /**
	 * Prepares the mapping for use.
	 */
    void open();

    /**
	 * Shuts down the mapping after use.
	 */
    void close();
}

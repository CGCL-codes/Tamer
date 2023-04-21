package com.sun.jini.outrigger;

/**
 * A description of a template's parameters.
 * 
 * @author Sun Microsystems, Inc.
 * 
 * @see EntryHandle#descFor
 * @see EntryHandle
 */
class EntryHandleTmplDesc {

    /** The hash value for the template itself, already masked */
    long hash;

    /**
	 * The mask for EntryHandle's hash codes -- if <code>handle.hash &
	 * mask != tmplDesc.hash</code>, then the template doesn't match the object
	 * held by the handle.
	 */
    long mask;

    public String toString() {
        return "0x" + Long.toHexString(hash) + " & 0x" + Long.toHexString(mask);
    }
}

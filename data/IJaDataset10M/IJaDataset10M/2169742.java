package net.eiroca.j2me.sm.util;

import javax.microedition.rms.RecordFilter;

/**
 * Implementation of the RMS RecordFilter interface.
 */
public class StoreFilterByID implements RecordFilter {

    /** The id. */
    private final long id;

    /**
   * Creates new <code>AddressRecordFilter</code> instance.
   * 
   * @param aID the a id
   */
    public StoreFilterByID(final long aID) {
        id = aID;
    }

    public boolean matches(final byte[] values) {
        if ((id != 0) && (Store.getID(values) == id)) {
            return true;
        }
        return false;
    }
}

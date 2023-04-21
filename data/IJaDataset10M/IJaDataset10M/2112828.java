package org.rvsnoop;

import rvsnoop.Record;
import ca.odell.glazedlists.BasicEventList;

/**
 * A ledger that uses a simple in-memory collection to hold the records.
 *
 * @author <a href="mailto:ianp@ianp.org">Ian Phillips</a>
 * @version $Revision: 407 $, $Date: 2008-08-11 13:08:21 -0400 (Mon, 11 Aug 2008) $
 * @since 1.7
 */
public final class InMemoryLedger extends RecordLedger {

    /**
     * Create a new in memory ledger instance.
     */
    public InMemoryLedger() {
        super(new BasicEventList<Record>());
    }
}

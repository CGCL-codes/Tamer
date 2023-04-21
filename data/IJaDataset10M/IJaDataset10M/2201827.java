package org.tripcom.tsadapter;

import org.tripcom.integration.entry.RdTSAdapterEntry;
import org.tripcom.integration.entry.Timeout;

public class ReadRequest implements Comparable<ReadRequest> {

    public final RdTSAdapterEntry e;

    public boolean waitForNewData = false;

    public ReadRequest(RdTSAdapterEntry e) {
        if (e == null) throw new IllegalArgumentException();
        if (e.timeout == null) {
            e.timeout = new Timeout(Long.MAX_VALUE);
        }
        this.e = e;
    }

    public int compareTo(ReadRequest c) {
        int compare = new Boolean(this.waitForNewData).compareTo(c.waitForNewData);
        if (compare != 0) return compare;
        long now = System.currentTimeMillis();
        long compare_time = now - c.e.timeout.getTimestamp() + c.e.timeout.getTimeout();
        long this_time = now - e.timeout.getTimestamp() + e.timeout.getTimeout();
        if (compare_time < this_time) return -1; else if (compare_time > this_time) return 1; else return 0;
    }
}

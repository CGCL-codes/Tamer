package Acme.Serve;

/**
 * Data item for ThrottledOutputStream.
 * <P>
 * <A HREF="/resources/classes/Acme/Serve/ThrottleItem.java">Fetch the software.</A><BR>
 * <A HREF="/resources/classes/Acme.tar.gz">Fetch the entire Acme package.</A>
 */
public class ThrottleItem {

    private long maxBps;

    /** Constructor. */
    public ThrottleItem(long maxBps) {
        this.maxBps = maxBps;
    }

    public long getMaxBps() {
        return maxBps;
    }
}

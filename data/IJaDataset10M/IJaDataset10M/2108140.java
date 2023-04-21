package net.sourceforge.myvd.protocol.ldap.mina.ldap.codec.extended.operations;

import net.sourceforge.myvd.protocol.ldap.mina.asn1.AbstractAsn1Object;

/**
 * A common class for graceful Disconnect and Shutdown extended operations.
 * 
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public abstract class GracefulAction extends AbstractAsn1Object {

    /** Undetermined value used for timeOffline */
    public static final int UNDETERMINED = 0;

    /** The shutdown is immediate */
    public static final int NOW = 0;

    /** offline Time after disconnection */
    protected int timeOffline = UNDETERMINED;

    /** Delay before disconnection */
    protected int delay = NOW;

    /**
     * Default constructor. The time offline will be set to UNDETERMINED and
     * there is no delay.
     */
    public GracefulAction() {
        super();
    }

    /**
     * Create a GracefulAction object, with a timeOffline and a delay
     * 
     * @param timeOffline The time the server will be offline
     * @param delay The delay before the disconnection
     */
    public GracefulAction(int timeOffline, int delay) {
        super();
        this.timeOffline = timeOffline;
        this.delay = delay;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getTimeOffline() {
        return timeOffline;
    }

    public void setTimeOffline(int timeOffline) {
        this.timeOffline = timeOffline;
    }
}

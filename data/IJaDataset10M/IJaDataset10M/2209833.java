package org.apache.batik.anim.timing;

import java.util.Calendar;

/**
 * A class to handle wallclock SMIL timing specifiers.
 *
 * @author <a href="mailto:cam%40mcc%2eid%2eau">Cameron McCormack</a>
 * @version $Id: WallclockTimingSpecifier.java 580338 2007-09-28 13:13:46Z cam $
 */
public class WallclockTimingSpecifier extends TimingSpecifier {

    /**
     * The wallclock time.
     */
    protected Calendar time;

    /**
     * The instance time.
     */
    protected InstanceTime instance;

    /**
     * Creates a new WallclockTimingSpecifier object.
     */
    public WallclockTimingSpecifier(TimedElement owner, boolean isBegin, Calendar time) {
        super(owner, isBegin);
        this.time = time;
    }

    /**
     * Returns a string representation of this timing specifier.
     */
    public String toString() {
        return "wallclock(" + time.toString() + ")";
    }

    /**
     * Initializes this timing specifier by adding the initial instance time
     * to the owner's instance time list or setting up any event listeners.
     */
    public void initialize() {
        float t = owner.getRoot().convertWallclockTime(time);
        instance = new InstanceTime(this, t, false);
        owner.addInstanceTime(instance, isBegin);
    }

    /**
     * Returns whether this timing specifier is event-like (i.e., if it is
     * an eventbase, accesskey or a repeat timing specifier).
     */
    public boolean isEventCondition() {
        return false;
    }
}

package net.sf.openforge.forge.api.pin;

import java.util.*;
import net.sf.openforge.forge.api.entry.EntryMethod;

/**
 * A ClockDomain pairs a ClockPin with a ResetPin, and
 * associates {@link EntryMethod EntryMethods} which
 * operate within the same clock domain.
 * <P>
 * EntryMethods can only be associated with a single domain.
 * 
 */
public class ClockDomain {

    /** A clock domain which uses the global clock and reset. */
    public static final ClockDomain GLOBAL = new ClockDomain("CLK", ClockPin.UNDEFINED_HZ);

    /** This domain's clock. */
    private ClockPin clockPin;

    /** This domain's reset. */
    private ResetPin resetPin;

    /** The <code>EntryMethods</code> which are associated with this
        domain. */
    private Collection entryMethods = new HashSet();

    /**
     * Constructs a ClockDomain with a specifically named set of pins.
     *
     * @param clock the name of the clock signal
     * @param frequency the frequency of the clock, in Hz
     */
    public ClockDomain(String clock, long frequency) {
        this.clockPin = new ClockPin(clock, frequency);
        this.resetPin = new ResetPin(clock + "_RESET");
        clockPin.setDomain(this);
        resetPin.setDomain(this);
    }

    /**
     * Gets the clock pin used by this domain.
     * 
     * @return the {@link ClockPin} used by this domain.
     */
    public ClockPin getClockPin() {
        return clockPin;
    }

    /**
     * Gets the reset pin used by this domain.
     * @return the {@link ResetPin} used by this domain.
     */
    public ResetPin getResetPin() {
        return resetPin;
    }
}

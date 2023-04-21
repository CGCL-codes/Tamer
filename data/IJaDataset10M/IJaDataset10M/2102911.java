package proguard.gui.splash;

/**
 * This Timing varies between 0 and 1, as a sine wave over time.
 *
 * @author Eric Lafortune
 */
public class SineTiming implements Timing {

    private long period;

    private long phase;

    /**
     * Creates a new SineTiming.
     * @param period the time period for a full cycle.
     * @param phase  the phase of the cycle, which is added to the actual time.
     */
    public SineTiming(long period, long phase) {
        this.period = period;
        this.phase = phase;
    }

    public double getTiming(long time) {
        return 0.5 + 0.5 * Math.sin(2.0 * Math.PI * (time + phase) / period);
    }
}

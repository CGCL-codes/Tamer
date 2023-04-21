package gov.sns.apps.scope;

/**
 * WaveformSample represents a sample from a waveform.  The sample simply consists of the sample time in turns 
 * from cycle start and the value of the sample.
 *
 * @author  tap
 */
final class WaveformSample {

    /** The sample's time relative to cycle start measured in turns. */
    public final double turn;

    /** The sample's value. */
    public final double value;

    /**
	 * The WaveformSample constructor.
	 * @param newTurn The sample's time relative to cycle start measured in turns.
	 * @param newValue The sample's value.
	 */
    public WaveformSample(double newTurn, double newValue) {
        turn = newTurn;
        value = newValue;
    }

    /**
	 * Get the sample's time from cycle start.
	 * @return the sample's time measured in turns from cycle start.
	 */
    public double getTime() {
        return turn;
    }

    /**
	 * Get the sample's value.
	 * @return the sample's value;
	 */
    public double getValue() {
        return value;
    }

    /**
	 * Overrides inherited toString() to provide a string representation of the WaveformSample.  It provides the
	 * time/value pair.
	 * @return A string representation of the WaveformSample.
	 */
    @Override
    public String toString() {
        return String.valueOf(turn) + " " + String.valueOf(value);
    }
}

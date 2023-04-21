package de.maramuse.soundcomp.events;

/**
 * This event holds data and code for an exponential tempo change
  	// given is start tempo, end tempo, number of beats.
	// With each beat, the tempo rises by the same factor.
	// This mode treats its own slow and fast parts equally.
	// punctual tempo at a certain beat: start * (end/start)^(beat/beats)
	// intermediate: ex = (- ln(endtempo/starttempo) / beats)
	// the duration of a certain beat fraction is:
	//	d = ex/starttempo * (e^(ex*endbeat) - e^(ex*startbeat))
	// punctual tempo at a certain time: to be calculated
	// actual beat at a certain time: to be calculated
 */
public class ExponentialTempoChangeEvent extends TempoChangeEvent {

    private double ex;

    public ExponentialTempoChangeEvent(double timestamp) {
        super(timestamp);
    }

    @Override
    public double calculateRelativeBeatTime(double relativeBeat) {
        if (relativeBeat > beats) return (relativeBeat - beats) / endTempo + endTimeVarying - timestamp;
        if (relativeBeat < 0) throw new IllegalArgumentException("Attempt to calculate tempo change outside range");
        return (Math.exp(ex * relativeBeat) - 1) / startTempo / ex;
    }

    @Override
    public String getDebugString() {
        return super.getDebugString() + " ExponentialTempoChange";
    }

    @Override
    public double calculateBeatFromTimestamp(double vtimestamp) {
        if (vtimestamp > endTimeVarying) {
            return getBeat() + getBeats() + (vtimestamp - endTimeVarying) * endTempo;
        }
        return Math.log((vtimestamp - timestamp) * startTempo * ex + 1) / ex + getBeat();
    }

    @Override
    public void init() {
        ex = -Math.log(endTempo / startTempo) / beats;
        endTimeVarying = calculateRelativeBeatTime(beats) + timestamp;
    }

    @Override
    public double calculateTempoFromTimestamp(double ctimestamp) {
        if (ctimestamp < 0) return Double.NaN;
        if (ctimestamp > endTimeVarying) return endTempo;
        return calculateTempoFromBeat(calculateBeatFromTimestamp(ctimestamp) - beat);
    }

    @Override
    public double calculateTempoFromBeat(double relativeBeat) {
        if (relativeBeat < 0) return Double.NaN;
        if (relativeBeat > beats) return endTempo;
        return startTempo * Math.exp(-ex * relativeBeat);
    }
}

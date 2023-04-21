package yapgen.engine;

import yapgen.base.types.Time;

/**
 *
 * @author riccardo
 */
public class Clock {

    private Time time;

    private long stepSeconds;

    Clock() {
    }

    public long getStepSeconds() {
        return stepSeconds;
    }

    public void setStepSeconds(long stepSeconds) {
        this.stepSeconds = stepSeconds;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public String getIsoTime() {
        return time.getIsoValue();
    }
}

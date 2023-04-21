package midi;

import java.io.Serializable;

public class TriggerInfo implements Serializable {

    protected boolean bSuspend;

    protected int time;

    protected double min;

    double max;

    protected int channel;

    protected int note;

    protected boolean release;

    public boolean isRelease() {
        return release;
    }

    public void setRelease(boolean release) {
        this.release = release;
    }

    public TriggerInfo() {
        bSuspend = false;
        time = 0;
        min = 950;
        max = 1000;
        channel = 0;
        note = 72;
        release = true;
    }

    public boolean isSuspend() {
        return bSuspend;
    }

    public void setSuspend(boolean suspend) {
        bSuspend = suspend;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double d) {
        this.min = d;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double d) {
        this.max = d;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public int getNote() {
        return note;
    }

    public void setNote(int i) {
        this.note = i;
    }
}

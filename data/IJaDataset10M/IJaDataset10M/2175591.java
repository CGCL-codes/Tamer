package jmms;

import com.lemu.play.LPlayer;

public class TickEvent {

    MPlayer source;

    double tickAt;

    public TickEvent(MPlayer source, double tickAt) {
        this.source = source;
        this.tickAt = tickAt;
    }

    public MPlayer getSource() {
        return source;
    }

    public double at() {
        return tickAt;
    }

    public void setAt(double at) {
        tickAt = at;
    }

    public TickEvent copy() {
        return new TickEvent(source, tickAt);
    }

    public TickEvent create(double newAt) {
        return new TickEvent(source, newAt);
    }

    public double getRes() {
        return ((LPlayer) source).res();
    }

    public String toString() {
        return new String(String.valueOf(this.at()));
    }
}

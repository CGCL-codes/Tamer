package de.sweetpete.percussionist.sequencer;

public class Tick {

    protected final double volume;

    protected final Sequence sequence;

    public Tick(double volume, Sequence sequence) {
        this.volume = volume;
        this.sequence = sequence;
    }

    public double getVolume() {
        return volume;
    }

    public Sequence getSequence() {
        return sequence;
    }
}

package uk.org.toot.midi.sequence;

public class SequencePosition {

    public static final int SNAP_OFF = 0;

    public static final int SNAP_BAR = 1;

    public static final int SNAP_2 = 2;

    public static final int SNAP_BEAT = 3;

    public static final int SNAP_8 = 4;

    public static final int SNAP_16 = 5;

    public static final int SNAP_32 = 6;

    public static final int SNAP_64 = 7;

    public SequencePosition(int bar) {
        this.bar = bar;
    }

    public SequencePosition(int bar, int beat) {
        this(bar);
        this.beat = beat;
    }

    public SequencePosition(int bar, int beat, int sixteenth) {
        this(bar, beat);
        this.sixteenth = sixteenth;
    }

    public SequencePosition(int bar, int beat, int tick, int resolution) {
        this(bar, beat);
        this.sixteenth = (int) (tick / (resolution / 4));
        tick %= resolution / 4;
        this.sixtyfourth = (int) (tick / (resolution / 16));
        tick %= resolution / 16;
        this.tick = tick;
        this.resolution = resolution;
    }

    public static long bodgeTick(long tick, int snap, int resolution) {
        if (snap < SNAP_2) return tick;
        return tick + resolution / (1 << (snap - 2));
    }

    public void snap(int snap) {
        switch(snap) {
            case SNAP_BAR:
                beat = 0;
            case SNAP_2:
                beat &= 2;
            case SNAP_BEAT:
                sixteenth = 0;
            case SNAP_8:
                sixteenth &= 2;
            case SNAP_16:
                sixtyfourth = 0;
            case SNAP_32:
                sixtyfourth &= 2;
            case SNAP_64:
                tick = 0;
            case SNAP_OFF:
                break;
        }
    }

    public int getTicksInBeat() {
        if (resolution == 0) return 0;
        return tick + sixteenth * (resolution / 4) + sixtyfourth * (resolution / 16);
    }

    public String toString() {
        return (1 + bar) + "." + (1 + beat) + "." + (1 + sixteenth) + "." + (1 + sixtyfourth) + "." + (1 + tick);
    }

    public int getBeat() {
        return beat;
    }

    public void setBeat(int beat) {
        this.beat = beat;
    }

    public int getBar() {
        return bar;
    }

    public void setBar(int bar) {
        this.bar = bar;
    }

    public int getTick() {
        return tick;
    }

    public void setTick(int tick) {
        this.tick = tick;
    }

    public int bar = 0;

    public int beat = 0;

    public int sixteenth = 0;

    public int sixtyfourth = 0;

    public int tick = 0;

    private int resolution = 0;

    public static void main(String[] args) {
        int resolution = 96;
        for (int t = 0; t < resolution; t++) {
            SequencePosition pos = new SequencePosition(0, 0, t, resolution);
            System.out.println(t + ": " + pos);
        }
    }
}

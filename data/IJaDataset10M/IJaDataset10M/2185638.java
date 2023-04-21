package ch.visnet.heartbreak;

import java.util.Calendar;

public class SineWave implements FunctionOfTime {

    private final long phase;

    private final double omega;

    private final double amplitude;

    private final double offset;

    public SineWave(double amplitude, double omega, long phase, double offset) {
        this.amplitude = amplitude;
        this.phase = phase;
        this.omega = omega;
        this.offset = offset;
    }

    public SineWave(double amplitude, double omega) {
        this(amplitude, omega, 0, 0);
    }

    public SineWave(double amplitude, double omega, long phase) {
        this(amplitude, omega, phase, 0);
    }

    public double getAt(Calendar time) {
        return offset + amplitude * Math.sin((time.getTimeInMillis() - phase) * 2 * Math.PI / omega);
    }
}

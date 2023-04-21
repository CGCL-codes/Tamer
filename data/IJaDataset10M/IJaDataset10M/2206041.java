package org.progeeks.audio;

/**
 *  Provided with a sample rate, a frequence, and a volume this
 *  will provide sin wave values.
 *
 *  @version   $Revision: 1.1 $
 *  @author    Paul Speed
 */
public class ToneGenerator {

    private double rads;

    private double delta;

    private double amplitude;

    public ToneGenerator(double sampleRate, double freq, double amplitude) {
        double period = sampleRate / freq;
        this.amplitude = amplitude;
        this.delta = (2 * Math.PI) / period;
    }

    public int nextValue() {
        int sin = (int) Math.round(Math.sin(rads) * amplitude);
        rads += delta;
        if (rads > Math.PI * 2) rads -= Math.PI * 2;
        return (sin);
    }
}

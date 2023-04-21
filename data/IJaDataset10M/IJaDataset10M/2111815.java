package ren.tonal;

import java.lang.Math;

public class TuningSystem {

    public static final double[] EQUAL_TEMPERED_12;

    static {
        double[] dt = new double[12];
        for (int i = 0; i < dt.length; i++) {
            dt[i] = Math.pow(2, (i * 1.0) / 12.0);
        }
        EQUAL_TEMPERED_12 = dt;
    }

    public static final double[] DEFAULT_TUNING = EQUAL_TEMPERED_12;

    public static final double DEFAULT_OCTAVES = 1;

    private double octaves;

    private double[] tuning;

    public TuningSystem(double[] tuning, double octaves) {
        this.tuning = tuning;
        this.octaves = octaves;
    }

    public TuningSystem(double[] tuning) {
        this(tuning, DEFAULT_OCTAVES);
    }

    public TuningSystem() {
        this(DEFAULT_TUNING);
    }

    /**
     * returns the number of octaves that the system
     * covers
     */
    public double getOctaves() {
        return octaves;
    }

    public int getStepsPerOctave() {
        return (int) (tuning.length / octaves + 0.5);
    }

    /**
     * Converts an array of cents into ratios
     */
    public double[] centsToRatios(double[] cents) {
        double[] ratios = new double[cents.length];
        for (int i = 0; i < cents.length; i++) {
            ratios[i] = Math.pow(Math.E, (cents[i] / (1200 / Math.log(2))));
        }
        return ratios;
    }

    /**
     * Converts an array of ratios into an array of
     * cents
     */
    public double[] ratiosToCents(double[] ratios) {
        double[] cents = new double[ratios.length];
        for (int i = 0; i < ratios.length; i++) {
            cents[i] = Math.log(ratios[i]) * (1200 / Math.log(2));
        }
        return cents;
    }
}

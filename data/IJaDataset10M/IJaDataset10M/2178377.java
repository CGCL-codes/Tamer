package net.sf.janalogtv;

import java.util.Random;

/**
 * Reception holds information about things that happen to a signal
 * on its way from the transmitter to the receiver.
 * The transmitter is represented by an {@link Input} and the receiver
 * is an {@link AnalogTV}.
 * The simulated effects include ghosting du to signal reflection in cables
 * and trees and frequency errors between channels.
 */
public class Reception {

    /** Ghosting FIR filter length */
    public static final int GHOSTFIR_LEN = 4;

    private Input input;

    private double ofs;

    private double level;

    private double multipath;

    private double freqerr;

    private double[] ghostfir = new double[GHOSTFIR_LEN];

    private double[] ghostfir2 = new double[GHOSTFIR_LEN];

    private double hfloss;

    private Random rng;

    /**
	 * Create a new Reception.
	 * @param input the Input to transmit.
	 * @param level the signal level.
	 */
    public Reception(Input input, double level) {
        this.input = input;
        this.level = level;
        rng = new Random();
    }

    /**
	 * Get the Input.
	 * @return the Input.
	 */
    public Input getInput() {
        return input;
    }

    /**
	 * Get the signal offset.
	 * @return the offset in samples.
	 */
    public int getOffset() {
        return (int) ofs;
    }

    /**
	 * Set the signal offset.
	 * Only useful when multiple Receptions are added together.
	 * @param ofs the new offset in samples.
	 */
    public void setOffset(int ofs) {
        this.ofs = ofs;
    }

    /**
	 * Set how much the offset changes each frame.
	 * @param freqerr offset change per frame.
	 */
    public void setFrequencyError(double freqerr) {
        this.freqerr = freqerr;
    }

    /**
	 * Set the amount by which the signal takes multiple paths between
	 * the antennas. This simulates signal reflections caused by moving trees.
	 * @param multipath the amount of multipath reflections.
	 */
    public void setMultipath(double multipath) {
        this.multipath = multipath;
    }

    /**
	 * Get the signal reception level.
	 * @return the reception level.
	 */
    public double getLevel() {
        return level;
    }

    /**
	 * Set the signal reception level.
	 * @param level the new reception level.
	 */
    public void setLevel(double level) {
        this.level = level;
    }

    /**
	 * Get the amount of high frequency loss.
	 * @return the about of HF loss.
	 */
    public double getHfloss() {
        return hfloss;
    }

    /**
	 * Set the amount of high frequency loss.
	 * @param hfloss the new amount.
	 */
    public void setHfloss(double hfloss) {
        this.hfloss = hfloss;
    }

    double doGhostFir(double s0, double s1, double s2, double s3) {
        return s0 * ghostfir[0] + s1 * ghostfir[1] + s2 * ghostfir[2] + s3 * ghostfir[3];
    }

    double getGhostFirSum() {
        return ghostfir[0] + ghostfir[1] + ghostfir[2] + ghostfir[3];
    }

    /**
	 * Update the state of the reception for the next frame.
	 */
    public void update() {
        if (multipath > 0.0) {
            for (int i = 0; i < GHOSTFIR_LEN; i++) {
                ghostfir2[i] += -(ghostfir2[i] / 16.0) + multipath * (rng.nextDouble() * 0.02 - 0.01);
            }
            if (rng.nextInt(20) == 0) {
                ghostfir2[rng.nextInt(GHOSTFIR_LEN)] = multipath * (rng.nextDouble() * 0.08 - 0.04);
            }
            for (int i = 0; i < GHOSTFIR_LEN; i++) {
                ghostfir[i] = 0.8 * ghostfir[i] + 0.2 * ghostfir2[i];
            }
        } else {
            for (int i = 0; i < GHOSTFIR_LEN; i++) {
                ghostfir[i] = (i >= GHOSTFIR_LEN / 2) ? ((i & 1) == 1 ? 0.04 : -0.08) / GHOSTFIR_LEN : 0.0;
            }
        }
        ofs += freqerr;
    }
}

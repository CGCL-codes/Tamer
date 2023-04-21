package com.jhlabs.image;

import java.awt.*;
import java.awt.image.*;

/**
 * A filter which changes the gain and bias of an image - similar to ContrastFilter.
 */
public class GainFilter extends TransferFilter {

    private float gain = 0.5f;

    private float bias = 0.5f;

    protected float transferFunction(float f) {
        f = ImageMath.gain(f, gain);
        f = ImageMath.bias(f, bias);
        return f;
    }

    /**
     * Set the gain.
     * @param gain the gain
     * @min-value: 0
     * @max-value: 1
     * @see #getGain
     */
    public void setGain(float gain) {
        this.gain = gain;
        initialized = false;
    }

    /**
     * Get the gain.
     * @return the gain
     * @see #setGain
     */
    public float getGain() {
        return gain;
    }

    /**
     * Set the bias.
     * @param bias the bias
     * @min-value: 0
     * @max-value: 1
     * @see #getBias
     */
    public void setBias(float bias) {
        this.bias = bias;
        initialized = false;
    }

    /**
     * Get the bias.
     * @return the bias
     * @see #setBias
     */
    public float getBias() {
        return bias;
    }

    public String toString() {
        return "Colors/Gain...";
    }
}

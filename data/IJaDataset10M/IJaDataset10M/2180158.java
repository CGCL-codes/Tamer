package net.sf.adastra.autopilot.controllers;

/**
 * I provide a basis for all Controllers.
 * 
 * @author David Dunwoody (david.dunwoody@gmail.com)
 * @version $Id: ControllerBase.java 62 2006-10-14 00:47:19Z ddunwoody $
 * 
 */
abstract class ControllerBase implements Controller {

    private float output;

    private float minOutput = -1;

    private float maxOutput = 1;

    public float getMaxOutput() {
        return maxOutput;
    }

    public void setMaxOutput(float maxOutput) {
        if (maxOutput < minOutput) {
            throw new IllegalArgumentException("cannot set maxOutput to " + maxOutput + " as that is less than minOutput of " + minOutput);
        }
        this.maxOutput = maxOutput;
    }

    public float getMinOutput() {
        return minOutput;
    }

    public void setMinOutput(float minOutput) {
        if (minOutput > maxOutput) {
            throw new IllegalArgumentException("cannot set minOutput to " + minOutput + " as that is greater than maxOutput of " + maxOutput);
        }
        this.minOutput = minOutput;
    }

    public float getOutput() {
        return output;
    }

    public void setOutput(float output) {
        this.output = clamp(output, minOutput, maxOutput);
    }

    protected void incrementOutput(float outputIncrement) {
        this.output = clamp(output + outputIncrement, minOutput, maxOutput);
    }

    protected void decrementOutput(float outputDecrement) {
        incrementOutput(-outputDecrement);
    }

    protected float clamp(float value, float min, float max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }
}

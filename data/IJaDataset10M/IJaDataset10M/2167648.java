package com.global360.sketchpadbpmn.graphic.constraints;

/**
 * @author andya
 *
 */
public class Interval {

    private double minimum = 0.0;

    private double maximum = Double.POSITIVE_INFINITY;

    public Interval() {
    }

    public Interval(double min, double max) {
        set(min, max);
    }

    public String toString() {
        return "[" + this.minimum + " <-> " + this.maximum + "]";
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Interval) {
            return this.equals((Interval) other);
        }
        return false;
    }

    public boolean equals(Interval other) {
        if (other != null) {
            return (this.minimum == other.minimum) && (this.maximum == other.maximum);
        }
        return false;
    }

    public boolean isEmpty() {
        return this.minimum == this.maximum;
    }

    public void set(Interval other) {
        this.set(other.getMinimum(), other.getMaximum());
    }

    public void set(double min, double max) {
        if (min <= max) {
            this.minimum = min;
            this.maximum = max;
        } else {
            this.minimum = max;
            this.maximum = min;
        }
    }

    public void setMinimum(double min) {
        set(min, this.maximum);
    }

    public void setMaximum(double max) {
        set(this.minimum, max);
    }

    public double getMinimum() {
        return this.minimum;
    }

    public double getMaximum() {
        return this.maximum;
    }

    public double getSize() {
        return this.maximum - this.minimum;
    }

    public boolean includes(double value) {
        return (value >= this.minimum) && (value <= this.maximum);
    }

    /**
   * @param toInterval
   * @return
   */
    public Interval intersection(Interval other) {
        Interval result = new Interval(0, 0);
        if (other != null) {
            double largestMinimum = Math.max(this.getMinimum(), other.getMinimum());
            double smallestMaximum = Math.min(this.getMaximum(), other.getMaximum());
            if (largestMinimum < smallestMaximum) {
                result.set(largestMinimum, smallestMaximum);
            }
        }
        return result;
    }

    /**
   * @param value
   * @return
   */
    public double constrain(double value) {
        if (value < this.minimum) {
            return this.minimum;
        }
        if (value > this.maximum) {
            return this.maximum;
        }
        return value;
    }

    /**
   * @param value
   * @return
   */
    public float constrain(float value) {
        if (value < this.minimum) {
            return (float) this.minimum;
        }
        if (value > this.maximum) {
            return (float) this.maximum;
        }
        return value;
    }
}

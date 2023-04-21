package com.bluemarsh.benoit.model;

import java.math.BigDecimal;

/**
 * This class holds the set of parameters for rendering a mandelbrot
 * set. It is meant to be a convenient data structure for passing
 * the parameters from one object to another within the system.
 *
 * @author  Nathan Fiedler
 */
public class Parameters implements Cloneable {

    /** The minimum x value of the region. */
    protected BenoitNumber minX;

    /** The minimum y value of the region. */
    protected BenoitNumber minY;

    /** The maximum x value of the region. */
    protected BenoitNumber maxX;

    /** The maximum y value of the region. */
    protected BenoitNumber maxY;

    /**
     * Creates a new set of parameters using the given initial values.
     *
     * @param  minX  minimum X position.
     * @param  maxX  maximum X position.
     * @param  minY  minimum Y position.
     * @param  maxY  maximum Y position.
     */
    public Parameters(BenoitNumber minX, BenoitNumber maxX, BenoitNumber minY, BenoitNumber maxY) {
        if (minX.compareTo(maxX) == 1) {
            this.minX = maxX;
            this.maxX = minX;
        } else {
            this.minX = minX;
            this.maxX = maxX;
        }
        if (minY.compareTo(maxY) == 1) {
            this.minY = maxY;
            this.maxY = minY;
        } else {
            this.minY = minY;
            this.maxY = maxY;
        }
    }

    /**
     * Adjust the parameters to suit the aspect ratio of the view,
     * given by the width and height parameters. This will modify
     * the maxY value to adjust the region height.
     *
     * @param  width   width of the view.
     * @param  height  height of the view.
     */
    public void adjustAspect(int width, int height) {
        BenoitNumber w = BenoitNumber.subtract(maxX, minX);
        BenoitNumber h = BenoitNumber.subtract(maxY, minY);
        if (w.compareTo(h) == 1) {
            double imageRatio = (double) width / (double) height;
            BenoitNumber rrbn = BenoitNumber.divide(w, h);
            double regionRatio = rrbn.doubleValue();
            if (Math.abs(imageRatio - regionRatio) > 0.01) {
                h.multiply(imageRatio);
                maxX = BenoitNumber.add(minX, h);
            }
        } else {
            double imageRatio = (double) height / (double) width;
            BenoitNumber rrbn = BenoitNumber.divide(h, w);
            double regionRatio = rrbn.doubleValue();
            if (Math.abs(imageRatio - regionRatio) > 0.01) {
                w.multiply(imageRatio);
                maxY = BenoitNumber.add(minY, w);
            }
        }
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param  obj  the reference object with which to compare.
     * @return  true if this object is the same as the obj argument;
     *          false otherwise.
     */
    public boolean equals(Object obj) {
        if (obj instanceof Parameters) {
            Parameters pobj = (Parameters) obj;
            return minX.equals(pobj.minX) && minY.equals(pobj.minY) && maxX.equals(pobj.maxX) && maxY.equals(pobj.maxY);
        } else {
            return false;
        }
    }

    /**
     * Gets the maximum X position.
     *
     * @return  value for maximum X.
     */
    public BenoitNumber getMaxX() {
        return maxX;
    }

    /**
     * Gets the maximum Y position.
     *
     * @return  value for maximum Y.
     */
    public BenoitNumber getMaxY() {
        return maxY;
    }

    /**
     * Gets the minimum X position.
     *
     * @return  value for minimum X.
     */
    public BenoitNumber getMinX() {
        return minX;
    }

    /**
     * Gets the minimum Y position.
     *
     * @return  value for minimum Y.
     */
    public BenoitNumber getMinY() {
        return minY;
    }

    /**
     * Gets the number of digits used in the parameter numbers
     *
     * @return  number of digits stored.
     */
    public int getScale() {
        return minX.getScale();
    }

    /**
     * Sets the maximum X position to the passed value.
     *
     * @param  x  new value for maximum X.
     */
    public synchronized void setMaxX(BenoitNumber x) {
        maxX = x;
    }

    /**
     * Sets the maximum Y position to the passed value.
     *
     * @param  y  new value for maximum Y.
     */
    public synchronized void setMaxY(BenoitNumber y) {
        maxY = y;
    }

    /**
     * Sets the minimum X position to the passed value.
     *
     * @param  x  new value for minimum X.
     */
    public synchronized void setMinX(BenoitNumber x) {
        minX = x;
    }

    /**
     * Sets the minimum Y position to the passed value.
     *
     * @param  y  new value for minimum Y.
     */
    public synchronized void setMinY(BenoitNumber y) {
        minY = y;
    }

    /**
     * Sets the number of digits these parameters will store. This
     * automatically converts the number to a BigDecimal, as that is
     * the only number that supports setting a specific scale.
     *
     * @param  numDigits  number of digits to store.
     */
    public void setScale(int numDigits) {
        minX.setScale(numDigits);
        maxX.setScale(numDigits);
        minY.setScale(numDigits);
        maxY.setScale(numDigits);
    }

    /**
     * Sets the precision of this number. Can be set to either
     * DOUBLE_TYPE or BIG_TYPE.
     *
     * @param  type  the new precision of this number.
     */
    public void setType(int type) {
        minX.setType(type);
        maxX.setType(type);
        minY.setType(type);
        maxY.setType(type);
    }

    /**
     * Returns the string representation of these parameters.
     *
     * @return  the string representation of these parameters.
     */
    public String toString() {
        return new String("Parameters=[minX=" + minX + ", maxX=" + maxX + ", minY=" + minY + ", maxY=" + maxY + "]");
    }

    /**
     * Calculate the position within the set of the given x value.
     * The width of the region that x falls into is necessary to
     * scale the result.
     *
     * @param  x  position within the region.
     * @param  w  width of the region.
     * @return  position within the set.
     */
    public BenoitNumber transformX(int x, int w) {
        BenoitNumber unknown = (BenoitNumber) maxX.clone();
        unknown.subtract(minX);
        unknown.multiply((double) x);
        unknown.divide((double) w);
        unknown.add(minX);
        return unknown;
    }

    /**
     * Calculate the position within the set of the given y value.
     * The height of the region that y falls into is necessary to
     * scale the result.
     *
     * @param  y  position within the region.
     * @param  h  height of the region.
     * @return  position within the set.
     */
    public BenoitNumber transformY(int y, int h) {
        BenoitNumber unknown = (BenoitNumber) maxY.clone();
        unknown.subtract(minY);
        unknown.multiply((double) y);
        unknown.divide((double) h);
        unknown.add(minY);
        return unknown;
    }
}

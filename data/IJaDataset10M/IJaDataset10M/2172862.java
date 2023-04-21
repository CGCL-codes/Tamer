package it.tukano.jps.math;

/**
 * An immutable 3 element tuple
 */
public final class NTuple3 {

    private final Number X;

    private final Number Y;

    private final Number Z;

    /**
     * Initializes this three element tuple
     * @param x the x value of this tuple
     * @param y the y value of this tuple
     * @param z the z value of this tuple
     */
    public NTuple3(Number x, Number y, Number z) {
        X = x;
        Y = y;
        Z = z;
    }

    /**
     * Returns the x value of this tuple
     * @return the x value of this tuple
     * <p><i>Memory consistency effects:</i> access to final field.</p>
     */
    public Number getX() {
        return X;
    }

    /**
     * Returns the y value of this tuple
     * @return the y value of this tuple
     * <p><i>Memory consistency effects:</i> access to final field.</p>
     */
    public Number getY() {
        return Y;
    }

    /**
     * Returns the z value of this tuple
     * @return the z value of this tuple
     * <p><i>Memory consistency effects:</i> access to final field.</p>
     */
    public Number getZ() {
        return Z;
    }
}

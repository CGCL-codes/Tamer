package org.jquantlib.math.interpolation;

/**
 * This class is intended to implement the default behavior of an Extrapolator.
 * 
 * @author Richard Gomes
 */
public class DefaultExtrapolator implements Extrapolator {

    private boolean extrapolate;

    public DefaultExtrapolator() {
        this.extrapolate = false;
    }

    /**
	 * enable extrapolation in subsequent calls
	 */
    public void enableExtrapolation() {
        extrapolate = true;
    }

    /**
	 * disable extrapolation in subsequent calls
	 */
    public void disableExtrapolation() {
        extrapolate = false;
    }

    /**
	 * tells whether extrapolation is enabled
	 */
    public final boolean allowsExtrapolation() {
        return extrapolate;
    }
}

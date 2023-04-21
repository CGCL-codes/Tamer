package com.gregdennis.drej;

import javax.vecmath.GVector;

/**
 * A total scalar function.
 * 
 * @author Greg Dennis (gdennis@mit.edu)
 */
public interface Function {

    /**
     * Evaluates the function at the specified point.
     */
    public double eval(GVector x);
}

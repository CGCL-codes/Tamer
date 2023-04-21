package org.dmikis.jmmengine.models;

/**
 * @author dmikis
 * @version 0.1
 * 
 * Abstract function.
 */
public interface Function {

    /**
     * Get function value at point <i>x</i>.
     * 
     * @param x function argument
     * @return function value
     */
    public double f(double x);
}

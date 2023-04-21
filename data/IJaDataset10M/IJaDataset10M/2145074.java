package org.expasy.jpl.commons.base;

/**
 * Any class that can be evaluate as a double needs to implement this interface.
 * 
 * @author nikitin
 * 
 * @version 1.0
 * 
 */
public interface Evaluable {

    /** evaluation of the {@code Evaluable} object */
    double eval();
}

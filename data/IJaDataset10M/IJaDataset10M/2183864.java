package com.platonov.network.core.input;

import java.io.Serializable;

/**
 * Or function for vector
 * <p/>
 * User: Platonov
 */
public class Or extends SummingFunction implements Serializable {

    private static final long serialVersionUID = 1L;

    public double getOutput(double[] inputVector) {
        boolean result = false;
        for (double input : inputVector) {
            result = result || (input >= 0.5d);
        }
        return result ? 1d : 0d;
    }
}

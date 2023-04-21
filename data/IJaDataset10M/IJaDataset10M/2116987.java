package net.sf.linns.model.activation;

import net.sf.linns.model.IActivationFunction;

/**
 * 
 * @author Christoph Bimminger
 * @date   30.03.2008
 *
 */
public class IdentityFunction implements IActivationFunction {

    public double getA(double net) {
        return (net);
    }

    public String toString() {
        return "Act_Identity";
    }

    public String getJavaString() {
        return "x";
    }
}

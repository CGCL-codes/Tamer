package com.rapidminer.operator.generator;

/** The label is positive if att1 < 0 or att2 > 0 and att3 < 0.
 *  
 *  @author Ingo Mierswa
 */
public class InteractionClassificationFunction extends ClassificationFunction {

    public double calculate(double[] att) throws FunctionException {
        if (att.length < 3) throw new FunctionException("Interactive classification function", "needs at least 3 attributes!");
        if ((att[0] < 0.0d) || (att[1] > 0.0d) && (att[2] < 0.0d)) return getLabel().getMapping().mapString("positive"); else return getLabel().getMapping().mapString("negative");
    }
}

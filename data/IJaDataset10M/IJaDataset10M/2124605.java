package com.rapidminer.operator.generator;

/** The label is positive if the sum of all arguments is positive. 
 * 
 *  @author Ingo Mierswa
 */
public class SumClassificationFunction extends ClassificationFunction {

    public double calculate(double[] args) {
        double sum = 0.0d;
        for (int i = 0; i < args.length; i++) sum += args[i];
        return (sum > 0 ? getLabel().getMapping().mapString("positive") : getLabel().getMapping().mapString("negative"));
    }
}

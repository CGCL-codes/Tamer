package com.rapidminer.operator.generator;

/** 
 * The label is att1*att2*att3 + att1*att2 + att2*att2. 
 * 
 * @author Ingo Mierswa
 */
public class NonLinearFunction extends RegressionFunction {

    public double calculate(double[] att) throws FunctionException {
        if (att.length < 3) throw new FunctionException("Non linear function", "needs at least 3 attributes!");
        return (att[0] * att[1] * att[2] + att[0] * att[1] + att[1] * att[1]);
    }
}

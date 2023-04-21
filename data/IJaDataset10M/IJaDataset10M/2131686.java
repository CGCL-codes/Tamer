package jmathlib.toolbox.trigonometric;

import jmathlib.core.functions.ExternalElementWiseFunction;
import jmathlib.toolbox.jmathlib.matrix.log;
import jmathlib.toolbox.jmathlib.matrix.sqrt;

public class acosh extends ExternalElementWiseFunction {

    public acosh() {
        name = "acosh";
    }

    /**Calculates the inverse hyperbolic cosine of a complex number
    @param arg = the angle as an array of double
    @return the result as an array of double*/
    public double[] evaluateValue(double[] arg) {
        double result[] = new double[2];
        double re = arg[REAL];
        double im = arg[IMAG];
        result[REAL] = ((re * re) - (im * im)) - 1.0;
        result[IMAG] = ((re * im) + (im * re)) - 0.0;
        sqrt sqrtF = new sqrt();
        result = sqrtF.evaluateValue(result);
        result[REAL] = re + result[REAL];
        result[IMAG] = im + result[IMAG];
        log logF = new log();
        result = logF.evaluateValue(result);
        return result;
    }
}

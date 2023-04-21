package jmathlib.toolbox.trigonometric;

import jmathlib.core.functions.ExternalElementWiseFunction;
import jmathlib.core.tokens.numbertokens.DoubleNumberToken;

public class acot extends ExternalElementWiseFunction {

    public acot() {
        name = "acot";
    }

    /**trigonometric functions -calculates the inverse cotangent
     * @param  double value array
     * @return the result as a double array
     */
    public double[] evaluateValue(double[] arg) {
        DoubleNumberToken num = new DoubleNumberToken();
        atan atanFunc = new atan();
        double[] temp = num.divide(new double[] { 1, 0 }, arg);
        double[] result = atanFunc.evaluateValue(temp);
        return result;
    }
}

package jmathlib.toolbox.jmathlib.matrix;

import jmathlib.core.tokens.Token;
import jmathlib.core.tokens.OperandToken;
import jmathlib.core.tokens.numbertokens.DoubleNumberToken;
import jmathlib.core.functions.ExternalFunction;
import jmathlib.core.interpreter.GlobalValues;

/**An external function that return 1.0 is real elements of the argument are zero  */
public class isimaginary extends ExternalFunction {

    /**return a  matrix 
	* @param operands[0] = matrix 
	* @return 1.0 if all elements of the argument are nonzero  */
    public OperandToken evaluate(Token[] operands, GlobalValues globals) {
        if (getNArgIn(operands) != 1) throwMathLibException("isimaginary: number of arguments != 1");
        if (!(operands[0] instanceof DoubleNumberToken)) throwMathLibException("isimaginary: works on numbers only");
        double[][] a_r = ((DoubleNumberToken) operands[0]).getReValues();
        int a_dy = a_r.length;
        int a_dx = a_r[0].length;
        boolean imagB = true;
        for (int xi = 0; xi < a_dx; xi++) {
            for (int yi = 0; yi < a_dy; yi++) {
                if (a_r[yi][xi] != 0.0) imagB = false;
            }
        }
        if (imagB) return new DoubleNumberToken(1.0); else return new DoubleNumberToken(0.0);
    }
}

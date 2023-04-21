package jmathlib.toolbox.general;

import jmathlib.core.tokens.*;
import jmathlib.core.tokens.numbertokens.DoubleNumberToken;
import jmathlib.core.functions.ExternalFunction;
import jmathlib.core.interpreter.GlobalValues;

public class ndims extends ExternalFunction {

    /**returns the dimensions of an array 1,2,3,4-dimensional 

	*/
    public OperandToken evaluate(Token[] operands, GlobalValues globals) {
        if (getNArgIn(operands) != 1) throwMathLibException("ndims: number of input arguments != 1");
        if (!(operands[0] instanceof DoubleNumberToken)) throwMathLibException("ndims: works only on numbers");
        int n = ((DoubleNumberToken) operands[0]).getSize().length;
        return new DoubleNumberToken(n);
    }
}

package jmathlib.toolbox.general;

import jmathlib.core.tokens.numbertokens.DoubleNumberToken;
import jmathlib.core.tokens.*;
import jmathlib.core.functions.ExternalFunction;
import jmathlib.core.interpreter.GlobalValues;

/**An external function which checks if the argument is numeric*/
public class isglobal extends ExternalFunction {

    public OperandToken evaluate(Token[] operands, GlobalValues globals) {
        if (getNArgIn(operands) != 1) throwMathLibException("isglobal: number of arguments != 1");
        if (!(operands[0] instanceof VariableToken)) throwMathLibException("isglobal: only works on variables");
        VariableToken var = (VariableToken) operands[0];
        String name = var.getName();
        if (globals.getVariable(name).isGlobal()) return new DoubleNumberToken(1.0); else return new DoubleNumberToken(0.0);
    }
}

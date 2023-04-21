package jmathlib.toolbox.io;

import jmathlib.core.tokens.*;
import jmathlib.core.tokens.numbertokens.DoubleNumberToken;
import jmathlib.core.functions.ExternalFunction;
import jmathlib.core.interpreter.GlobalValues;
import java.io.*;

public class isdirectory extends ExternalFunction {

    public OperandToken evaluate(Token[] operands, GlobalValues globals) {
        if (getNArgIn(operands) != 1) throwMathLibException("isdirectory: number of arguments != 1");
        if (!(operands[0] instanceof CharToken)) throwMathLibException("isdirectory: argument must be a string");
        String name = ((CharToken) operands[0]).toString();
        try {
            File dir = new File(globals.getWorkingDirectory(), name);
            if (dir.isDirectory()) return new DoubleNumberToken(1); else return new DoubleNumberToken(0);
        } catch (Exception e) {
            throwMathLibException("isdirectory: IO exception");
        }
        return null;
    }
}

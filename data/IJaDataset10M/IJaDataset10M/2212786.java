package jmathlib.toolbox.jmathlib.graphics;

import jmathlib.core.tokens.*;
import jmathlib.core.tokens.numbertokens.DoubleNumberToken;
import jmathlib.core.functions.ExternalFunction;
import jmathlib.core.graphics.*;
import jmathlib.core.interpreter.GlobalValues;
import java.awt.Color;

public class get extends ExternalFunction {

    public OperandToken evaluate(Token[] operands, GlobalValues globals) {
        double x = 0;
        if (getNArgIn(operands) > 2) throwMathLibException("get: number of arguments >2");
        if (getNArgIn(operands) >= 1) {
            if (!(operands[0] instanceof DoubleNumberToken)) throwMathLibException("get: argument must be a number");
            x = ((DoubleNumberToken) operands[0]).getValueRe();
        }
        HandleObject ho = null;
        try {
            ho = HandleObject.getHandleObject((int) x);
        } catch (Exception e) {
        }
        if (getNArgIn(operands) == 1) {
            globals.getInterpreter().displayText("");
            ho.show(globals.getInterpreter());
            return null;
        } else if (getNArgIn(operands) == 2) {
            if (!(operands[1] instanceof CharToken)) throwMathLibException("get: second argument must be a string");
            String prop = ((CharToken) operands[1]).toString();
            Object obj = null;
            try {
                obj = HandleObject.getHandleObject((int) x).getProperty(prop).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (obj instanceof Boolean) {
                boolean b = ((Boolean) obj).booleanValue();
                if (b) return new DoubleNumberToken(1); else return new DoubleNumberToken(0);
            } else if (obj instanceof String) {
                return new CharToken(((String) obj).toString());
            } else if (obj instanceof Number) {
                return new DoubleNumberToken(((Number) obj).intValue());
            } else if (obj instanceof double[]) {
                double[] d = ((double[]) obj);
                return new DoubleNumberToken(new double[][] { d });
            } else if (obj instanceof double[][]) {
                double[][] d = ((double[][]) obj);
                return new DoubleNumberToken(d);
            } else if (obj instanceof Color) {
                Color c = ((Color) obj);
                double[] d = new double[3];
                d[0] = (double) c.getRed() / 255.0;
                d[1] = (double) c.getGreen() / 255.0;
                d[2] = (double) c.getBlue() / 255.0;
                return new DoubleNumberToken(new double[][] { d });
            } else throwMathLibException("get: unknown return value ");
        }
        return null;
    }
}

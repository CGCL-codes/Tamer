package jmathlib.toolbox.jmathlib.graphics.graph3d;

import jmathlib.core.tokens.*;
import jmathlib.core.functions.ExternalFunction;
import jmathlib.core.tokens.numbertokens.DoubleNumberToken;
import jmathlib.core.graphics.*;
import jmathlib.core.graphics.axes.*;
import jmathlib.core.interpreter.GlobalValues;

/** rotate*/
public class rotate extends ExternalFunction {

    public OperandToken evaluate(Token[] operands, GlobalValues globals) {
        double phiX = 0;
        double phiY = 0;
        double phiZ = 0;
        if (getNArgIn(operands) != 3) throwMathLibException("rotate: number of arguments != 3"); else if ((operands[0] instanceof DoubleNumberToken) && (operands[1] instanceof DoubleNumberToken) && (operands[2] instanceof DoubleNumberToken)) {
            phiX = (((DoubleNumberToken) operands[0]).getReValues()[0][0]);
            phiY = (((DoubleNumberToken) operands[1]).getReValues()[0][0]);
            phiZ = (((DoubleNumberToken) operands[2]).getReValues()[0][0]);
        } else {
            throwMathLibException("rotate: wrong argument");
        }
        ((Axes3DObject) globals.getGraphicsManager().getCurrentFigure().getCurrentAxes()).rotate(phiX, phiY, phiZ);
        globals.getGraphicsManager().getCurrentFigure().repaint();
        return null;
    }
}

package com.yerihyo.yeritools.rserve;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPDouble;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.RList;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

public class RToolkit {

    public static String createRStringOfVariable(String variableName, int[] values) {
        int length = values.length;
        StringBuffer rStringOfVariable = new StringBuffer();
        rStringOfVariable.append(variableName).append(" <- c(");
        for (int i = 0; i < length; i++) {
            if (i != 0) {
                rStringOfVariable.append(", ");
            }
            rStringOfVariable.append(values[i]);
        }
        rStringOfVariable.append(")");
        return rStringOfVariable.toString();
    }

    public static String createRStringOfVariable(String variableName, double[] values) {
        int length = values.length;
        StringBuffer rStringOfVariable = new StringBuffer();
        rStringOfVariable.append(variableName).append(" <- c(");
        for (int i = 0; i < length; i++) {
            if (i != 0) {
                rStringOfVariable.append(", ");
            }
            rStringOfVariable.append(values[i]);
        }
        rStringOfVariable.append(")");
        return rStringOfVariable.toString();
    }

    public static <T> String createRStringOfVariable(String variableName, T[] values) {
        int length = values.length;
        StringBuffer rStringOfVariable = new StringBuffer();
        rStringOfVariable.append(variableName).append(" <- c(");
        for (int i = 0; i < length; i++) {
            if (i != 0) {
                rStringOfVariable.append(", ");
            }
            rStringOfVariable.append(values[i]);
        }
        rStringOfVariable.append(")");
        return rStringOfVariable.toString();
    }

    public static double ttest(int[] values1, int[] values2) throws RserveException, REXPMismatchException {
        int values1Length = values1.length;
        int values2Length = values2.length;
        if (values1Length != values2Length) {
            throw new IllegalArgumentException("Length of inputs (" + values1Length + ", " + values2Length + ") are different!");
        }
        String rStringOfVariableX = createRStringOfVariable("x", values1);
        String rStringOfVariableY = createRStringOfVariable("y", values2);
        RConnection c = new RConnection();
        c.eval(rStringOfVariableX);
        c.eval(rStringOfVariableY);
        REXP rexp = c.eval("out <- t.test(x, y, mu = 0, paired = TRUE, var.equal = FALSE,) ");
        RList rList = rexp.asList();
        REXPDouble rexpDouble = (REXPDouble) rList.get("p.value");
        double pValue = rexpDouble.asDouble();
        c.close();
        return pValue;
    }

    public static double ttest(double[] values1, double[] values2) throws RserveException, REXPMismatchException {
        int values1Length = values1.length;
        int values2Length = values2.length;
        if (values1Length != values2Length) {
            throw new IllegalArgumentException("Length of inputs (" + values1Length + ", " + values2Length + ") are different!");
        }
        String rStringOfVariableX = createRStringOfVariable("x", values1);
        String rStringOfVariableY = createRStringOfVariable("y", values2);
        RConnection c = new RConnection();
        c.eval(rStringOfVariableX);
        c.eval(rStringOfVariableY);
        REXP rexp = c.eval("out <- t.test(x, y, mu = 0, paired = TRUE, var.equal = FALSE,) ");
        RList rList = rexp.asList();
        REXPDouble rexpDouble = (REXPDouble) rList.get("p.value");
        double pValue = rexpDouble.asDouble();
        c.close();
        return pValue;
    }

    public static <T> double ttest(T[] values1, T[] values2) throws RserveException, REXPMismatchException {
        int values1Length = values1.length;
        int values2Length = values2.length;
        if (values1Length != values2Length) {
            throw new IllegalArgumentException("Length of inputs (" + values1Length + ", " + values2Length + ") are different!");
        }
        String rStringOfVariableX = createRStringOfVariable("x", values1);
        String rStringOfVariableY = createRStringOfVariable("y", values2);
        RConnection c = new RConnection();
        c.eval(rStringOfVariableX);
        c.eval(rStringOfVariableY);
        REXP rexp = c.eval("out <- t.test(x, y, mu = 0, paired = TRUE, var.equal = FALSE,) ");
        RList rList = rexp.asList();
        REXPDouble rexpDouble = (REXPDouble) rList.get("p.value");
        double pValue = rexpDouble.asDouble();
        c.close();
        return pValue;
    }
}

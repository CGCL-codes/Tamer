package jat.matvec.function;

import jat.matvec.data.Matrix;
import jat.matvec.function.expressionParser.Evaluator;

public class MatrixFunctionExpression extends MatrixFunction {

    private String expression;

    private String[] variables;

    private Evaluator ME = new Evaluator();

    public MatrixFunctionExpression(String exp, String[] vars) {
        argNumber = vars.length;
        setFunction(exp, vars);
    }

    public MatrixFunctionExpression(String exp, String vars) {
        argNumber = 1;
        String[] variable = new String[1];
        variable[0] = vars;
        setFunction(exp, variable);
    }

    private void setFunction(String exp, String[] vars) {
        expression = exp;
        variables = vars;
    }

    private void setVariableValues(Matrix[] values) {
        for (int i = 0; i < variables.length; i++) {
            ME.addVariable(variables[i], values[i]);
        }
        ME.setExpression(expression);
    }

    private void setVariableValues(Matrix value) {
        ME.addVariable(variables[0], value);
        ME.setExpression(expression);
    }

    public Matrix eval(Matrix[] values) {
        checkArgNumber(values.length);
        setVariableValues(values);
        return (Matrix) (ME.getValue());
    }

    public Matrix eval(Matrix value) {
        checkArgNumber(1);
        setVariableValues(value);
        return (Matrix) (ME.getValue());
    }
}

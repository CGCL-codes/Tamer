package org.gvsig.symbology.fmap.rendering.filter.operations;

import java.util.ArrayList;
import java.util.Hashtable;
import com.hardcode.gdbms.engine.values.Value;
import com.iver.cit.gvsig.fmap.Messages;

/**
 * Implements the funcionality of the multiplication operator
 *
 * @author Pepe Vidal Salvador - jose.vidal.salvador@iver.es
 *
 */
public class MultOperator extends Operator {

    private ArrayList<Expression> arguments = new ArrayList<Expression>();

    public String getName() {
        return OperationTags.MULT_OP;
    }

    public MultOperator(Hashtable<String, Value> symbol_table) {
        super(symbol_table);
    }

    public Object evaluate() throws ExpressionException {
        Double doubleResult = null;
        Integer intResult = null;
        try {
            intResult = (Integer) ((Expression) arguments.get(0)).evaluate();
            if (intResult == null) return new Integer(0);
            if (arguments.size() == 1) return intResult;
            for (int i = 1; i < arguments.size(); i++) {
                Expression function = (Expression) arguments.get(i);
                Integer eval = (Integer) function.evaluate();
                if (eval == null) eval = new Integer(0);
                intResult *= eval;
            }
            return intResult;
        } catch (ClassCastException e) {
            doubleResult = new Double(((Expression) arguments.get(0)).evaluate().toString());
            if (doubleResult == null) return new Double(0);
            if (arguments.size() == 1) return doubleResult;
            for (int i = 1; i < arguments.size(); i++) {
                Expression function = (Expression) arguments.get(i);
                Object eval = function.evaluate();
                if (eval == null) eval = new Double(0);
                doubleResult *= new Double(eval.toString());
            }
            return doubleResult;
        }
    }

    public void addArgument(int i, Expression arg) {
        arguments.add(i, arg);
    }

    public String getPattern() {
        return "(" + Messages.getString(OperationTags.OPERAND) + OperationTags.MULT_OP + Messages.getString(OperationTags.OPERAND) + ")\n" + Messages.getString(OperationTags.OPERAND) + " = " + Messages.getString(OperationTags.NUMERIC_VALUE);
    }

    public ArrayList<Expression> getArguments() {
        return arguments;
    }

    public void setArguments(ArrayList<Expression> arguments) {
        this.arguments = arguments;
    }

    public void check() throws ExpressionException {
        for (int i = 0; i < arguments.size(); i++) {
            if (!(arguments.get(i) instanceof NumericalConstant)) throw new ExpressionException(ExpressionException.CLASS_CASTING_EXCEPTION);
        }
    }
}

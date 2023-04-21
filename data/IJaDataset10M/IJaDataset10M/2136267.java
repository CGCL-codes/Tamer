package org.gvsig.symbology.fmap.rendering.filter.operations;

import java.util.ArrayList;
import java.util.Hashtable;
import com.hardcode.gdbms.engine.values.Value;
import com.iver.cit.gvsig.fmap.Messages;

/**
 * Implements the funcionality of a division operator
 *
 * @author Pepe Vidal Salvador - jose.vidal.salvador@iver.es
 *
 */
public class DivOperator extends Operator {

    private ArrayList<Expression> arguments = new ArrayList<Expression>();

    public String getName() {
        return OperationTags.DIV_OP;
    }

    public DivOperator(Hashtable<String, Value> symbol_table) {
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
                Integer value2 = ((Integer) function.evaluate());
                if (value2 == null) value2 = new Integer(1);
                intResult /= value2;
            }
            return intResult;
        } catch (ClassCastException e) {
            Object value1 = ((Expression) arguments.get(0)).evaluate();
            if (value1 == null) return new Double(0);
            doubleResult = new Double(value1.toString());
            if (arguments.size() == 1) return doubleResult;
            for (int i = 1; i < arguments.size(); i++) {
                Expression function = (Expression) arguments.get(i);
                Object value2 = ((Expression) function).evaluate();
                if (value2 == null) {
                    value2 = new Double(1);
                }
                doubleResult /= new Double(((Expression) function).evaluate().toString());
            }
            return doubleResult;
        }
    }

    public void addArgument(int i, Expression arg) {
        arguments.add(i, arg);
    }

    public String getPattern() {
        return "(" + Messages.getString(OperationTags.OPERAND) + OperationTags.DIV_OP + Messages.getString(OperationTags.OPERAND) + ")\n" + Messages.getString(OperationTags.OPERAND) + " = " + Messages.getString(OperationTags.NUMERIC_VALUE);
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
            Double doubleResult = new Double(((Expression) arguments.get(i)).evaluate().toString());
            if ((Double.isInfinite(doubleResult)) || (Double.isNaN(doubleResult)) || (i != 0 && doubleResult.equals(0.0))) throw new ExpressionException(ExpressionException.DIVIDED_BY_CERO);
        }
    }
}

package r2q2.processing.expression.operators;

import java.util.HashMap;
import java.util.Iterator;
import org.jdom.Element;
import r2q2.processing.expression.Expression;
import r2q2.processing.expression.ExpressionFactory;
import r2q2.processing.expression.InvalidArgumentsException;
import r2q2.variable.ItemVariable;
import r2q2.variable.TransitoryVariable;
import r2q2.variable.ItemVariable.BaseType;
import r2q2.variable.ItemVariable.Cardinality;

/**
 * 
 * subtract operator expression
 * 
 * constraints: 
 *   takes two sub expressions of numerical base type
 *   sub expressions must have single cardinality
 *   returns a single float OR a single integer if both expressions are integer base type
 *   if either sub expression is NULL, NULL is returned
 */
public class Subtract implements Expression {

    private Expression subpart[];

    public Subtract(Element _e) throws InvalidArgumentsException {
        subpart = new Expression[2];
        ExpressionFactory of = ExpressionFactory.getInstance();
        Iterator children = _e.getChildren().iterator();
        int i = 0;
        try {
            while (children.hasNext()) {
                if (i < 2) {
                    subpart[i] = of.makeExpression((Element) children.next());
                }
                i++;
            }
        } catch (InvalidArgumentsException iae) {
            throw new InvalidArgumentsException("Error occured in building childrens expression: ", iae);
        }
        if (i < 2 || i > 2) throw new InvalidArgumentsException("Error: usage violation, 'subtract' must be called with exactly 2 argument rather than the " + i + " arguments specified");
    }

    public ItemVariable eval(HashMap<String, ItemVariable> vars) throws InvalidArgumentsException {
        try {
            ItemVariable test1 = subpart[0].eval(vars);
            ItemVariable test2 = subpart[1].eval(vars);
            if (test1 == null || test2 == null) return null;
            if (test1.card != Cardinality.single || test2.card != Cardinality.single) throw new InvalidArgumentsException("Error: can only take two, single cardinality numerics as arguements, aborting");
            if ((test1.varType == BaseType.afloat || test1.varType == BaseType.integer) && (test2.varType == BaseType.afloat || test2.varType == BaseType.integer)) {
                if (test1.varType == BaseType.integer && test2.varType == BaseType.integer) {
                    return new TransitoryVariable(BaseType.integer, Cardinality.single, new Integer(((Integer) test1.value).intValue() - ((Integer) test2.value).intValue()));
                } else {
                    double d1;
                    double d2;
                    if (test1.varType == BaseType.afloat) d1 = ((Double) test1.value).doubleValue(); else d1 = ((Integer) test1.value).doubleValue();
                    if (test2.varType == BaseType.afloat) d2 = ((Double) test2.value).doubleValue(); else d2 = ((Integer) test2.value).doubleValue();
                    return new TransitoryVariable(BaseType.afloat, Cardinality.single, new Double(d1 - d2));
                }
            } else throw new InvalidArgumentsException("Error: non-numeric, or unsupported numeric types being used, aborting");
        } catch (InvalidArgumentsException iae) {
            throw new InvalidArgumentsException("Error: evaluation of container yielded unexpected exception", iae);
        }
    }
}

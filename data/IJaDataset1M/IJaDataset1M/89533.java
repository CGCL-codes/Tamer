package r2q2.processing.expression;

import java.util.HashMap;
import java.util.Random;
import org.jdom.Element;
import r2q2.processing.expression.Expression;
import r2q2.processing.expression.InvalidArgumentsException;
import r2q2.variable.ItemVariable;
import r2q2.variable.TransitoryVariable;
import r2q2.variable.ItemVariable.BaseType;
import r2q2.variable.ItemVariable.Cardinality;

/**
 * 
 * RandomInteger operator expression
 * 
 * constraints: 
 *   generates a random integer within a range and to a given step size
 */
public class RandomInteger implements Expression {

    private static Random rGen = null;

    private static Random getRandom() {
        if (rGen == null) rGen = new Random();
        return rGen;
    }

    int min = 0;

    int max;

    int step = 1;

    public RandomInteger(Element _e) throws InvalidArgumentsException {
        if (_e.getAttributeValue("min") != null) min = Integer.parseInt(_e.getAttributeValue("min"));
        if (_e.getAttributeValue("max") != null) max = Integer.parseInt(_e.getAttributeValue("max"));
        if (_e.getAttributeValue("step") != null) step = Integer.parseInt(_e.getAttributeValue("step"));
        if (step == 0) step = 1;
    }

    public ItemVariable eval(HashMap<String, ItemVariable> vars) throws InvalidArgumentsException {
        return new TransitoryVariable(BaseType.integer, Cardinality.single, new Integer((getRandom().nextInt((max - min) / step) * step) + min));
    }
}

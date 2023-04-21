package r2q2.processing.expression.operators;

import java.util.HashMap;
import java.util.Iterator;
import org.jdom.Element;
import r2q2.processing.expression.Expression;
import r2q2.processing.expression.ExpressionFactory;
import r2q2.processing.expression.InvalidArgumentsException;
import r2q2.variable.ItemVariable;

/**
 * 
 * durationLT operator expression
 * 
 * constraints: 
 *  ...
 *  
 *  CURRENTLY NOT IMPLEMENTED!!!!
 */
public class DurationLT implements Expression {

    private Expression subpart[];

    public DurationLT(Element _e) throws InvalidArgumentsException {
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
        if (i < 2 || i > 2) throw new InvalidArgumentsException("Error: usage violation, 'lt' must be called with exactly 2 argument rather than the " + i + " arguments specified");
    }

    public ItemVariable eval(HashMap<String, ItemVariable> vars) throws InvalidArgumentsException {
        throw new InvalidArgumentsException("Error: due to confusing definition this method is not implemented within this version, aborting");
    }
}

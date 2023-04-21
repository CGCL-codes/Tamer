package org.jaxen.function;

import java.util.List;
import org.jaxen.Context;
import org.jaxen.Function;
import org.jaxen.FunctionCallException;

/**
 * <p><b>4.1</b> <code><i>number</i> position()</code></p>
 *
 * 
 * <blockquote src="http://www.w3.org/TR/xpath">
 * The position function returns a number equal to the context position from the expression evaluation context.
 * </blockquote>
 * 
 * @author bob mcwhirter (bob @ werken.com)
 * @see <a href="http://www.w3.org/TR/xpath#function-position" target="_top">Section 4.1 of the XPath Specification</a>
 */
public class PositionFunction implements Function {

    /**
     * Create a new <code>PositionFunction</code> object.
     */
    public PositionFunction() {
    }

    /**
     * Returns the position of the context node in the context node-set.
     * 
     * @param context the context at the point in the
     *         expression where the function is called
     * @param args an empty list
     * 
     * @return a <code>Double</code> containing the context position
     * 
     * @throws FunctionCallException if <code>args</code> is not empty
     * 
     * @see Context#getSize()
     */
    public Object call(Context context, List args) throws FunctionCallException {
        if (args.size() == 0) {
            return evaluate(context);
        }
        throw new FunctionCallException("position() does not take any arguments.");
    }

    /**
     * 
     * Returns the position of the context node in the context node-set.
     * 
     * @param context the context at the point in the
     *         expression where the function is called
     * 
     * @return a <code>Double</code> containing the context position
     * 
     * @see Context#getPosition()
     */
    public static Double evaluate(Context context) {
        return new Double(context.getPosition());
    }
}

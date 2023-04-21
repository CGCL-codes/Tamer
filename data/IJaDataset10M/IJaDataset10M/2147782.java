package org.jaxen.function;

import java.util.List;
import org.jaxen.Context;
import org.jaxen.Function;
import org.jaxen.FunctionCallException;
import org.jaxen.Navigator;

/**
 *  <p><b>4.2</b> <code><i>boolean</i> contains(<i>string</i>,<i>string</i>)</code></p>
 *  
 * <blockquote src="http://www.w3.org/TR/xpath">
 * The <b>contains</b> function returns true if the first argument 
 * string contains the second argument string, and otherwise returns false.
 * </blockquote>
 * 
 * @author bob mcwhirter (bob @ werken.com)
 * 
 * @see <a href="http://www.w3.org/TR/xpath#function-contains">Section 4.2 of the XPath Specification</a>
 */
public class ContainsFunction implements Function {

    /**
     * Create a new <code>ContainsFunction</code> object.
     */
    public ContainsFunction() {
    }

    /** 
     * <p>
     *  Returns true if the string-value of the 
     *  first item in <code>args</code> contains string-value of the second 
     *  item; false otherwise.
     *  If necessary one or both items are converted to a string as if by the XPath
     *  <code>string()</code> function.
     * </p>
     *
     * @param context the context at the point in the
     *         expression when the function is called
     * @param args a list containing exactly two items
     * 
     * @return the result of evaluating the function; 
     *     <code>Boolean.TRUE</code> or <code>Boolean.FALSE</code>
     * 
     * @throws FunctionCallException if <code>args</code> does not have exactly two items
     */
    public Object call(Context context, List args) throws FunctionCallException {
        if (args.size() == 2) {
            return evaluate(args.get(0), args.get(1), context.getNavigator());
        }
        throw new FunctionCallException("contains() requires two arguments.");
    }

    /** 
     * <p>Returns true if the first string contains the second string; false otherwise.
     * If necessary one or both arguments are converted to a string as if by the XPath
     * <code>string()</code> function.
     * </p>
     * 
     * @param strArg the containing string
     * @param matchArg the contained string
     * @param nav used to calculate the string-values of the first two arguments
     * 
     * @return <code>Boolean.TRUE</code> if true if the first string contains 
     *     the second string; <code>Boolean.FALSE</code> otherwise.
     */
    public static Boolean evaluate(Object strArg, Object matchArg, Navigator nav) {
        String str = StringFunction.evaluate(strArg, nav);
        String match = StringFunction.evaluate(matchArg, nav);
        return ((str.indexOf(match) >= 0) ? Boolean.TRUE : Boolean.FALSE);
    }
}

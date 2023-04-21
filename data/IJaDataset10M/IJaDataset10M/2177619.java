package org.genxdm.processor.xpath.v10.functions;

import org.genxdm.processor.xpath.v10.expressions.WrappedBooleanExpr;
import org.genxdm.xpath.v10.ExprContextStatic;
import org.genxdm.xpath.v10.ExprParseException;
import org.genxdm.xpath.v10.extend.ConvertibleExpr;

/**
 * a single argument XPath function which casts its argument to a boolean Function: boolean boolean(object)
 * 
 * <p>
 * The boolean function converts its argument to a boolean as follows:
 * </p>
 *<ul>
 * <li>a number is true if and only if it is neither positive or negative zero nor NaN</li>
 * 
 * <li>a node-set is true if and only if it is non-empty</li>
 * 
 * <li>a string is true if and only if its length is non-zero</li>
 * 
 * <li>an object of a type other than the four basic types is converted to a boolean in a way that is dependent on that type</li>
 *</ul>
 */
public class BooleanFunction extends Function1 {

    ConvertibleExpr makeCallExpr(final ConvertibleExpr e, final ExprContextStatic statEnv) throws ExprParseException {
        return WrappedBooleanExpr.wrap(e.makeBooleanExpr(statEnv));
    }
}

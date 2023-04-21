package org.jato.expression;

import org.jato.JatoException;
import org.jato.*;
import org.jdom.*;

/**
 * Performs a logical AND operation (&& in Java) on two operands.
 *
 * <pre>
 * Syntax:   opand-exp1 and opand-exp2
 * Returns:  result as a Boolean
 * </pre>
 *
 * @author Andy Krumel
 */
public class AndOperator extends Operator {

    /**
     * Creates an instance of this expression.
     */
    public AndOperator() {
        super("and ");
    }

    /**
    * Gets the type for the object returned from {@link #eval}.
    *
    * @param  obj         the object returned from {@link #eval}.
    * @param  jato        the current interpreter being run.
    * @param  tag         the script tag being processed.
    * @param  thisClass   the class object for the current object.
    * @param  thisObj     the current Jato object.
    * @param  xmlIn       the current element from the XML input
    *                     document being processed.
    * @param  xmlOut      the current element from the XML output
    *                     document attributes and elements are
    *                     added to.
    *
    * @return the class representing the type.
    *
    * @throws JatoException catch-all exception for any errors.
    */
    public Class type(Object obj, Interpreter jato, ScriptTag tag, Class thisClass, Object thisObj, Element xmlIn, Element xmlOut) throws JatoException {
        return boolean.class;
    }

    /**
    * The operator body. Converts the two operands to booleans and performs an
    * logical AND operation.
    *
    * @param left - the left operand value
    * @param right - the right operand value
    *
    * @return the value that results from evaluating the operator.
    *
    * @throws JatoException catch-all exception for any errors.
    */
    protected Object doOperator(Object left, Object right) throws JatoException {
        return new Boolean(convertBoolean(left) && convertBoolean(right));
    }
}

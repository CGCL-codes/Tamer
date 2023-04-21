package org.jato.expression;

import org.jato.JatoException;
import org.jato.*;
import org.jdom.*;

/**
 * Performs a Get command on the current Jato {@link org.jato.JatoIntegrator integrator} 
 * object (see {@link org.jato.JatoIntegrator#getObject}).
 *
 * <pre>
 * Syntax:   get(expression)
 * Returns:  the retrieved Object
 * Examples:
 *     get(path("@foo")) * 
 * </pre>
 */
public class GetFunction extends Function {

    Expression fResolver;

    /**
    * Creates an instance of this expression.
    */
    public GetFunction() {
        super("get(");
    }

    /**
    * Returns this expression as it would appear in an expression string.
    *
    * @return the string
    */
    public String toString() {
        return fKeyword + fResolver + ")" + chainToString();
    }

    /**
    * Parses the expression.
    *
    * @param exp  the expression string being parsed by the
    *             {@link org.jato.expression.ExpressionFactory} class.
    * @param sidx  the index to start parsing from.
    *
    * @return the next index to be processed.
    *
    * @throws JatoException  catch-all exception to indicate parsing errors.
    */
    public int parse(String cond, int sidx) throws JatoException {
        if (!cond.startsWith(fKeyword, sidx)) {
            throw new JatoException("Condition is not operator '" + fKeyword + "' at index " + sidx);
        }
        int start = sidx + fKeyword.length();
        int end = findClosingParen(cond, start);
        String str = cond.substring(start, end);
        fResolver = ExpressionFactory.parseExpression(null, str, 0, true);
        return end + 1;
    }

    /**
    * The expression body for a function.
    *
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
    * @return the return value of the static function or
    *         <code>null</code> if it returns void.
    *
    * @throws JatoException catch-all exception for any errors.
    */
    protected Object doFunction(Interpreter jato, ScriptTag tag, Class thisClass, Object thisObj, Element xmlIn, Element xmlOut) throws JatoException {
        Object key = fResolver.eval(jato, tag, thisClass, thisObj, xmlIn, xmlOut);
        return jato.getObject(key.toString(), null);
    }
}

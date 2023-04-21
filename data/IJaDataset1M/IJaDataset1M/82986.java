package org.jato.expression;

import org.jato.JatoException;
import org.jato.*;
import org.jdom.*;

/**
 * Base class for all expressions that perform their task as a function, such as 
 * invoking a method, loading a class, running a macro. All functions have a return value
 * which may be null. This class provides support for expression chaining where each 
 * function returning an object that can be used as argument to other functions.
 * <p>    For example, the following expression:    </p>
 * <blockquote>   
 * <pre>
 *    var('format').parse(path('@modified'))<br>   
 * </pre>
 * </blockquote>
 * retrieves the script variable named '<code>format</code>', assumes 
 * it is a Java object and tries to invoke the method    <code>parse()</code>
 * on it with a single argument: the result of    the XPath statement "
 * <code>@modified</code>" which select the    string value of attribute 
 * '<code>modified</code>' of the current element node. 
 * 
 * @author Andy Krumel
 */
public abstract class Function extends Operand {

    protected Operand fChain;

    protected Function(String keyword) {
        super(keyword);
    }

    /**
     * Determines if another object is equivalent to this expression.
     *
     * @param o  the object to compare equivalence.
     *
     * @return true if the equivalent.
     */
    public boolean equals(Object o) {
        if (o instanceof Function) {
            Function f = (Function) o;
            if (fChain != null) {
                return super.equals(o) && fChain.equals(f.fChain);
            } else {
                return super.equals(o);
            }
        }
        return false;
    }

    /**
    * Returns this expression as it would appear in an expression string.
    *
    * @return the string
    */
    public String toString() {
        return fKeyword;
    }

    protected String chainToString() {
        if (fChain != null) {
            return fChain.toString();
        }
        return "";
    }

    public void setNext(Operand exp) {
        fChain = exp;
    }

    /**
    * Parses the expression. Subclasses will normally override this method.
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
        return sidx + fKeyword.length();
    }

    /**
    * The generic expression body. Invokes {@link #doFunction} and then evaluates the
    * chained expression attached to this function (if specified) using the return value
    * from {@link #doFunction} as the reference object (current object) for the 
    * evaluation.
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
    * @return the value that results from evaluating the expression.
    *
    * @throws JatoException catch-all exception for any errors.
    */
    public final Object eval(Interpreter jato, ScriptTag tag, Class thisClass, Object thisObj, Element xmlIn, Element xmlOut) throws JatoException {
        Object rslt = doFunction(jato, tag, thisClass, thisObj, xmlIn, xmlOut);
        if (fChain == null) {
            return rslt;
        }
        thisClass = (rslt != null) ? rslt.getClass() : null;
        return fChain.eval(jato, tag, thisClass, rslt, xmlIn, xmlOut);
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
    protected abstract Object doFunction(Interpreter jato, ScriptTag tag, Class thisClass, Object thisObj, Element xmlIn, Element xmlOut) throws JatoException;
}

package net.sf.saxon.xpath;

import net.sf.saxon.expr.Expression;
import net.sf.saxon.expr.StaticContext;
import net.sf.saxon.functions.FunctionLibrary;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.om.StructuredQName;
import javax.xml.namespace.QName;
import javax.xml.xpath.XPathFunction;
import javax.xml.xpath.XPathFunctionResolver;

/**
 * The XPathFunctionLibrary is a FunctionLibrary that supports binding of XPath function
 * calls to instances of the JAXP XPathFunction interface returned by an XPathFunctionResolver.
 */
public class XPathFunctionLibrary implements FunctionLibrary {

    private XPathFunctionResolver resolver;

    /**
     * Construct a XPathFunctionLibrary
     */
    public XPathFunctionLibrary() {
    }

    /**
      * Set the resolver
      * @param resolver The XPathFunctionResolver wrapped by this FunctionLibrary
      */
    public void setXPathFunctionResolver(XPathFunctionResolver resolver) {
        this.resolver = resolver;
    }

    /**
      * Get the resolver
      * @return the XPathFunctionResolver wrapped by this FunctionLibrary
      */
    public XPathFunctionResolver getXPathFunctionResolver() {
        return resolver;
    }

    /**
     * Test whether an XPath function with a given name and arity is available. This supports
     * the function-available() function in XSLT. It is thus never used, and always returns false
     * @param functionName
      * @param arity The number of arguments. This is set to -1 in the case of the single-argument
     * function-available() function; in this case the method should return true if there is some
      */
    public boolean isAvailable(StructuredQName functionName, int arity) {
        return false;
    }

    /**
     * Bind a function, given the URI and local parts of the function name,
     * and the list of expressions supplied as arguments. This method is called at compile
     * time.
     * @param functionName
     * @param staticArgs  The expressions supplied statically in the function call. The intention is
     * that the static type of the arguments (obtainable via getItemType() and getCardinality() may
     * be used as part of the binding algorithm.
     * @param env
     * @return An object representing the extension function to be called, if one is found;
     * null if no extension function was found matching the required name, arity, or signature.
     */
    public Expression bind(StructuredQName functionName, Expression[] staticArgs, StaticContext env) throws XPathException {
        if (resolver == null) {
            return null;
        }
        QName name = new QName(functionName.getNamespaceURI(), functionName.getLocalName());
        XPathFunction function = resolver.resolveFunction(name, staticArgs.length);
        if (function == null) {
            return null;
        }
        XPathFunctionCall fc = new XPathFunctionCall(function);
        fc.setArguments(staticArgs);
        return fc;
    }

    /**
     * This method creates a copy of a FunctionLibrary: if the original FunctionLibrary allows
     * new functions to be added, then additions to this copy will not affect the original, or
     * vice versa.
     *
     * @return a copy of this function library. This must be an instance of the original class.
     */
    public FunctionLibrary copy() {
        XPathFunctionLibrary xfl = new XPathFunctionLibrary();
        xfl.resolver = resolver;
        return xfl;
    }
}

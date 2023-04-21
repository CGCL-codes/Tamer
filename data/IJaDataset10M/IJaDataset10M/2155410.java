package org.activebpel.rt.bpel.def.expr.xpath;

import org.activebpel.rt.AeException;
import org.activebpel.rt.expr.def.AeAbstractExpressionParser;
import org.activebpel.rt.expr.def.IAeExpressionParseResult;
import org.activebpel.rt.expr.def.IAeExpressionParserContext;
import org.jaxen.saxpath.XPathReader;
import org.jaxen.saxpath.helpers.XPathReaderFactory;

/**
 * A base implementation of an XPath expression parser.
 */
public abstract class AeAbstractXPathExpressionParser extends AeAbstractExpressionParser {

    /**
    * Constructs an xpath parser given the context.
    * 
    * @param aParserContext
    */
    public AeAbstractXPathExpressionParser(IAeExpressionParserContext aParserContext) {
        super(aParserContext);
    }

    /**
    * Uses Jaxen to parse the XPath expression.  This implementation installs a custom Jaxen XPath 
    * parser event handler which will build a parse tree of the expression.  This parse tree is then
    * walked by the AeXPathParseResult object.
    * 
    * @see org.activebpel.rt.expr.def.IAeExpressionParser#parse(java.lang.String)
    */
    public IAeExpressionParseResult parse(String aExpression) throws AeException {
        AeXPathParseHandler handler = new AeXPathParseHandler(getParserContext().getNamespaceContext());
        try {
            XPathReader reader = XPathReaderFactory.createReader();
            reader.setXPathHandler(handler);
            reader.parse(aExpression);
            return createParseResult(aExpression, handler);
        } catch (Exception e) {
            throw new AeException(e.getMessage(), e);
        }
    }

    /**
    * Creates the parse result object from the expression and xpath parse handler.
    * 
    * @param aExpression
    * @param aHandler
    */
    protected abstract IAeExpressionParseResult createParseResult(String aExpression, AeXPathParseHandler aHandler);
}

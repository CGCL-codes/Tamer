package net.sf.saxon.instruct;

import net.sf.saxon.expr.ExpressionTool;
import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.om.ValueRepresentation;
import net.sf.saxon.trans.XPathException;

/**
 * Handler for local xsl:variable elements in stylesheet. Not used in XQuery. In fact, the class is used
 * only transiently in XSLT: local variables are compiled first to a LocalVariable object, and subsequently
 * to a LetExpression.
*/
public class LocalVariable extends GeneralVariable {

    /**
    * Process the local variable declaration
    */
    public TailCall processLeavingTail(XPathContext context) throws XPathException {
        context.setLocalVariable(getSlotNumber(), ExpressionTool.evaluate(getSelectExpression(), evaluationMode, context, 10));
        return null;
    }

    /**
    * Evaluate the variable
    */
    public ValueRepresentation evaluateVariable(XPathContext c) throws XPathException {
        return c.evaluateLocalVariable(getSlotNumber());
    }
}

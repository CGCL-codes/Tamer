package com.armatiek.infofuze.xslt.functions.request;

import javax.servlet.http.HttpServletRequest;
import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.om.SequenceIterator;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.tree.iter.SingletonIterator;
import net.sf.saxon.value.SequenceType;
import net.sf.saxon.value.StringValue;
import com.armatiek.infofuze.config.Definitions;

/**
 * @author Maarten Kroon
 *
 */
public class ServletPath extends ExtensionFunctionDefinition {

    private static final long serialVersionUID = 1L;

    private static final StructuredQName qName = new StructuredQName("", Definitions.REQUEST_NAMESPACE, "servlet-path");

    public StructuredQName getFunctionQName() {
        return qName;
    }

    public int getMinimumNumberOfArguments() {
        return 0;
    }

    public int getMaximumNumberOfArguments() {
        return 0;
    }

    public SequenceType[] getArgumentTypes() {
        return new SequenceType[] {};
    }

    public SequenceType getResultType(SequenceType[] suppliedArgumentTypes) {
        return SequenceType.SINGLE_STRING;
    }

    public ExtensionFunctionCall makeCallExpression() {
        return new ServletPathCall();
    }

    private static class ServletPathCall extends ExtensionFunctionCall {

        private static final long serialVersionUID = 1L;

        public SequenceIterator call(SequenceIterator[] arguments, XPathContext context) throws XPathException {
            HttpServletRequest request = (HttpServletRequest) context.getController().getParameter("{" + Definitions.REQUEST_NAMESPACE + "}request");
            return SingletonIterator.makeIterator(new StringValue(request.getServletPath()));
        }
    }
}

package com.armatiek.infofuze.xslt.functions.request;

import javax.servlet.http.HttpServletRequest;
import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.om.SequenceIterator;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.SequenceType;
import net.sf.saxon.value.StringValue;
import com.armatiek.infofuze.config.Definitions;
import com.armatiek.infofuze.xslt.functions.ExtensionFunctionCall;

/**
 * @author Maarten Kroon
 *
 */
public class RemoveAttribute extends ExtensionFunctionDefinition {

    private static final long serialVersionUID = 1L;

    private static final StructuredQName qName = new StructuredQName("", Definitions.REQUEST_NAMESPACE, "remove-attribute");

    public StructuredQName getFunctionQName() {
        return qName;
    }

    public int getMinimumNumberOfArguments() {
        return 1;
    }

    public int getMaximumNumberOfArguments() {
        return 1;
    }

    public SequenceType[] getArgumentTypes() {
        return new SequenceType[] { SequenceType.SINGLE_STRING };
    }

    public SequenceType getResultType(SequenceType[] suppliedArgumentTypes) {
        return SequenceType.SINGLE_BOOLEAN;
    }

    public ExtensionFunctionCall makeCallExpression() {
        return new RemoveAttributeCall();
    }

    private static class RemoveAttributeCall extends ExtensionFunctionCall {

        private static final long serialVersionUID = 1L;

        public SequenceIterator call(SequenceIterator[] arguments, XPathContext context) throws XPathException {
            HttpServletRequest request = (HttpServletRequest) context.getController().getParameter("{" + Definitions.REQUEST_NAMESPACE + "}request");
            String name = ((StringValue) arguments[0].next()).getStringValue();
            request.removeAttribute(name);
            return objectToSequenceIterator(Boolean.TRUE);
        }
    }
}

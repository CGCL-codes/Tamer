package com.armatiek.infofuze.xslt.functions.response;

import javax.servlet.http.HttpServletResponse;
import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.om.SequenceIterator;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.Int64Value;
import net.sf.saxon.value.SequenceType;
import com.armatiek.infofuze.config.Definitions;
import com.armatiek.infofuze.xslt.functions.ExtensionFunctionCall;

/**
 * 
 * 
 * @author Maarten Kroon
 */
public class SetContentLength extends ExtensionFunctionDefinition {

    private static final long serialVersionUID = 1L;

    private static final StructuredQName qName = new StructuredQName("", Definitions.RESPONSE_NAMESPACE, "set-content-length");

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
        return new SequenceType[] { SequenceType.SINGLE_INTEGER };
    }

    public SequenceType getResultType(SequenceType[] suppliedArgumentTypes) {
        return SequenceType.SINGLE_BOOLEAN;
    }

    public ExtensionFunctionCall makeCallExpression() {
        return new SetContentLengthCall();
    }

    private static class SetContentLengthCall extends ExtensionFunctionCall {

        private static final long serialVersionUID = 1L;

        public SequenceIterator call(SequenceIterator[] arguments, XPathContext context) throws XPathException {
            HttpServletResponse response = (HttpServletResponse) context.getController().getParameter("{" + Definitions.RESPONSE_NAMESPACE + "}response");
            int length = (int) ((Int64Value) arguments[0].next()).longValue();
            response.setContentLength(length);
            return objectToSequenceIterator(Boolean.TRUE);
        }
    }
}

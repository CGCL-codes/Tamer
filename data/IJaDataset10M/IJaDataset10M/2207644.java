package com.armatiek.infofuze.xslt.functions.response;

import javax.servlet.http.HttpServletResponse;
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
public class EncodeURL extends ExtensionFunctionDefinition {

    private static final long serialVersionUID = 1L;

    private static final StructuredQName qName = new StructuredQName("", Definitions.RESPONSE_NAMESPACE, "encode-url");

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
        return SequenceType.SINGLE_STRING;
    }

    public ExtensionFunctionCall makeCallExpression() {
        return new EncodeURLCall();
    }

    private static class EncodeURLCall extends ExtensionFunctionCall {

        private static final long serialVersionUID = 1L;

        public SequenceIterator call(SequenceIterator[] arguments, XPathContext context) throws XPathException {
            HttpServletResponse response = (HttpServletResponse) context.getController().getParameter("{" + Definitions.RESPONSE_NAMESPACE + "}response");
            String url = ((StringValue) arguments[0].next()).getStringValue();
            return SingletonIterator.makeIterator(new StringValue(response.encodeURL(url)));
        }
    }
}

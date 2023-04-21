package org.exist.xquery.functions.request;

import java.util.Enumeration;
import org.apache.log4j.Logger;
import org.exist.dom.QName;
import org.exist.http.servlets.RequestWrapper;
import org.exist.xquery.BasicFunction;
import org.exist.xquery.Cardinality;
import org.exist.xquery.FunctionSignature;
import org.exist.xquery.Variable;
import org.exist.xquery.XPathException;
import org.exist.xquery.XQueryContext;
import org.exist.xquery.value.FunctionReturnSequenceType;
import org.exist.xquery.value.JavaObjectValue;
import org.exist.xquery.value.Sequence;
import org.exist.xquery.value.StringValue;
import org.exist.xquery.value.Type;
import org.exist.xquery.value.ValueSequence;

/**
 * @author Alain Pannetier <alain.m.pannetier@gmail.com>
 * 
 * Adjusted and Committed by Adam Retter <adam.retter@devon.gov.uk>
 */
public class GetHeaderNames extends BasicFunction {

    protected static final Logger logger = Logger.getLogger(GetHeaderNames.class);

    public static final FunctionSignature signature = new FunctionSignature(new QName("get-header-names", RequestModule.NAMESPACE_URI, RequestModule.PREFIX), "Returns a sequence containing the names of all headers passed in the current request", null, new FunctionReturnSequenceType(Type.STRING, Cardinality.ZERO_OR_MORE, "a sequence containing the names of all headers passed in the current request"));

    /**
         * @param context
         */
    public GetHeaderNames(XQueryContext context) {
        super(context, signature);
    }

    public Sequence eval(Sequence[] args, Sequence contextSequence) throws XPathException {
        RequestModule myModule = (RequestModule) context.getModule(RequestModule.NAMESPACE_URI);
        Variable var = myModule.resolveVariable(RequestModule.REQUEST_VAR);
        if (var == null || var.getValue() == null) throw new XPathException(this, "No request object found in the current XQuery context.");
        if (var.getValue().getItemType() != Type.JAVA_OBJECT) throw new XPathException(this, "Variable $request is not bound to an Java object.");
        JavaObjectValue value = (JavaObjectValue) var.getValue().itemAt(0);
        if (value.getObject() instanceof RequestWrapper) {
            ValueSequence result = new ValueSequence();
            for (Enumeration<String> e = ((RequestWrapper) value.getObject()).getHeaderNames(); e.hasMoreElements(); ) {
                String param = e.nextElement();
                result.add(new StringValue(param));
            }
            return result;
        } else throw new XPathException(this, "Variable $request is not bound to a Request object.");
    }
}

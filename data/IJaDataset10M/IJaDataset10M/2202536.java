package org.exist.xquery.functions.request;

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

/**
 * @author Pierrick Brihaye <pierrick.brihaye@free.fr>
 */
public class GetURL extends BasicFunction {

    protected static final Logger logger = Logger.getLogger(GetURL.class);

    public static final FunctionSignature signature = new FunctionSignature(new QName("get-url", RequestModule.NAMESPACE_URI, RequestModule.PREFIX), "Returns the URL of the current request.", null, new FunctionReturnSequenceType(Type.STRING, Cardinality.EXACTLY_ONE, "the URL of the current request"));

    /**
	 * @param context
	 */
    public GetURL(XQueryContext context) {
        super(context, signature);
    }

    public Sequence eval(Sequence[] args, Sequence contextSequence) throws XPathException {
        RequestModule myModule = (RequestModule) context.getModule(RequestModule.NAMESPACE_URI);
        Variable var = myModule.resolveVariable(RequestModule.REQUEST_VAR);
        if (var == null || var.getValue() == null) throw new XPathException(this, "No request object found in the current XQuery context.");
        if (var.getValue().getItemType() != Type.JAVA_OBJECT) throw new XPathException(this, "Variable $request is not bound to an Java object.");
        JavaObjectValue value = (JavaObjectValue) var.getValue().itemAt(0);
        if (value.getObject() instanceof RequestWrapper) {
            return new StringValue(((RequestWrapper) value.getObject()).getRequestURL().toString());
        } else throw new XPathException(this, "Variable $request is not bound to a Request object.");
    }
}

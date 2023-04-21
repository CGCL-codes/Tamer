package org.exist.xquery.functions.request;

import org.exist.dom.QName;
import org.exist.http.servlets.RequestWrapper;
import org.exist.xquery.BasicFunction;
import org.exist.xquery.Cardinality;
import org.exist.xquery.FunctionSignature;
import org.exist.xquery.Variable;
import org.exist.xquery.XPathException;
import org.exist.xquery.XPathUtil;
import org.exist.xquery.XQueryContext;
import org.exist.xquery.value.JavaObjectValue;
import org.exist.xquery.value.Sequence;
import org.exist.xquery.value.SequenceType;
import org.exist.xquery.value.Type;

/**
 * @author wolf
 */
public class RequestParameter extends BasicFunction {

    public static final FunctionSignature signature = new FunctionSignature(new QName("request-parameter", RequestModule.NAMESPACE_URI, RequestModule.PREFIX), "Returns the HTTP request parameter identified by $a. If the parameter could not be found, " + "the default value specified in $b is returned instead.", new SequenceType[] { new SequenceType(Type.STRING, Cardinality.EXACTLY_ONE), new SequenceType(Type.ITEM, Cardinality.ZERO_OR_MORE) }, new SequenceType(Type.STRING, Cardinality.ZERO_OR_MORE));

    public RequestParameter(XQueryContext context) {
        super(context, signature);
    }

    public Sequence eval(Sequence[] args, Sequence contextSequence) throws XPathException {
        RequestModule myModule = (RequestModule) context.getModule(RequestModule.NAMESPACE_URI);
        Variable var = myModule.resolveVariable(RequestModule.REQUEST_VAR);
        if (var == null || var.getValue().getItemType() != Type.JAVA_OBJECT) throw new XPathException("Variable $request is not bound to an Java object.");
        String param = args[0].getStringValue();
        JavaObjectValue value = (JavaObjectValue) var.getValue().itemAt(0);
        if (value.getObject() instanceof RequestWrapper) {
            String[] values = ((RequestWrapper) value.getObject()).getParameterValues(param);
            if (values == null || values.length == 0) return args[1];
            if (values.length == 1) return XPathUtil.javaObjectToXPath(values[0]); else return XPathUtil.javaObjectToXPath(values);
        } else throw new XPathException("Variable $request is not bound to a Request object.");
    }
}

package org.exist.xquery.functions.request;

import org.exist.dom.QName;
import org.exist.http.servlets.RequestWrapper;
import org.exist.http.servlets.SessionWrapper;
import org.exist.xquery.Cardinality;
import org.exist.xquery.Function;
import org.exist.xquery.FunctionSignature;
import org.exist.xquery.Variable;
import org.exist.xquery.XPathException;
import org.exist.xquery.XQueryContext;
import org.exist.xquery.value.Item;
import org.exist.xquery.value.JavaObjectValue;
import org.exist.xquery.value.Sequence;
import org.exist.xquery.value.SequenceType;
import org.exist.xquery.value.Type;
import org.exist.xquery.value.StringValue;

/**
 * Returns the ID of the current session or an empty sequence
 * if there is no session.
 * 
 * @author Adam Retter <adam.retter@devon.gov.uk>
 */
public class GetSessionID extends Function {

    public static final FunctionSignature signature = new FunctionSignature(new QName("get-session-id", RequestModule.NAMESPACE_URI, RequestModule.PREFIX), "Returns the ID of the current session or an empty sequence if there is no session.", null, new SequenceType(Type.STRING, Cardinality.ZERO_OR_ONE));

    public GetSessionID(XQueryContext context) {
        super(context, signature);
    }

    public Sequence eval(Sequence contextSequence, Item contextItem) throws XPathException {
        RequestModule myModule = (RequestModule) context.getModule(RequestModule.NAMESPACE_URI);
        Variable var = myModule.resolveVariable(RequestModule.REQUEST_VAR);
        if (var == null || var.getValue() == null) throw new XPathException("Request object not found");
        if (var.getValue().getItemType() != Type.JAVA_OBJECT) throw new XPathException("Variable $request is not bound to an Java object.");
        JavaObjectValue value = (JavaObjectValue) var.getValue().itemAt(0);
        if (value.getObject() instanceof RequestWrapper) {
            RequestWrapper request = (RequestWrapper) value.getObject();
            SessionWrapper session = request.getSession(false);
            if (session == null) {
                return (Sequence.EMPTY_SEQUENCE);
            } else {
                return (new StringValue(session.getId()));
            }
        } else {
            throw new XPathException("Type error: variable $request is not bound to a request object");
        }
    }
}

package org.exist.xquery.functions;

import org.exist.dom.NodeSet;
import org.exist.dom.QName;
import org.exist.xquery.Cardinality;
import org.exist.xquery.Function;
import org.exist.xquery.FunctionSignature;
import org.exist.xquery.Module;
import org.exist.xquery.XQueryContext;
import org.exist.xquery.XPathException;
import org.exist.xquery.value.Item;
import org.exist.xquery.value.Sequence;
import org.exist.xquery.value.SequenceType;
import org.exist.xquery.value.StringValue;
import org.exist.xquery.value.Type;
import org.w3c.dom.Node;

/**
 * xpath-library function: local-name(object)
 *
 */
public class FunNamespaceURI extends Function {

    public static final FunctionSignature signatures[] = { new FunctionSignature(new QName("namespace-uri", Module.BUILTIN_FUNCTION_NS), new SequenceType[0], new SequenceType(Type.STRING, Cardinality.EXACTLY_ONE), true), new FunctionSignature(new QName("namespace-uri", Module.BUILTIN_FUNCTION_NS), new SequenceType[] { new SequenceType(Type.NODE, Cardinality.ZERO_OR_ONE) }, new SequenceType(Type.STRING, Cardinality.EXACTLY_ONE), true) };

    public FunNamespaceURI(XQueryContext context, FunctionSignature signature) {
        super(context, signature);
    }

    public Sequence eval(Sequence contextSequence, Item contextItem) throws XPathException {
        Node n = null;
        if (contextItem != null) contextSequence = contextItem.toSequence();
        if (getArgumentCount() > 0) {
            NodeSet result = getArgument(0).eval(contextSequence).toNodeSet();
            if (result.getLength() > 0) n = result.item(0);
        } else {
            if (contextSequence.getLength() > 0 && contextSequence.getItemType() == Type.NODE) n = ((NodeSet) contextSequence).item(0);
        }
        if (n != null) {
            switch(n.getNodeType()) {
                case Node.ELEMENT_NODE:
                case Node.ATTRIBUTE_NODE:
                    return new StringValue(n.getNamespaceURI());
                default:
                    return new StringValue("");
            }
        }
        return new StringValue("");
    }
}

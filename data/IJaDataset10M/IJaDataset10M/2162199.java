package org.exist.xquery.functions;

import org.exist.dom.QName;
import org.exist.xquery.BasicFunction;
import org.exist.xquery.Cardinality;
import org.exist.xquery.FunctionSignature;
import org.exist.xquery.Module;
import org.exist.xquery.XPathException;
import org.exist.xquery.XQueryContext;
import org.exist.xquery.value.AnyURIValue;
import org.exist.xquery.value.QNameValue;
import org.exist.xquery.value.Sequence;
import org.exist.xquery.value.SequenceType;
import org.exist.xquery.value.StringValue;
import org.exist.xquery.value.Type;

/**
 * @author wolf
 *
 */
public class QNameFunctions extends BasicFunction {

    public static final FunctionSignature prefixFromQName = new FunctionSignature(new QName("prefix-from-QName", Module.BUILTIN_FUNCTION_NS), "Returns an xs:NCName representing the prefix of $a. If $a is the empty " + "sequence, returns the empty sequence.", new SequenceType[] { new SequenceType(Type.QNAME, Cardinality.ZERO_OR_ONE) }, new SequenceType(Type.NCNAME, Cardinality.ZERO_OR_ONE));

    public static final FunctionSignature localNameFromQName = new FunctionSignature(new QName("local-name-from-QName", Module.BUILTIN_FUNCTION_NS), "Returns an xs:NCName representing the local part of $a. If $a is the empty " + "sequence, returns the empty sequence.", new SequenceType[] { new SequenceType(Type.QNAME, Cardinality.ZERO_OR_ONE) }, new SequenceType(Type.NCNAME, Cardinality.ZERO_OR_ONE));

    public static final FunctionSignature namespaceURIFromQName = new FunctionSignature(new QName("namespace-uri-from-QName", Module.BUILTIN_FUNCTION_NS), "Returns the namespace URI for $a. If $a is the empty " + "sequence, returns the empty sequence.", new SequenceType[] { new SequenceType(Type.QNAME, Cardinality.ZERO_OR_ONE) }, new SequenceType(Type.ANY_URI, Cardinality.ZERO_OR_ONE));

    /**
	 * @param context
	 * @param signature
	 */
    public QNameFunctions(XQueryContext context, FunctionSignature signature) {
        super(context, signature);
    }

    public Sequence eval(Sequence[] args, Sequence contextSequence) throws XPathException {
        if (args[0].getLength() == 0) return Sequence.EMPTY_SEQUENCE;
        QNameValue value = (QNameValue) args[0].itemAt(0);
        QName qname = value.getQName();
        if (isCalledAs("prefix-from-QName")) {
            String prefix = qname.getPrefix();
            if (prefix == null) return Sequence.EMPTY_SEQUENCE; else return new StringValue(prefix, Type.NCNAME);
        } else if (isCalledAs("local-name-from-QName")) return new StringValue(qname.getLocalName(), Type.NCNAME); else {
            String uri = qname.getNamespaceURI();
            if (uri == null) uri = "";
            return new AnyURIValue(uri);
        }
    }
}

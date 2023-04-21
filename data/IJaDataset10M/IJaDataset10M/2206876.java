package org.exist.xquery.functions.fn;

import org.exist.dom.QName;
import org.exist.xquery.BasicFunction;
import org.exist.xquery.Cardinality;
import org.exist.xquery.Dependency;
import org.exist.xquery.Function;
import org.exist.xquery.FunctionSignature;
import org.exist.xquery.Profiler;
import org.exist.xquery.XPathException;
import org.exist.xquery.XQueryContext;
import org.exist.xquery.value.AnyURIValue;
import org.exist.xquery.value.FunctionParameterSequenceType;
import org.exist.xquery.value.FunctionReturnSequenceType;
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

    public static final FunctionSignature prefixFromQName = new FunctionSignature(new QName("prefix-from-QName", Function.BUILTIN_FUNCTION_NS), "Returns an xs:NCName representing the prefix of $arg. If $arg is the empty " + "sequence, returns the empty sequence.", new SequenceType[] { new FunctionParameterSequenceType("arg", Type.QNAME, Cardinality.ZERO_OR_ONE, "The QName") }, new FunctionReturnSequenceType(Type.NCNAME, Cardinality.ZERO_OR_ONE, "the prefix"));

    public static final FunctionSignature localNameFromQName = new FunctionSignature(new QName("local-name-from-QName", Function.BUILTIN_FUNCTION_NS), "Returns an xs:NCName representing the local part of $arg. If $arg is the empty " + "sequence, returns the empty sequence.", new SequenceType[] { new FunctionParameterSequenceType("arg", Type.QNAME, Cardinality.ZERO_OR_ONE, "The QName") }, new FunctionReturnSequenceType(Type.NCNAME, Cardinality.ZERO_OR_ONE, "the local name"));

    public static final FunctionSignature namespaceURIFromQName = new FunctionSignature(new QName("namespace-uri-from-QName", Function.BUILTIN_FUNCTION_NS), "Returns the namespace URI for $arg. If $arg is the empty " + "sequence, returns the empty sequence.", new SequenceType[] { new FunctionParameterSequenceType("arg", Type.QNAME, Cardinality.ZERO_OR_ONE, "The QName") }, new FunctionReturnSequenceType(Type.ANY_URI, Cardinality.ZERO_OR_ONE, "the namespace URI"));

    /**
	 * @param context
	 * @param signature
	 */
    public QNameFunctions(XQueryContext context, FunctionSignature signature) {
        super(context, signature);
    }

    public Sequence eval(Sequence[] args, Sequence contextSequence) throws XPathException {
        if (context.getProfiler().isEnabled()) {
            context.getProfiler().start(this);
            context.getProfiler().message(this, Profiler.DEPENDENCIES, "DEPENDENCIES", Dependency.getDependenciesName(this.getDependencies()));
            if (contextSequence != null) context.getProfiler().message(this, Profiler.START_SEQUENCES, "CONTEXT SEQUENCE", contextSequence);
        }
        Sequence result;
        if (args[0].isEmpty()) result = Sequence.EMPTY_SEQUENCE; else {
            QNameValue value = (QNameValue) args[0].itemAt(0);
            QName qname = value.getQName();
            if (isCalledAs("prefix-from-QName")) {
                String prefix = qname.getPrefix();
                if (prefix == null || prefix.length() == 0) result = Sequence.EMPTY_SEQUENCE; else result = new StringValue(prefix, Type.NCNAME);
            } else if (isCalledAs("local-name-from-QName")) result = new StringValue(qname.getLocalName(), Type.NCNAME); else {
                String uri = qname.getNamespaceURI();
                if (uri == null) uri = "";
                result = new AnyURIValue(uri);
            }
        }
        if (context.getProfiler().isEnabled()) context.getProfiler().end(this, "", result);
        return result;
    }
}

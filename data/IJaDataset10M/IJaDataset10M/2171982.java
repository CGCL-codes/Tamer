package org.exist.xslt.functions;

import org.exist.dom.QName;
import org.exist.xquery.BasicFunction;
import org.exist.xquery.Cardinality;
import org.exist.xquery.FunctionSignature;
import org.exist.xquery.XPathException;
import org.exist.xquery.XQueryContext;
import org.exist.xquery.value.Sequence;
import org.exist.xquery.value.SequenceType;
import org.exist.xquery.value.Type;

/**
 * key($key-name as xs:string, $key-value as xs:anyAtomicType*) as node()* 
 * key($key-name as xs:string, $key-value as xs:anyAtomicType*, $top as node()) as node()* 
 * 
 * @author <a href="mailto:shabanovd@gmail.com">Dmitriy Shabanov</a>
 *
 */
public class Key extends BasicFunction {

    public static final FunctionSignature signatures[] = { new FunctionSignature(new QName("key", XSLModule.NAMESPACE_URI, XSLModule.PREFIX), "The function does for keys what the id FO function does for IDs", new SequenceType[] { new SequenceType(Type.STRING, Cardinality.ONE), new SequenceType(Type.ATOMIC, Cardinality.ZERO_OR_MORE) }, new SequenceType(Type.NODE, Cardinality.ZERO_OR_MORE)), new FunctionSignature(new QName("key", XSLModule.NAMESPACE_URI, XSLModule.PREFIX), "The function does for keys what the id FO function does for IDs", new SequenceType[] { new SequenceType(Type.STRING, Cardinality.ONE), new SequenceType(Type.ATOMIC, Cardinality.ZERO_OR_MORE), new SequenceType(Type.NODE, Cardinality.ONE) }, new SequenceType(Type.NODE, Cardinality.ZERO_OR_ONE)) };

    /**
	 * @param context
	 */
    public Key(XQueryContext context, FunctionSignature signature) {
        super(context, signature);
    }

    @Override
    public Sequence eval(Sequence[] args, Sequence contextSequence) throws XPathException {
        throw new RuntimeException("Method is not implemented");
    }
}

package org.exist.xquery.functions;

import org.exist.dom.QName;
import org.exist.xquery.Cardinality;
import org.exist.xquery.Function;
import org.exist.xquery.FunctionSignature;
import org.exist.xquery.Module;
import org.exist.xquery.XQueryContext;
import org.exist.xquery.XPathException;
import org.exist.xquery.value.Item;
import org.exist.xquery.value.NumericValue;
import org.exist.xquery.value.Sequence;
import org.exist.xquery.value.SequenceType;
import org.exist.xquery.value.Type;

/**
 * @author Wolfgang Meier (wolfgang@exist-db.org)
 */
public class FunAbs extends Function {

    public static final FunctionSignature signature = new FunctionSignature(new QName("abs", Module.BUILTIN_FUNCTION_NS), "Returns the absolute value of the argument. If the argument is negative " + "returns -arg otherwise returns arg.", new SequenceType[] { new SequenceType(Type.NUMBER, Cardinality.ZERO_OR_ONE) }, new SequenceType(Type.NUMBER, Cardinality.EXACTLY_ONE));

    /**
	 * @param context
	 * @param signature
	 */
    public FunAbs(XQueryContext context) {
        super(context, signature);
    }

    public Sequence eval(Sequence contextSequence, Item contextItem) throws XPathException {
        if (contextItem != null) contextSequence = contextItem.toSequence();
        Sequence seq = getArgument(0).eval(contextSequence, contextItem);
        if (seq.getLength() == 0) return Sequence.EMPTY_SEQUENCE;
        NumericValue value = (NumericValue) seq.itemAt(0).convertTo(Type.NUMBER);
        return value.abs();
    }
}

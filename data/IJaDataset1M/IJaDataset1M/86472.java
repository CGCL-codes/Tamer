package org.exist.xquery.functions;

import java.text.Collator;
import org.exist.dom.QName;
import org.exist.xquery.Cardinality;
import org.exist.xquery.FunctionSignature;
import org.exist.xquery.Module;
import org.exist.xquery.XPathException;
import org.exist.xquery.XQueryContext;
import org.exist.xquery.value.AtomicValue;
import org.exist.xquery.value.DoubleValue;
import org.exist.xquery.value.Item;
import org.exist.xquery.value.NumericValue;
import org.exist.xquery.value.Sequence;
import org.exist.xquery.value.SequenceIterator;
import org.exist.xquery.value.SequenceType;
import org.exist.xquery.value.Type;

/**
 * @author Wolfgang Meier (wolfgang@exist-db.org)
 */
public class FunMin extends CollatingFunction {

    public static final FunctionSignature signatures[] = { new FunctionSignature(new QName("min", Module.BUILTIN_FUNCTION_NS), "Selects an item from the input sequence $a whose value is less than or equal to " + "the value of every other item in the input sequence.", new SequenceType[] { new SequenceType(Type.ATOMIC, Cardinality.ZERO_OR_MORE) }, new SequenceType(Type.ATOMIC, Cardinality.ZERO_OR_ONE)), new FunctionSignature(new QName("min", Module.BUILTIN_FUNCTION_NS), "Selects an item from the input sequence $a whose value is less than or equal to " + "the value of every other item in the input sequence. The collation specified in $b is " + "used for string comparisons.", new SequenceType[] { new SequenceType(Type.ATOMIC, Cardinality.ZERO_OR_MORE), new SequenceType(Type.STRING, Cardinality.EXACTLY_ONE) }, new SequenceType(Type.ATOMIC, Cardinality.ZERO_OR_ONE)) };

    /**
	 * @param context
	 * @param signature
	 */
    public FunMin(XQueryContext context, FunctionSignature signature) {
        super(context, signature);
    }

    public Sequence eval(Sequence contextSequence, Item contextItem) throws XPathException {
        Sequence arg = getArgument(0).eval(contextSequence, contextItem);
        if (arg.getLength() == 0) return Sequence.EMPTY_SEQUENCE;
        Collator collator = getCollator(contextSequence, contextItem, 2);
        SequenceIterator iter = arg.iterate();
        AtomicValue min = (AtomicValue) iter.nextItem();
        AtomicValue current;
        while (iter.hasNext()) {
            current = (AtomicValue) iter.nextItem();
            if (current.getType() == Type.ATOMIC) current = current.convertTo(Type.DOUBLE);
            if (Type.subTypeOf(current.getType(), Type.NUMBER) && ((NumericValue) current).isNaN()) return DoubleValue.NaN;
            min = min.min(collator, current);
        }
        return min;
    }
}

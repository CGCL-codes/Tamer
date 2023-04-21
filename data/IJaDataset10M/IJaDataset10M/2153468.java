package net.sf.saxon.functions;

import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.om.Item;
import net.sf.saxon.om.FastStringBuffer;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.AtomicValue;
import net.sf.saxon.value.SequenceType;
import net.sf.saxon.value.StringValue;

public class Concat extends SystemFunction {

    /**
    * Get the required type of the nth argument
    */
    protected SequenceType getRequiredType(int arg) {
        return getDetails().argumentTypes[0];
    }

    /**
    * Evaluate the function in a string context
    */
    public CharSequence evaluateAsString(XPathContext c) throws XPathException {
        return evaluateItem(c).getStringValueCS();
    }

    /**
    * Evaluate in a general context
    */
    public Item evaluateItem(XPathContext c) throws XPathException {
        int numArgs = argument.length;
        FastStringBuffer sb = new FastStringBuffer(1024);
        for (int i = 0; i < numArgs; i++) {
            AtomicValue val = (AtomicValue) argument[i].evaluateItem(c);
            if (val != null) {
                sb.append(val.getStringValueCS());
            }
        }
        return StringValue.makeStringValue(sb.condense());
    }
}

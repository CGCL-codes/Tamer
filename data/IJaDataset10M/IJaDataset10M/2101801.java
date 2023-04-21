package net.sf.saxon.functions;

import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.om.Item;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.AtomicValue;
import net.sf.saxon.value.StringValue;

/**
* This class implements the upper-case() and lower-case() functions
*/
public class ForceCase extends SystemFunction {

    public static final int UPPERCASE = 0;

    public static final int LOWERCASE = 1;

    /**
    * Evaluate in a general context
    */
    public Item evaluateItem(XPathContext c) throws XPathException {
        AtomicValue sv = (AtomicValue) argument[0].evaluateItem(c);
        if (sv == null) {
            return StringValue.EMPTY_STRING;
        }
        switch(operation) {
            case UPPERCASE:
                return StringValue.makeStringValue(sv.getStringValue().toUpperCase());
            case LOWERCASE:
                return StringValue.makeStringValue(sv.getStringValue().toLowerCase());
            default:
                throw new UnsupportedOperationException("Unknown function");
        }
    }
}

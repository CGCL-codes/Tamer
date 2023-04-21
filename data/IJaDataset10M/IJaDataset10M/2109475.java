package org.tagbox.xpath.function;

import java.util.List;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeIterator;
import org.tagbox.xpath.primitive.Primitive;
import org.tagbox.xpath.iterator.Nodeset;
import org.tagbox.xpath.Expression;
import org.tagbox.xpath.primitive.NumberPrimitive;
import org.tagbox.xpath.XPathContext;
import org.tagbox.xpath.XPathException;
import org.tagbox.util.Log;

/**
 * Number last()
 */
public class LastFunction implements Function {

    public Primitive eval(Primitive args[], XPathContext ctxt) throws XPathException {
        if (args.length != 0) throw new XPathException("function 'last' takes no arguments");
        Nodeset set = ctxt.getContextNodeset();
        return new NumberPrimitive(new Integer(set.size()));
    }

    public String toString() {
        return getClass().getName();
    }
}

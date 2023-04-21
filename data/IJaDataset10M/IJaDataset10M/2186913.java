package org.tagbox.xpath.axis;

import org.w3c.dom.traversal.NodeIterator;
import org.w3c.dom.traversal.NodeFilter;
import org.tagbox.xpath.step.NodeTest;
import org.tagbox.xpath.XPathContext;
import org.tagbox.xpath.XPathException;
import org.tagbox.xpath.iterator.Nodeset;
import org.tagbox.xpath.iterator.DescendantNodeIterator;

public class DescendantAxis extends Axis {

    public DescendantAxis() {
        super(DESCENDANT);
    }

    public Nodeset eval(Nodeset parent, NodeTest test, XPathContext context) throws XPathException {
        DescendantNodeIterator nit = new DescendantNodeIterator(parent, test.eval(context));
        if (test.principal()) nit.setWhatToShow(NodeFilter.SHOW_ELEMENT);
        return nit;
    }

    public String toString() {
        return getClass().getName();
    }
}

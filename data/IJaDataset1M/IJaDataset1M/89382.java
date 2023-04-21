package org.tagbox.xpath.step;

import org.w3c.dom.traversal.NodeIterator;
import org.w3c.dom.traversal.NodeFilter;
import org.tagbox.xpath.XPathException;
import org.tagbox.xpath.Expression;
import org.tagbox.xpath.XPathContext;
import org.tagbox.xpath.iterator.PredicateIterator;
import org.tagbox.xpath.filter.PredicateFilter;
import org.tagbox.xpath.iterator.Nodeset;

public class Predicate {

    private Expression expr;

    public Predicate(Expression expr) {
        this.expr = expr;
    }

    public Nodeset eval(Nodeset parent, XPathContext ctxt) throws XPathException {
        return new PredicateIterator(parent, expr, ctxt);
    }

    public String toString() {
        return getClass().getName() + "[" + expr + "]";
    }
}

package org.jaxen.expr.iter;

import java.util.Iterator;
import org.jaxen.ContextSupport;
import org.jaxen.UnsupportedAxisException;

public class IterableFollowingSiblingAxis extends IterableAxis {

    /**
     * 
     */
    private static final long serialVersionUID = 4412705219546610009L;

    public IterableFollowingSiblingAxis(int value) {
        super(value);
    }

    public Iterator iterator(Object contextNode, ContextSupport support) throws UnsupportedAxisException {
        return support.getNavigator().getFollowingSiblingAxisIterator(contextNode);
    }
}

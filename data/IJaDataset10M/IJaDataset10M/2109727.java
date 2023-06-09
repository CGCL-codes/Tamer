package org.xmlpull.v1.builder.xpath.jaxen.util;

import org.xmlpull.v1.builder.xpath.jaxen.Navigator;
import org.xmlpull.v1.builder.xpath.jaxen.UnsupportedAxisException;
import java.util.Iterator;
import java.util.Collections;
import java.util.List;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class PrecedingSiblingAxisIterator implements Iterator {

    private Object contextNode;

    private Navigator navigator;

    private Iterator siblingIter;

    private Object nextObj;

    public PrecedingSiblingAxisIterator(Object contextNode, Navigator navigator) throws UnsupportedAxisException {
        this.contextNode = contextNode;
        this.navigator = navigator;
        init();
        stepAhead();
    }

    private void init() throws UnsupportedAxisException {
        Object parent = this.navigator.getParentNode(this.contextNode);
        List siblings = Collections.EMPTY_LIST;
        if (parent != null) {
            Iterator childIter = this.navigator.getChildAxisIterator(parent);
            Object eachChild = null;
            siblings = new LinkedList();
            while (childIter.hasNext()) {
                eachChild = childIter.next();
                if (eachChild == this.contextNode) {
                    break;
                }
                ((LinkedList) siblings).addFirst(eachChild);
            }
        }
        this.siblingIter = siblings.iterator();
    }

    public boolean hasNext() {
        return (this.nextObj != null);
    }

    public Object next() throws NoSuchElementException {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        Object obj = this.nextObj;
        this.nextObj = null;
        stepAhead();
        return obj;
    }

    public void remove() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    private void stepAhead() {
        if (this.nextObj != null) {
            return;
        }
        Object obj = null;
        while (siblingIter.hasNext()) {
            obj = siblingIter.next();
            this.nextObj = obj;
            break;
        }
    }
}

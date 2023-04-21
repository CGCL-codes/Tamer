package org.cubicunit.internal.ie;

import com.tapsterrock.jiffie.IHTMLDOMNode;

public class IeCubicResult {

    private final IHTMLDOMNode node;

    private final IeCubicElement element;

    private final int result;

    public IeCubicResult(IHTMLDOMNode node, IeCubicElement element, int result) {
        this.node = node;
        this.element = element;
        this.result = result;
    }

    public int getResult() {
        return result;
    }

    public IHTMLDOMNode getNode() {
        return node;
    }

    public IeCubicElement getElement() {
        return element;
    }
}

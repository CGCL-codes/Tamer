package com.jclark.xsl.expr;

import com.jclark.xsl.om.*;

class UniqueNodeIterator implements NodeIterator {

    private NodeIterator iter;

    private Node lastNode = null;

    UniqueNodeIterator(NodeIterator iter) {
        this.iter = iter;
    }

    public Node next() throws XSLException {
        for (; ; ) {
            Node tem = iter.next();
            if (tem == null) break;
            if (!tem.equals(lastNode)) return lastNode = tem;
        }
        return null;
    }
}

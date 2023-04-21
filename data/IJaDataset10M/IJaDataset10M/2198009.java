package com.blogspot.radialmind.html;

public abstract class Node implements IHTMLVisitor {

    private Node prevNode;

    public final Node getPrevNode() {
        return prevNode;
    }

    public final void setPrevNode(Node prevNode) {
        this.prevNode = prevNode;
    }
}

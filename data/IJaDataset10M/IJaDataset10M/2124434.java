package com.dukesoftware.utils.node.graph;

public class ConnectionWithAttribute<F, T, A> extends Connection<F, T> {

    private A attr;

    public ConnectionWithAttribute(F from, T to, A attr) {
        super(from, to);
        this.attr = attr;
    }

    public void setAttribute(A attr) {
        this.attr = attr;
    }

    public A getAttribute() {
        return attr;
    }
}

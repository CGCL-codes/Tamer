package com.brentcroft.jdbc.expressions;

public class SimpleNode implements Node {

    protected Node parent;

    protected Node[] children;

    protected int id;

    protected Object value;

    protected ExpressionParser parser;

    public Token token;

    public Token getToken() {
        return token;
    }

    public SimpleNode(final int i) {
        id = i;
    }

    public SimpleNode(final ExpressionParser p, final int i) {
        this(i);
        parser = p;
    }

    public void jjtOpen() {
    }

    public void jjtClose() {
    }

    public void jjtSetParent(final Node n) {
        parent = n;
    }

    public Node jjtGetParent() {
        return parent;
    }

    public void jjtAddChild(final Node n, final int i) {
        if (children == null) {
            children = new Node[i + 1];
        } else if (i >= children.length) {
            final Node c[] = new Node[i + 1];
            System.arraycopy(children, 0, c, 0, children.length);
            children = c;
        }
        children[i] = n;
    }

    public Node jjtGetChild(final int i) {
        return children[i];
    }

    public int jjtGetNumChildren() {
        return (children == null) ? 0 : children.length;
    }

    public void jjtSetValue(final Object value) {
        this.value = value;
    }

    public Object jjtGetValue() {
        return value;
    }

    public String toString() {
        return ExpressionParserTreeConstants.jjtNodeName[id];
    }

    public String toString(final String prefix) {
        return prefix + toString();
    }

    public void dump(final String prefix) {
        System.out.println(toString(prefix));
        if (children != null) {
            for (int i = 0; i < children.length; ++i) {
                final SimpleNode n = (SimpleNode) children[i];
                if (n != null) {
                    n.dump(prefix + " ");
                }
            }
        }
    }

    public int getId() {
        return id;
    }
}

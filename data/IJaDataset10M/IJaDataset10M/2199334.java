package org.salamy.tiger.impl;

public abstract class Node {

    /** The String ID of the node. */
    protected String id;

    /** Returns the ID String of this node. */
    public String getId() {
        return this.id;
    }

    /** Sets the ID String of this node. */
    public void setId(String newid) {
        id = newid;
    }

    public Node(String id) {
        super();
        if (id == null) {
            throw new NullPointerException("id cannot be null");
        }
        if ("".equals(id)) {
            throw new IllegalArgumentException("id cannot be null");
        }
        this.id = id;
    }
}

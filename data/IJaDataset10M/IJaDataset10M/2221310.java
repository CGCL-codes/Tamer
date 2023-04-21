package com.google.devtools.depan.graph.basic;

/**
 * A simple concrete node that just saves the id from its constructor.
 * 
 * @author <a href="leeca@google.com">Lee Carver</a>
 *
 */
public class SimpleNode<T> extends BasicNode<T> {

    /** Internal storage for the id. */
    private final T id;

    /**
   * Create a SimpleNode for the provided id.
   * 
   * @param id id to associate with SimpleNode
   */
    public SimpleNode(T id) {
        super();
        this.id = id;
    }

    /**
   * {@inheritDoc}
   */
    public T getId() {
        return id;
    }
}

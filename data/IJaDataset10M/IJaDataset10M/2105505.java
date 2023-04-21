package com.dyuproject.protostuff.me;

/**
 * An output that keeps the state of the schema being used.
 *
 * @author David Yu
 * @created Jan 24, 2011
 */
public interface StatefulOutput extends Output {

    /**
     * Updates the schema if {@code lastSchema} was indeed the last schema used.
     */
    public void updateLast(Schema schema, Schema lastSchema);
}

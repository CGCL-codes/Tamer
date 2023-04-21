package com.google.gwt.validation.client.impl;

import junit.framework.TestCase;
import javax.validation.Path.Node;

/**
 * Tests for {@link NodeImpl}.
 */
public class NodeImplTest extends TestCase {

    public void testFoo() throws Exception {
        assertNode(NodeImpl.createNode("foo"), "foo", false, null, null);
    }

    public void testFoo_iterable() throws Exception {
        assertNode(NodeImpl.createIterableNode("foo"), "foo", true, null, null);
    }

    public void testFoo1() throws Exception {
        assertNode(NodeImpl.createIndexedNode("foo", 1), "foo", true, null, Integer.valueOf(1));
    }

    public void testFooBar() throws Exception {
        assertNode(NodeImpl.createKeyedNode("foo", "bar"), "foo", true, "bar", null);
    }

    public void testRoot() throws Exception {
        assertNode(NodeImpl.ROOT_NODE, null, false, null, null);
    }

    protected void assertNode(Node node, String expectedName, boolean expectedInIterator, Object expectedKey, Integer expectedIndex) {
        assertEquals(node + " name", expectedName, node.getName());
        assertEquals(node + " isInIterator", expectedInIterator, node.isInIterable());
        assertEquals(node + " key", expectedKey, node.getKey());
        assertEquals(node + " index", expectedIndex, node.getIndex());
    }
}

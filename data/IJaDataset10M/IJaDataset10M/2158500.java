package net.sf.joafip.redblacktree;

import net.sf.joafip.AbstractJoafipTestCase;
import net.sf.joafip.NotStorableClass;
import net.sf.joafip.redblacktree.entity.IRBTNode;
import net.sf.joafip.redblacktree.impl.memory.entity.RBTNode;
import net.sf.joafip.redblacktree.impl.memory.service.RBTNodeManager;
import net.sf.joafip.redblacktree.service.IRBTNodeManager;
import net.sf.joafip.redblacktree.service.RBTException;
import net.sf.joafip.redblacktree.service.RedBlackTree;

@NotStorableClass
public class TestSearchUnique extends AbstractJoafipTestCase {

    private RedBlackTree<Integer> tree;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        final IRBTNodeManager<Integer> nodeManager = new RBTNodeManager<Integer>();
        tree = new RedBlackTree<Integer>(nodeManager, false, true);
        IRBTNode<Integer> nodeToAppend;
        nodeToAppend = new RBTNode<Integer>(10);
        tree.append(nodeToAppend);
        nodeToAppend = new RBTNode<Integer>(20);
        tree.append(nodeToAppend);
        nodeToAppend = new RBTNode<Integer>(30);
        tree.append(nodeToAppend);
        nodeToAppend = new RBTNode<Integer>(40);
        tree.append(nodeToAppend);
        nodeToAppend = new RBTNode<Integer>(50);
        tree.append(nodeToAppend);
        nodeToAppend = new RBTNode<Integer>(60);
        tree.append(nodeToAppend);
        nodeToAppend = new RBTNode<Integer>(70);
        tree.append(nodeToAppend);
        nodeToAppend = new RBTNode<Integer>(80);
        tree.append(nodeToAppend);
        nodeToAppend = new RBTNode<Integer>(90);
        tree.append(nodeToAppend);
        nodeToAppend = new RBTNode<Integer>(100);
        tree.append(nodeToAppend);
    }

    @Override
    protected void tearDown() throws Exception {
        tree = null;
        super.tearDown();
    }

    public void testSearch() throws RBTException {
        IRBTNode<Integer> node;
        node = tree.search(5);
        assertNull("5 must not exist", node);
        node = tree.search(10);
        assertNotNull("must found 10", node);
        assertEquals("value must be 10", Integer.valueOf(10), node.getElement());
        node = tree.search(20);
        assertNotNull("must found 20", node);
        assertEquals("value must be 20", Integer.valueOf(20), node.getElement());
    }

    public void testClosestGreaterOrEqual() throws RBTException {
        IRBTNode<Integer> node;
        node = tree.closestGreaterOrEqual(10);
        assertNotNull("must found 10", node);
        assertEquals("value must be 10", Integer.valueOf(10), node.getElement());
        node = tree.closestGreaterOrEqual(15);
        assertNotNull("must found greater than 15", node);
        assertEquals("value must be 20", Integer.valueOf(20), node.getElement());
    }

    public void testStrictlyGreater() throws RBTException {
        IRBTNode<Integer> node;
        node = tree.strictlyGreater(10);
        assertNotNull("must found greater than 10", node);
        assertEquals("value must be 20", Integer.valueOf(20), node.getElement());
    }

    public void testClosestLessOrEqual() throws RBTException {
        IRBTNode<Integer> node;
        node = tree.closestLessOrEqual(100);
        assertNotNull("must found 100", node);
        assertEquals("value must be 100", Integer.valueOf(100), node.getElement());
        node = tree.closestLessOrEqual(95);
        assertNotNull("must found less than 95", node);
        assertEquals("value must be 90", Integer.valueOf(90), node.getElement());
    }

    public void testStrictlyLess() throws RBTException {
        IRBTNode<Integer> node;
        node = tree.strictlyLess(100);
        assertNotNull("must found less than 100", node);
        assertEquals("value must be 90", Integer.valueOf(90), node.getElement());
    }
}

package org.unitmetrics.java.ui.views.graph.cycle;

import org.unitmetrics.junit.TestCase;

/**
 * @author Martin Kersten
 */
public class NodeTest extends TestCase {

    public void testNodeCreation() {
        Node node = new Node(1);
        assertEquals("The id of the node should be 1", 1, node.getId());
        assertEmpty("Node may not have a neighbour", node.getNeighbours());
        Node nodeA = new Node(1);
        Node nodeB = new Node(2);
        Node[] neighbours = new Node[] { nodeA };
        node = new Node(3, neighbours);
        node.addNeighbour(nodeB);
        assertEquals("The id of the node should be 3", 3, node.getId());
        assertSize("Node should have two neighbours", 2, node.getNeighbours());
        assertContains("NodeA should be a neighbour", nodeA, node.getNeighbours());
        assertContains("NodeB should be a neighbour", nodeB, node.getNeighbours());
    }
}

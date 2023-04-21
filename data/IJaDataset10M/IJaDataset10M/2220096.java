package ontorama.backends;

import junit.framework.TestCase;
import ontorama.util.IteratorUtil;
import java.util.*;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 *
 * Not testing following methods:
 * - all private methods
 * - clearAllEdges method
 * - removeEdge method
 * - removeAllEdges method
 */
public class TestEdge extends TestCase {

    private GraphNode node1;

    private GraphNode node2;

    private GraphNode node3;

    private GraphNode node4;

    private GraphNode node5;

    private GraphNode node6;

    private Edge edge;

    private EdgeObject edgeObject1;

    private EdgeObject edgeObject2;

    private EdgeObject edgeObject3;

    private EdgeObject edgeObject4;

    private EdgeObject edgeObject5;

    private EdgeObject edgeObject6;

    private EdgeObject edgeObject7;

    private LinkedList relLinksSet;

    private LinkedList outboundEdgesListForNode1 = new LinkedList();

    private LinkedList inboundEdgesListForNode6 = new LinkedList();

    private LinkedList outboundEdgesListForNode1Relation1 = new LinkedList();

    private LinkedList inboundEdgesListForNode6Relation1 = new LinkedList();

    private LinkedList outboundNodesListForNode1RelLinkSet = new LinkedList();

    private LinkedList inboundNodesListForNode6RelLinkSet = new LinkedList();

    private LinkedList outboundNodesListForNode1Relation1 = new LinkedList();

    private LinkedList inboundNodesListForNode6Relation2 = new LinkedList();

    private LinkedList outboundNodesListForNode1 = new LinkedList();

    private LinkedList inboundNodesListForNode6 = new LinkedList();

    /**
     *
     */
    public TestEdge(String name) {
        super(name);
        this.edge = new Edge();
    }

    /**
     *
     */
    protected void setUp() {
        this.edge.clearEdgesList();
        node1 = new GraphNode("node1");
        node2 = new GraphNode("node2");
        node3 = new GraphNode("node3");
        node4 = new GraphNode("node4");
        node5 = new GraphNode("node5");
        node6 = new GraphNode("node6");
        edgeObject1 = new EdgeObject(node1, node2, 1, "http://undefined.se");
        edgeObject2 = new EdgeObject(node1, node3, 1, "http://undefined.se");
        edgeObject3 = new EdgeObject(node1, node4, 2, "http://undefined.se");
        edgeObject4 = new EdgeObject(node1, node5, 3, "http://undefined.se");
        edgeObject5 = new EdgeObject(node2, node6, 1, "http://undefined.se");
        edgeObject6 = new EdgeObject(node3, node6, 2, "http://undefined.se");
        edgeObject7 = new EdgeObject(node4, node6, 3, "http://undefined.se");
        this.edge.addEdge(edgeObject1);
        this.edge.addEdge(edgeObject2);
        this.edge.addEdge(edgeObject3);
        this.edge.addEdge(edgeObject4);
        this.edge.addEdge(edgeObject5);
        this.edge.addEdge(edgeObject6);
        this.edge.addEdge(edgeObject7);
        relLinksSet = new LinkedList();
        relLinksSet.add(new Integer(2));
        relLinksSet.add(new Integer(3));
        outboundEdgesListForNode1.add(edgeObject1);
        outboundEdgesListForNode1.add(edgeObject2);
        outboundEdgesListForNode1.add(edgeObject3);
        outboundEdgesListForNode1.add(edgeObject4);
        inboundEdgesListForNode6.add(edgeObject5);
        inboundEdgesListForNode6.add(edgeObject6);
        inboundEdgesListForNode6.add(edgeObject7);
        outboundEdgesListForNode1Relation1.add(edgeObject1);
        outboundEdgesListForNode1Relation1.add(edgeObject2);
        inboundEdgesListForNode6Relation1.add(edgeObject5);
        outboundNodesListForNode1RelLinkSet.add(node4);
        outboundNodesListForNode1RelLinkSet.add(node5);
        inboundNodesListForNode6RelLinkSet.add(node3);
        inboundNodesListForNode6RelLinkSet.add(node4);
        outboundNodesListForNode1Relation1.add(node2);
        outboundNodesListForNode1Relation1.add(node3);
        inboundNodesListForNode6Relation2.add(node3);
        outboundNodesListForNode1.add(node2);
        outboundNodesListForNode1.add(node3);
        outboundNodesListForNode1.add(node4);
        outboundNodesListForNode1.add(node5);
        inboundNodesListForNode6.add(node2);
        inboundNodesListForNode6.add(node3);
        inboundNodesListForNode6.add(node4);
    }

    /**
     * test number of edges in the list
     */
    public void testEdgeListSize() {
        assertEquals(7, this.edge.getIteratorSize(this.edge.getEdgeIterator()));
    }

    /**
     *
     */
    public void testEdgeList() {
        Iterator edgesIt = this.edge.getEdgeIterator();
        int nrEdges = this.edge.getIteratorSize(this.edge.getEdgeIterator());
        EdgeObject currEdgeObject;
        for (int i = 0; i < nrEdges; i++) {
            currEdgeObject = (EdgeObject) edgesIt.next();
            if (i == 0) {
                assertEquals(edgeObject1, currEdgeObject);
                continue;
            }
            if (i == 1) {
                assertEquals(edgeObject2, currEdgeObject);
                continue;
            }
            if (i == 2) {
                assertEquals(edgeObject3, currEdgeObject);
                continue;
            }
            if (i == 3) {
                assertEquals(edgeObject4, currEdgeObject);
                continue;
            }
            if (i == 4) {
                assertEquals(edgeObject5, currEdgeObject);
                continue;
            }
            if (i == 5) {
                assertEquals(edgeObject6, currEdgeObject);
                continue;
            }
            if (i == 6) {
                assertEquals(edgeObject7, currEdgeObject);
                continue;
            }
        }
    }

    /**
     * test method getFromNode
     */
    public void testGetFromNode() {
        assertEquals(node1, edgeObject1.getFromNode());
        assertEquals(node1, edgeObject2.getFromNode());
        assertEquals(node1, edgeObject3.getFromNode());
        assertEquals(node1, edgeObject4.getFromNode());
        assertEquals(node2, edgeObject5.getFromNode());
        assertEquals(node3, edgeObject6.getFromNode());
        assertEquals(node4, edgeObject7.getFromNode());
    }

    /**
     * test method getToNode
     */
    public void testGetToNode() {
        assertEquals(node2, edgeObject1.getToNode());
        assertEquals(node3, edgeObject2.getToNode());
        assertEquals(node4, edgeObject3.getToNode());
        assertEquals(node5, edgeObject4.getToNode());
        assertEquals(node6, edgeObject5.getToNode());
        assertEquals(node6, edgeObject6.getToNode());
        assertEquals(node6, edgeObject7.getToNode());
    }

    /**
     * test method getType
     */
    public void testGetType() {
        assertEquals(1, edgeObject1.getType());
        assertEquals(1, edgeObject2.getType());
        assertEquals(2, edgeObject3.getType());
        assertEquals(3, edgeObject4.getType());
        assertEquals(1, edgeObject5.getType());
        assertEquals(2, edgeObject6.getType());
        assertEquals(3, edgeObject7.getType());
    }

    /**
     * test method getOutboundEdges
     */
    public void testGetOutboundEdges() {
        assertEquals(outboundEdgesListForNode1.size(), getIteratorSize(this.edge.getOutboundEdges(node1)));
        compareListToIterator(outboundEdgesListForNode1, this.edge.getOutboundEdges(node1));
    }

    /**
     * test method getInboundEdges
     */
    public void testGetInboundEdges() {
        assertEquals(inboundEdgesListForNode6.size(), getIteratorSize(this.edge.getInboundEdges(node6)));
        compareListToIterator(inboundEdgesListForNode6, this.edge.getInboundEdges(node6));
    }

    /**
     * test method getOutboundEdges (node, type)
     */
    public void testOutboundEdgesForRelationType() {
        assertEquals(outboundEdgesListForNode1Relation1.size(), getIteratorSize(this.edge.getOutboundEdges(node1, 1)));
        compareListToIterator(outboundEdgesListForNode1Relation1, this.edge.getOutboundEdges(node1, 1));
    }

    /**
     * test method getInboundEdges (node, type)
     */
    public void testInboundEdgesForRelationType() {
        assertEquals(inboundEdgesListForNode6Relation1.size(), getIteratorSize(this.edge.getInboundEdges(node6, 1)));
        compareListToIterator(inboundEdgesListForNode6Relation1, this.edge.getInboundEdges(node6, 1));
    }

    /**
     * test method getOutboundEdgeNodes(GraphNode node, Set relationLinks)
     */
    public void testGetOutboundEdgeNodesForRelationLinksSet() {
        Iterator outboundNodes = this.edge.getOutboundEdgeNodes(node1, relLinksSet);
        assertEquals(outboundNodesListForNode1RelLinkSet.size(), getIteratorSize(outboundNodes));
        outboundNodes = this.edge.getOutboundEdgeNodes(node1, relLinksSet);
        compareListToIterator(outboundNodesListForNode1RelLinkSet, outboundNodes);
    }

    /**
     * test method getInboundEdgeNodes(GraphNode node, Set relationLinks)
     */
    public void testGetInboundEdgeNodesForRelationLinksSet() {
        Iterator inboundNodes = this.edge.getInboundEdgeNodes(node6, relLinksSet);
        assertEquals(inboundNodesListForNode6RelLinkSet.size(), getIteratorSize(inboundNodes));
        inboundNodes = this.edge.getInboundEdgeNodes(node6, relLinksSet);
        compareListToIterator(inboundNodesListForNode6RelLinkSet, inboundNodes);
    }

    /**
     * test method getOutboundEdgeNodes (GraphNode node, int relationType)
     */
    public void testGetOutboundEdgeNodesForRelationType() {
        Iterator outboundNodes = this.edge.getOutboundEdgeNodes(node1, 1);
        assertEquals(outboundNodesListForNode1Relation1.size(), getIteratorSize(outboundNodes));
        outboundNodes = this.edge.getOutboundEdgeNodes(node1, 1);
        compareListToIterator(outboundNodesListForNode1Relation1, outboundNodes);
    }

    /**
     * test method getInboundEdgeNodes (GraphNode node, int relationType)
     */
    public void testGetInboundEdgeNodesForRelationType() {
        Iterator inboundNodes = this.edge.getInboundEdgeNodes(node6, 2);
        assertEquals(inboundNodesListForNode6Relation2.size(), getIteratorSize(inboundNodes));
        inboundNodes = this.edge.getInboundEdgeNodes(node6, 2);
        compareListToIterator(inboundNodesListForNode6Relation2, inboundNodes);
    }

    /**
     * test method getOutboundEdgeNodes
     */
    public void testGetOutboundEdgeNodes() {
        Iterator outboundNodes = this.edge.getOutboundEdgeNodes(node1);
        assertEquals(outboundNodesListForNode1.size(), getIteratorSize(outboundNodes));
        outboundNodes = this.edge.getOutboundEdgeNodes(node1);
        compareListToIterator(outboundNodesListForNode1, outboundNodes);
    }

    /**
     * test method getInboundEdgeNodes
     */
    public void testGetInboundEdgeNodes() {
        Iterator inboundNodes = this.edge.getInboundEdgeNodes(node6);
        assertEquals(inboundNodesListForNode6.size(), getIteratorSize(inboundNodes));
        inboundNodes = this.edge.getInboundEdgeNodes(node6);
        compareListToIterator(inboundNodesListForNode6, inboundNodes);
    }

    /**
     * test method getOutboundEdgeNodesList
     */
    public void testGetOutboundEdgeNodesList() {
        Iterator outboundNodes = this.edge.getOutboundEdgeNodes(node1);
        assertEquals(outboundNodesListForNode1.size(), getIteratorSize(outboundNodes));
        outboundNodes = this.edge.getOutboundEdgeNodes(node1);
        compareListToIterator(outboundNodesListForNode1, outboundNodes);
    }

    /**
     * test method getInboundEdgeNodesList
     */
    public void testGetInboundEdgeNodesList() {
        Iterator inboundNodes = this.edge.getInboundEdgeNodes(node6);
        assertEquals(inboundNodesListForNode6.size(), getIteratorSize(inboundNodes));
        inboundNodes = this.edge.getInboundEdgeNodes(node6);
        compareListToIterator(inboundNodesListForNode6, inboundNodes);
    }

    /**
     *
     */
    private int getIteratorSize(Iterator it) {
        return IteratorUtil.getIteratorSize(it);
    }

    /**
     * compare contents of given iterator and given list
     *
     * The idea is: if method we want to check returns an iterator,
     * we build list of expected objects and then go through iterator
     * and check if objects in corresponding places are equal
     */
    private void compareListToIterator(List list, Iterator iterator) {
        int count = 0;
        while (iterator.hasNext()) {
            Object cur = iterator.next();
            assertEquals(list.get(count), cur);
            count++;
        }
    }

    /**
     * create a set of int's
     */
    private Set createSet(int int1, int int2) {
        Set set = new HashSet();
        set.add(new Integer(int1));
        set.add(new Integer(int2));
        return set;
    }
}

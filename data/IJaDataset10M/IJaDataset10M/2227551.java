package net.sourceforge.combean.test.graph.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import junit.framework.TestCase;
import net.sourceforge.combean.graph.impl.MapGraph;
import net.sourceforge.combean.util.Twins;
import org.apache.commons.collections.CollectionUtils;

public class TestMapGraph extends TestCase {

    private static final Integer neighDef[][] = { { 1, 2 }, { 2, 3 }, { 3 }, {} };

    private MapGraph<Integer> graph = null;

    public TestMapGraph(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        this.graph = MapGraph.newIntMapGraphFromArray(neighDef);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetOutgoingEdges() {
        for (int node = 0; node < neighDef.length; node++) {
            List<Integer> adjacentNodes = new ArrayList<Integer>(neighDef.length);
            Iterator<Twins<Integer>> itOut = this.graph.getOutgoingEdges(node);
            while (itOut.hasNext()) {
                Twins<Integer> edge = itOut.next();
                assertEquals(new Integer(node), this.graph.getFirstNode(edge));
                adjacentNodes.add(this.graph.getSecondNode(edge));
            }
            assertEquals(Arrays.asList(neighDef[node]), adjacentNodes);
        }
    }

    public void testGetNumNodes() {
        assertEquals(neighDef.length, this.graph.getNumNodes());
    }

    public void testGetAllNodes() {
        SortedSet<Integer> nodeSet = new TreeSet<Integer>();
        CollectionUtils.addAll(nodeSet, this.graph.getAllNodesIterator());
        assertEquals(Arrays.asList(0, 1, 2, 3), new ArrayList<Integer>(nodeSet));
    }
}

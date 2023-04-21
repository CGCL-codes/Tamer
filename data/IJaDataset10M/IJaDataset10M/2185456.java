package org.cleanbuild.graph;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.cleanbuild.graph.DirectGraphDependency;
import org.cleanbuild.graph.DirectedAcyclicGraph;
import org.cleanbuild.graph.GraphNode;
import junit.framework.TestCase;

/**
 * @author Malcolm Sparks
 */
public class TopographicSortTest extends TestCase {

    static final String[] PAIRS = new String[] { "AC", "BC", "BD", "CE", "CF", "DF", "DG", "ZX" };

    TestGraphNodeFactory factory = new TestGraphNodeFactory();

    DirectedAcyclicGraph graph = new DirectedAcyclicGraph();

    public void setUp() throws Exception {
        for (int i = 0; i < PAIRS.length; i++) {
            GraphNode dependant = factory.getNode(String.valueOf(PAIRS[i].substring(0, 1)));
            GraphNode dependency = factory.getNode(String.valueOf(PAIRS[i].substring(1, 2)));
            dependant.addDependency(dependency);
        }
        for (Iterator<GraphNode> it = factory.iterator(); it.hasNext(); ) {
            GraphNode graphNode = it.next();
            graph.addNode(graphNode);
        }
    }

    public void testGraphIsClosed() throws Exception {
        assertTrue(graph.isClosed());
    }

    public void testGraphHasNoCircularDependencies() throws Exception {
        assertFalse(graph.hasCircularDependencies());
    }

    public void testNumbersOfDependencies() throws Exception {
        assertEquals(1, factory.getNode("A").getNumberOfDependencies());
        assertEquals(2, factory.getNode("B").getNumberOfDependencies());
        assertEquals(2, factory.getNode("C").getNumberOfDependencies());
        assertEquals(2, factory.getNode("D").getNumberOfDependencies());
        assertEquals(0, factory.getNode("E").getNumberOfDependencies());
        assertEquals(0, factory.getNode("F").getNumberOfDependencies());
        assertEquals(0, factory.getNode("G").getNumberOfDependencies());
    }

    public void testTopographicSort() throws Exception {
        Set<GraphNode> seen = new HashSet<GraphNode>();
        Iterator<GraphNode> it = graph.iterator();
        while (it.hasNext()) {
            GraphNode node = it.next();
            for (DirectGraphDependency dependency : node.getDirectDependencies()) {
                assertTrue(seen.contains(dependency.getTarget()));
            }
            seen.add(node);
        }
    }
}

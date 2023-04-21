package nz.ac.vuw.ecs.kcassell.callgraph;

import java.util.SortedSet;
import java.util.Vector;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

public class CallGraphClusterTest extends TestCase {

    @Before
    public void setUp() {
        ScoreComparator comparator = new ScoreComparator();
        comparator.setAscending(false);
        comparator.setScoreBeingCompared(ScoreType.BASIC);
        CallGraphCluster.setComparator(comparator);
    }

    @Test
    public void testCallGraphCluster() {
        CallGraphNode node1 = new CallGraphNode();
        node1.setId(1);
        node1.setScore(ScoreType.BASIC, 1.0);
        CallGraphNode node2 = new CallGraphNode();
        node2.setId(2);
        node2.setScore(ScoreType.BASIC, 2.0);
        CallGraphNode node3 = new CallGraphNode();
        node3.setId(3);
        node3.setScore(ScoreType.BASIC, 3.0);
        Vector<CallGraphNode> nodeVec = new Vector<CallGraphNode>();
        nodeVec.add(node2);
        nodeVec.add(node3);
        nodeVec.add(node1);
        CallGraphCluster cluster1 = new CallGraphCluster(nodeVec);
        SortedSet<CallGraphNode> nodes = cluster1.getElements();
        assertEquals(node3, nodes.first());
        assertEquals(node1, nodes.last());
    }

    @Test
    public void testToString() {
        CallGraphNode node1 = new CallGraphNode();
        node1.setId(1);
        node1.setLabel("node1");
        node1.setScore(ScoreType.BASIC, 1.0);
        CallGraphNode node2 = new CallGraphNode();
        node2.setId(2);
        node2.setLabel("node2");
        node2.setScore(ScoreType.BASIC, 2.0);
        CallGraphNode node3 = new CallGraphNode();
        node3.setId(3);
        node3.setLabel("node3");
        node3.setScore(ScoreType.BASIC, 3.0);
        Vector<CallGraphNode> nodeVec = new Vector<CallGraphNode>();
        nodeVec.add(node2);
        nodeVec.add(node3);
        nodeVec.add(node1);
        CallGraphCluster cluster1 = new CallGraphCluster(nodeVec);
        SortedSet<CallGraphNode> nodes = cluster1.getElements();
        String result = nodes.toString();
        int indexNode1 = result.indexOf("node1");
        int indexNode2 = result.indexOf("node2");
        int indexNode3 = result.indexOf("node3");
        assertTrue(indexNode1 > indexNode2);
        assertTrue(indexNode2 > indexNode3);
    }
}

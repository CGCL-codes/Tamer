package com.google.devtools.depan.view;

import com.google.common.collect.Lists;
import com.google.devtools.depan.graph.basic.MultipleDirectedRelationFinder;
import com.google.devtools.depan.model.GraphModel;
import com.google.devtools.depan.model.GraphNode;
import com.google.devtools.depan.model.testing.TestUtils;
import junit.framework.TestCase;
import java.util.Collection;

public class CollapserTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
   * Assert that the ViewModel provides the expected number of
   * exposed nodes and edges.
   *
   * @param testView ViewModel to test
   * @param nodeCnt expected number of exposed nodes
   * @param edgeCnt expected number of exposed edges
   */
    private static void assertGraphNodesEdges(GraphModel testGraph, int nodeCnt, int edgeCnt) {
        assertEquals(nodeCnt, testGraph.getNodes().size());
        assertEquals(edgeCnt, testGraph.getEdges().size());
    }

    /**
   * Create a simple complete-5 graph, and verify that everything arrives
   * in the complete view.
   */
    public void testBasic() {
        GraphModel testGraph = new GraphModel();
        GraphNode srcNodes[] = TestUtils.buildComplete(testGraph, 5, SampleRelation.sampleRelation);
    }

    /**
   * Create a simple complete-5 graph with a view, and then collapse
   * the least 2 significant nodes into a collapsed node.
   */
    public void testCollapse() {
        Collapser collapser = new Collapser();
        GraphModel testGraph = new GraphModel();
        GraphNode srcNodes[] = TestUtils.buildComplete(testGraph, 5, SampleRelation.sampleRelation);
        assertGraphNodesEdges(testGraph, 5, 10);
        GraphNode master = srcNodes[3];
        Collection<GraphNode> collapsed = Lists.newArrayList();
        collapsed.add(master);
        collapsed.add(srcNodes[4]);
        collapser.collapse(master, collapsed, true);
        assertGraphNodesEdges(collapser.buildExposedGraph(testGraph), 4, 9);
    }

    /**
   * Create a simple complete-5 graph with a view, and then:
   * 1) collapse the least 2 significant nodes into a collapsed node.
   * 2) collapse the next least significant node into the collapsed master
   * from step 1
   */
    public void testNestedCollapse() {
        Collapser collapser = new Collapser();
        GraphModel testGraph = new GraphModel();
        GraphNode srcNodes[] = TestUtils.buildComplete(testGraph, 5, SampleRelation.sampleRelation);
        assertGraphNodesEdges(testGraph, 5, 10);
        GraphNode masterOne = srcNodes[3];
        Collection<GraphNode> collapseOne = Lists.newArrayList();
        collapseOne.add(masterOne);
        collapseOne.add(srcNodes[4]);
        collapser.collapse(masterOne, collapseOne, true);
        assertGraphNodesEdges(collapser.buildExposedGraph(testGraph), 4, 9);
        GraphNode masterTwo = srcNodes[2];
        Collection<GraphNode> collapseTwo = Lists.newArrayList();
        collapseTwo.add(masterOne);
        collapseTwo.add(masterTwo);
        collapser.collapse(masterTwo, collapseTwo, false);
        assertGraphNodesEdges(collapser.buildExposedGraph(testGraph), 3, 7);
    }

    /**
   * Create a simple complete-5 graph with a view, and then:
   * 1) collapse the least 2 significant nodes into a collapsed node.
   * 2) collapse the next 2 least significant nodes into a separate
   * collapse group.
   * <p>
   * Note that reusing the master and picked list demonstrates that the
   * collapse group does not change if the input variables are altered.
   */
    public void testDoubleCollapse() {
        Collapser collapser = new Collapser();
        GraphModel testGraph = new GraphModel();
        GraphNode srcNodes[] = TestUtils.buildComplete(testGraph, 5, SampleRelation.sampleRelation);
        assertGraphNodesEdges(testGraph, 5, 10);
        GraphNode master;
        Collection<GraphNode> picked = Lists.newArrayList();
        master = srcNodes[3];
        picked.add(master);
        picked.add(srcNodes[4]);
        collapser.collapse(master, picked, false);
        assertGraphNodesEdges(collapser.buildExposedGraph(testGraph), 4, 9);
        master = srcNodes[1];
        picked.clear();
        picked.add(master);
        picked.add(srcNodes[2]);
        collapser.collapse(master, picked, false);
        assertGraphNodesEdges(collapser.buildExposedGraph(testGraph), 3, 8);
    }

    public void testAutoCollapse() {
        Collapser collapser = new Collapser();
        GraphModel testGraph = new GraphModel();
        GraphNode srcNodes[] = TestUtils.buildComplete(testGraph, 5, SampleRelation.sampleRelation);
        assertGraphNodesEdges(testGraph, 5, 10);
        MultipleDirectedRelationFinder finder = new MultipleDirectedRelationFinder();
        finder.addRelation(SampleRelation.sampleRelation, true, false);
        TreeModel treeData = new TreeModel(testGraph.computeSuccessorHierarchy(finder));
        Collection<CollapseData> collapseChanges = collapser.collapseTree(testGraph, treeData);
        assertGraphNodesEdges(collapser.buildExposedGraph(testGraph), 1, 0);
        collapser.uncollapse(srcNodes[0], false);
        assertGraphNodesEdges(collapser.buildExposedGraph(testGraph), 2, 4);
        collapser.uncollapse(srcNodes[1], false);
        assertGraphNodesEdges(collapser.buildExposedGraph(testGraph), 3, 7);
        collapser.uncollapse(srcNodes[2], false);
        assertGraphNodesEdges(collapser.buildExposedGraph(testGraph), 4, 9);
        collapser.uncollapse(srcNodes[3], false);
        assertGraphNodesEdges(collapser.buildExposedGraph(testGraph), 5, 10);
    }
}

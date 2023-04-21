package org.openscience.cdk.test.graph;

import java.util.List;
import junit.framework.Test;
import junit.framework.TestSuite;
import org._3pq.jgrapht.graph.SimpleGraph;
import org.openscience.cdk.graph.BFSShortestPath;
import org.openscience.cdk.graph.MoleculeGraphs;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.templates.MoleculeFactory;
import org.openscience.cdk.test.CDKTestCase;

/**
 * @cdk.module test-standard
 */
public class BFSShortestPathTest extends CDKTestCase {

    public BFSShortestPathTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(BFSShortestPathTest.class);
    }

    public void testFindPathBetween_Graph_Object_Object() {
        IMolecule apinene = MoleculeFactory.makeAlphaPinene();
        SimpleGraph graph = MoleculeGraphs.getMoleculeGraph(apinene);
        Object startVertex = graph.vertexSet().toArray()[0];
        Object endVertex = graph.vertexSet().toArray()[5];
        List list = BFSShortestPath.findPathBetween(graph, startVertex, endVertex);
        assertTrue(list.size() > 0);
    }
}

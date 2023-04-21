package org.openscience.cdk.test.graph;

import junit.framework.Test;
import junit.framework.TestSuite;
import org._3pq.jgrapht.graph.SimpleGraph;
import org.openscience.cdk.graph.MoleculeGraphs;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.templates.MoleculeFactory;
import org.openscience.cdk.test.CDKTestCase;

/**
 * @cdk.module test-standard
 */
public class MoleculeGraphsTest extends CDKTestCase {

    public MoleculeGraphsTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(MoleculeGraphsTest.class);
    }

    /**
	 * Tests that the jgrapht graph has as many vertices as atoms,
	 * and as many edges as bonds in alpha-pinene.
	 */
    public void testGetMoleculeGraph_IAtomContainer() {
        IMolecule apinene = MoleculeFactory.makeAlphaPinene();
        SimpleGraph graph = MoleculeGraphs.getMoleculeGraph(apinene);
        assertEquals(apinene.getAtomCount(), graph.vertexSet().size());
        assertEquals(apinene.getBondCount(), graph.edgeSet().size());
    }
}

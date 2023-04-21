package com.hp.hpl.jena.reasoner.test;

import junit.framework.*;
import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.test.AbstractTestGraph;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.reasoner.InfGraph;

/**
     Needs extending; relys on knowing that the only InfGraph currently used is
     the Jena-provided base. Needs to be made into an abstract test and
     parametrised with the InfGraph being tested (hence getInfGraph).
    @author hedgehog
*/
public class TestInfGraph extends AbstractTestGraph {

    public TestInfGraph(String name) {
        super(name);
    }

    public static TestSuite suite() {
        return new TestSuite(TestInfGraph.class);
    }

    private InfGraph getInfGraph() {
        return (InfGraph) ModelFactory.createOntologyModel().getGraph();
    }

    public Graph getGraph() {
        return getInfGraph();
    }

    public void testInfGraph() {
        InfGraph ig = getInfGraph();
        assertSame(ig.getPrefixMapping(), ig.getRawGraph().getPrefixMapping());
    }

    public void testInfReification() {
        InfGraph ig = getInfGraph();
        assertSame(ig.getReifier(), ig.getRawGraph().getReifier());
    }

    /**
         Placeholder. Will need revision later.
    */
    public void testInfCapabilities() {
        assertTrue(getInfGraph().getCapabilities().findContractSafe());
    }
}

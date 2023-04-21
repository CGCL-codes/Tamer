package com.hp.hpl.jena.rdf.model.test;

import java.util.*;
import com.hp.hpl.jena.graph.*;
import com.hp.hpl.jena.graph.impl.WrappedGraph;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.impl.ModelCom;
import junit.framework.TestSuite;

public class TestRemoveSPO extends ModelTestBase {

    public TestRemoveSPO(String name) {
        super(name);
    }

    public static TestSuite suite() {
        return new TestSuite(TestRemoveSPO.class);
    }

    public void testRemoveSPOReturnsModel() {
        Model m = new ModelCom(Factory.createDefaultGraph());
        assertSame(m, m.remove(resource("R"), property("P"), rdfNode(m, "17")));
    }

    public void testRemoveSPOCallsGraphDeleteTriple() {
        Graph base = Factory.createDefaultGraph();
        final List deleted = new ArrayList();
        Graph wrapped = new WrappedGraph(base) {

            public void delete(Triple t) {
                deleted.add(t);
            }
        };
        Model m = new ModelCom(wrapped);
        m.remove(resource("R"), property("P"), rdfNode(m, "17"));
        assertEquals(listOfOne(Triple.create("R P 17")), deleted);
    }
}

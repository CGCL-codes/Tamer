package com.hp.hpl.jena.reasoner.test;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.util.PrintUtil;
import com.hp.hpl.jena.vocabulary.RDFS;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Test machinery in InfModel which is not associated with any 
 * particular reasoner.
 * 
 * @author <a href="mailto:der@hplb.hpl.hp.com">Dave Reynolds</a>
 * @version $Revision: 1.2 $
 */
public class TestInfModel extends TestCase {

    /**
     * Boilerplate for junit
     */
    public TestInfModel(String name) {
        super(name);
    }

    /**
     * Boilerplate for junit.
     * This is its own test suite
     */
    public static TestSuite suite() {
        return new TestSuite(TestInfModel.class);
    }

    /**
     * Check interface extensions which had an earlier bug with null handling
     */
    public void testListWithPosits() {
        String NS = PrintUtil.egNS;
        Model data = ModelFactory.createDefaultModel();
        Resource c1 = data.createResource(NS + "C1");
        Resource c2 = data.createResource(NS + "C2");
        Resource c3 = data.createResource(NS + "C3");
        data.add(c2, RDFS.subClassOf, c3);
        Model premise = ModelFactory.createDefaultModel();
        premise.add(c1, RDFS.subClassOf, c2);
        InfModel im = ModelFactory.createInfModel(ReasonerRegistry.getRDFSReasoner(), data);
        TestUtil.assertIteratorValues(this, im.listStatements(c1, RDFS.subClassOf, null, premise), new Object[] { data.createStatement(c1, RDFS.subClassOf, c2), data.createStatement(c1, RDFS.subClassOf, c3), data.createStatement(c1, RDFS.subClassOf, c1) });
        OntModel om = ModelFactory.createOntologyModel(OntModelSpec.RDFS_MEM_RDFS_INF, data);
        TestUtil.assertIteratorValues(this, om.listStatements(c1, RDFS.subClassOf, null, premise), new Object[] { data.createStatement(c1, RDFS.subClassOf, c2), data.createStatement(c1, RDFS.subClassOf, c3), data.createStatement(c1, RDFS.subClassOf, c1) });
    }
}

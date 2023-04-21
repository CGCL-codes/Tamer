package org.herakles.ml.selection.trainingData;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;

public class OntologyManagerTest {

    private static OntologyManager manager;

    private static IRI iri;

    @Test
    public void getOnotologyTest() {
        OWLOntology ontology = manager.getOntology(iri);
        assertNotNull(ontology);
        assertEquals(getName(ontology.getOntologyID().getOntologyIRI()), getName(iri));
    }

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        manager = new OntologyManager();
        iri = IRI.create("http://www.co-ode.org/ontologies/pizza/2007/02/12/pizza.owl");
    }

    @After
    public void tearDown() throws Exception {
    }

    private static String getName(IRI iri) {
        String str = iri.toString();
        String[] strs = str.split("/");
        return strs[strs.length - 1];
    }
}

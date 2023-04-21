package de.fzi.harmonia.basematcher;

import java.io.File;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

/**
 * Common superclass for all tests that use external test ontologies.
 * This class provides the {@link OWLOntologyManager},
 * the {@link OWLDataFactory}, and two {@link OWLOntology}s. Two
 * test ontologies are loaded. Also this class provides a method to
 * obtain entity references from the two test ontologies.
 * 
 * This class handles setup before class and tear down after class. Each
 * extending class should not use these methods, but rather the setup before
 * test and tear down after test methods.
 * 
 * @author bock
 *
 */
public abstract class OntologyBasedTest {

    protected static final String ONTO_FILE_1 = "src/test/resources/ontologies/testOnto1.owl";

    protected static final String ONTO_FILE_2 = "src/test/resources/ontologies/testOnto2.owl";

    protected static OWLOntologyManager manager;

    protected static OWLDataFactory factory;

    protected static OWLOntology ontology1, ontology2;

    /**
     * Creates a manager, a factory, and two ontologies.
     * 
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        File ontoDoc1 = new File(ONTO_FILE_1);
        File ontoDoc2 = new File(ONTO_FILE_2);
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        ontology1 = manager.loadOntologyFromOntologyDocument(ontoDoc1);
        ontology2 = manager.loadOntologyFromOntologyDocument(ontoDoc2);
        factory = manager.getOWLDataFactory();
    }

    /**
     * Sets all static objects null.
     * 
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        factory = null;
        ontology2 = null;
        ontology1 = null;
        manager = null;
    }

    /**
     * Retrieves an entity reference from an ontology. An exception is thrown if the 
     * ontology does not contain the requested entity.
     * 
     * @param iri {@link IRI} of the entity to be retrieved.
     * @param type Type of the entity. Can be one of <code>OWLClass.class</code>, <code>OWLObjectProperty.class</code>,
     *             <code>OWLDataProperty.class</code>, and <code>OWLNamedIndividual.class</code>
     * @return Reference to the {@link OWLEntity}.
     * @throws AlignmentException If the ontology does not contain the requested entity.  
     */
    protected OWLEntity getEntity(String iri, Class<? extends OWLEntity> type, OWLOntology ontology) throws BaseMatcherException {
        OWLEntity ent = null;
        if (type == OWLClass.class) {
            ent = factory.getOWLClass(IRI.create(iri));
            if (!ontology.getClassesInSignature().contains(ent)) throw new BaseMatcherException("Test entity not contained in test ontology: " + iri);
        } else if (type == OWLObjectProperty.class) {
            ent = factory.getOWLObjectProperty(IRI.create(iri));
            if (!ontology.getObjectPropertiesInSignature().contains(ent)) throw new BaseMatcherException("Test entity not contained in test ontology: " + iri);
        } else if (type == OWLDataProperty.class) {
            ent = factory.getOWLDataProperty(IRI.create(iri));
            if (!ontology.getDataPropertiesInSignature().contains(ent)) throw new BaseMatcherException("Test entity not contained in test ontology: " + iri);
        } else if (type == OWLNamedIndividual.class) {
            ent = factory.getOWLNamedIndividual(IRI.create(iri));
            if (!ontology.getIndividualsInSignature().contains(ent)) throw new BaseMatcherException("Test entity not contained in test ontology: " + iri);
        } else throw new IllegalArgumentException("Entity type not supported. Must be ont of " + "OWLClass, OWLObjectProperty, OWLDataProperty, OWLNamedIndividual.");
        return ent;
    }
}

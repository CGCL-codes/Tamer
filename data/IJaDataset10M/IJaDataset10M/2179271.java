package de.fzi.harmonia.commons.alignmentevaluator;

import static org.junit.Assert.assertEquals;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import de.fzi.harmonia.commons.alignmentevaluators.AbstractAlignmentEvaluator;
import de.fzi.harmonia.commons.alignmentevaluators.ontologyevaluators.CrissCrossAlignmentEvaluator;
import de.fzi.harmonia.commons.basematcher.BaseMatcherTest;
import de.fzi.kadmos.api.Alignment;
import de.fzi.kadmos.api.impl.SimpleAlignmentFactory;
import de.fzi.kadmos.api.impl.SimpleCorrespondenceFactory;

/**
 * Test for the {@link AbstractAlignmentEvaluator}.
 * 
 * @author Matthias Stumpp (stumpp@fzi.de)
 *
 */
public class CrissCrossAlignmentEvaluatorTest extends BaseMatcherTest {

    private static final Log logger = LogFactory.getLog(CrissCrossAlignmentEvaluatorTest.class);

    private static OWLOntologyManager manager = null;

    private static OWLDataFactory dataFactory = null;

    private static OWLOntology ontology1;

    private static OWLOntology ontology2;

    private static int ontCounter = 0;

    private static final double EPSILON = 0.0001d;

    @Before
    public void setUp() throws Exception {
        manager = OWLManager.createOWLOntologyManager();
        dataFactory = manager.getOWLDataFactory();
        ontology1 = getOntology();
        ontology2 = getOntology();
        alignment = SimpleAlignmentFactory.getInstance().createAlignment(ontology1, ontology2);
    }

    @After
    public void tearDown() throws Exception {
        baseMatcher = null;
    }

    /**
     * Test method for {@link de.fzi.harmonia.commons.alignmentevaluators.ontologyevaluators.CrissCrossAlignmentEvaluator#CrossingCorrespondenceEvaluator(Properties, String, Alignment)}.
	 * conditions:
	 * - 4 correspondences
	 * - 0 correspondences cross each other
     * Expected: 1.d
     */
    @Test
    public final void testNoneOfFourInvolvedInCrossing() throws Exception {
        final double expected = 1.d;
        final Properties properties = new Properties();
        final String id = "id1";
        OWLClass ont1_clazz1 = dataFactory.getOWLClass(IRI.create(ontology1.getOntologyID().getOntologyIRI() + "#clazz1"));
        OWLClass ont2_clazz1 = dataFactory.getOWLClass(IRI.create(ontology2.getOntologyID().getOntologyIRI() + "#clazz1"));
        OWLClass ont1_clazz2 = dataFactory.getOWLClass(IRI.create(ontology1.getOntologyID().getOntologyIRI() + "#clazz2"));
        OWLClass ont2_clazz2 = dataFactory.getOWLClass(IRI.create(ontology2.getOntologyID().getOntologyIRI() + "#clazz2"));
        OWLClass ont1_clazz3 = dataFactory.getOWLClass(IRI.create(ontology1.getOntologyID().getOntologyIRI() + "#clazz3"));
        OWLClass ont2_clazz3 = dataFactory.getOWLClass(IRI.create(ontology2.getOntologyID().getOntologyIRI() + "#clazz3"));
        OWLClass ont1_clazz4 = dataFactory.getOWLClass(IRI.create(ontology1.getOntologyID().getOntologyIRI() + "#clazz4"));
        OWLClass ont2_clazz4 = dataFactory.getOWLClass(IRI.create(ontology2.getOntologyID().getOntologyIRI() + "#clazz4"));
        alignment.addCorrespondence(SimpleCorrespondenceFactory.getInstance().createCorrespondence(ont1_clazz1, ont2_clazz1));
        alignment.addCorrespondence(SimpleCorrespondenceFactory.getInstance().createCorrespondence(ont1_clazz2, ont2_clazz2));
        alignment.addCorrespondence(SimpleCorrespondenceFactory.getInstance().createCorrespondence(ont1_clazz3, ont2_clazz3));
        alignment.addCorrespondence(SimpleCorrespondenceFactory.getInstance().createCorrespondence(ont1_clazz4, ont2_clazz4));
        AbstractAlignmentEvaluator alignmentEvaluator = new CrissCrossAlignmentEvaluator(properties, id, alignment);
        final double actual = alignmentEvaluator.getEvaluation(alignment);
        assertEquals(expected, actual, EPSILON);
    }

    /**
     * Test method for {@link de.fzi.harmonia.commons.alignmentevaluators.ontologyevaluators.CrissCrossAlignmentEvaluator#CrossingCorrespondenceEvaluator(Properties, String, Alignment)}.
	 * conditions:
	 * - 4 correspondences
	 * - 2 of 4 correspondences cross each other
     * Expected: 0.5
     */
    @Test
    public final void testTwoOfFourCorresCrossing() throws Exception {
        final double expected = 0.5;
        final Properties properties = new Properties();
        final String id = "id1";
        OWLClass ont1_clazz1 = dataFactory.getOWLClass(IRI.create(ontology1.getOntologyID().getOntologyIRI() + "#clazz1"));
        OWLClass ont2_clazz1 = dataFactory.getOWLClass(IRI.create(ontology2.getOntologyID().getOntologyIRI() + "#clazz1"));
        OWLClass ont1_clazz2 = dataFactory.getOWLClass(IRI.create(ontology1.getOntologyID().getOntologyIRI() + "#clazz2"));
        OWLClass ont2_clazz2 = dataFactory.getOWLClass(IRI.create(ontology2.getOntologyID().getOntologyIRI() + "#clazz2"));
        OWLClass ont1_clazz3 = dataFactory.getOWLClass(IRI.create(ontology1.getOntologyID().getOntologyIRI() + "#clazz3"));
        OWLClass ont2_clazz3 = dataFactory.getOWLClass(IRI.create(ontology2.getOntologyID().getOntologyIRI() + "#clazz3"));
        OWLClass ont1_clazz4 = dataFactory.getOWLClass(IRI.create(ontology1.getOntologyID().getOntologyIRI() + "#clazz4"));
        OWLClass ont2_clazz4 = dataFactory.getOWLClass(IRI.create(ontology2.getOntologyID().getOntologyIRI() + "#clazz4"));
        alignment.addCorrespondence(SimpleCorrespondenceFactory.getInstance().createCorrespondence(ont1_clazz1, ont2_clazz1));
        alignment.addCorrespondence(SimpleCorrespondenceFactory.getInstance().createCorrespondence(ont1_clazz2, ont2_clazz3));
        alignment.addCorrespondence(SimpleCorrespondenceFactory.getInstance().createCorrespondence(ont1_clazz3, ont2_clazz2));
        alignment.addCorrespondence(SimpleCorrespondenceFactory.getInstance().createCorrespondence(ont1_clazz4, ont2_clazz4));
        AbstractAlignmentEvaluator alignmentEvaluator = new CrissCrossAlignmentEvaluator(properties, id, alignment);
        final double actual = alignmentEvaluator.getEvaluation(alignment);
        assertEquals(expected, actual, EPSILON);
    }

    /**
     * Test method for {@link de.fzi.harmonia.commons.alignmentevaluators.ontologyevaluators.CrissCrossAlignmentEvaluator#CrossingCorrespondenceEvaluator(Properties, String, Alignment)}.
	 * conditions:
	 * - 4 correspondences
	 * - 4 correspondences cross each other
     * Expected: 0.d
     */
    @Test
    public final void testFourOfFourCorresCrossing() throws Exception {
        final double expected = 0.d;
        final Properties properties = new Properties();
        final String id = "id1";
        OWLClass ont1_clazz1 = dataFactory.getOWLClass(IRI.create(ontology1.getOntologyID().getOntologyIRI() + "#clazz1"));
        OWLClass ont2_clazz1 = dataFactory.getOWLClass(IRI.create(ontology2.getOntologyID().getOntologyIRI() + "#clazz1"));
        OWLClass ont1_clazz2 = dataFactory.getOWLClass(IRI.create(ontology1.getOntologyID().getOntologyIRI() + "#clazz2"));
        OWLClass ont2_clazz2 = dataFactory.getOWLClass(IRI.create(ontology2.getOntologyID().getOntologyIRI() + "#clazz2"));
        OWLClass ont1_clazz3 = dataFactory.getOWLClass(IRI.create(ontology1.getOntologyID().getOntologyIRI() + "#clazz3"));
        OWLClass ont2_clazz3 = dataFactory.getOWLClass(IRI.create(ontology2.getOntologyID().getOntologyIRI() + "#clazz3"));
        OWLClass ont1_clazz4 = dataFactory.getOWLClass(IRI.create(ontology1.getOntologyID().getOntologyIRI() + "#clazz4"));
        OWLClass ont2_clazz4 = dataFactory.getOWLClass(IRI.create(ontology2.getOntologyID().getOntologyIRI() + "#clazz4"));
        alignment.addCorrespondence(SimpleCorrespondenceFactory.getInstance().createCorrespondence(ont1_clazz1, ont2_clazz2));
        alignment.addCorrespondence(SimpleCorrespondenceFactory.getInstance().createCorrespondence(ont1_clazz2, ont2_clazz1));
        alignment.addCorrespondence(SimpleCorrespondenceFactory.getInstance().createCorrespondence(ont1_clazz3, ont2_clazz4));
        alignment.addCorrespondence(SimpleCorrespondenceFactory.getInstance().createCorrespondence(ont1_clazz4, ont2_clazz3));
        AbstractAlignmentEvaluator alignmentEvaluator = new CrissCrossAlignmentEvaluator(properties, id, alignment);
        final double actual = alignmentEvaluator.getEvaluation(alignment);
        assertEquals(expected, actual, EPSILON);
    }

    /**
     * Create some ontology with classes, properties, annotations to be used for testing
     */
    private static OWLOntology getOntology() {
        OWLOntology ont = getFreshOntology();
        String iriBase = ont.getOntologyID().getOntologyIRI().toString();
        OWLClass clazz1 = dataFactory.getOWLClass(IRI.create(iriBase + "#clazz1"));
        OWLClass clazz2 = dataFactory.getOWLClass(IRI.create(iriBase + "#clazz2"));
        OWLClass clazz3 = dataFactory.getOWLClass(IRI.create(iriBase + "#clazz3"));
        OWLClass clazz4 = dataFactory.getOWLClass(IRI.create(iriBase + "#clazz4"));
        OWLClass clazz5 = dataFactory.getOWLClass(IRI.create(iriBase + "#clazz5"));
        OWLNamedIndividual clazz1_ind1 = dataFactory.getOWLNamedIndividual(IRI.create(iriBase + "#clazz1_ind1"));
        OWLNamedIndividual clazz2_ind1 = dataFactory.getOWLNamedIndividual(IRI.create(iriBase + "#clazz2_ind1"));
        OWLNamedIndividual clazz3_ind1 = dataFactory.getOWLNamedIndividual(IRI.create(iriBase + "#clazz3_ind1"));
        OWLNamedIndividual clazz4_ind1 = dataFactory.getOWLNamedIndividual(IRI.create(iriBase + "#clazz4_ind1"));
        OWLNamedIndividual clazz5_ind1 = dataFactory.getOWLNamedIndividual(IRI.create(iriBase + "#clazz5_ind1"));
        manager.addAxiom(ont, dataFactory.getOWLSubClassOfAxiom(clazz2, clazz1));
        manager.addAxiom(ont, dataFactory.getOWLSubClassOfAxiom(clazz3, clazz2));
        manager.addAxiom(ont, dataFactory.getOWLSubClassOfAxiom(clazz4, clazz3));
        manager.addAxiom(ont, dataFactory.getOWLSubClassOfAxiom(clazz5, clazz4));
        manager.addAxiom(ont, dataFactory.getOWLAnnotationAssertionAxiom(clazz1.getIRI(), dataFactory.getOWLAnnotation(dataFactory.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_COMMENT.getIRI()), dataFactory.getOWLLiteral("clazz1_anno1"))));
        manager.addAxiom(ont, dataFactory.getOWLAnnotationAssertionAxiom(clazz2.getIRI(), dataFactory.getOWLAnnotation(dataFactory.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_COMMENT.getIRI()), dataFactory.getOWLLiteral("clazz2_anno1"))));
        manager.addAxiom(ont, dataFactory.getOWLAnnotationAssertionAxiom(clazz3.getIRI(), dataFactory.getOWLAnnotation(dataFactory.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_COMMENT.getIRI()), dataFactory.getOWLLiteral("clazz3_anno1"))));
        manager.addAxiom(ont, dataFactory.getOWLAnnotationAssertionAxiom(clazz4.getIRI(), dataFactory.getOWLAnnotation(dataFactory.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_COMMENT.getIRI()), dataFactory.getOWLLiteral("clazz4_anno1"))));
        manager.addAxiom(ont, dataFactory.getOWLAnnotationAssertionAxiom(clazz5.getIRI(), dataFactory.getOWLAnnotation(dataFactory.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_COMMENT.getIRI()), dataFactory.getOWLLiteral("clazz5_anno1"))));
        manager.addAxiom(ont, dataFactory.getOWLClassAssertionAxiom(clazz1, clazz1_ind1));
        manager.addAxiom(ont, dataFactory.getOWLClassAssertionAxiom(clazz2, clazz2_ind1));
        manager.addAxiom(ont, dataFactory.getOWLClassAssertionAxiom(clazz3, clazz3_ind1));
        manager.addAxiom(ont, dataFactory.getOWLClassAssertionAxiom(clazz4, clazz4_ind1));
        manager.addAxiom(ont, dataFactory.getOWLClassAssertionAxiom(clazz5, clazz5_ind1));
        manager.addAxiom(ont, dataFactory.getOWLAnnotationAssertionAxiom(clazz1_ind1.getIRI(), dataFactory.getOWLAnnotation(dataFactory.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_COMMENT.getIRI()), dataFactory.getOWLLiteral("clazz1_ind1_anno1"))));
        manager.addAxiom(ont, dataFactory.getOWLAnnotationAssertionAxiom(clazz2_ind1.getIRI(), dataFactory.getOWLAnnotation(dataFactory.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_COMMENT.getIRI()), dataFactory.getOWLLiteral("clazz2_ind1_anno1"))));
        manager.addAxiom(ont, dataFactory.getOWLAnnotationAssertionAxiom(clazz3_ind1.getIRI(), dataFactory.getOWLAnnotation(dataFactory.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_COMMENT.getIRI()), dataFactory.getOWLLiteral("clazz3_ind1_anno1"))));
        manager.addAxiom(ont, dataFactory.getOWLAnnotationAssertionAxiom(clazz4_ind1.getIRI(), dataFactory.getOWLAnnotation(dataFactory.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_COMMENT.getIRI()), dataFactory.getOWLLiteral("clazz4_ind1_anno1"))));
        manager.addAxiom(ont, dataFactory.getOWLAnnotationAssertionAxiom(clazz5_ind1.getIRI(), dataFactory.getOWLAnnotation(dataFactory.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_COMMENT.getIRI()), dataFactory.getOWLLiteral("clazz5_ind1_anno1"))));
        OWLObjectProperty obj_prop1 = dataFactory.getOWLObjectProperty(IRI.create(iriBase + "#obj_prop1"));
        OWLObjectProperty obj_prop2 = dataFactory.getOWLObjectProperty(IRI.create(iriBase + "#obj_prop2"));
        OWLObjectProperty obj_prop3 = dataFactory.getOWLObjectProperty(IRI.create(iriBase + "#obj_prop3"));
        OWLObjectProperty obj_prop4 = dataFactory.getOWLObjectProperty(IRI.create(iriBase + "#obj_prop4"));
        OWLObjectProperty obj_prop5 = dataFactory.getOWLObjectProperty(IRI.create(iriBase + "#obj_prop5"));
        manager.addAxiom(ont, dataFactory.getOWLSubObjectPropertyOfAxiom(obj_prop2, obj_prop1));
        manager.addAxiom(ont, dataFactory.getOWLSubObjectPropertyOfAxiom(obj_prop3, obj_prop2));
        manager.addAxiom(ont, dataFactory.getOWLSubObjectPropertyOfAxiom(obj_prop4, obj_prop3));
        manager.addAxiom(ont, dataFactory.getOWLSubObjectPropertyOfAxiom(obj_prop5, obj_prop4));
        manager.addAxiom(ont, dataFactory.getOWLAnnotationAssertionAxiom(obj_prop1.getIRI(), dataFactory.getOWLAnnotation(dataFactory.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_COMMENT.getIRI()), dataFactory.getOWLLiteral("obj_prop1_anno1"))));
        manager.addAxiom(ont, dataFactory.getOWLAnnotationAssertionAxiom(obj_prop2.getIRI(), dataFactory.getOWLAnnotation(dataFactory.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_COMMENT.getIRI()), dataFactory.getOWLLiteral("obj_prop2_anno1"))));
        manager.addAxiom(ont, dataFactory.getOWLAnnotationAssertionAxiom(obj_prop3.getIRI(), dataFactory.getOWLAnnotation(dataFactory.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_COMMENT.getIRI()), dataFactory.getOWLLiteral("obj_prop3_anno1"))));
        manager.addAxiom(ont, dataFactory.getOWLAnnotationAssertionAxiom(obj_prop4.getIRI(), dataFactory.getOWLAnnotation(dataFactory.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_COMMENT.getIRI()), dataFactory.getOWLLiteral("obj_prop4_anno1"))));
        manager.addAxiom(ont, dataFactory.getOWLAnnotationAssertionAxiom(obj_prop5.getIRI(), dataFactory.getOWLAnnotation(dataFactory.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_COMMENT.getIRI()), dataFactory.getOWLLiteral("obj_prop5_anno1"))));
        manager.addAxiom(ont, dataFactory.getOWLObjectPropertyAssertionAxiom(obj_prop1, clazz1_ind1, clazz2_ind1));
        manager.addAxiom(ont, dataFactory.getOWLObjectPropertyAssertionAxiom(obj_prop2, clazz2_ind1, clazz3_ind1));
        manager.addAxiom(ont, dataFactory.getOWLObjectPropertyAssertionAxiom(obj_prop3, clazz3_ind1, clazz4_ind1));
        manager.addAxiom(ont, dataFactory.getOWLObjectPropertyAssertionAxiom(obj_prop4, clazz4_ind1, clazz5_ind1));
        OWLDataProperty data_prop1 = dataFactory.getOWLDataProperty(IRI.create(iriBase + "#data_prop1"));
        OWLDataProperty data_prop2 = dataFactory.getOWLDataProperty(IRI.create(iriBase + "#data_prop2"));
        OWLDataProperty data_prop3 = dataFactory.getOWLDataProperty(IRI.create(iriBase + "#data_prop3"));
        OWLDataProperty data_prop4 = dataFactory.getOWLDataProperty(IRI.create(iriBase + "#data_prop4"));
        OWLDataProperty data_prop5 = dataFactory.getOWLDataProperty(IRI.create(iriBase + "#data_prop5"));
        manager.addAxiom(ont, dataFactory.getOWLSubDataPropertyOfAxiom(data_prop2, data_prop1));
        manager.addAxiom(ont, dataFactory.getOWLSubDataPropertyOfAxiom(data_prop3, data_prop2));
        manager.addAxiom(ont, dataFactory.getOWLSubDataPropertyOfAxiom(data_prop4, data_prop3));
        manager.addAxiom(ont, dataFactory.getOWLSubDataPropertyOfAxiom(data_prop5, data_prop4));
        manager.addAxiom(ont, dataFactory.getOWLAnnotationAssertionAxiom(data_prop1.getIRI(), dataFactory.getOWLAnnotation(dataFactory.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_COMMENT.getIRI()), dataFactory.getOWLLiteral("data_prop1_anno1"))));
        manager.addAxiom(ont, dataFactory.getOWLAnnotationAssertionAxiom(data_prop2.getIRI(), dataFactory.getOWLAnnotation(dataFactory.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_COMMENT.getIRI()), dataFactory.getOWLLiteral("data_prop2_anno1"))));
        manager.addAxiom(ont, dataFactory.getOWLAnnotationAssertionAxiom(data_prop3.getIRI(), dataFactory.getOWLAnnotation(dataFactory.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_COMMENT.getIRI()), dataFactory.getOWLLiteral("data_prop3_anno1"))));
        manager.addAxiom(ont, dataFactory.getOWLAnnotationAssertionAxiom(data_prop4.getIRI(), dataFactory.getOWLAnnotation(dataFactory.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_COMMENT.getIRI()), dataFactory.getOWLLiteral("data_prop4_anno1"))));
        manager.addAxiom(ont, dataFactory.getOWLAnnotationAssertionAxiom(data_prop5.getIRI(), dataFactory.getOWLAnnotation(dataFactory.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_COMMENT.getIRI()), dataFactory.getOWLLiteral("data_prop5_anno1"))));
        manager.addAxiom(ont, dataFactory.getOWLDataPropertyAssertionAxiom(data_prop1, clazz1_ind1, 1.d));
        manager.addAxiom(ont, dataFactory.getOWLDataPropertyAssertionAxiom(data_prop2, clazz2_ind1, 2.d));
        manager.addAxiom(ont, dataFactory.getOWLDataPropertyAssertionAxiom(data_prop3, clazz3_ind1, 3.d));
        manager.addAxiom(ont, dataFactory.getOWLDataPropertyAssertionAxiom(data_prop4, clazz4_ind1, 4.d));
        manager.addAxiom(ont, dataFactory.getOWLDataPropertyAssertionAxiom(data_prop5, clazz5_ind1, 5.d));
        manager.addAxiom(ont, dataFactory.getOWLObjectPropertyDomainAxiom(obj_prop1, clazz1));
        manager.addAxiom(ont, dataFactory.getOWLObjectPropertyDomainAxiom(obj_prop2, clazz1));
        manager.addAxiom(ont, dataFactory.getOWLObjectPropertyDomainAxiom(obj_prop2, clazz2));
        manager.addAxiom(ont, dataFactory.getOWLObjectPropertyRangeAxiom(obj_prop4, clazz5));
        manager.addAxiom(ont, dataFactory.getOWLObjectPropertyRangeAxiom(obj_prop5, clazz5));
        manager.addAxiom(ont, dataFactory.getOWLObjectPropertyRangeAxiom(obj_prop5, clazz1));
        manager.addAxiom(ont, dataFactory.getOWLDataPropertyDomainAxiom(data_prop1, clazz1));
        manager.addAxiom(ont, dataFactory.getOWLDataPropertyDomainAxiom(data_prop2, clazz1));
        manager.addAxiom(ont, dataFactory.getOWLDataPropertyDomainAxiom(data_prop2, clazz2));
        return ont;
    }

    /**
     * Create an empty ontology
     */
    private static OWLOntology getFreshOntology() {
        OWLOntology ont = null;
        IRI iriBase = IRI.create("http://www.test.org/" + ontCounter + ".owl");
        ontCounter++;
        try {
            ont = manager.createOntology(iriBase);
        } catch (OWLOntologyCreationException e) {
            final String msg = "could not create fresh ontology";
            logger.debug(msg);
        }
        return ont;
    }
}

package de.fzi.harmonia.commons.alignmentevaluator;

import static org.junit.Assert.*;
import java.util.Properties;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import de.fzi.harmonia.commons.alignmentevaluators.AlignmentEvaluator;
import de.fzi.harmonia.commons.alignmentevaluators.ontologyevaluators.AlignmentCoherenceEvaluator;
import de.fzi.kadmos.api.Alignment;
import de.fzi.kadmos.api.impl.SimpleAlignmentFactory;
import de.fzi.kadmos.api.impl.SimpleCorrespondenceFactory;

/**
 * Test for the {@link AlignmentCoherenceEvaluator}.
 * 
 * @author Juergen Bock (bock@fzi.de)
 * @author Matthias Stumpp (stumpp@fzi.de)
 *
 */
public class AlignmentCoherenceEvaluatorTest {

    private static final String ontology1Resource = "/de/fzi/harmonia/commons/alignmentevaluator/AlignmentUnsatisfiableClassesEvaluator1.owl";

    private static final String ontology2Resource = "/de/fzi/harmonia/commons/alignmentevaluator/AlignmentUnsatisfiableClassesEvaluator2.owl";

    private static final String ontology3Resource = "/de/fzi/harmonia/commons/alignmentevaluator/AlignmentUnsatisfiableClassesEvaluator3.owl";

    private static final String ontology4Resource = "/de/fzi/harmonia/commons/alignmentevaluator/AlignmentUnsatisfiableClassesEvaluator4.owl";

    private static final String ontology5Resource = "/de/fzi/harmonia/commons/alignmentevaluator/AlignmentUnsatisfiableClassesEvaluator5.owl";

    private static final String ontology6Resource = "/de/fzi/harmonia/commons/alignmentevaluator/AlignmentUnsatisfiableClassesEvaluator6.owl";

    private static final String ontology7Resource = "/de/fzi/harmonia/commons/alignmentevaluator/AlignmentUnsatisfiableClassesEvaluator7.owl";

    private static final String ontology8Resource = "/de/fzi/harmonia/commons/alignmentevaluator/AlignmentUnsatisfiableClassesEvaluator8.owl";

    private static final IRI class1Airi = IRI.create("http://example.org/onto1.owl#A");

    private static final IRI class1Biri = IRI.create("http://example.org/onto1.owl#B");

    private static final IRI class2Airi = IRI.create("http://example.org/onto2.owl#A");

    private static final IRI class2Biri = IRI.create("http://example.org/onto2.owl#B");

    private static final IRI class3Airi = IRI.create("http://example.org/onto3.owl#A");

    private static final IRI class3Biri = IRI.create("http://example.org/onto3.owl#B");

    private static final IRI class4Airi = IRI.create("http://example.org/onto4.owl#A");

    private static final IRI class4Biri = IRI.create("http://example.org/onto4.owl#B");

    private static final IRI class5Airi = IRI.create("http://example.org/onto5.owl#A");

    private static final IRI class5Biri = IRI.create("http://example.org/onto5.owl#B");

    private static final IRI class6Airi = IRI.create("http://example.org/onto6.owl#A");

    private static final IRI class6Biri = IRI.create("http://example.org/onto6.owl#B");

    private static final IRI class7Airi = IRI.create("http://example.org/onto7.owl#A");

    private static final IRI class7Biri = IRI.create("http://example.org/onto7.owl#B");

    private static final IRI class8Airi = IRI.create("http://example.org/onto8.owl#A");

    private static final IRI class8Biri = IRI.create("http://example.org/onto8.owl#B");

    private static OWLOntologyManager manager;

    private OWLOntology onto1;

    private OWLOntology onto2;

    private static OWLClass class1A;

    private static OWLClass class1B;

    private static OWLClass class2A;

    private static OWLClass class2B;

    private static OWLClass class3A;

    private static OWLClass class3B;

    private static OWLClass class4A;

    private static OWLClass class4B;

    private static OWLClass class5A;

    private static OWLClass class5B;

    private static OWLClass class6A;

    private static OWLClass class6B;

    private static OWLClass class7A;

    private static OWLClass class7B;

    private static OWLClass class8A;

    private static OWLClass class8B;

    private static final double EPSILON = 0.00001;

    /**
     * Creates the {@link OWLOntologManager}.
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        manager = OWLManager.createOWLOntologyManager();
        class1A = manager.getOWLDataFactory().getOWLClass(class1Airi);
        class1B = manager.getOWLDataFactory().getOWLClass(class1Biri);
        class2A = manager.getOWLDataFactory().getOWLClass(class2Airi);
        class2B = manager.getOWLDataFactory().getOWLClass(class2Biri);
        class3A = manager.getOWLDataFactory().getOWLClass(class3Airi);
        class3B = manager.getOWLDataFactory().getOWLClass(class3Biri);
        class4A = manager.getOWLDataFactory().getOWLClass(class4Airi);
        class4B = manager.getOWLDataFactory().getOWLClass(class4Biri);
        class5A = manager.getOWLDataFactory().getOWLClass(class5Airi);
        class5B = manager.getOWLDataFactory().getOWLClass(class5Biri);
        class6A = manager.getOWLDataFactory().getOWLClass(class6Airi);
        class6B = manager.getOWLDataFactory().getOWLClass(class6Biri);
        class7A = manager.getOWLDataFactory().getOWLClass(class7Airi);
        class7B = manager.getOWLDataFactory().getOWLClass(class7Biri);
        class8A = manager.getOWLDataFactory().getOWLClass(class8Airi);
        class8B = manager.getOWLDataFactory().getOWLClass(class8Biri);
    }

    /**
     * Removes the loaded ontologies from the manager.
     */
    @After
    public void tearDown() throws Exception {
        manager.removeOntology(onto1);
        manager.removeOntology(onto2);
        onto1 = null;
        onto2 = null;
    }

    /**
     * Test method for {@link de.fzi.harmonia.commons.alignmentevaluators.AbstractAlignmentEvaluator#getEvaluation(de.fzi.kadmos.api.Evaluable)}.
     * Both ontologies no unsatisfiable classes,
     * merged ontology no unsatisfiable classes,
     * alignment does not cause unsatisfiable classes.
     * Expected: 1.0
     * TODO check why this test fails when running the complete maven build at the command line
     */
    @Test
    @Ignore
    public final void testOntologiesNoUnsatisfiableClassesAlignmentNoUnsatisfiableClasses() throws Exception {
        final double expected = 1.d;
        Properties params = new Properties();
        onto1 = manager.loadOntologyFromOntologyDocument(AlignmentCoherenceEvaluatorTest.class.getResourceAsStream(ontology1Resource));
        onto2 = manager.loadOntologyFromOntologyDocument(AlignmentCoherenceEvaluatorTest.class.getResourceAsStream(ontology2Resource));
        Alignment alignment = SimpleAlignmentFactory.getInstance().createAlignment(onto1, onto2);
        alignment.addCorrespondence(SimpleCorrespondenceFactory.getInstance().createCorrespondence(class1A, class2A));
        alignment.addCorrespondence(SimpleCorrespondenceFactory.getInstance().createCorrespondence(class1B, class2B));
        AlignmentEvaluator evaluator = new AlignmentCoherenceEvaluator(params, "id", alignment);
        final double actual = evaluator.getEvaluation(alignment);
        assertEquals(expected, actual, EPSILON);
    }

    /**
     * Test method for {@link de.fzi.harmonia.commons.alignmentevaluators.AbstractAlignmentEvaluator#getEvaluation(de.fzi.kadmos.api.Evaluable)}.
     * Both ontologies no unsatisfiable classes,
     * merged ontology no unsatisfiable classes,
     * alignment empty.
     * Expected: 1.0
     * @throws OntologyMergerException 
     * @throws IllegalArgumentException 
     */
    @Test
    public final void testOntologiesNoUnsatisfiableClassesAlignmentEmpty() throws Exception {
        final double expected = 1.d;
        Properties params = new Properties();
        onto1 = manager.loadOntologyFromOntologyDocument(AlignmentCoherenceEvaluatorTest.class.getResourceAsStream(ontology1Resource));
        onto2 = manager.loadOntologyFromOntologyDocument(AlignmentCoherenceEvaluatorTest.class.getResourceAsStream(ontology2Resource));
        Alignment alignment = SimpleAlignmentFactory.getInstance().createAlignment(onto1, onto2);
        AlignmentEvaluator evaluator = new AlignmentCoherenceEvaluator(params, "id", alignment);
        final double actual = evaluator.getEvaluation(alignment);
        assertEquals(expected, actual, EPSILON);
    }

    /**
     * Test method for {@link de.fzi.harmonia.commons.alignmentevaluators.AbstractAlignmentEvaluator#getEvaluation(de.fzi.kadmos.api.Evaluable)}.
     * Both ontologies no unsatisfiable classes,
     * merged ontology no unsatisfiable classes,
     * alignment causes unsatisfiable classes.
     * Expected: 0.5
     */
    @Test
    public final void testMergedOntologyNoUnsatisfiableClassesAlignmentUnsatisfiableClasses() throws Exception {
        final double expected = 0.5;
        Properties params = new Properties();
        onto1 = manager.loadOntologyFromOntologyDocument(AlignmentCoherenceEvaluatorTest.class.getResourceAsStream(ontology3Resource));
        onto2 = manager.loadOntologyFromOntologyDocument(AlignmentCoherenceEvaluatorTest.class.getResourceAsStream(ontology4Resource));
        Alignment alignment = SimpleAlignmentFactory.getInstance().createAlignment(onto1, onto2);
        alignment.addCorrespondence(SimpleCorrespondenceFactory.getInstance().createCorrespondence(class3A, class4A));
        alignment.addCorrespondence(SimpleCorrespondenceFactory.getInstance().createCorrespondence(class3B, class4B));
        AlignmentEvaluator evaluator = new AlignmentCoherenceEvaluator(params, "id", alignment);
        final double actual = evaluator.getEvaluation(alignment);
        System.out.println(actual);
        assertEquals(expected, actual, EPSILON);
    }

    /**
     * Test method for {@link de.fzi.harmonia.commons.alignmentevaluators.AbstractAlignmentEvaluator#getEvaluation(de.fzi.kadmos.api.Evaluable)}.
     * Both ontologies unsatisfiable classes,
     * merged ontology unsatisfiable classes,
     * alignment causes no unsatisfiable classes.
     * Expected: 1.0
     */
    @Test
    public final void testMergedOntologyUnsatisfiableClassesAlignmentNoUnsatisfiableClasses() throws Exception {
        final double expected = 1.d;
        Properties params = new Properties();
        onto1 = manager.loadOntologyFromOntologyDocument(AlignmentCoherenceEvaluatorTest.class.getResourceAsStream(ontology5Resource));
        onto2 = manager.loadOntologyFromOntologyDocument(AlignmentCoherenceEvaluatorTest.class.getResourceAsStream(ontology6Resource));
        Alignment alignment = SimpleAlignmentFactory.getInstance().createAlignment(onto1, onto2);
        alignment.addCorrespondence(SimpleCorrespondenceFactory.getInstance().createCorrespondence(class5B, class6A));
        alignment.addCorrespondence(SimpleCorrespondenceFactory.getInstance().createCorrespondence(class5A, class6B));
        AlignmentEvaluator evaluator = new AlignmentCoherenceEvaluator(params, "id", alignment);
        final double actual = evaluator.getEvaluation(alignment);
        assertEquals(expected, actual, EPSILON);
    }

    /**
     * Test method for {@link de.fzi.harmonia.commons.alignmentevaluators.AbstractAlignmentEvaluator#getEvaluation(de.fzi.kadmos.api.Evaluable)}.
     * Both ontologies unsatisfiable classes,
     * merged ontology unsatisfiable classes,
     * alignment causes unsatisfiable classes.
     * Expected: 0.0
     */
    @Test
    public final void testMergedOntologyUnsatisfiableClassesAlignmentUnsatisfiableClasses() throws Exception {
        final double expected = 0.d;
        Properties params = new Properties();
        onto1 = manager.loadOntologyFromOntologyDocument(AlignmentCoherenceEvaluatorTest.class.getResourceAsStream(ontology7Resource));
        onto2 = manager.loadOntologyFromOntologyDocument(AlignmentCoherenceEvaluatorTest.class.getResourceAsStream(ontology8Resource));
        Alignment alignment = SimpleAlignmentFactory.getInstance().createAlignment(onto1, onto2);
        alignment.addCorrespondence(SimpleCorrespondenceFactory.getInstance().createCorrespondence(class7A, class8A));
        alignment.addCorrespondence(SimpleCorrespondenceFactory.getInstance().createCorrespondence(class7B, class8B));
        AlignmentEvaluator evaluator = new AlignmentCoherenceEvaluator(params, "id", alignment);
        final double actual = evaluator.getEvaluation(alignment);
        assertEquals(expected, actual, EPSILON);
    }
}

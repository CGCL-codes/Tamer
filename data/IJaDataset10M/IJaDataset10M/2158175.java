package de.fzi.harmonia.basematcher;

import static org.junit.Assert.assertEquals;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import de.fzi.kadmos.api.Correspondence;
import de.fzi.kadmos.api.impl.SimpleAlignmentFactory;
import de.fzi.kadmos.api.impl.SimpleCorrespondenceFactory;

/**
 * Unit test for the {@link EntityCommentDistanceMatcher}.
 * 
 */
public class EntityCommentDistanceMatcherTest extends BaseMatcherTest {

    @Before
    public void setUp() throws Exception {
        baseMatcher = new EntityCommentDistanceMatcher();
        baseMatcher.init(SimpleAlignmentFactory.getInstance().createAlignment(ontology1, ontology2));
    }

    @After
    public void tearDown() throws Exception {
        baseMatcher = null;
    }

    /**
     * Total rdfs:comment mismatch of OWL classes. Expected: worst match (1.0)
     * 
     * @throws Exception
     */
    @Test
    public final void testClassesTotalMismatch() throws Exception {
        double distExpected = 1.d;
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#Numbers", OWLClass.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#Characters", OWLClass.class, ontology2);
        Correspondence<? extends OWLEntity> corr = SimpleCorrespondenceFactory.getInstance().createCorrespondence(ent1, ent2);
        double distActual = baseMatcher.getDistance(corr);
        assertEquals("Distance mismatch.", distExpected, distActual, 0.);
    }

    /**
     * Total rdfs:comment match of OWL classes. Expected: best match (0.0)
     * 
     * @throws Exception
     */
    @Test
    public final void testClassesTotalMatch() throws Exception {
        double distExpected = 0.d;
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#Numbers", OWLClass.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#Numbers", OWLClass.class, ontology2);
        Correspondence<? extends OWLEntity> corr = SimpleCorrespondenceFactory.getInstance().createCorrespondence(ent1, ent2);
        double distActual = baseMatcher.getDistance(corr);
        assertEquals("Distance mismatch.", distExpected, distActual, 0.);
    }

    /**
     * No rdfs:comment in both OWL classes. Expected:
     * {@link InfeasibleBaseMatcherException}
     * 
     * @throws Exception
     */
    @Test(expected = InfeasibleBaseMatcherException.class)
    public final void testClassesNoComment() throws Exception {
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#NoComment", OWLClass.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#NoComment", OWLClass.class, ontology2);
        Correspondence<? extends OWLEntity> corr = SimpleCorrespondenceFactory.getInstance().createCorrespondence(ent1, ent2);
        baseMatcher.getDistance(corr);
    }

    /**
     * No rdfs:comment in first OWL class. Expected:
     * {@link InfeasibleBaseMatcherException}
     * 
     * @throws Exception
     */
    @Test(expected = InfeasibleBaseMatcherException.class)
    public final void testClassesNoCommentEntity1() throws Exception {
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#NoComment", OWLClass.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#Numbers", OWLClass.class, ontology2);
        Correspondence<? extends OWLEntity> corr = SimpleCorrespondenceFactory.getInstance().createCorrespondence(ent1, ent2);
        baseMatcher.getDistance(corr);
    }

    /**
     * No rdfs:comment in second OWL class. Expected:
     * {@link InfeasibleBaseMatcherException}
     * 
     * @throws Exception
     */
    @Test(expected = InfeasibleBaseMatcherException.class)
    public final void testClassesNoCommentEntity2() throws Exception {
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#Numbers", OWLClass.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#NoComment", OWLClass.class, ontology2);
        Correspondence<? extends OWLEntity> corr = SimpleCorrespondenceFactory.getInstance().createCorrespondence(ent1, ent2);
        baseMatcher.getDistance(corr);
    }

    /**
     * Total rdfs:comment mismatch of object properites. Expected: worst match
     * (1.0)
     * 
     * @throws Exception
     */
    @Test
    public final void testObjectPropertiesTotalMismatch() throws Exception {
        double distExpected = 1.d;
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#hasComment", OWLObjectProperty.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#hasAntipathyAgainstComment", OWLObjectProperty.class, ontology2);
        Correspondence<? extends OWLEntity> corr = SimpleCorrespondenceFactory.getInstance().createCorrespondence(ent1, ent2);
        double distActual = baseMatcher.getDistance(corr);
        assertEquals("Distance mismatch.", distExpected, distActual, 0.);
    }

    /**
     * Total rdfs:comment match of object properties. Expected: best match (0.0)
     * 
     * @throws Exception
     */
    @Test
    public final void testObjectPropertiesTotalMatch() throws Exception {
        double distExpected = 0.d;
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#hasComment", OWLObjectProperty.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#hasComment", OWLObjectProperty.class, ontology2);
        Correspondence<? extends OWLEntity> corr = SimpleCorrespondenceFactory.getInstance().createCorrespondence(ent1, ent2);
        double distActual = baseMatcher.getDistance(corr);
        assertEquals("Distance mismatch.", distExpected, distActual, 0.);
    }

    /**
     * No rdfs:comment in both object properties. Expected:
     * {@link InfeasibleBaseMatcherException}
     * 
     * @throws Exception
     */
    @Test(expected = InfeasibleBaseMatcherException.class)
    public final void testObjectPropertiesNoComment() throws Exception {
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#hasNoComment", OWLObjectProperty.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#hasNoComment", OWLObjectProperty.class, ontology2);
        Correspondence<? extends OWLEntity> corr = SimpleCorrespondenceFactory.getInstance().createCorrespondence(ent1, ent2);
        baseMatcher.getDistance(corr);
    }

    /**
     * No rdfs:comment in first object property. Expected:
     * {@link InfeasibleBaseMatcherException}
     * 
     * @throws Exception
     */
    @Test(expected = InfeasibleBaseMatcherException.class)
    public final void testObjectPropertiesNoCommentEntity1() throws Exception {
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#hasNoComment", OWLObjectProperty.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#hasComment", OWLObjectProperty.class, ontology2);
        Correspondence<? extends OWLEntity> corr = SimpleCorrespondenceFactory.getInstance().createCorrespondence(ent1, ent2);
        baseMatcher.getDistance(corr);
    }

    /**
     * No rdfs:comment in second object property. Expected:
     * {@link InfeasibleBaseMatcherException}
     * 
     * @throws Exception
     */
    @Test(expected = InfeasibleBaseMatcherException.class)
    public final void testObjectPropertiesNoCommentEntity2() throws Exception {
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#hasComment", OWLObjectProperty.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#hasNoComment", OWLObjectProperty.class, ontology2);
        Correspondence<? extends OWLEntity> corr = SimpleCorrespondenceFactory.getInstance().createCorrespondence(ent1, ent2);
        baseMatcher.getDistance(corr);
    }

    /**
     * Total rdfs:comment mismatch of datatype properites. Expected: worst match
     * (1.0)
     * 
     * @throws Exception
     */
    @Test
    public final void testDataPropertiesTotalMismatch() throws Exception {
        double distExpected = 1.d;
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#nameComment", OWLDataProperty.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#hasMeltingPointComment", OWLDataProperty.class, ontology2);
        Correspondence<? extends OWLEntity> corr = SimpleCorrespondenceFactory.getInstance().createCorrespondence(ent1, ent2);
        double distActual = baseMatcher.getDistance(corr);
        assertEquals("Distance mismatch.", distExpected, distActual, 0.);
    }

    /**
     * Total rdfs:comment match of datatype properties. Expected: best match
     * (0.0)
     * 
     * @throws Exception
     */
    @Test
    public final void testDataPropertiesTotalMatch() throws Exception {
        double distExpected = 0.d;
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#hasMeltingPointComment", OWLDataProperty.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#hasMeltingPointComment", OWLDataProperty.class, ontology2);
        Correspondence<? extends OWLEntity> corr = SimpleCorrespondenceFactory.getInstance().createCorrespondence(ent1, ent2);
        double distActual = baseMatcher.getDistance(corr);
        assertEquals("Distance mismatch.", distExpected, distActual, 0.);
    }

    /**
     * No rdfs:comment in both datatype properties. Expected:
     * {@link InfeasibleBaseMatcherException}
     * 
     * @throws Exception
     */
    @Test(expected = InfeasibleBaseMatcherException.class)
    public final void testDataPropertiesNoComment() throws Exception {
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#datatypePropertyWithNoComment", OWLDataProperty.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#datatypePropertyWithNoComment", OWLDataProperty.class, ontology2);
        Correspondence<? extends OWLEntity> corr = SimpleCorrespondenceFactory.getInstance().createCorrespondence(ent1, ent2);
        baseMatcher.getDistance(corr);
    }

    /**
     * No rdfs:comment in first datatype property. Expected:
     * {@link InfeasibleBaseMatcherException}
     * 
     * @throws Exception
     */
    @Test(expected = InfeasibleBaseMatcherException.class)
    public final void testDataPropertiesNoCommentEntity1() throws Exception {
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#datatypePropertyWithNoComment", OWLDataProperty.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#datatypePropertyWithComment", OWLDataProperty.class, ontology2);
        Correspondence<? extends OWLEntity> corr = SimpleCorrespondenceFactory.getInstance().createCorrespondence(ent1, ent2);
        baseMatcher.getDistance(corr);
    }

    /**
     * No rdfs:comment in second datatype property. Expected:
     * {@link InfeasibleBaseMatcherException}
     * 
     * @throws Exception
     */
    @Test(expected = InfeasibleBaseMatcherException.class)
    public final void testDataPropertiesNoCommentEntity2() throws Exception {
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#datatypePropertyWithComment", OWLDataProperty.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#datatypePropertyWithNoComment", OWLDataProperty.class, ontology2);
        Correspondence<? extends OWLEntity> corr = SimpleCorrespondenceFactory.getInstance().createCorrespondence(ent1, ent2);
        baseMatcher.getDistance(corr);
    }

    /**
     * Total rdfs:comment mismatch of individuals. Expected: worst match (1.0)
     * 
     * @throws Exception
     */
    @Test
    public final void testIndividualsTotalMismatch() throws Exception {
        double distExpected = 1.d;
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#Emma", OWLNamedIndividual.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#Peter", OWLNamedIndividual.class, ontology2);
        Correspondence<? extends OWLEntity> corr = SimpleCorrespondenceFactory.getInstance().createCorrespondence(ent1, ent2);
        double distActual = baseMatcher.getDistance(corr);
        assertEquals("Distance mismatch.", distExpected, distActual, 0.);
    }

    /**
     * Total rdfs:comment match of individuals. Expected: best match (0.0)
     * 
     * @throws Exception
     */
    @Test
    public final void testIndividualsTotalMatch() throws Exception {
        double distExpected = 0.d;
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#Peter", OWLNamedIndividual.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#Peter", OWLNamedIndividual.class, ontology2);
        Correspondence<? extends OWLEntity> corr = SimpleCorrespondenceFactory.getInstance().createCorrespondence(ent1, ent2);
        double distActual = baseMatcher.getDistance(corr);
        assertEquals("Distance mismatch.", distExpected, distActual, 0.);
    }

    /**
     * No rdfs:comment in both individuals. Expected:
     * {@link InfeasibleBaseMatcherException}
     * 
     * @throws Exception
     */
    @Test(expected = InfeasibleBaseMatcherException.class)
    public final void testIndividualsNoComment() throws Exception {
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#Nobody", OWLNamedIndividual.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#Nobody", OWLNamedIndividual.class, ontology2);
        Correspondence<? extends OWLEntity> corr = SimpleCorrespondenceFactory.getInstance().createCorrespondence(ent1, ent2);
        baseMatcher.getDistance(corr);
    }

    /**
     * No rdfs:comment in first individual. Expected:
     * {@link InfeasibleBaseMatcherException}
     * 
     * @throws Exception
     */
    @Test(expected = InfeasibleBaseMatcherException.class)
    public final void testIndividualsNoCommentEntity1() throws Exception {
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#Nobody", OWLNamedIndividual.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#Peter", OWLNamedIndividual.class, ontology2);
        Correspondence<? extends OWLEntity> corr = SimpleCorrespondenceFactory.getInstance().createCorrespondence(ent1, ent2);
        baseMatcher.getDistance(corr);
    }

    /**
     * No rdfs:comment in second individual. Expected:
     * {@link InfeasibleBaseMatcherException}
     * 
     * @throws Exception
     */
    @Test(expected = InfeasibleBaseMatcherException.class)
    public final void testIndividualsNoCommentEntity2() throws Exception {
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#Peter", OWLNamedIndividual.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#Nobody", OWLNamedIndividual.class, ontology2);
        Correspondence<? extends OWLEntity> corr = SimpleCorrespondenceFactory.getInstance().createCorrespondence(ent1, ent2);
        baseMatcher.getDistance(corr);
    }
}

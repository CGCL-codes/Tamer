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
 * Unit test for the {@link EntityTextLabelDistanceMatcher}.
 * 
 * @author bock
 *
 */
public class EntityTextLabelDistanceMatcherTest extends BaseMatcherTest {

    @Before
    public void setUp() throws Exception {
        baseMatcher = new EntityTextLabelDistanceMatcher();
        baseMatcher.init(SimpleAlignmentFactory.getInstance().createAlignment(ontology1, ontology2));
    }

    @After
    public void tearDown() throws Exception {
        baseMatcher = null;
    }

    /**
     * Total rdfs:label mismatch of OWL classes.
     * Expected: worst match (1.0)
     * @throws Exception 
     */
    @Test
    public final void testClassesTotalMismatch1() throws Exception {
        double distExpected = 1.d;
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#Gas", OWLClass.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#Liquid", OWLClass.class, ontology2);
        Correspondence<? extends OWLEntity> corr = SimpleCorrespondenceFactory.getInstance().createCorrespondence(ent1, ent2);
        double distActual = baseMatcher.getDistance(corr);
        assertEquals("Distance mismatch.", distExpected, distActual, 0.);
    }

    /**
     * Total rdfs:label mismatch of OWL classes.
     * Expected: worst match (1.0)
     * @throws Exception 
     */
    @Test
    public final void testClassesTotalMismatch2() throws Exception {
        double distExpected = 1.d;
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#TextLabelClass", OWLClass.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#DifferentTextLabelClass", OWLClass.class, ontology2);
        Correspondence<? extends OWLEntity> corr = SimpleCorrespondenceFactory.getInstance().createCorrespondence(ent1, ent2);
        double distActual = baseMatcher.getDistance(corr);
        assertEquals("Distance mismatch.", distExpected, distActual, 0.);
    }

    /**
     * Total rdfs:label match of OWL classes.
     * Expected: best match (0.0)
     * @throws Exception 
     */
    @Test
    public final void testClassesTotalMatch1() throws Exception {
        double distExpected = 0.d;
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#Liquid", OWLClass.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#Liquid", OWLClass.class, ontology2);
        Correspondence<? extends OWLEntity> corr = SimpleCorrespondenceFactory.getInstance().createCorrespondence(ent1, ent2);
        double distActual = baseMatcher.getDistance(corr);
        assertEquals("Distance mismatch.", distExpected, distActual, 0.);
    }

    /**
     * Total rdfs:label match of OWL classes. Second class label contains "_" instead of " ".
     * Expected: best match (0.0)
     * @throws Exception 
     */
    @Test
    public final void testClassesTotalMatch2() throws Exception {
        double distExpected = 0.d;
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#TextLabelClass", OWLClass.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#TextLabelClass", OWLClass.class, ontology2);
        Correspondence<? extends OWLEntity> corr = SimpleCorrespondenceFactory.getInstance().createCorrespondence(ent1, ent2);
        double distActual = baseMatcher.getDistance(corr);
        assertEquals("Distance mismatch.", distExpected, distActual, 0.);
    }

    /**
     * No rdfs:label in both OWL classes.
     * Expected: {@link InfeasibleBaseMatcherException}
     * @throws Exception 
     */
    @Test(expected = InfeasibleBaseMatcherException.class)
    public final void testClassesNoLabel() throws Exception {
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#NoLabel", OWLClass.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#NoLabel", OWLClass.class, ontology2);
        Correspondence<? extends OWLEntity> corr = SimpleCorrespondenceFactory.getInstance().createCorrespondence(ent1, ent2);
        baseMatcher.getDistance(corr);
    }

    /**
     * No rdfs:label in first OWL class.
     * Expected: {@link InfeasibleBaseMatcherException}
     * @throws Exception 
     */
    @Test(expected = InfeasibleBaseMatcherException.class)
    public final void testClassesNoLabelEntity1() throws Exception {
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#NoLabel", OWLClass.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#Liquid", OWLClass.class, ontology2);
        Correspondence<? extends OWLEntity> corr = SimpleCorrespondenceFactory.getInstance().createCorrespondence(ent1, ent2);
        baseMatcher.getDistance(corr);
    }

    /**
     * No rdfs:label in second OWL class.
     * Expected: {@link InfeasibleBaseMatcherException}
     * @throws Exception 
     */
    @Test(expected = InfeasibleBaseMatcherException.class)
    public final void testClassesNoLabelEntity2() throws Exception {
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#Liquid", OWLClass.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#NoLabel", OWLClass.class, ontology2);
        Correspondence<? extends OWLEntity> corr = SimpleCorrespondenceFactory.getInstance().createCorrespondence(ent1, ent2);
        baseMatcher.getDistance(corr);
    }

    /**
     * Total rdfs:label mismatch of object properites.
     * Expected: worst match (1.0)
     * @throws Exception 
     */
    @Test
    public final void testObjectPropertiesTotalMismatch1() throws Exception {
        double distExpected = 1.d;
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#loves", OWLObjectProperty.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#hasAntipathyAgainst", OWLObjectProperty.class, ontology2);
        Correspondence<? extends OWLEntity> corr = SimpleCorrespondenceFactory.getInstance().createCorrespondence(ent1, ent2);
        double distActual = baseMatcher.getDistance(corr);
        assertEquals("Distance mismatch.", distExpected, distActual, 0.);
    }

    /**
     * Total rdfs:label mismatch of object properites.
     * Expected: worst match (1.0)
     * @throws Exception 
     */
    @Test
    public final void testObjectPropertiesTotalMismatch2() throws Exception {
        double distExpected = 1.d;
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#hasTextLabel", OWLObjectProperty.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#hasDifferentTextLabel", OWLObjectProperty.class, ontology2);
        Correspondence<? extends OWLEntity> corr = SimpleCorrespondenceFactory.getInstance().createCorrespondence(ent1, ent2);
        double distActual = baseMatcher.getDistance(corr);
        assertEquals("Distance mismatch.", distExpected, distActual, 0.);
    }

    /**
     * Total rdfs:label match of object properties.
     * Expected: best match (0.0)
     * @throws Exception 
     */
    @Test
    public final void testObjectPropertiesTotalMatch1() throws Exception {
        double distExpected = 0.d;
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#loves", OWLObjectProperty.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#loves", OWLObjectProperty.class, ontology2);
        Correspondence<? extends OWLEntity> corr = SimpleCorrespondenceFactory.getInstance().createCorrespondence(ent1, ent2);
        double distActual = baseMatcher.getDistance(corr);
        assertEquals("Distance mismatch.", distExpected, distActual, 0.);
    }

    /**
     * Total rdfs:label match of object properties.
     * Expected: best match (0.0)
     * @throws Exception 
     */
    @Test
    public final void testObjectPropertiesTotalMatch2() throws Exception {
        double distExpected = 0.d;
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#hasTextLabel", OWLObjectProperty.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#hasTextLabel", OWLObjectProperty.class, ontology2);
        Correspondence<? extends OWLEntity> corr = SimpleCorrespondenceFactory.getInstance().createCorrespondence(ent1, ent2);
        double distActual = baseMatcher.getDistance(corr);
        assertEquals("Distance mismatch.", distExpected, distActual, 0.);
    }

    /**
     * No rdfs:label in both object properties.
     * Expected: {@link InfeasibleBaseMatcherException}
     * @throws Exception 
     */
    @Test(expected = InfeasibleBaseMatcherException.class)
    public final void testObjectPropertiesNoLabel() throws Exception {
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#hasNoLabel", OWLObjectProperty.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#hasNoLabel", OWLObjectProperty.class, ontology2);
        Correspondence<? extends OWLEntity> corr = SimpleCorrespondenceFactory.getInstance().createCorrespondence(ent1, ent2);
        baseMatcher.getDistance(corr);
    }

    /**
     * No rdfs:label in first object property.
     * Expected: {@link InfeasibleBaseMatcherException}
     * @throws Exception 
     */
    @Test(expected = InfeasibleBaseMatcherException.class)
    public final void testObjectPropertiesNoLabelEntity1() throws Exception {
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#hasNoLabel", OWLObjectProperty.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#hasLabel", OWLObjectProperty.class, ontology2);
        Correspondence<? extends OWLEntity> corr = SimpleCorrespondenceFactory.getInstance().createCorrespondence(ent1, ent2);
        baseMatcher.getDistance(corr);
    }

    /**
     * No rdfs:label in second object property.
     * Expected: {@link InfeasibleBaseMatcherException}
     * @throws Exception 
     */
    @Test(expected = InfeasibleBaseMatcherException.class)
    public final void testObjectPropertiesNoLabelEntity2() throws Exception {
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#hasLabel", OWLObjectProperty.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#hasNoLabel", OWLObjectProperty.class, ontology2);
        Correspondence<? extends OWLEntity> corr = SimpleCorrespondenceFactory.getInstance().createCorrespondence(ent1, ent2);
        baseMatcher.getDistance(corr);
    }

    /**
     * Total rdfs:label mismatch of datatype properites.
     * Expected: worst match (1.0)
     * @throws Exception 
     */
    @Test
    public final void testDataPropertiesTotalMismatch1() throws Exception {
        double distExpected = 1.d;
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#name", OWLDataProperty.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#hasMeltingPoint", OWLDataProperty.class, ontology2);
        Correspondence<? extends OWLEntity> corr = SimpleCorrespondenceFactory.getInstance().createCorrespondence(ent1, ent2);
        double distActual = baseMatcher.getDistance(corr);
        assertEquals("Distance mismatch.", distExpected, distActual, 0.);
    }

    /**
     * Total rdfs:label mismatch of datatype properites.
     * Expected: worst match (1.0)
     * @throws Exception 
     */
    @Test
    public final void testDataPropertiesTotalMismatch2() throws Exception {
        double distExpected = 1.d;
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#datatypePropertyTextLabel", OWLDataProperty.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#datatypePropertyDifferentTextLabel", OWLDataProperty.class, ontology2);
        Correspondence<? extends OWLEntity> corr = SimpleCorrespondenceFactory.getInstance().createCorrespondence(ent1, ent2);
        double distActual = baseMatcher.getDistance(corr);
        assertEquals("Distance mismatch.", distExpected, distActual, 0.);
    }

    /**
     * Total rdfs:label match of datatype properties.
     * Expected: best match (0.0)
     * @throws Exception 
     */
    @Test
    public final void testDataPropertiesTotalMatch1() throws Exception {
        double distExpected = 0.d;
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#hasSteamingPoint", OWLDataProperty.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#hasSteamingPoint", OWLDataProperty.class, ontology2);
        Correspondence<? extends OWLEntity> corr = SimpleCorrespondenceFactory.getInstance().createCorrespondence(ent1, ent2);
        double distActual = baseMatcher.getDistance(corr);
        assertEquals("Distance mismatch.", distExpected, distActual, 0.);
    }

    /**
     * Total rdfs:label match of datatype properties.
     * Expected: best match (0.0)
     * @throws Exception 
     */
    @Test
    public final void testDataPropertiesTotalMatch2() throws Exception {
        double distExpected = 0.d;
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#datatypePropertyTextLabel", OWLDataProperty.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#datatypePropertyTextLabel", OWLDataProperty.class, ontology2);
        Correspondence<? extends OWLEntity> corr = SimpleCorrespondenceFactory.getInstance().createCorrespondence(ent1, ent2);
        double distActual = baseMatcher.getDistance(corr);
        assertEquals("Distance mismatch.", distExpected, distActual, 0.);
    }

    /**
     * No rdfs:label in both datatype properties.
     * Expected: {@link InfeasibleBaseMatcherException}
     * @throws Exception 
     */
    @Test(expected = InfeasibleBaseMatcherException.class)
    public final void testDataPropertiesNoLabel() throws Exception {
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#datatypePropertyWithNoLabel", OWLDataProperty.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#datatypePropertyWithNoLabel", OWLDataProperty.class, ontology2);
        Correspondence<? extends OWLEntity> corr = SimpleCorrespondenceFactory.getInstance().createCorrespondence(ent1, ent2);
        baseMatcher.getDistance(corr);
    }

    /**
     * No rdfs:label in first datatype property.
     * Expected: {@link InfeasibleBaseMatcherException}
     * @throws Exception 
     */
    @Test(expected = InfeasibleBaseMatcherException.class)
    public final void testDataPropertiesNoLabelEntity1() throws Exception {
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#datatypePropertyWithNoLabel", OWLDataProperty.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#datatypePropertyWithLabel", OWLDataProperty.class, ontology2);
        Correspondence<? extends OWLEntity> corr = SimpleCorrespondenceFactory.getInstance().createCorrespondence(ent1, ent2);
        baseMatcher.getDistance(corr);
    }

    /**
     * No rdfs:label in second datatype property.
     * Expected: {@link InfeasibleBaseMatcherException}
     * @throws Exception 
     */
    @Test(expected = InfeasibleBaseMatcherException.class)
    public final void testDataPropertiesNoLabelEntity2() throws Exception {
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#datatypePropertyWithLabel", OWLDataProperty.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#datatypePropertyWithNoLabel", OWLDataProperty.class, ontology2);
        Correspondence<? extends OWLEntity> corr = SimpleCorrespondenceFactory.getInstance().createCorrespondence(ent1, ent2);
        baseMatcher.getDistance(corr);
    }

    /**
     * Total rdfs:label mismatch of individuals.
     * Expected: worst match (1.0)
     * @throws Exception 
     */
    @Test
    public final void testIndividualsTotalMismatch1() throws Exception {
        double distExpected = 1.d;
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#John", OWLNamedIndividual.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#Mary", OWLNamedIndividual.class, ontology2);
        Correspondence<? extends OWLEntity> corr = SimpleCorrespondenceFactory.getInstance().createCorrespondence(ent1, ent2);
        double distActual = baseMatcher.getDistance(corr);
        assertEquals("Distance mismatch.", distExpected, distActual, 0.);
    }

    /**
     * Total rdfs:label mismatch of individuals.
     * Expected: worst match (1.0)
     * @throws Exception 
     */
    @Test
    public final void testIndividualsTotalMismatch2() throws Exception {
        double distExpected = 1.d;
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#textLabeledIndividual", OWLNamedIndividual.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#differentTextLabeledIndividual", OWLNamedIndividual.class, ontology2);
        Correspondence<? extends OWLEntity> corr = SimpleCorrespondenceFactory.getInstance().createCorrespondence(ent1, ent2);
        double distActual = baseMatcher.getDistance(corr);
        assertEquals("Distance mismatch.", distExpected, distActual, 0.);
    }

    /**
     * Total rdfs:label match of individuals.
     * Expected: best match (0.0)
     * @throws Exception 
     */
    @Test
    public final void testIndividualsTotalMatch1() throws Exception {
        double distExpected = 0.d;
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#John", OWLNamedIndividual.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#John", OWLNamedIndividual.class, ontology2);
        Correspondence<? extends OWLEntity> corr = SimpleCorrespondenceFactory.getInstance().createCorrespondence(ent1, ent2);
        double distActual = baseMatcher.getDistance(corr);
        assertEquals("Distance mismatch.", distExpected, distActual, 0.);
    }

    /**
     * Total rdfs:label match of individuals.
     * Expected: best match (0.0)
     * @throws Exception 
     */
    @Test
    public final void testIndividualsTotalMatch2() throws Exception {
        double distExpected = 0.d;
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#textLabeledIndividual", OWLNamedIndividual.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#textLabeledIndividual", OWLNamedIndividual.class, ontology2);
        Correspondence<? extends OWLEntity> corr = SimpleCorrespondenceFactory.getInstance().createCorrespondence(ent1, ent2);
        double distActual = baseMatcher.getDistance(corr);
        assertEquals("Distance mismatch.", distExpected, distActual, 0.);
    }

    /**
     * No rdfs:label in both individuals.
     * Expected: {@link InfeasibleBaseMatcherException}
     * @throws Exception 
     */
    @Test(expected = InfeasibleBaseMatcherException.class)
    public final void testIndividualsNoLabel() throws Exception {
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#Nobody", OWLNamedIndividual.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#Nobody", OWLNamedIndividual.class, ontology2);
        Correspondence<? extends OWLEntity> corr = SimpleCorrespondenceFactory.getInstance().createCorrespondence(ent1, ent2);
        baseMatcher.getDistance(corr);
    }

    /**
     * No rdfs:label in first individual.
     * Expected: {@link InfeasibleBaseMatcherException}
     * @throws Exception 
     */
    @Test(expected = InfeasibleBaseMatcherException.class)
    public final void testIndividualsNoLabelEntity1() throws Exception {
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#Nobody", OWLNamedIndividual.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#Somebody", OWLNamedIndividual.class, ontology2);
        Correspondence<? extends OWLEntity> corr = SimpleCorrespondenceFactory.getInstance().createCorrespondence(ent1, ent2);
        baseMatcher.getDistance(corr);
    }

    /**
     * No rdfs:label in second individual.
     * Expected: {@link InfeasibleBaseMatcherException}
     * @throws Exception 
     */
    @Test(expected = InfeasibleBaseMatcherException.class)
    public final void testIndividualsNoLabelEntity2() throws Exception {
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#Somebody", OWLNamedIndividual.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#Nobody", OWLNamedIndividual.class, ontology2);
        Correspondence<? extends OWLEntity> corr = SimpleCorrespondenceFactory.getInstance().createCorrespondence(ent1, ent2);
        baseMatcher.getDistance(corr);
    }
}

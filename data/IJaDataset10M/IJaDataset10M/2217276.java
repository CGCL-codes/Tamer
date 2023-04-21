package de.fzi.mappso.basematcher;

import static org.junit.Assert.assertEquals;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import org.apache.log4j.PropertyConfigurator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owl.align.AlignmentException;
import org.semanticweb.owl.align.Cell;
import org.semanticweb.owl.align.Relation;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import fr.inrialpes.exmo.align.impl.BasicCell;
import fr.inrialpes.exmo.align.impl.rel.EquivRelation;

/**
 * Unit test for the {@link EntityLabelDistanceMatcher}.
 * 
 * @author bock
 *
 */
public class EntityLabelDistanceMatcherTest extends BaseMatcherTest {

    private static Relation rel;

    public EntityLabelDistanceMatcherTest() {
        PropertyConfigurator.configure("log4j.properties");
    }

    @Before
    public void setUp() throws Exception {
        baseMatcher = new EntityLabelDistanceMatcher();
        baseMatcher.init(ontology1, ontology2, new Hashtable<Object, Set<Cell>>(), new Hashtable<Object, Set<Cell>>(), new HashSet<Cell>());
        rel = new EquivRelation();
    }

    @After
    public void tearDown() throws Exception {
        rel = null;
        baseMatcher = null;
    }

    /**
     * Total rdfs:label mismatch of OWL classes.
     * Expected: worst match (1.0)
     * @throws AlignmentException 
     */
    @Test
    public final void testClassesTotalMismatch() throws AlignmentException {
        double distExpected = 1.d;
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#Gas", OWLClass.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#Liquid", OWLClass.class, ontology2);
        Cell corr = new BasicCell("", ent1, ent2, rel, 0);
        double distActual = baseMatcher.getDistance(corr);
        assertEquals("Distance mismatch.", distExpected, distActual, 0.);
    }

    /**
     * Total rdfs:label match of OWL classes.
     * Expected: best match (0.0)
     * @throws AlignmentException 
     */
    @Test
    public final void testClassesTotalMatch() throws AlignmentException {
        double distExpected = 0.d;
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#Liquid", OWLClass.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#Liquid", OWLClass.class, ontology2);
        Cell corr = new BasicCell("", ent1, ent2, rel, 0);
        double distActual = baseMatcher.getDistance(corr);
        assertEquals("Distance mismatch.", distExpected, distActual, 0.);
    }

    /**
     * No rdfs:label in both OWL classes.
     * Expected: {@link InfeasibleBaseMatcherException}
     * @throws AlignmentException 
     */
    @Test(expected = InfeasibleBaseMatcherException.class)
    public final void testClassesNoLabel() throws AlignmentException {
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#NoLabel", OWLClass.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#NoLabel", OWLClass.class, ontology2);
        Cell corr = new BasicCell("", ent1, ent2, rel, 0);
        baseMatcher.getDistance(corr);
    }

    /**
     * No rdfs:label in first OWL class.
     * Expected: {@link InfeasibleBaseMatcherException}
     * @throws AlignmentException 
     */
    @Test(expected = InfeasibleBaseMatcherException.class)
    public final void testClassesNoLabelEntity1() throws AlignmentException {
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#NoLabel", OWLClass.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#Liquid", OWLClass.class, ontology2);
        Cell corr = new BasicCell("", ent1, ent2, rel, 0);
        baseMatcher.getDistance(corr);
    }

    /**
     * No rdfs:label in second OWL class.
     * Expected: {@link InfeasibleBaseMatcherException}
     * @throws AlignmentException 
     */
    @Test(expected = InfeasibleBaseMatcherException.class)
    public final void testClassesNoLabelEntity2() throws AlignmentException {
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#Liquid", OWLClass.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#NoLabel", OWLClass.class, ontology2);
        Cell corr = new BasicCell("", ent1, ent2, rel, 0);
        baseMatcher.getDistance(corr);
    }

    /**
     * Total rdfs:label mismatch of object properites.
     * Expected: worst match (1.0)
     * @throws AlignmentException 
     */
    @Test
    public final void testObjectPropertiesTotalMismatch() throws AlignmentException {
        double distExpected = 1.d;
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#loves", OWLObjectProperty.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#hasAntipathyAgainst", OWLObjectProperty.class, ontology2);
        Cell corr = new BasicCell("", ent1, ent2, rel, 0);
        double distActual = baseMatcher.getDistance(corr);
        assertEquals("Distance mismatch.", distExpected, distActual, 0.);
    }

    /**
     * Total rdfs:label match of object properties.
     * Expected: best match (0.0)
     * @throws AlignmentException 
     */
    @Test
    public final void testObjectPropertiesTotalMatch() throws AlignmentException {
        double distExpected = 0.d;
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#loves", OWLObjectProperty.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#loves", OWLObjectProperty.class, ontology2);
        Cell corr = new BasicCell("", ent1, ent2, rel, 0);
        double distActual = baseMatcher.getDistance(corr);
        assertEquals("Distance mismatch.", distExpected, distActual, 0.);
    }

    /**
     * No rdfs:label in both object properties.
     * Expected: {@link InfeasibleBaseMatcherException}
     * @throws AlignmentException 
     */
    @Test(expected = InfeasibleBaseMatcherException.class)
    public final void testObjectPropertiesNoLabel() throws AlignmentException {
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#hasNoLabel", OWLObjectProperty.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#hasNoLabel", OWLObjectProperty.class, ontology2);
        Cell corr = new BasicCell("", ent1, ent2, rel, 0);
        baseMatcher.getDistance(corr);
    }

    /**
     * No rdfs:label in first object property.
     * Expected: {@link InfeasibleBaseMatcherException}
     * @throws AlignmentException 
     */
    @Test(expected = InfeasibleBaseMatcherException.class)
    public final void testObjectPropertiesNoLabelEntity1() throws AlignmentException {
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#hasNoLabel", OWLObjectProperty.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#hasLabel", OWLObjectProperty.class, ontology2);
        Cell corr = new BasicCell("", ent1, ent2, rel, 0);
        baseMatcher.getDistance(corr);
    }

    /**
     * No rdfs:label in second object property.
     * Expected: {@link InfeasibleBaseMatcherException}
     * @throws AlignmentException 
     */
    @Test(expected = InfeasibleBaseMatcherException.class)
    public final void testObjectPropertiesNoLabelEntity2() throws AlignmentException {
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#hasLabel", OWLObjectProperty.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#hasNoLabel", OWLObjectProperty.class, ontology2);
        Cell corr = new BasicCell("", ent1, ent2, rel, 0);
        baseMatcher.getDistance(corr);
    }

    /**
     * Total rdfs:label mismatch of datatype properites.
     * Expected: worst match (1.0)
     * @throws AlignmentException 
     */
    @Test
    public final void testDataPropertiesTotalMismatch() throws AlignmentException {
        double distExpected = 1.d;
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#name", OWLDataProperty.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#hasMeltingPoint", OWLDataProperty.class, ontology2);
        Cell corr = new BasicCell("", ent1, ent2, rel, 0);
        double distActual = baseMatcher.getDistance(corr);
        assertEquals("Distance mismatch.", distExpected, distActual, 0.);
    }

    /**
     * Total rdfs:label match of datatype properties.
     * Expected: best match (0.0)
     * @throws AlignmentException 
     */
    @Test
    public final void testDataPropertiesTotalMatch() throws AlignmentException {
        double distExpected = 0.d;
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#hasSteamingPoint", OWLDataProperty.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#hasSteamingPoint", OWLDataProperty.class, ontology2);
        Cell corr = new BasicCell("", ent1, ent2, rel, 0);
        double distActual = baseMatcher.getDistance(corr);
        assertEquals("Distance mismatch.", distExpected, distActual, 0.);
    }

    /**
     * No rdfs:label in both datatype properties.
     * Expected: {@link InfeasibleBaseMatcherException}
     * @throws AlignmentException 
     */
    @Test(expected = InfeasibleBaseMatcherException.class)
    public final void testDataPropertiesNoLabel() throws AlignmentException {
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#datatypePropertyWithNoLabel", OWLDataProperty.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#datatypePropertyWithNoLabel", OWLDataProperty.class, ontology2);
        Cell corr = new BasicCell("", ent1, ent2, rel, 0);
        baseMatcher.getDistance(corr);
    }

    /**
     * No rdfs:label in first datatype property.
     * Expected: {@link InfeasibleBaseMatcherException}
     * @throws AlignmentException 
     */
    @Test(expected = InfeasibleBaseMatcherException.class)
    public final void testDataPropertiesNoLabelEntity1() throws AlignmentException {
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#datatypePropertyWithNoLabel", OWLDataProperty.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#datatypePropertyWithLabel", OWLDataProperty.class, ontology2);
        Cell corr = new BasicCell("", ent1, ent2, rel, 0);
        baseMatcher.getDistance(corr);
    }

    /**
     * No rdfs:label in second datatype property.
     * Expected: {@link InfeasibleBaseMatcherException}
     * @throws AlignmentException 
     */
    @Test(expected = InfeasibleBaseMatcherException.class)
    public final void testDataPropertiesNoLabelEntity2() throws AlignmentException {
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#datatypePropertyWithLabel", OWLDataProperty.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#datatypePropertyWithNoLabel", OWLDataProperty.class, ontology2);
        Cell corr = new BasicCell("", ent1, ent2, rel, 0);
        baseMatcher.getDistance(corr);
    }

    /**
     * Total rdfs:label mismatch of individuals.
     * Expected: worst match (1.0)
     * @throws AlignmentException 
     */
    @Test
    public final void testIndividualsTotalMismatch() throws AlignmentException {
        double distExpected = 1.d;
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#John", OWLNamedIndividual.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#Mary", OWLNamedIndividual.class, ontology2);
        Cell corr = new BasicCell("", ent1, ent2, rel, 0);
        double distActual = baseMatcher.getDistance(corr);
        assertEquals("Distance mismatch.", distExpected, distActual, 0.);
    }

    /**
     * Total rdfs:label match of individuals.
     * Expected: best match (0.0)
     * @throws AlignmentException 
     */
    @Test
    public final void testIndividualsTotalMatch() throws AlignmentException {
        double distExpected = 0.d;
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#John", OWLNamedIndividual.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#John", OWLNamedIndividual.class, ontology2);
        Cell corr = new BasicCell("", ent1, ent2, rel, 0);
        double distActual = baseMatcher.getDistance(corr);
        assertEquals("Distance mismatch.", distExpected, distActual, 0.);
    }

    /**
     * No rdfs:label in both individuals.
     * Expected: {@link InfeasibleBaseMatcherException}
     * @throws AlignmentException 
     */
    @Test(expected = InfeasibleBaseMatcherException.class)
    public final void testIndividualsNoLabel() throws AlignmentException {
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#Nobody", OWLNamedIndividual.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#Nobody", OWLNamedIndividual.class, ontology2);
        Cell corr = new BasicCell("", ent1, ent2, rel, 0);
        baseMatcher.getDistance(corr);
    }

    /**
     * No rdfs:label in first individual.
     * Expected: {@link InfeasibleBaseMatcherException}
     * @throws AlignmentException 
     */
    @Test(expected = InfeasibleBaseMatcherException.class)
    public final void testIndividualsNoLabelEntity1() throws AlignmentException {
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#Nobody", OWLNamedIndividual.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#Somebody", OWLNamedIndividual.class, ontology2);
        Cell corr = new BasicCell("", ent1, ent2, rel, 0);
        baseMatcher.getDistance(corr);
    }

    /**
     * No rdfs:label in second individual.
     * Expected: {@link InfeasibleBaseMatcherException}
     * @throws AlignmentException 
     */
    @Test(expected = InfeasibleBaseMatcherException.class)
    public final void testIndividualsNoLabelEntity2() throws AlignmentException {
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#Somebody", OWLNamedIndividual.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#Nobody", OWLNamedIndividual.class, ontology2);
        Cell corr = new BasicCell("", ent1, ent2, rel, 0);
        baseMatcher.getDistance(corr);
    }
}

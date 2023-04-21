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
 * Unit test for the {@link EntityTextNameDistanceMatcher}.
 * 
 * @author bock
 * 
 */
public class EntityTextNameDistanceMatcherTest extends BaseMatcherTest {

    private static Relation rel;

    public EntityTextNameDistanceMatcherTest() {
        PropertyConfigurator.configure("log4j.properties");
    }

    @Before
    public void setUp() throws Exception {
        baseMatcher = new EntityTextNameDistanceMatcher();
        baseMatcher.init(ontology1, ontology2, new Hashtable<Object, Set<Cell>>(), new Hashtable<Object, Set<Cell>>(), new HashSet<Cell>());
        rel = new EquivRelation();
    }

    @After
    public void tearDown() throws Exception {
        rel = null;
        baseMatcher = null;
    }

    /**
     * Total IRI fragment mismatch of OWL classes. Expected: worst match (1.0)
     * 
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
     * Total IRI fragment match of OWL classes. Expected: best match (0.0)
     * 
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
     * Total IRI fragment match of OWL classes. Expected: best match (0.0)
     * Second IRI fragment contains fillers.
     * 
     * @throws AlignmentException
     */
    @Test
    public final void testClassesTotalMatchWithFillers() throws AlignmentException {
        double distExpected = 0.d;
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#Long-Class-Name", OWLClass.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#Long_Class_Name", OWLClass.class, ontology2);
        Cell corr = new BasicCell("", ent1, ent2, rel, 0);
        double distActual = baseMatcher.getDistance(corr);
        assertEquals("Distance mismatch.", distExpected, distActual, 0.);
    }

    /**
     * Total IRI fragment match of OWL classes, but no rdfs:label in both
     * entities. Expected: no match (0.0) -- labels don't matter here.
     * 
     * @throws AlignmentException
     */
    @Test
    public final void testClassesNoLabel() throws AlignmentException {
        double distExpected = 0.d;
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#NoLabel", OWLClass.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#NoLabel", OWLClass.class, ontology2);
        Cell corr = new BasicCell("", ent1, ent2, rel, 0);
        double distActual = baseMatcher.getDistance(corr);
        assertEquals("Distance mismatch.", distExpected, distActual, 0.);
    }

    /**
     * Total IRI fragment mismatch of object properties. Expected: worst match
     * (1.0)
     * 
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
     * Total IRI fragment match of object properties. Expected: best match (0.0)
     * 
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
     * Total IRI fragment match of object properties. Expected: best match (0.0)
     * Second IRI fragment contains fillers.
     * 
     * @throws AlignmentException
     */
    @Test
    public final void testObjectPropertiesTotalMatchWithFillers() throws AlignmentException {
        double distExpected = 0.d;
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#long-Object-Property-Name", OWLObjectProperty.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#long_object_property_name", OWLObjectProperty.class, ontology2);
        Cell corr = new BasicCell("", ent1, ent2, rel, 0);
        double distActual = baseMatcher.getDistance(corr);
        assertEquals("Distance mismatch.", distExpected, distActual, 0.);
    }

    /**
     * Total IRI fragment match of object properties, but no rdfs:label in both
     * entities. Expected: no match (0.0) -- labels don't matter here.
     * 
     * @throws AlignmentException
     */
    @Test
    public final void testObjectPropertiesNoLabel() throws AlignmentException {
        double distExpected = 0.d;
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#hasNoLabel", OWLObjectProperty.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#hasNoLabel", OWLObjectProperty.class, ontology2);
        Cell corr = new BasicCell("", ent1, ent2, rel, 0);
        double distActual = baseMatcher.getDistance(corr);
        assertEquals("Distance mismatch.", distExpected, distActual, 0.);
    }

    /**
     * Total IRI fragment mismatch of datatype properties. Expected: worst match
     * (1.0)
     * 
     * @throws AlignmentException
     */
    @Test
    public final void testDataPropertiesTotalMismatch() throws AlignmentException {
        double distExpected = 1.d;
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#hasSteamingPoint", OWLDataProperty.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#name", OWLDataProperty.class, ontology2);
        Cell corr = new BasicCell("", ent1, ent2, rel, 0);
        double distActual = baseMatcher.getDistance(corr);
        assertEquals("Distance mismatch.", distExpected, distActual, 0.);
    }

    /**
     * Total IRI fragment match of datatype properties. Expected: best match
     * (0.0)
     * 
     * @throws AlignmentException
     */
    @Test
    public final void testDataPropertiesTotalMatch() throws AlignmentException {
        double distExpected = 0.d;
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#hasMeltingPoint", OWLDataProperty.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#hasMeltingPoint", OWLDataProperty.class, ontology2);
        Cell corr = new BasicCell("", ent1, ent2, rel, 0);
        double distActual = baseMatcher.getDistance(corr);
        assertEquals("Distance mismatch.", distExpected, distActual, 0.);
    }

    /**
     * Total IRI fragment match of datatype properties. Expected: best match
     * (0.0) Second IRI fragment contains fillers.
     * 
     * @throws AlignmentException
     */
    @Test
    public final void testDataPropertiesTotalMatchWithFillers() throws AlignmentException {
        double distExpected = 0.d;
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#long-Datatype-Property-Name", OWLDataProperty.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#long_datatype_property_name", OWLDataProperty.class, ontology2);
        Cell corr = new BasicCell("", ent1, ent2, rel, 0);
        double distActual = baseMatcher.getDistance(corr);
        assertEquals("Distance mismatch.", distExpected, distActual, 0.);
    }

    /**
     * Total IRI fragment match of datatype properties, but no rdfs:label in
     * both entities. Expected: no match (0.0) -- labels don't matter here.
     * 
     * @throws AlignmentException
     */
    @Test
    public final void testDataPropertiesNoLabel() throws AlignmentException {
        double distExpected = 0.d;
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#datatypePropertyWithNoLabel", OWLDataProperty.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#datatypePropertyWithNoLabel", OWLDataProperty.class, ontology2);
        Cell corr = new BasicCell("", ent1, ent2, rel, 0);
        double distActual = baseMatcher.getDistance(corr);
        assertEquals("Distance mismatch.", distExpected, distActual, 0.);
    }

    /**
     * Total IRI fragment mismatch of individuals. Expected: worst match (1.0)
     * 
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
     * Total IRI fragment match of individuals. Expected: best match (0.0)
     * 
     * @throws AlignmentException
     */
    @Test
    public final void testIndividualsTotalMatch() throws AlignmentException {
        double distExpected = 0.d;
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#Mary", OWLNamedIndividual.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#Mary", OWLNamedIndividual.class, ontology2);
        Cell corr = new BasicCell("", ent1, ent2, rel, 0);
        double distActual = baseMatcher.getDistance(corr);
        assertEquals("Distance mismatch.", distExpected, distActual, 0.);
    }

    /**
     * Total IRI fragment match of individuals. Expected: best match (0.0)
     * Second IRI fragment contains fillers.
     * 
     * @throws AlignmentException
     */
    @Test
    public final void testIndividualsTotalMatchWithFillers() throws AlignmentException {
        double distExpected = 0.d;
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#long-Individual-Name", OWLNamedIndividual.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#long_individual_name", OWLNamedIndividual.class, ontology2);
        Cell corr = new BasicCell("", ent1, ent2, rel, 0);
        double distActual = baseMatcher.getDistance(corr);
        assertEquals("Distance mismatch.", distExpected, distActual, 0.);
    }

    /**
     * Total IRI fragment match of individuals, but no rdfs:label in both
     * entities. Expected: no match (0.0) -- labels don't matter here.
     * 
     * @throws AlignmentException
     */
    @Test
    public final void testIndividualsNoLabel() throws AlignmentException {
        double distExpected = 0.d;
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#Nobody", OWLNamedIndividual.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#Nobody", OWLNamedIndividual.class, ontology2);
        Cell corr = new BasicCell("", ent1, ent2, rel, 0);
        double distActual = baseMatcher.getDistance(corr);
        assertEquals("Distance mismatch.", distExpected, distActual, 0.);
    }
}

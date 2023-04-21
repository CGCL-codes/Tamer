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

public class IndividualDataPropLiteralDistanceMatcherTest extends BaseMatcherTest {

    private static Relation rel = new EquivRelation();

    public IndividualDataPropLiteralDistanceMatcherTest() {
        PropertyConfigurator.configure("log4j.properties");
    }

    @Before
    public void setUp() throws Exception {
        baseMatcher = new IndividualDataPropLiteralDistanceMatcher();
    }

    @After
    public void tearDown() throws Exception {
        baseMatcher = null;
    }

    /**
     * No data property assertion for both individuals.
     * Expected: {@link InfeasibleBaseMatcherException}
     * @throws AlignmentException
     */
    @Test(expected = InfeasibleBaseMatcherException.class)
    public final void testNoDataPropertiesBothIndividuals() throws AlignmentException {
        baseMatcher.init(ontology1, ontology2, new Hashtable<Object, Set<Cell>>(), new Hashtable<Object, Set<Cell>>(), new HashSet<Cell>());
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#Mary", OWLNamedIndividual.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#Mary", OWLNamedIndividual.class, ontology2);
        Cell corr = new BasicCell("", ent1, ent2, rel, 0);
        baseMatcher.getDistance(corr);
    }

    /**
     * No data property assertion for first individual.
     * Expected: {@link InfeasibleBaseMatcherException}
     * @throws AlignmentException
     */
    @Test(expected = InfeasibleBaseMatcherException.class)
    public final void testNoDataPropertiesFirstIndividual() throws AlignmentException {
        baseMatcher.init(ontology1, ontology2, new Hashtable<Object, Set<Cell>>(), new Hashtable<Object, Set<Cell>>(), new HashSet<Cell>());
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#Mary", OWLNamedIndividual.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#Emma", OWLNamedIndividual.class, ontology2);
        Cell corr = new BasicCell("", ent1, ent2, rel, 0);
        baseMatcher.getDistance(corr);
    }

    /**
     * No data property assertion for second individual.
     * Expected: {@link InfeasibleBaseMatcherException}
     * @throws AlignmentException
     */
    @Test(expected = InfeasibleBaseMatcherException.class)
    public final void testNoDataPropertiesSecondIndividual() throws AlignmentException {
        baseMatcher.init(ontology1, ontology2, new Hashtable<Object, Set<Cell>>(), new Hashtable<Object, Set<Cell>>(), new HashSet<Cell>());
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#Emma", OWLNamedIndividual.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#Mary", OWLNamedIndividual.class, ontology2);
        Cell corr = new BasicCell("", ent1, ent2, rel, 0);
        baseMatcher.getDistance(corr);
    }

    /**
     * No data properties of the two individuals match.
     * Expected: 1.0 (worst match)
     * @throws AlignmentException
     */
    @Test
    public final void testNoDataPropertiesMatch() throws AlignmentException {
        double expected = 1.d;
        baseMatcher.init(ontology1, ontology2, new Hashtable<Object, Set<Cell>>(), new Hashtable<Object, Set<Cell>>(), new HashSet<Cell>());
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#Emma", OWLNamedIndividual.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#John", OWLNamedIndividual.class, ontology2);
        Cell corr = new BasicCell("", ent1, ent2, rel, 0);
        double actual = baseMatcher.getDistance(corr);
        assertEquals("Distance mismatch.", expected, actual, 0.);
    }

    /**
     * Data properties of the two individuals match, but their literals don't.
     * Expected: 1.0 (worst match)
     * @throws AlignmentException
     */
    @Test
    public final void testDataPropertiesMatchNoLiteralMatch1() throws AlignmentException {
        double expected = 1.d;
        OWLEntity dp1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#name", OWLDataProperty.class, ontology1);
        OWLEntity dp2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#firstName", OWLDataProperty.class, ontology2);
        Cell dpCorr = new BasicCell("", dp1, dp2, rel, 0);
        initWithCorrespondences(dpCorr);
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#Emma", OWLNamedIndividual.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#John", OWLNamedIndividual.class, ontology2);
        Cell corr = new BasicCell("", ent1, ent2, rel, 0);
        double actual = baseMatcher.getDistance(corr);
        assertEquals("Distance mismatch.", expected, actual, 0.);
    }

    /**
     * Data properties of the two individuals match, but their literals differ slightly.
     * Expected: 1.0 (worst match)
     * @throws AlignmentException
     */
    @Test
    public final void testDataPropertiesMatchLiteralsDifferSlightly1() throws AlignmentException {
        double expected = 1.d;
        OWLEntity dp1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#name", OWLDataProperty.class, ontology1);
        OWLEntity dp2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#surName", OWLDataProperty.class, ontology2);
        Cell dpCorr = new BasicCell("", dp1, dp2, rel, 0);
        initWithCorrespondences(dpCorr);
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#Emma", OWLNamedIndividual.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#Peter", OWLNamedIndividual.class, ontology2);
        Cell corr = new BasicCell("", ent1, ent2, rel, 0);
        double actual = baseMatcher.getDistance(corr);
        assertEquals("Distance mismatch.", expected, actual, 0.);
    }

    /**
     * Data properties of the two individuals match, literals match too.
     * Expected: 0.0 (best match)
     * @throws AlignmentException
     */
    @Test
    public final void testDataPropertiesMatchLiteralsMatch1() throws AlignmentException {
        double expected = 0.d;
        OWLEntity dp1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#name", OWLDataProperty.class, ontology1);
        OWLEntity dp2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#surName", OWLDataProperty.class, ontology2);
        Cell dpCorr = new BasicCell("", dp1, dp2, rel, 0);
        initWithCorrespondences(dpCorr);
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#Emma", OWLNamedIndividual.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#John", OWLNamedIndividual.class, ontology2);
        Cell corr = new BasicCell("", ent1, ent2, rel, 0);
        double actual = baseMatcher.getDistance(corr);
        assertEquals("Distance mismatch.", expected, actual, 0.);
    }

    /**
     * Data properties of the two individuals match, but their literals don't.
     * First individual has 1 data property, second individual has 2 data properties.
     * Expected: 1.0 (worst match)
     * @throws AlignmentException
     */
    @Test
    public final void testDataPropertiesMatchNoLiteralMatch2() throws AlignmentException {
        double expected = 1.d;
        OWLEntity dp1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#name", OWLDataProperty.class, ontology1);
        OWLEntity dp2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#firstName", OWLDataProperty.class, ontology2);
        Cell dpCorr = new BasicCell("", dp1, dp2, rel, 0);
        initWithCorrespondences(dpCorr);
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#Emma", OWLNamedIndividual.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#Somebody", OWLNamedIndividual.class, ontology2);
        Cell corr = new BasicCell("", ent1, ent2, rel, 0);
        double actual = baseMatcher.getDistance(corr);
        assertEquals("Distance mismatch.", expected, actual, 0.);
    }

    /**
     * Data properties of the two individuals match, but their literals differ slightly.
     * First individual has 1 data property, second individual has 2 data properties.
     * Expected: 1.0 (worst match)
     * @throws AlignmentException
     */
    @Test
    public final void testDataPropertiesMatchLiteralsDifferSlightly2() throws AlignmentException {
        double expected = 1.d;
        OWLEntity dp1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#firstName", OWLDataProperty.class, ontology1);
        OWLEntity dp2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#firstName", OWLDataProperty.class, ontology2);
        Cell dpCorr = new BasicCell("", dp1, dp2, rel, 0);
        initWithCorrespondences(dpCorr);
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#Nobody", OWLNamedIndividual.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#Somebody", OWLNamedIndividual.class, ontology2);
        Cell corr = new BasicCell("", ent1, ent2, rel, 0);
        double actual = baseMatcher.getDistance(corr);
        assertEquals("Distance mismatch.", expected, actual, 0.);
    }

    /**
     * Data properties of the two individuals match, literals match too.
     * First individual has 1 data property, second individual has 2 data properties.
     * Expected: 0.0 (best match)
     * @throws AlignmentException
     */
    @Test
    public final void testDataPropertiesMatchLiteralsMatch2() throws AlignmentException {
        double expected = 0.d;
        OWLEntity dp1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#firstName", OWLDataProperty.class, ontology1);
        OWLEntity dp2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#firstName", OWLDataProperty.class, ontology2);
        Cell dpCorr = new BasicCell("", dp1, dp2, rel, 0);
        initWithCorrespondences(dpCorr);
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#Somebody", OWLNamedIndividual.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#Somebody", OWLNamedIndividual.class, ontology2);
        Cell corr = new BasicCell("", ent1, ent2, rel, 0);
        double actual = baseMatcher.getDistance(corr);
        assertEquals("Distance mismatch.", expected, actual, 0.);
    }

    /**
     * One of two data properties of the two individuals match, but their literals don't.
     * First individual has 2 data property, second individual has 2 data properties.
     * Expected: 1.0 (worst match)
     * @throws AlignmentException
     */
    @Test
    public final void testDataPropertiesMatchNoLiteralMatch3() throws AlignmentException {
        double expected = 1.d;
        OWLEntity dp1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#firstName", OWLDataProperty.class, ontology1);
        OWLEntity dp2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#firstName", OWLDataProperty.class, ontology2);
        Cell dpCorr = new BasicCell("", dp1, dp2, rel, 0);
        initWithCorrespondences(dpCorr);
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#Fry", OWLNamedIndividual.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#Somebody", OWLNamedIndividual.class, ontology2);
        Cell corr = new BasicCell("", ent1, ent2, rel, 0);
        double actual = baseMatcher.getDistance(corr);
        assertEquals("Distance mismatch.", expected, actual, 0.);
    }

    /**
     * Both data properties of the two individuals match, but only for one correspondence
     * the literals match.
     * First individual has 2 data property, second individual has 2 data properties.
     * Expected: 0.5
     * @throws AlignmentException
     */
    @Test
    public final void testTwoDataPropertiesMatchOnceLiteralsMatch() throws AlignmentException {
        double expected = 0.5;
        OWLEntity dp11 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#firstName", OWLDataProperty.class, ontology1);
        OWLEntity dp12 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#firstName", OWLDataProperty.class, ontology2);
        Cell dp1Corr = new BasicCell("", dp11, dp12, rel, 0);
        OWLEntity dp21 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#name", OWLDataProperty.class, ontology1);
        OWLEntity dp22 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#surName", OWLDataProperty.class, ontology2);
        Cell dp2Corr = new BasicCell("", dp21, dp22, rel, 0);
        initWithCorrespondences(dp1Corr, dp2Corr);
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#Fry", OWLNamedIndividual.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#Jackson", OWLNamedIndividual.class, ontology2);
        Cell corr = new BasicCell("", ent1, ent2, rel, 0);
        double actual = baseMatcher.getDistance(corr);
        assertEquals("Distance mismatch.", expected, actual, 0.);
    }

    /**
     * Both data properties of the two individuals match, literals match too.
     * First individual has 2 data property, second individual has 2 data properties.
     * Expected: 0.0 (best match)
     * @throws AlignmentException
     */
    @Test
    public final void testDataPropertiesMatchLiteralsMatch3() throws AlignmentException {
        double expected = 0.d;
        OWLEntity dp11 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#firstName", OWLDataProperty.class, ontology1);
        OWLEntity dp12 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#firstName", OWLDataProperty.class, ontology2);
        Cell dp1Corr = new BasicCell("", dp11, dp12, rel, 0);
        OWLEntity dp21 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#name", OWLDataProperty.class, ontology1);
        OWLEntity dp22 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#name", OWLDataProperty.class, ontology2);
        Cell dp2Corr = new BasicCell("", dp21, dp22, rel, 0);
        initWithCorrespondences(dp1Corr, dp2Corr);
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#Fry", OWLNamedIndividual.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#Fry", OWLNamedIndividual.class, ontology2);
        Cell corr = new BasicCell("", ent1, ent2, rel, 0);
        double actual = baseMatcher.getDistance(corr);
        assertEquals("Distance mismatch.", expected, actual, 0.);
    }

    /**
     * Base matcher used with wrong correspondence type.
     * ({@link OWLClass} as correspondence objects.)
     * @throws AlignmentException
     */
    @Test(expected = InfeasibleBaseMatcherException.class)
    public final void testWrongCorrespondenceTypeOWLClass() throws AlignmentException {
        baseMatcher.init(ontology1, ontology2, new Hashtable<Object, Set<Cell>>(), new Hashtable<Object, Set<Cell>>(), new HashSet<Cell>());
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#Water", OWLClass.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#Gas", OWLClass.class, ontology2);
        Cell corr = new BasicCell("", ent1, ent2, rel, 0);
        baseMatcher.getDistance(corr);
    }

    /**
     * Base matcher used with wrong correspondence type.
     * ({@link OWLObjectProperty} as correspondence objects.)
     * @throws AlignmentException
     */
    @Test(expected = InfeasibleBaseMatcherException.class)
    public final void testWrongCorrespondenceTypeOWLObjectProperty() throws AlignmentException {
        baseMatcher.init(ontology1, ontology2, new Hashtable<Object, Set<Cell>>(), new Hashtable<Object, Set<Cell>>(), new HashSet<Cell>());
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#loves", OWLObjectProperty.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#hasAntipathyAgainst", OWLObjectProperty.class, ontology2);
        Cell corr = new BasicCell("", ent1, ent2, rel, 0);
        baseMatcher.getDistance(corr);
    }

    /**
     * Base matcher used with wrong correspondence type.
     * ({@link OWLDataProperty} as correspondence objects.)
     * @throws AlignmentException
     */
    @Test(expected = InfeasibleBaseMatcherException.class)
    public final void testWrongCorrespondenceTypeOWLDataProperty() throws AlignmentException {
        baseMatcher.init(ontology1, ontology2, new Hashtable<Object, Set<Cell>>(), new Hashtable<Object, Set<Cell>>(), new HashSet<Cell>());
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#hasMeltingPoint", OWLDataProperty.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#hasSteamingPoint", OWLDataProperty.class, ontology2);
        Cell corr = new BasicCell("", ent1, ent2, rel, 0);
        baseMatcher.getDistance(corr);
    }
}

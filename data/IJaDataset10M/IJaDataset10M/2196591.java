package de.fzi.harmonia.commons.basematcher.propertybasematcher;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import de.fzi.harmonia.commons.InfeasibleEvaluatorException;
import de.fzi.harmonia.commons.basematcher.BaseMatcherTest;
import de.fzi.harmonia.commons.basematchers.propertybasematcher.ObjPropIndividualDistanceMatcher;
import de.fzi.kadmos.api.Correspondence;
import de.fzi.kadmos.api.impl.SimpleCorrespondenceFactory;

/**
 * Unit test for the {@link ObjPropIndividualDistanceMatcher}.
 * 
 */
public class ObjPropIndividualDistanceMatcherTest extends BaseMatcherTest {

    static {
        ONTO_FILE_1 = "/ontologies/testOnto1.owl";
        ONTO_FILE_2 = "/ontologies/testOnto2.owl";
    }

    @Before
    public void setUp() throws Exception {
        baseMatcher = new ObjPropIndividualDistanceMatcher(params, id, alignment);
    }

    /**
     * No data property assertion for both individuals.
     * Expected: {@link InfeasibleEvaluatorException}
     */
    @Test(expected = InfeasibleEvaluatorException.class)
    public final void testNoDataPropertiesBothIndividuals() throws Exception {
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#Mary", OWLNamedIndividual.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#Mary", OWLNamedIndividual.class, ontology2);
        Correspondence<? extends OWLEntity> corr = SimpleCorrespondenceFactory.getInstance().createCorrespondence(ent1, ent2);
        baseMatcher.getEvaluation(corr);
    }

    /**
     * TODO What is this test doing? 
     */
    @SuppressWarnings("unchecked")
    @Test
    @Ignore
    public final void mytest() throws Exception {
        double expected = 1.d;
        OWLNamedIndividual emma1 = (OWLNamedIndividual) getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#Emma", OWLNamedIndividual.class, ontology1);
        OWLNamedIndividual emma2 = (OWLNamedIndividual) getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#Emma", OWLNamedIndividual.class, ontology2);
        Correspondence<? extends OWLEntity> corrEmma = SimpleCorrespondenceFactory.getInstance().createCorrespondence(emma1, emma2);
        OWLNamedIndividual peter1 = (OWLNamedIndividual) getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#Peter", OWLNamedIndividual.class, ontology1);
        OWLNamedIndividual peter2 = (OWLNamedIndividual) getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#Peter", OWLNamedIndividual.class, ontology2);
        Correspondence<? extends OWLEntity> corrPeter = SimpleCorrespondenceFactory.getInstance().createCorrespondence(peter1, peter2);
        initWithCorrespondences(corrEmma, corrPeter);
        OWLEntity ent1 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto1.owl#loves", OWLObjectProperty.class, ontology1);
        OWLEntity ent2 = getEntity("http://www.fzi.de/ipe/mappso/ontologies/testOnto2.owl#loves", OWLObjectProperty.class, ontology2);
        Correspondence<? extends OWLEntity> corr = SimpleCorrespondenceFactory.getInstance().createCorrespondence(ent1, ent2);
        baseMatcher.getEvaluation(corr);
        double actual = baseMatcher.getEvaluation(corr);
        assertEquals("Distance mismatch.", expected, actual, .0);
    }
}

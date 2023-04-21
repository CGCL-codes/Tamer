package de.fzi.mapevo.operators;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import de.fzi.mapevo.data.AbstractCorrespondencePermutation;
import de.fzi.mapevo.data.ClassesPermutation;
import de.fzi.mapevo.data.PermutData;

/**
 * Test for {@link MutationOperator}.
 * 
 * @author Juergen Bock (bock@fzi.de)
 *
 */
public class MutationOperatorTest {

    private static final String ontology1IRI = "http://example.org/onto1.owl";

    private static final String ontology2IRI = "http://example.org/onto2.owl";

    private static final int onto1Classes = 10;

    private static final int onto2Classes = 9;

    private static final int onto1ObjProps = 8;

    private static final int onto2ObjProps = 15;

    private static final int onto1DataProps = 8;

    private static final int onto2DataProps = 7;

    private static OWLOntology onto1;

    private static OWLOntology onto2;

    private PermutData data;

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        onto1 = manager.createOntology(IRI.create(ontology1IRI));
        onto2 = manager.createOntology(IRI.create(ontology2IRI));
        for (int i = 0; i < onto1Classes; i++) {
            OWLClass cls = manager.getOWLDataFactory().getOWLClass(IRI.create(ontology1IRI + "#C" + i));
            manager.addAxiom(onto1, manager.getOWLDataFactory().getOWLDeclarationAxiom(cls));
        }
        for (int i = 0; i < onto2Classes; i++) {
            OWLClass cls = manager.getOWLDataFactory().getOWLClass(IRI.create(ontology2IRI + "#C" + i));
            manager.addAxiom(onto2, manager.getOWLDataFactory().getOWLDeclarationAxiom(cls));
        }
        for (int i = 0; i < onto1ObjProps; i++) {
            OWLObjectProperty op = manager.getOWLDataFactory().getOWLObjectProperty(IRI.create(ontology1IRI + "#op" + i));
            manager.addAxiom(onto1, manager.getOWLDataFactory().getOWLDeclarationAxiom(op));
        }
        for (int i = 0; i < onto2ObjProps; i++) {
            OWLObjectProperty op = manager.getOWLDataFactory().getOWLObjectProperty(IRI.create(ontology2IRI + "#op" + i));
            manager.addAxiom(onto2, manager.getOWLDataFactory().getOWLDeclarationAxiom(op));
        }
        for (int i = 0; i < onto1DataProps; i++) {
            OWLDataProperty dp = manager.getOWLDataFactory().getOWLDataProperty(IRI.create(ontology1IRI + "#dp" + i));
            manager.addAxiom(onto1, manager.getOWLDataFactory().getOWLDeclarationAxiom(dp));
        }
        for (int i = 0; i < onto2DataProps; i++) {
            OWLDataProperty dp = manager.getOWLDataFactory().getOWLDataProperty(IRI.create(ontology2IRI + "#dp" + i));
            manager.addAxiom(onto2, manager.getOWLDataFactory().getOWLDeclarationAxiom(dp));
        }
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        data = new PermutData(onto1, onto2);
        AbstractCorrespondencePermutation permCls = data.getRefForClasses();
        AbstractCorrespondencePermutation permObjProps = data.getRefForObject();
        AbstractCorrespondencePermutation permDataProps = data.getRefForData();
        for (int i = 0; i < permCls.getPermutations().length; i++) if (permCls.getPermutation(i) != null) permCls.setConfidence(i, 0.d);
        for (int i = 0; i < permObjProps.getPermutations().length; i++) if (permObjProps.getPermutation(i) != null) permObjProps.setConfidence(i, 0.d);
        for (int i = 0; i < permDataProps.getPermutations().length; i++) if (permDataProps.getPermutation(i) != null) permDataProps.setConfidence(i, 0.d);
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for {@link de.fzi.mapevo.operators.MutationOperator#swapEntities()}.
     * If all confidences are set to 0, something should change here.
     * Cannot be quantified, though, so watch the output and see for yourself.
     */
    @Test
    public final void testSwapEntitiesChangeExpected() throws Exception {
        AbstractCorrespondencePermutation permCls = data.getRefForClasses();
        AbstractCorrespondencePermutation permObjProps = data.getRefForObject();
        AbstractCorrespondencePermutation permDataProps = data.getRefForData();
        for (int i = 0; i < permCls.getPermutations().length; i++) if (permCls.getPermutation(i) != null) permCls.setConfidence(i, 0.d);
        for (int i = 0; i < permObjProps.getPermutations().length; i++) if (permObjProps.getPermutation(i) != null) permObjProps.setConfidence(i, 0.d);
        for (int i = 0; i < permDataProps.getPermutations().length; i++) if (permDataProps.getPermutation(i) != null) permDataProps.setConfidence(i, 0.d);
        MutationOperator mo = new MutationOperator(data);
        final Integer[] cpermOrig = data.getRefForClasses().getPermutations().clone();
        final Integer[] opermOrig = data.getRefForObject().getPermutations().clone();
        final Integer[] dpermOrig = data.getRefForData().getPermutations().clone();
        mo.swapEntities();
        final Integer[] cpermSwapped = data.getRefForClasses().getPermutations().clone();
        final Integer[] opermSwapped = data.getRefForObject().getPermutations().clone();
        final Integer[] dpermSwapped = data.getRefForData().getPermutations().clone();
        for (int i = 0; i < cpermSwapped.length; i++) System.out.println("class index [" + i + "] original: " + cpermOrig[i] + " swapped: " + cpermSwapped[i]);
        for (int i = 0; i < opermSwapped.length; i++) System.out.println("object property index [" + i + "] original: " + opermOrig[i] + " swapped: " + opermSwapped[i]);
        for (int i = 0; i < dpermSwapped.length; i++) System.out.println("data property index [" + i + "] original: " + dpermOrig[i] + " swapped: " + dpermSwapped[i]);
    }

    /**
     * Test method for {@link de.fzi.mapevo.operators.MutationOperator#swapEntities()}.
     * If all confidences are set to 1, nothing should change here.
     */
    @Test
    public final void testSwapEntitiesNoChangeExpected() throws Exception {
        AbstractCorrespondencePermutation permCls = data.getRefForClasses();
        AbstractCorrespondencePermutation permObjProps = data.getRefForObject();
        AbstractCorrespondencePermutation permDataProps = data.getRefForData();
        for (int i = 0; i < permCls.getPermutations().length; i++) if (permCls.getPermutation(i) != null) permCls.setConfidence(i, 1.d);
        for (int i = 0; i < permObjProps.getPermutations().length; i++) if (permObjProps.getPermutation(i) != null) permObjProps.setConfidence(i, 1.d);
        for (int i = 0; i < permDataProps.getPermutations().length; i++) if (permDataProps.getPermutation(i) != null) permDataProps.setConfidence(i, 1.d);
        MutationOperator mo = new MutationOperator(data);
        final Integer[] cpermOrig = data.getRefForClasses().getPermutations().clone();
        final Integer[] opermOrig = data.getRefForObject().getPermutations().clone();
        final Integer[] dpermOrig = data.getRefForData().getPermutations().clone();
        mo.swapEntities();
        final Integer[] cpermSwapped = data.getRefForClasses().getPermutations().clone();
        final Integer[] opermSwapped = data.getRefForObject().getPermutations().clone();
        final Integer[] dpermSwapped = data.getRefForData().getPermutations().clone();
        for (int i = 0; i < cpermSwapped.length; i++) {
            System.out.println("class index [" + i + "] original: " + cpermOrig[i] + " swapped: " + cpermSwapped[i]);
            assertEquals(cpermOrig[i], cpermSwapped[i]);
        }
        for (int i = 0; i < opermSwapped.length; i++) {
            System.out.println("object property index [" + i + "] original: " + opermOrig[i] + " swapped: " + opermSwapped[i]);
            assertEquals(opermOrig[i], opermSwapped[i]);
        }
        for (int i = 0; i < dpermSwapped.length; i++) {
            System.out.println("data property index [" + i + "] original: " + dpermOrig[i] + " swapped: " + dpermSwapped[i]);
            assertEquals(dpermOrig[i], dpermSwapped[i]);
        }
    }

    /**
     * Test method for {@link de.fzi.mapevo.operators.MutationOperator#operator2nd(int, int)}.
     */
    @Test
    @Ignore
    public final void testOperator2nd() {
        fail("Not yet implemented");
    }
}

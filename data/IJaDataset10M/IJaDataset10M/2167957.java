package org.deri.iris.operations.relations;

import static org.deri.iris.factory.Factory.RELATION;
import static org.deri.iris.factory.Factory.RELATION_OPERATION;
import static org.deri.iris.factory.Factory.TERM;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.deri.iris.MiscHelper;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.operations.relation.ISelection;
import org.deri.iris.api.storage.IMixedDatatypeRelation;
import org.deri.iris.basics.seminaive.NonEqualityTerm;

/**
 * For a tuple t:
 * t = MiscHelper.createTuple(
 * 				new NonEqualityTerm("a"), 
 * 				TERM.createString("d"), 
 * 				null, 
 * 				new NonEqualityTerm("d"),
 * 				null, 
 * 				null)
 * 
 * and an array arr:
 * 
 * arr = new int[]{1, 1, 0, 0, -1, -1}
 * 			
 * we are creating a selection condition which select:
 * 
 * tuples which do not have term "a"
 * at its first position and term "d" at fourth
 * position, but have term "d" at the second position.
 * 
 * Also the array arr forces the first and second terms 
 * to be equal and fifth and sixth terms not to be equal,
 * while there is no eqality condition on third and fourth
 * terms (0 means: ignore a position on which 0 is placed).
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date   31.05.2006 11:34:30
 */
public class SelectionStringTest extends TestCase {

    static IMixedDatatypeRelation relation = null;

    public static Test suite() {
        return new TestSuite(SelectionStringTest.class, SelectionStringTest.class.getSimpleName());
    }

    private static void setRelation() {
        relation = RELATION.getMixedRelation(4);
        relation.add(MiscHelper.createTuple("x", "y", "z", "w"));
        relation.add(MiscHelper.createTuple("a", "a", "a", "a"));
        relation.add(MiscHelper.createTuple("a", "b", "a", "b"));
        relation.add(MiscHelper.createTuple("a", "b", "b", "c"));
        relation.add(MiscHelper.createTuple("d", "d", "d", "d"));
        relation.add(MiscHelper.createTuple("d", "d", "a", "e"));
        relation.add(MiscHelper.createTuple("a", "d", "d", "f"));
    }

    /**
	 * Checks out createSelectionOperator(IMixedDatatypeRelation relation, ITuple pattern);
	 * 
	 * Also see:
	 * (non-Javadoc)
	 * @see org.deri.iris.api.factory.IRelationOperationsFactory#createSelectionOperator(IMixedDatatypeRelation relation, ITuple pattern)
	 */
    protected static void runSelection_0(final ITuple p, final Collection<ITuple> e) {
        setRelation();
        ISelection selectionOperator = RELATION_OPERATION.createSelectionOperator(relation, p);
        IMixedDatatypeRelation result = selectionOperator.select();
        assertResults(result, e);
    }

    /**
	 * Checks out createSelectionOperator(IMixedDatatypeRelation relation, int[] indexes);
	 * 
	 * Also see:
	 * (non-Javadoc)
	 * @see org.deri.iris.api.factory.IRelationOperationsFactory#createSelectionOperator(IMixedDatatypeRelation relation, int[] indexes)
	 */
    protected static void runSelection_1(final int[] indexes, final Collection<ITuple> e) {
        setRelation();
        ISelection selectionOperator = RELATION_OPERATION.createSelectionOperator(relation, indexes);
        IMixedDatatypeRelation result = selectionOperator.select();
        assertResults(result, e);
    }

    /**
	 * Checks out createSelectionOperator(IMixedDatatypeRelation relation, int[] indexes);
	 * 
	 * Also see:
	 * (non-Javadoc)
	 * @see org.deri.iris.api.factory.IRelationOperationsFactory#createSelectionOperator(IMixedDatatypeRelation relation, ITuple pattern, int[] indexes)
	 */
    protected static void runSelection_2(final ITuple p, final int[] indexes, final Collection<ITuple> e) {
        setRelation();
        ISelection selectionOperator = RELATION_OPERATION.createSelectionOperator(relation, p, indexes);
        IMixedDatatypeRelation result = selectionOperator.select();
        assertResults(result, e);
    }

    public void testSelect_ddnn_0() {
        final List<ITuple> e = new ArrayList<ITuple>();
        e.add(MiscHelper.createTuple("d", "d", "d", "d"));
        e.add(MiscHelper.createTuple("d", "d", "a", "e"));
        runSelection_0(MiscHelper.createTuple("d", "d", null, null), e);
    }

    public void testSelect_anan_0() {
        final List<ITuple> e = new ArrayList<ITuple>();
        e.add(MiscHelper.createTuple("a", "a", "a", "a"));
        e.add(MiscHelper.createTuple("a", "b", "a", "b"));
        runSelection_0(MiscHelper.createTuple("a", null, "a", null), e);
    }

    public void testSelect_abab_0() {
        final List<ITuple> e = new ArrayList<ITuple>();
        e.add(MiscHelper.createTuple("a", "b", "a", "b"));
        runSelection_0(MiscHelper.createTuple("a", "b", "a", "b"), e);
    }

    public void testSelect_addf_0() {
        final List<ITuple> e = new ArrayList<ITuple>();
        e.add(MiscHelper.createTuple("a", "d", "d", "f"));
        runSelection_0(MiscHelper.createTuple("a", "d", "d", "f"), e);
    }

    /**
	 * Select tuples which do not have term "a"
	 * at its first and fourth position
	 * but have term "d" at the second position.
	 */
    public void testSelect_notEqAndEq_0() {
        final List<ITuple> e = new ArrayList<ITuple>();
        e.add(MiscHelper.createTuple("d", "d", "a", "e"));
        e.add(MiscHelper.createTuple("d", "d", "d", "d"));
        runSelection_0(MiscHelper.createTuple(new NonEqualityTerm(TERM.createString("a")), TERM.createString("d"), null, new NonEqualityTerm(TERM.createString("a"))), e);
    }

    public void testSelect_1n1n_1() {
        final List<ITuple> e = new ArrayList<ITuple>();
        e.add(MiscHelper.createTuple("a", "a", "a", "a"));
        e.add(MiscHelper.createTuple("a", "b", "a", "b"));
        e.add(MiscHelper.createTuple("d", "d", "d", "d"));
        runSelection_1(new int[] { 1, 0, 1, 0 }, e);
    }

    public void testSelect_1212_1() {
        final List<ITuple> e = new ArrayList<ITuple>();
        e.add(MiscHelper.createTuple("a", "a", "a", "a"));
        e.add(MiscHelper.createTuple("a", "b", "a", "b"));
        e.add(MiscHelper.createTuple("d", "d", "d", "d"));
        runSelection_1(new int[] { 1, 2, 1, 2 }, e);
    }

    public void testSelect_1111_1() {
        final List<ITuple> e = new ArrayList<ITuple>();
        e.add(MiscHelper.createTuple("a", "a", "a", "a"));
        e.add(MiscHelper.createTuple("d", "d", "d", "d"));
        runSelection_1(new int[] { 1, 1, 1, 1 }, e);
    }

    /**
	 * m1 - minus 1: first and fourth term need to be different  
	 * p1 - plus 1:  second and third term need to be the same (equal)
	 */
    public void testSelect_m1p1p1m1_1() {
        final List<ITuple> e = new ArrayList<ITuple>();
        e.add(MiscHelper.createTuple("a", "b", "b", "c"));
        e.add(MiscHelper.createTuple("a", "d", "d", "f"));
        runSelection_1(new int[] { -1, 1, 1, -1 }, e);
    }

    public void testSelect_m1m1m2m2_1() {
        final List<ITuple> e = new ArrayList<ITuple>();
        e.add(MiscHelper.createTuple("x", "y", "z", "w"));
        e.add(MiscHelper.createTuple("a", "b", "b", "c"));
        e.add(MiscHelper.createTuple("a", "d", "d", "f"));
        e.add(MiscHelper.createTuple("a", "b", "a", "b"));
        runSelection_1(new int[] { -1, -1, -2, -2 }, e);
    }

    public void testSelect_a101_2() {
        final List<ITuple> e = new ArrayList<ITuple>();
        e.add(MiscHelper.createTuple("a", "a", "a", "a"));
        e.add(MiscHelper.createTuple("a", "b", "a", "b"));
        runSelection_2(MiscHelper.createTuple("a", null, null, null), new int[] { 0, 1, 0, 1 }, e);
    }

    public void testSelect_d111_2() {
        final List<ITuple> e = new ArrayList<ITuple>();
        e.add(MiscHelper.createTuple("d", "d", "d", "d"));
        runSelection_2(MiscHelper.createTuple("d", null, null, null), new int[] { 0, 1, 1, 1 }, e);
    }

    /**
	 * Select tuples which do not have term "a"
	 * at its first position and term "d" at fourth
	 * position but have term "d" at the second position.
	 * Also the first and second term must be equal.
	 */
    public void testSelect_complex_0() {
        final List<ITuple> e = new ArrayList<ITuple>();
        e.add(MiscHelper.createTuple("d", "d", "a", "e"));
        runSelection_2(MiscHelper.createTuple(new NonEqualityTerm(TERM.createString("a")), TERM.createString("d"), null, new NonEqualityTerm(TERM.createString("d"))), new int[] { 1, 1, 0, 0 }, e);
    }

    /**
	 * Tests the relation against a list of tuples using the assert methods
	 * of JUnit. The length of the relation and the list must be equal, 
	 * and the relation must contain all tuples of the list.
	 * 
	 * @param r
	 *            the relation to check
	 * @param e
	 *            the Collection containing all expected tuples
	 */
    protected static void assertResults(final IMixedDatatypeRelation r, final Collection<ITuple> e) {
        Assert.assertEquals("The length of relation and the list of" + " expected tuples must be equal", e.size(), r.size());
        Assert.assertTrue("The relation must contain all expected tuples", r.containsAll(e));
    }
}

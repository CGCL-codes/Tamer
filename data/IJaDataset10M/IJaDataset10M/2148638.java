package org.deri.iris.operations.relations;

import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.CONCRETE;
import static org.deri.iris.factory.Factory.RELATION;
import static org.deri.iris.factory.Factory.TERM;
import static org.deri.iris.MiscHelper.createTuple;
import java.util.Set;
import java.util.HashSet;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.storage.IMixedDatatypeRelation;

/**
 * <p>
 * Tests for the SimpelSelection.
 * </p>
 * <p>
 * $Id: SimpleSelectionTest.java,v 1.1 2007-07-18 14:32:17 poettler_ric Exp $
 * </p>
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision: 1.1 $
 */
public class SimpleSelectionTest extends TestCase {

    private IMixedDatatypeRelation r0;

    private static final ITuple[] TUPLES = new ITuple[] { createTuple("a", "a", "a", "a"), createTuple("a", "a", "b", "b"), createTuple("b", "b", "a", "a"), createTuple("b", "a", "a", "b"), createTuple("a", "a", "c", "c"), createTuple("c", "c", "d", "d"), createTuple("c", "a", "a", "c"), createTuple("a", "b", "c", "d"), createTuple("a", "b", "b", "d"), createTuple("z", "x", "x", "a"), createTuple("z", "b", "d", "z") };

    public static Test suite() {
        return new TestSuite(SimpleSelectionTest.class, SimpleSelectionTest.class.getSimpleName());
    }

    public void setUp() {
        r0 = RELATION.getMixedRelation(4);
        r0.addAll(java.util.Arrays.asList(TUPLES));
    }

    public void testSelectOnEqualColumns0() {
        final SimpleSelection ss = new SimpleSelection(r0, new int[] { -1, 0, 0, -1 }, new ITerm[] {}, new JoinCondition[] {});
        final Set<ITuple> ref = new HashSet<ITuple>();
        ref.add(createTuple("a", "a", "a", "a"));
        ref.add(createTuple("b", "a", "a", "b"));
        ref.add(createTuple("c", "a", "a", "c"));
        ref.add(createTuple("a", "b", "b", "d"));
        ref.add(createTuple("z", "x", "x", "a"));
        runEval(ss, ref);
    }

    public void testSelectOnEqualColumns1() {
        final SimpleSelection ss = new SimpleSelection(r0, new int[] { 0, 1, 1, 0 }, new ITerm[] {}, new JoinCondition[] {});
        final Set<ITuple> ref = new HashSet<ITuple>();
        ref.add(createTuple("a", "a", "a", "a"));
        ref.add(createTuple("b", "a", "a", "b"));
        ref.add(createTuple("c", "a", "a", "c"));
        runEval(ss, ref);
    }

    public void testSelectArbitrary0() {
        final SimpleSelection ss = new SimpleSelection(r0, new int[] { -1, 0, 1, -1 }, new ITerm[] { TERM.createString("a"), TERM.createString("a") }, new JoinCondition[] { JoinCondition.EQUALS, JoinCondition.GREATER_THAN });
        final Set<ITuple> ref = new HashSet<ITuple>();
        ref.add(createTuple("a", "a", "b", "b"));
        ref.add(createTuple("a", "a", "c", "c"));
        runEval(ss, ref);
    }

    public void testSelectArbitrary1() {
        final SimpleSelection ss = new SimpleSelection(r0, new int[] { 0, 1, 2, 3 }, new ITerm[] { TERM.createString("a"), TERM.createString("b"), TERM.createString("c"), TERM.createString("d") }, new JoinCondition[] { JoinCondition.EQUALS, JoinCondition.EQUALS, JoinCondition.EQUALS, JoinCondition.EQUALS });
        final Set<ITuple> ref = new HashSet<ITuple>();
        ref.add(createTuple("a", "b", "c", "d"));
        runEval(ss, ref);
    }

    public void testSelectArbitrary2() {
        final SimpleSelection ss = new SimpleSelection(r0, new int[] { -1, 0, 1, -1 }, new ITerm[] { TERM.createString("a"), TERM.createString("a") }, new JoinCondition[] { JoinCondition.EQUALS, JoinCondition.EQUALS });
        final Set<ITuple> ref = new HashSet<ITuple>();
        ref.add(createTuple("a", "a", "a", "a"));
        ref.add(createTuple("b", "a", "a", "b"));
        ref.add(createTuple("c", "a", "a", "c"));
        runEval(ss, ref);
    }

    public void testPuttingAllTogether() {
        final SimpleSelection ss = new SimpleSelection(r0, new int[] { 2, 0, 1, 2 }, new ITerm[] { TERM.createString("a"), TERM.createString("a") }, new JoinCondition[] { JoinCondition.GREATER_THAN, JoinCondition.GREATER_THAN });
        final Set<ITuple> ref = new HashSet<ITuple>();
        ref.add(createTuple("z", "b", "d", "z"));
        runEval(ss, ref);
    }

    /**
	 * Runs the evaluation and asserts the result.
	 * @param ss the selection to evaluate
	 * @param ref the expected tuples
	 * @throws NullPointerException if the selection is <code>null</code>
	 * @throws NullPointerException if the reference set is <code>null</code>
	 */
    private void runEval(final SimpleSelection ss, final Set<ITuple> ref) {
        if (ss == null) {
            throw new NullPointerException("The selection must not be null");
        }
        if (ref == null) {
            throw new NullPointerException("The reference set must not be null");
        }
        final IMixedDatatypeRelation res = ss.evaluate();
        assertEquals("The size of the result (" + res.size() + ") doesn't match " + ref.size(), ref.size(), res.size());
        assertTrue("The result (" + res + ") doesn't contain all (" + ref + ")", res.containsAll(ref));
    }
}

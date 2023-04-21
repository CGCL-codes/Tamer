package org.deri.iris.basics;

import static org.deri.iris.factory.Factory.BASIC;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.deri.iris.MiscHelper;
import org.deri.iris.ObjectTests;
import org.deri.iris.api.basics.IAtom;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.ITuple;

/**
 * <p>
 * Tests for the literal.
 * </p>
 * <p>
 * $Id$
 * </p>
 *
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision$
 */
public class LiteralTest extends TestCase {

    private static final boolean NEGATIVE = false;

    private static final boolean POSITIVE = true;

    private static final int ARITY = 3;

    private static final String SYMBOL = "date";

    private static final ITuple TUPLE = MiscHelper.createTuple("a", "b", "c");

    private static final ITuple TUPLEMORE = MiscHelper.createTuple("a", "b", "d");

    private static final IPredicate PREDICATE = new Predicate(SYMBOL, ARITY);

    private static final IPredicate PREDICATEMORE = new Predicate(SYMBOL + 1, ARITY);

    private static final IAtom ATOM = BASIC.createAtom(PREDICATE, BASIC.createTuple(TUPLE));

    public static Test suite() {
        return new TestSuite(LiteralTest.class, LiteralTest.class.getSimpleName());
    }

    public void testIsPositive() {
        final ILiteral pos = new Literal(POSITIVE, ATOM);
        assertTrue("The literal should be positive", pos.isPositive());
        final ILiteral neg = new Literal(NEGATIVE, ATOM);
        assertFalse("The literal should be negative", neg.isPositive());
    }

    public void testGetAtom() {
        final ILiteral lit = new Literal(POSITIVE, ATOM);
        assertEquals("The getAtom method doesn't work properly", ATOM, lit.getAtom());
    }

    public void testEquals() {
        ObjectTests.runTestEquals(new Literal(NEGATIVE, BASIC.createAtom(PREDICATE, TUPLE)), new Literal(NEGATIVE, BASIC.createAtom(PREDICATE, TUPLE)), new Literal(BASIC.createAtom(PREDICATE, TUPLEMORE)));
        ObjectTests.runTestEquals(new Literal(NEGATIVE, BASIC.createAtom(PREDICATE, TUPLE)), new Literal(NEGATIVE, BASIC.createAtom(PREDICATE, TUPLE)), new Literal(!NEGATIVE, BASIC.createAtom(PREDICATE, TUPLE)));
    }

    public void testHashCode() {
        ObjectTests.runTestHashCode(new Literal(NEGATIVE, BASIC.createAtom(PREDICATE, TUPLE)), new Literal(NEGATIVE, BASIC.createAtom(PREDICATE, TUPLE)));
    }

    public void testCompareTo() {
        ObjectTests.runTestCompareTo(new Literal(BASIC.createAtom(PREDICATE, TUPLE)), new Literal(BASIC.createAtom(PREDICATE, TUPLE)), new Literal(BASIC.createAtom(PREDICATE, TUPLEMORE)), new Literal(BASIC.createAtom(PREDICATEMORE, TUPLE)));
        ObjectTests.runTestCompareTo(new Literal(NEGATIVE, BASIC.createAtom(PREDICATE, TUPLE)), new Literal(NEGATIVE, BASIC.createAtom(PREDICATE, TUPLE)), new Literal(NEGATIVE, BASIC.createAtom(PREDICATE, TUPLEMORE)), new Literal(BASIC.createAtom(PREDICATE, TUPLE)));
    }
}

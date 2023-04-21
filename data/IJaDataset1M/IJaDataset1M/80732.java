package org.deri.iris.basics;

import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.CONCRETE;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.deri.iris.MiscHelper;
import org.deri.iris.ObjectTests;
import org.deri.iris.api.basics.ILiteral;

/**
 * <p>
 * Tests for the query.
 * </p>
 * <p>
 * $Id$
 * </p>
 *
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision$
 */
public class QueryTest extends TestCase {

    private static final List<ILiteral> BODY;

    private static final List<ILiteral> BODYMORE;

    static {
        BODY = new ArrayList<ILiteral>();
        ILiteral literal = BASIC.createLiteral(true, BASIC.createAtom(BASIC.createPredicate("sin", 1), BASIC.createTuple(CONCRETE.createInteger(1))));
        BODY.add(literal);
        BODY.add(MiscHelper.createLiteral("cos", "X"));
        BODY.add(MiscHelper.createLiteral("date", "J", "K", "L"));
        BODYMORE = new ArrayList<ILiteral>(BODY);
        BODYMORE.set(2, MiscHelper.createLiteral("date", "J", "K", "M"));
    }

    public static Test suite() {
        return new TestSuite(QueryTest.class, QueryTest.class.getSimpleName());
    }

    public void testEquals() {
        ObjectTests.runTestEquals(new Query(BODY), new Query(BODY), new Query(BODYMORE));
    }

    public void testHashCode() {
        ObjectTests.runTestHashCode(new Query(BODY), new Query(BODY));
    }
}

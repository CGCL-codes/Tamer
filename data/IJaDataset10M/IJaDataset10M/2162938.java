package org.deri.iris.builtins.list;

import org.deri.iris.EvaluationException;
import org.deri.iris.api.terms.concrete.IList;
import org.deri.iris.terms.concrete.IntTerm;

public class SubListFromToBuiltinTest extends AbstractListBuiltinTest {

    private SubListFromToBuiltin builtin;

    private IList list_1, list_2, expected;

    public void testBuiltin() throws EvaluationException {
        try {
            builtin = new SubListFromToBuiltin();
            System.out.println(builtin.toString());
            fail("An IllegalArgumentException should be thrown if built-in has the wrong amount of paramenters.");
        } catch (IllegalArgumentException e) {
        }
        builtin = new SubListFromToBuiltin(EMPTY_LIST, new IntTerm(0), new IntTerm(0));
        list_1 = new org.deri.iris.terms.concrete.List();
        list_2 = new org.deri.iris.terms.concrete.List();
        expected = new org.deri.iris.terms.concrete.List();
        assertEquals(expected, builtin.computeResult(list_1, ZERO, ZERO));
        list_1.add(ZERO);
        list_1.add(ONE);
        list_1.add(TWO);
        list_1.add(THREE);
        list_1.add(FOUR);
        assertEquals(expected, builtin.computeResult(list_1, ZERO, ZERO));
        list_2.clear();
        list_2.add(ZERO);
        assertEquals(list_2, builtin.computeResult(list_1, ZERO, ONE));
        expected.clear();
        expected.add(ZERO);
        expected.add(ONE);
        expected.add(TWO);
        expected.add(THREE);
        assertEquals(expected, builtin.computeResult(list_1, ZERO, FOUR));
        expected.add(FOUR);
        assertEquals(expected, builtin.computeResult(list_1, ZERO, new IntTerm(10)));
        expected.clear();
        expected.add(ZERO);
        expected.add(ONE);
        expected.add(TWO);
        assertEquals(expected, builtin.computeResult(list_1, ZERO, new IntTerm(-2)));
        expected.clear();
        expected.add(TWO);
        expected.add(THREE);
        assertEquals(expected, builtin.computeResult(list_1, TWO, FOUR));
        expected.clear();
        expected.add(TWO);
        assertEquals(expected, builtin.computeResult(list_1, TWO, new IntTerm(-2)));
    }
}

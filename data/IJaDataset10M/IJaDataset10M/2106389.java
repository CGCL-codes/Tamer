package net.sf.jga.fn.comparison;

import net.sf.jga.fn.AbstractVisitor;
import net.sf.jga.fn.BinaryFunctor;
import net.sf.jga.fn.FunctorTest;
import net.sf.jga.fn.UnaryFunctor;

/**
 * Exercises EqualEqual predicate
 *
 *
 * Created: Sun Apr 14 01:37:38 2002
 *
 * <p>Copyright &copy; 2002-2005  David A. Hall
 *
 * @author <a href="mailto:davidahall@users.sourceforge.net">David A. Hall</a>
 */
public class TestEqualEqual extends FunctorTest<EqualEqual<String>> {

    public TestEqualEqual(String name) {
        super(name);
    }

    private static final String ONE = "ONE";

    private static final String TWO = "TWO";

    private String TOO;

    public void setUp() {
        TOO = "W";
        TOO = "T" + TOO;
        TOO += "O";
    }

    public void testFunctorInterface() {
        EqualEqual<String> pred = makeSerial(new EqualEqual<String>());
        assertEquals(Boolean.TRUE, pred.fn(ONE, ONE));
        assertEquals(Boolean.FALSE, pred.fn(ONE, TWO));
        assertEquals(Boolean.FALSE, pred.fn(TWO, ONE));
        assertEquals(Boolean.FALSE, pred.fn(TWO, null));
        assertEquals(Boolean.FALSE, pred.fn(null, TWO));
        assertEquals(Boolean.FALSE, pred.fn(TWO, TOO));
        assertEquals(Boolean.TRUE, pred.fn(null, null));
    }

    public void testStaticBuilders() {
        BinaryFunctor<String, String, Boolean> pred = ComparisonFunctors.equalEqual();
        assertEquals(Boolean.TRUE, pred.fn(ONE, ONE));
        assertEquals(Boolean.FALSE, pred.fn(ONE, TWO));
        assertEquals(Boolean.FALSE, pred.fn(TWO, ONE));
        assertEquals(Boolean.FALSE, pred.fn(TWO, null));
        assertEquals(Boolean.FALSE, pred.fn(null, TWO));
        assertEquals(Boolean.FALSE, pred.fn(TWO, TOO));
        assertEquals(Boolean.TRUE, pred.fn(null, null));
        UnaryFunctor<String, Boolean> eqTwo = ComparisonFunctors.equalEqual(TWO);
        assertEquals(Boolean.FALSE, eqTwo.fn(ONE));
        assertEquals(Boolean.TRUE, eqTwo.fn(TWO));
        assertEquals(Boolean.FALSE, eqTwo.fn(TOO));
    }

    public void testVisitableInterface() {
        EqualEqual<String> pred = new EqualEqual<String>();
        TestVisitor tv = new TestVisitor();
        pred.accept(tv);
        assertEquals(pred, tv.host);
    }

    private class TestVisitor extends AbstractVisitor implements EqualEqual.Visitor {

        public Object host;

        public void visit(EqualEqual<?> host) {
            this.host = host;
        }
    }

    public static void main(String[] args) {
        junit.swingui.TestRunner.run(TestEqualEqual.class);
    }
}

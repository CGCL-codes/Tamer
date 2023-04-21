package net.sf.jga.fn.comparison;

import java.util.Comparator;
import java.util.Date;
import net.sf.jga.SampleStringComparator;
import net.sf.jga.fn.AbstractVisitor;
import net.sf.jga.fn.BinaryFunctor;
import net.sf.jga.fn.FunctorTest;
import net.sf.jga.fn.UnaryFunctor;
import net.sf.jga.util.NullComparator;

/**
 * Exercises NotNotEqualTo predicate
 * <p>
 * Copyright &copy; 2002-2005  David A. Hall
 *
 * @author <a href="mailto:davidahall@users.sourceforge.net">David A. Hall</a>
 */
public class TestNotEqualTo extends FunctorTest<NotEqualTo<Boolean>> {

    public TestNotEqualTo(String name) {
        super(name);
    }

    public void testFunctorInterface() {
        NotEqualTo<Boolean> pred = makeSerial(new NotEqualTo<Boolean>());
        assertEquals(Boolean.FALSE, pred.fn(Boolean.TRUE, Boolean.TRUE));
        assertEquals(Boolean.TRUE, pred.fn(Boolean.TRUE, Boolean.FALSE));
        assertEquals(Boolean.TRUE, pred.fn(Boolean.FALSE, Boolean.TRUE));
        assertEquals(Boolean.TRUE, pred.fn(Boolean.FALSE, (Boolean) null));
        assertEquals(Boolean.TRUE, pred.fn((Boolean) null, Boolean.FALSE));
        assertEquals(Boolean.FALSE, pred.fn((Boolean) null, (Boolean) null));
    }

    public void testStaticBuilders() {
        BinaryFunctor<Boolean, Boolean, Boolean> boolFn = ComparisonFunctors.notEqualTo(Boolean.class);
        assertEquals(Boolean.FALSE, boolFn.fn(Boolean.TRUE, Boolean.TRUE));
        assertEquals(Boolean.TRUE, boolFn.fn(Boolean.TRUE, Boolean.FALSE));
        assertEquals(Boolean.TRUE, boolFn.fn(Boolean.FALSE, (Boolean) null));
        assertEquals(Boolean.TRUE, boolFn.fn((Boolean) null, Boolean.FALSE));
        assertEquals(Boolean.FALSE, boolFn.fn((Boolean) null, (Boolean) null));
        Date epoch = new Date(0L);
        UnaryFunctor<Date, Boolean> dateFn = ComparisonFunctors.notEqualTo(epoch);
        assertEquals(Boolean.TRUE, dateFn.fn(new Date()));
        assertEquals(Boolean.FALSE, dateFn.fn(epoch));
        assertEquals(Boolean.TRUE, dateFn.fn(null));
        dateFn = ComparisonFunctors.notEqualTo((Date) null);
        assertEquals(Boolean.TRUE, dateFn.fn(new Date()));
        assertEquals(Boolean.TRUE, dateFn.fn(epoch));
        assertEquals(Boolean.FALSE, dateFn.fn(null));
        Comparator<String> comp = new NullComparator<String>(new SampleStringComparator());
        BinaryFunctor<String, String, Boolean> strFn = ComparisonFunctors.notEqualTo(comp);
        assertEquals(Boolean.FALSE, strFn.fn("foo", "foo"));
        assertEquals(Boolean.TRUE, strFn.fn("foo", "bar"));
        assertEquals(Boolean.TRUE, strFn.fn("foo", (String) null));
        assertEquals(Boolean.TRUE, strFn.fn((String) null, "foo"));
        assertEquals(Boolean.FALSE, strFn.fn((String) null, (String) null));
        UnaryFunctor<String, Boolean> eqFoo = ComparisonFunctors.notEqualTo(comp, "foo");
        assertEquals(Boolean.FALSE, eqFoo.fn("foo"));
        assertEquals(Boolean.TRUE, eqFoo.fn("bar"));
        assertEquals(Boolean.TRUE, eqFoo.fn((String) null));
    }

    public void testVisitableInterface() {
        NotEqualTo<Boolean> pred = new NotEqualTo<Boolean>();
        TestVisitor tv = new TestVisitor();
        pred.accept(tv);
        assertEquals(pred, tv.host);
    }

    private class TestVisitor extends AbstractVisitor implements NotEqualTo.Visitor {

        public Object host;

        public void visit(NotEqualTo<?> host) {
            this.host = host;
        }
    }

    public static void main(String[] args) {
        junit.swingui.TestRunner.run(TestNotEqualTo.class);
    }
}

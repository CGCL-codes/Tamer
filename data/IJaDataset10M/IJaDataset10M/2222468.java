package net.sf.jga.fn.adaptor;

import net.sf.jga.SampleUnaryFunctor;
import net.sf.jga.fn.AbstractVisitor;
import net.sf.jga.fn.FunctorTest;
import net.sf.jga.fn.UnaryFunctor;

/**
 * Exercises ApplyUnary functor
 * <p>
 * Copyright &copy; 2002-2005  David A. Hall
 *
 * @author <a href="mailto:davidahall@users.sourceforge.net">David A. Hall</a>
 */
public class TestApplyUnary extends FunctorTest<ApplyUnary<String>> {

    public TestApplyUnary(String name) {
        super(name);
    }

    public static final String FOO = "_foo_";

    public static final String BAR = "_bar_";

    public static final String BAZ = "_baz_";

    UnaryFunctor<String, String> bf1 = new SampleUnaryFunctor<String, String>(FOO, FOO);

    UnaryFunctor<String, String> bf2 = new SampleUnaryFunctor<String, String>(FOO, BAR);

    UnaryFunctor<String, String> bf3 = new SampleUnaryFunctor<String, String>(FOO, BAZ);

    @SuppressWarnings("unchecked")
    UnaryFunctor<String, String>[] functors = new UnaryFunctor[] { bf1, bf2, bf3 };

    private ApplyUnary<String> makeApply = makeSerial(new ApplyUnary<String>(functors));

    public void testFunctorInterface() {
        Object[] result = makeApply.fn(FOO);
        assertEquals(FOO, result[0]);
        assertEquals(BAR, result[1]);
        assertEquals(BAZ, result[2]);
        assertEquals(3, result.length);
    }

    public void testEmptyArgList() {
        @SuppressWarnings("unchecked") UnaryFunctor<String, ?>[] noFns = new UnaryFunctor[] {};
        ApplyUnary<String> fn = new ApplyUnary<String>(noFns);
        Object[] result = fn.fn(FOO);
        assertEquals(0, result.length);
    }

    public void testVisitableInterface() {
        TestVisitor tv = new TestVisitor();
        makeApply.accept(tv);
        assertEquals(makeApply, tv.host);
    }

    private class TestVisitor extends AbstractVisitor implements ApplyUnary.Visitor {

        public Object host;

        public void visit(ApplyUnary<?> host) {
            this.host = host;
        }
    }

    public static void main(String[] args) {
        junit.swingui.TestRunner.run(TestApplyUnary.class);
    }
}

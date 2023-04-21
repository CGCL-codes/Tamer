package net.sf.jga.fn.adaptor;

import junit.framework.AssertionFailedError;
import net.sf.jga.SampleUnaryFunctor;
import net.sf.jga.fn.AbstractVisitor;
import net.sf.jga.fn.FunctorTest;

/**
 * Exercises Generate
 * <p>
 * Copyright &copy; 2003-2005  David A. Hall
 *
 * @author <a href="mailto:davidahall@users.sourceforge.net">David A. Hall</a>
 */
public class TestGenerate extends FunctorTest<Generate<String, Integer>> {

    public static final String FOO = "_foo_";

    public static final Integer ONE = new Integer(1);

    public TestGenerate(String name) {
        super(name);
    }

    private SampleUnaryFunctor<String, Integer> uf = new SampleUnaryFunctor<String, Integer>(FOO, ONE);

    private Constant<String> foogen = new Constant<String>(FOO);

    private Generate<String, Integer> func = new Generate<String, Integer>(uf, foogen);

    public void testFunctorInterface() {
        assertEquals(ONE, func.fn());
        assertEquals(FOO, uf._got);
    }

    public void testSerialization() {
        Generate<String, Integer> fn = makeSerial(func);
        assertEquals(ONE, fn.fn());
        try {
            assertEquals(ONE, fn.fn());
            fail("This should fail: the inner exception is expecting TRUE");
        } catch (AssertionFailedError x) {
        }
    }

    public void testVisitableInterface() {
        TestVisitor tv = new TestVisitor();
        func.accept(tv);
        assertEquals(func, tv.host);
    }

    private class TestVisitor extends AbstractVisitor implements Generate.Visitor {

        public Object host;

        public void visit(Generate<?, ?> host) {
            this.host = host;
        }
    }

    public static void main(String[] args) {
        junit.swingui.TestRunner.run(TestGenerate.class);
    }
}

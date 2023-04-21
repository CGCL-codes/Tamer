package org.enerj.jga.fn.adaptor;

import org.enerj.jga.SampleObject;
import org.enerj.jga.Samples;
import org.enerj.jga.fn.AbstractVisitor;
import org.enerj.jga.fn.FunctorTest;

public class TestConstantBinary extends FunctorTest<ConstantBinary<Object, Boolean, String>> {

    public static final String FOO = "_foo_";

    public TestConstantBinary(String name) {
        super(name);
    }

    private ConstantBinary<Object, Boolean, String> func = makeSerial(new ConstantBinary<Object, Boolean, String>(FOO));

    public void testFunctorInterface() {
        assertEquals(FOO, func.fn(null, null));
        String testString = "_TEST_STRING_";
        assertEquals(FOO, func.fn(testString, Boolean.TRUE));
    }

    public void testVisitableInterface() {
        TestVisitor tv = new TestVisitor();
        func.accept(tv);
        assertEquals(func, tv.host);
    }

    private class TestVisitor extends AbstractVisitor implements ConstantBinary.Visitor {

        public Object host;

        public void visit(ConstantBinary host) {
            this.host = host;
        }
    }

    public static void main(String[] args) {
        junit.swingui.TestRunner.run(TestConstantBinary.class);
    }
}

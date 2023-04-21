package net.sf.jga.fn.arithmetic;

import java.math.BigInteger;
import net.sf.jga.fn.AbstractVisitor;
import net.sf.jga.fn.BinaryFunctor;
import net.sf.jga.fn.FunctorTest;
import net.sf.jga.fn.UnaryFunctor;

/**
 * Exercises BitwiseAnd
 * <p>
 * Copyright &copy; 2003-2005  David A. Hall
 *
 * @author <a href="mailto:davidahall@users.sourceforge.net">David A. Hall</a>
 */
public class TestBitwiseAnd extends FunctorTest<BitwiseAnd<Long>> {

    public TestBitwiseAnd(String name) {
        super(name);
    }

    public void testFunctorInterface() {
        byte b0 = 0x11, b1 = 0x55, b2 = 0x33;
        BitwiseAnd<Byte> bfn = new BitwiseAnd<Byte>(Byte.class);
        assertEquals(new Byte(b0), bfn.fn(new Byte(b1), new Byte(b2)));
        short s0 = 0x111, s1 = 0x555, s2 = 0x333;
        BitwiseAnd<Short> sfn = new BitwiseAnd<Short>(Short.class);
        assertEquals(new Short(s0), sfn.fn(new Short(s1), new Short(s2)));
        BitwiseAnd<Integer> ifn = new BitwiseAnd<Integer>(Integer.class);
        assertEquals(new Integer(0x3333), ifn.fn(new Integer(0xFFFF), new Integer(0x3333)));
        BitwiseAnd<Long> lfn = new BitwiseAnd<Long>(Long.class);
        assertEquals(new Long(0x66666666L), lfn.fn(new Long(0xEEEEEEEEL), new Long(0x76767676L)));
        BitwiseAnd<BigInteger> bifn = new BitwiseAnd<BigInteger>(BigInteger.class);
        assertEquals(new BigInteger("456"), bifn.fn(new BigInteger("65535"), new BigInteger("456")));
    }

    public void testSerialization() {
        BitwiseAnd<Long> lfn = makeSerial(new BitwiseAnd<Long>(Long.class));
        assertEquals(new Long(0x66666666L), lfn.fn(new Long(0xEEEEEEEEL), new Long(0x76767676L)));
    }

    public void testStaticBuilder() {
        BinaryFunctor<Long, Long, Long> lfn = ArithmeticFunctors.bitwiseAnd(Long.class);
        assertEquals(new Long(0x66666666L), lfn.fn(new Long(0xEEEEEEEEL), new Long(0x76767676L)));
        UnaryFunctor<Long, Long> lastEven = ArithmeticFunctors.bitwiseAnd(new Long(0xFFFFFFFEL));
        assertEquals(new Long(28L), lastEven.fn(new Long(29L)));
    }

    public void testVisitableInterface() {
        BitwiseAnd<Integer> ifn = new BitwiseAnd<Integer>(Integer.class);
        TestVisitor tv = new TestVisitor();
        ifn.accept(tv);
        assertEquals(ifn, tv.host);
    }

    private class TestVisitor extends AbstractVisitor implements BitwiseAnd.Visitor {

        public Object host;

        public void visit(BitwiseAnd<?> host) {
            this.host = host;
        }
    }

    public static void main(String[] args) {
        junit.swingui.TestRunner.run(TestBitwiseAnd.class);
    }
}

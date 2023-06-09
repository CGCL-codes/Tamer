package org.jlinalg.demo;

import org.jlinalg.LinAlgFactory;
import org.jlinalg.Matrix;
import org.jlinalg.f2.F2;

/**
 * Demonstrate the use of the F2 data type
 * 
 * @author Georg Thimm
 */
public class F2Demo {

    /**
	 * @param args
	 *            is not used.
	 */
    public static void main(String[] args) {
        example1();
        example2();
    }

    /**
	 * show how to obtain instances from F2 and how to operate on them.
	 */
    static void example1() {
        System.out.println("=========== Example 1 ============");
        F2 a = F2.FACTORY.get(1);
        F2 b = F2.FACTORY.get(0);
        F2 c = F2.FACTORY.get(711);
        System.out.println("The integer 1 becomes " + a.toString());
        System.out.println("The integer 0 becomes " + b.toString());
        System.out.println("The integer 711 becomes " + c.toString());
        System.out.println(a + "+" + b + "=" + a.add(b));
        System.out.println(a + "+" + a + "=" + a.add(a));
        System.out.println(a + "*" + a + "=" + a.multiply(a));
        System.out.println("inverse(" + a + ")=" + a.invert());
    }

    /**
	 * Create matrices and operate on them.
	 */
    static void example2() {
        System.out.println("=========== Example 2 ============");
        LinAlgFactory<F2> f2LinAlgFactory = new LinAlgFactory<F2>(F2.FACTORY);
        Matrix<F2> m1 = f2LinAlgFactory.identity(3);
        System.out.println("m1=\n" + m1);
        Matrix<F2> m2 = new Matrix<F2>(3, 3, F2.FACTORY);
        m2.setAll(F2.FACTORY.one());
        System.out.println("m2=\n" + m2);
        System.out.println("set m2 = m2-m1:\nm2=");
        m2.subtractReplace(m1);
        System.out.println(m2);
        System.out.println("det(m2)=" + m2.det() + "  yeah, this is correct ;-)");
        Matrix<F2> m3 = m2.copy();
        System.out.println("set m3[1,3] = 0");
        m3.set(1, 3, F2.FACTORY.zero());
        System.out.println(" m3=");
        System.out.println(m3);
        System.out.println("det(m3)=" + m3.det());
    }
}

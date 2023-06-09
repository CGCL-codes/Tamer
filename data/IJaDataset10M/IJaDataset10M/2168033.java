package org.csiro.darjeeling.test.tests;

import org.csiro.darjeeling.test.classes.A;
import org.csiro.darjeeling.test.classes.AInterface;
import org.csiro.darjeeling.test.classes.B;
import org.csiro.darjeeling.test.classes.C;
import org.csiro.darjeeling.test.classes.D;
import org.csiro.darjeeling.test.classes.E;
import darjeeling.system.Darjeeling;

public class InvokeVirtualTest {

    public static void constructorTest(int testBase) {
        A a = new A();
        Darjeeling.assertTrue(testBase + 0, a.getX() == 2);
        Darjeeling.assertTrue(testBase + 1, a.getY() == 3);
        Darjeeling.assertTrue(testBase + 2, a.getSquaredLength() == 2 * 2 + 3 * 3);
        a = new A(5, -2);
        Darjeeling.assertTrue(testBase + 3, a.getX() == 5);
        Darjeeling.assertTrue(testBase + 4, a.getY() == -2);
        Darjeeling.assertTrue(testBase + 5, a.getSquaredLength() == 5 * 5 + -2 * -2);
    }

    public static void inheritanceTest(int testBase) {
        A a, b, c, d;
        E e;
        a = new A();
        b = new B();
        c = new C();
        d = new D();
        e = new E();
        Darjeeling.assertTrue(testBase + 0, a.virtualMethod() == 0);
        Darjeeling.assertTrue(testBase + 1, b.virtualMethod() == 1);
        Darjeeling.assertTrue(testBase + 2, c.virtualMethod() == 2);
        Darjeeling.assertTrue(testBase + 3, d.virtualMethod() == 3);
        Darjeeling.assertTrue(testBase + 4, a.AInterfaceMethod() == 0);
        Darjeeling.assertTrue(testBase + 5, b.AInterfaceMethod() == 0);
        Darjeeling.assertTrue(testBase + 6, c.AInterfaceMethod() == 0);
        Darjeeling.assertTrue(testBase + 7, d.AInterfaceMethod() == 0);
        Darjeeling.assertTrue(testBase + 8, e.AInterfaceMethod() == 1);
        AInterface aa, bb, cc, dd, ee;
        aa = a;
        bb = b;
        cc = c;
        dd = d;
        ee = e;
        Darjeeling.assertTrue(testBase + 9, aa.AInterfaceMethod() == 0);
        Darjeeling.assertTrue(testBase + 10, bb.AInterfaceMethod() == 0);
        Darjeeling.assertTrue(testBase + 11, cc.AInterfaceMethod() == 0);
        Darjeeling.assertTrue(testBase + 12, dd.AInterfaceMethod() == 0);
        Darjeeling.assertTrue(testBase + 13, ee.AInterfaceMethod() == 1);
    }

    public static void test(int testBase) {
        constructorTest(testBase + 00);
        inheritanceTest(testBase + 20);
    }
}

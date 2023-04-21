package org.jmlspecs.ajmlrac.testcase.racrun;

import junit.framework.TestCase;
import junit.framework.TestSuite;

public class Desugaring4 extends TestCase {

    public void m1() {
        throw new NullPointerException();
    }

    public void m2() {
        throw new NullPointerException();
    }

    public void m3() throws NullPointerException {
        throw new NullPointerException();
    }

    public void m4() throws NullPointerException {
        throw new NullPointerException();
    }

    public void m5() throws NullPointerException {
        throw new NullPointerException();
    }

    public void m6() throws NullPointerException {
        throw new NullPointerException();
    }

    public void testM1() {
        Desugaring4 d4 = new Desugaring4();
        d4.m1();
    }

    public void testM2() {
        Desugaring4 d4 = new Desugaring4();
        d4.m2();
    }

    public void testM3() {
        Desugaring4 d4 = new Desugaring4();
        d4.m3();
    }

    public void testM4() {
        Desugaring4 d4 = new Desugaring4();
        d4.m4();
    }

    public void testM5() {
        Desugaring4 d4 = new Desugaring4();
        d4.m5();
    }

    public void testM6() {
        Desugaring4 d4 = new Desugaring4();
        d4.m6();
    }

    public static void main(String args[]) {
        junit.textui.TestRunner.run(new TestSuite(Desugaring4.class));
    }
}

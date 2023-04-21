package com.enerjy.analyzer.java.rules.testfiles.T0250;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Generic test case for JUnit rules.
 */
public class PTest01 {

    int x = 1;

    public void setX(int i) {
        this.x = i;
    }

    public int getX() {
        return x;
    }

    int y = 1;

    public void setY(int i) {
        this.y = i;
    }

    public int getY() {
        return y;
    }

    public PTest01() {
        this(1, 1);
    }

    public PTest01(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int area() {
        return x * y;
    }
}

class PTest01Test extends TestCase {

    PTest01 ptest = null;

    PTest01Test() {
        this("DefaultName");
    }

    PTest01Test(String str) {
        super(str);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new PTest01Test("testAdd"));
        return suite;
    }

    public void setUp() throws Exception {
        super.setUp();
        ptest = new PTest01();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testArea() {
        ptest.setX(5);
        ptest.setY(5);
        int area = ptest.area();
        assertEquals("Invalid area() computation", 25, area);
    }
}

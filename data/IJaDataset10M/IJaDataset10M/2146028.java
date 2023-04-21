package com.enerjy.analyzer.java.rules.testfiles.T0246;

import junit.framework.TestCase;

/**
 * Missing message on jUnit assertion
 */
public class FTest09 extends TestCase {

    public void testArea() {
        Object g = new Object(), h = new Object();
        assertEquals(g, h);
    }
}

package com.enerjy.analyzer.java.rules.testfiles.T0246;

import junit.framework.TestCase;

/**
 * Missing message on jUnit assertion
 */
public class FTest16 extends TestCase {

    public void testArea() {
        boolean a = true;
        assertTrue(a);
    }
}

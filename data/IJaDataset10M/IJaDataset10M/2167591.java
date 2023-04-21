package com.enerjy.analyzer.java.rules.testfiles.T0246;

import junit.framework.TestCase;

/**
 * Missing message on jUnit assertion
 */
public class FTest06 extends TestCase {

    public void testArea() {
        assertEquals(0f, 1f, 0.1f);
    }
}

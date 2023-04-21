package com.hp.hpl.jena.reasoner.dig.test;

import junit.framework.*;

/**
 * <p>
 * Collected unit tests for DIG adapter
 * </p>
 *
 * @author Ian Dickinson, HP Labs
 *         (<a  href="mailto:Ian.Dickinson@hp.com" >email</a>)
 * @version CVS $Id: TestPackage.java,v 1.6 2006/03/22 13:53:31 andy_seaborne Exp $
 */
public class TestPackage extends TestSuite {

    public static TestSuite suite() {
        return new TestPackage();
    }

    /** Creates new TestPackage */
    private TestPackage() {
        super("reasoner.dig");
        addTestSuite(TestConsistency.class);
        addTestSuite(TestPellet.class);
        addTest("TestDigReasoner", TestDigReasoner.suite());
    }

    private void addTest(String name, TestSuite tc) {
        tc.setName(name);
        addTest(tc);
    }
}

package org.mortbay.http.handler;

import junit.framework.TestSuite;

/** Util meta JUnitTestHarness.
 * @version $Id: Tests.java,v 1.6 2004/05/09 20:33:31 gregwilkins Exp $
 * @author Juancarlo A�ez <juancarlo@modelistica.com>
 * @author Brett Sealey
 */
public class Tests extends junit.framework.TestCase {

    /** Create the named test case.
     * @param name The name of the test case.
     */
    public Tests(String name) {
        super(name);
    }

    /** Get the Test suite for the org.mortbay.http.handler package.
     * @return A TestSuite for this package.
     */
    public static junit.framework.Test suite() {
        TestSuite testSuite = new TestSuite(Tests.class);
        testSuite.addTest(TestSetResponseHeadersHandler.suite());
        return testSuite;
    }

    /** main.
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public void testPlaceHolder() {
        assertTrue(true);
    }
}

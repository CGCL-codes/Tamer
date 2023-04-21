package uk.ac.ebi.intact.application.dataConversion.psiDownload.xmlGenerator.util.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Testsuite that is composed of the individual JUnit test suites. Any new test suite should be added here.
 *
 * @author Samuel Kerrien (skerrien@ebi.ac.uk)
 * @version $Id: AllJUnitTests.java 4385 2005-10-24 17:40:30Z skerrien $
 */
public class AllJUnitTests extends TestCase {

    /**
     * The constructor with the test name.
     *
     * @param name the name of the test.
     */
    public AllJUnitTests(final String name) {
        super(name);
    }

    /**
     * Returns a suite containing tests.
     *
     * @return a suite containing tests.
     *         <p/>
     *         <pre>
     *         <p/>
     *                               post: return != null
     *         <p/>
     *                               post: return->forall(obj : Object | obj.oclIsTypeOf(TestSuite))
     *         <p/>
     *                               </pre>
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTest(ToolBoxTest.suite());
        return suite;
    }
}

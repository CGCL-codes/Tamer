package uk.ac.ebi.intact.application.editor.struts.view.feature.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Contains all JUnit Tests class for the
 * uk.ac.ebi.intact.application.editor.struts.view.feature package.
 *
 * @author Sugath Mudali (smudali@ebi.ac.uk)
 * @version $Id: AllJUnitTests.java 3684 2005-02-08 17:09:51Z smudali $
 */
public class AllJUnitTests extends TestCase {

    /**
     * Constructs an AllJUnitTests instance with the specified name.
     *
     * @param name the name of the test.
     */
    public AllJUnitTests(String name) {
        super(name);
    }

    /**
     * Returns a suite containing tests.
     *
     * </br><b>OCL:</b>
     * <pre>
     * post: return != null
     * post: return->forall(obj : Object | obj.oclIsTypeOf(Test))
     * </pre>
     */
    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(RangeBeanTest.suite());
        suite.addTest(FeatureActionFormTest.suite());
        return suite;
    }
}

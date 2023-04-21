package uk.ac.ebi.intact.persistence.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * A template for AllJUnitTests class.
 *
 * @author Sugath Mudali (smudali@ebi.ac.uk)
 * @version $Id: AllJUnitTests.java 4173 2005-07-15 12:11:54Z smudali $
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
     * <p/>
     * </br><b>OCL:</b>
     * <pre>
     * post: return != null
     * post: return->forall(obj : Object | obj.oclIsTypeOf(Test))
     * </pre>
     */
    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(AnnotatedObjectTest.suite());
        suite.addTest(NucleicAcidTest.suite());
        suite.addTest(ProteinTest.suite());
        suite.addTest(InteractionTest.suite());
        suite.addTest(ExperimentTest.suite());
        return suite;
    }
}

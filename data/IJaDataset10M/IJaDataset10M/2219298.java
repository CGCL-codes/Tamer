package org.jgap;

import junit.framework.*;

/**
 * Test suite for all tests of package org.jgap.
*
 * @author Klaus Meffert
 * @since 1.1
 */
public class AllBaseTests extends TestSuite {

    /** String containing the CVS revision. Read out via reflection!*/
    private static final String CVS_REVISION = "$Revision: 1.12 $";

    public static Test suite() {
        TestSuite suite = new TestSuite("AllBaseTests");
        suite.addTest(BaseGeneTest.suite());
        suite.addTest(BaseRateCalculatorTest.suite());
        suite.addTest(ChromosomeTest.suite());
        suite.addTest(ConfigurationTest.suite());
        suite.addTest(DefaultFitnessEvaluatorTest.suite());
        suite.addTest(DeltaFitnessEvaluatorTest.suite());
        suite.addTest(FitnessFunctionTest.suite());
        suite.addTest(GenotypeTest.suite());
        suite.addTest(PopulationTest.suite());
        return suite;
    }
}

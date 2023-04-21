package org.gvsig.normalization.operations;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 * 
 */
public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.gvsig.normalization.operations");
        suite.addTestSuite(TestMarshall.class);
        suite.addTestSuite(TestFilterString.class);
        suite.addTestSuite(TestNormAlgorithm.class);
        suite.addTestSuite(TestCreateNewPattern.class);
        suite.addTestSuite(TestNormalizeStringsFromFile.class);
        suite.addTestSuite(TestAllTypeData.class);
        suite.addTestSuite(TestNormalizeTableAlterTable.class);
        suite.addTestSuite(TestNormalizeTableJoinTable.class);
        return suite;
    }
}

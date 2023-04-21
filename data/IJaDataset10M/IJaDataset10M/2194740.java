package org.openscience.cdk.test.qsar.model.weka;

import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.libio.weka.Weka;
import org.openscience.cdk.qsar.model.QSARModelException;
import org.openscience.cdk.qsar.model.weka.DensityBasedClustererModel;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.openscience.cdk.test.CDKTestCase;

/**
 * TestSuite that runs a test for the DensityBasedClustererModel
 *
 * @author Mario Baseda
 * @cdk.module test-qsar
 */
public class DensityBasedClustererModelTest extends CDKTestCase {

    /**
	 * Constructor of the DensityBasedClustererModelTest
	 */
    public DensityBasedClustererModelTest() {
    }

    /**
	 * A unit test suite for JUnit
	 *
	 * @return The test suite
	 */
    public static Test suite() {
        return new TestSuite(DensityBasedClustererModelTest.class);
    }

    /**
	 * @throws Exception
	 */
    public void testDensityBasedClustererModel() throws Exception {
        DensityBasedClustererModel test = new DensityBasedClustererModel();
        int[] typAttrib = { Weka.NUMERIC, Weka.NUMERIC, Weka.NUMERIC };
        String[] classAttrib = { "A_", "B_", "C_" };
        double[][] x = { { 10, 10, 10 }, { 10, -10, -10 }, { -10, -10, -10 }, { 11, 11, 11 }, { 11, -11, -11 }, { -11, -11, -11 } };
        Double[][] xD = new Double[x.length][x[0].length];
        for (int i = 0; i < xD.length; i++) for (int j = 0; j < xD[i].length; j++) xD[i][j] = new Double(x[i][j]);
        String[] y = { "A_", "B_", "C_", "A_", "B_", "C_" };
        String[] attrib = { "X1", "X2", "X3" };
        test.setData(attrib, typAttrib, classAttrib, y, xD);
        test.build();
        Double[][] newx = { { new Double(99), new Double(89), new Double(79) }, { new Double(19), new Double(29), new Double(39) } };
        test.setParameters(newx);
        assertNotNull(test.distributionForInstance());
        assertNotNull(test.logDensityForInstance());
        assertNotNull(test.logDensityPerClusterForInstance());
        assertNotNull(test.logJointDensitiesForInstance());
        assertNotNull(test.clusterPriors());
    }
}

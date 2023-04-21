package org.jcvi.vics.compute.service.export;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Created by IntelliJ IDEA.
 * User: smurphy
 * Date: Jul 11, 2008
 * Time: 4:04:46 PM
 *
 */
public class ExportTestSuite extends TestSuite {

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(ExportTestBlastResult.class);
        suite.addTestSuite(ExportTestClusterAnnotation.class);
        suite.addTestSuite(ExportTestFileNode.class);
        suite.addTestSuite(ExportTestSequence.class);
        return suite;
    }
}

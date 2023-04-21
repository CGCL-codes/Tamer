package org.gvsig.gpe.gml.writer;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.gvsig.gpe.gml.writer.sfp0.GMLsfp0WritersTestSuite;
import org.gvsig.gpe.gml.writer.v2.GMLv2WritersTestSuite;

/**
 * @author Jorge Piera LLodr� (jorge.piera@iver.es)
 */
public class GMLWritersTestSuite {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.gvsig.gpe.gml.writers");
        suite.addTest(GMLv2WritersTestSuite.suite());
        suite.addTest(GMLsfp0WritersTestSuite.suite());
        return suite;
    }
}

package org.dcm4che;

import junit.framework.*;

/**
 *
 * @author  gunter.zeilinger@tiani.com
 * @version 1.0.0
 */
public class PackageTest extends Object {

    private PackageTest() {
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(org.dcm4che.data.PackageTest.suite());
        suite.addTest(org.dcm4che.dict.PackageTest.suite());
        suite.addTest(org.dcm4che.hl7.PackageTest.suite());
        suite.addTest(org.dcm4che.media.PackageTest.suite());
        suite.addTest(org.dcm4che.net.PackageTest.suite());
        suite.addTest(org.dcm4che.srom.PackageTest.suite());
        suite.addTest(org.dcm4che.util.PackageTest.suite());
        return suite;
    }
}

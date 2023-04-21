package test.core.gut;

import junit.framework.Test;
import junit.framework.TestCase;
import test.core.CoreTestSuite;

/**
 * @author mike
 */
public class TestGUT {

    public static Test suite() {
        return CoreTestSuite.suite(TestGUT.class);
    }

    public static void main(String[] args) throws Throwable {
        CoreTestSuite cts = new CoreTestSuite(TestGUT.class);
        TestCase t = cts.createTestCase(cts.getResourceDirs(), "dontvirtualize.t");
        t.runBare();
    }
}

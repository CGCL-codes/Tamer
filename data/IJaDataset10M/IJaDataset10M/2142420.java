package tudresden.ocl.test;

import junit.framework.*;
import java.util.*;

public class TestAll extends TestCase {

    public TestAll(String s) {
        super(s);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(tudresden.ocl.lib.test.TestAll.suite());
        suite.addTest(TestNameCreator.suite());
        suite.addTest(TestParser.suite());
        suite.addTest(TestNormalize.suite());
        suite.addTest(new TestSuite(TestJavaGenerator.class));
        suite.addTest(tudresden.ocl.check.types.xmifacade.stress.Test.suite());
        suite.addTest(tudresden.ocl.injection.test.TestInjection.suite());
        suite.addTest(new TestSuite(TestTypeCheck.class));
        suite.addTest(tudresden.ocl.injection.reverseeng.test.RevengTestSuite.suite());
        suite.addTest(tudresden.ocl.test.sql.TestSQLClasses.suite());
        suite.addTest(tudresden.ocl.interp.test.TestAll.suite());
        return suite;
    }
}

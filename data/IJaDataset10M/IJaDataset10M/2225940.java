package info.bliki.wiki.addon.test.filter;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AllAddonFilterTests extends TestCase {

    public AllAddonFilterTests(String name) {
        super(name);
    }

    public static Test suite() {
        TestSuite s = new TestSuite();
        s.addTestSuite(ChartTest.class);
        s.addTestSuite(EvalTest.class);
        s.addTestSuite(YacasTest.class);
        s.addTestSuite(SampleTest.class);
        s.addTestSuite(PlotTest.class);
        s.addTestSuite(TeXTest.class);
        return s;
    }
}

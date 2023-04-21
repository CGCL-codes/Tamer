package test.net.sourceforge.clearantlib;

import org.apache.tools.ant.BuildFileTest;

/**
 * Simple JUnit class to test the <cclscoselector> type
 * @author Kevin A. Lee
 */
public class TestClearToolLscoSelector extends BuildFileTest {

    public TestClearToolLscoSelector() {
        this("TestClearToolLscoSelector");
    }

    public TestClearToolLscoSelector(String name) {
        super(name);
    }

    public void setUp() {
        configureProject("etc/testcases/cclscoselector-tasks.xml");
        executeTarget("setup");
    }

    public void tearDown() {
        executeTarget("cleanup");
    }

    public void testFileSet() {
        executeTarget("lsco-target");
    }
}

package test.core.box.events;

import junit.framework.Test;
import junit.framework.TestCase;
import test.core.CoreTestSuite;
import test.core.box.TestBox;

/**
 * @author mike
 */
public class TestBoxEvents extends TestBox {

    public TestBoxEvents() {
        super(TestBoxEvents.class);
    }

    protected boolean filter(String name) {
        return super.filter(name);
    }

    public static Test suite() {
        return CoreTestSuite.suite(new TestBoxEvents());
    }

    public static void main(String[] args) throws Throwable {
        CoreTestSuite cts = new TestBoxEvents();
        TestCase t = cts.createTestCase(cts.getResourceDirs(), "simpleMove.t");
        t.runBare();
    }
}

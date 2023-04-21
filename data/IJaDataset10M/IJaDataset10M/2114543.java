package be.lassi.lanbox.cuesteps;

import org.testng.annotations.Test;
import be.lassi.support.ObjectBuilder;
import be.lassi.support.ObjectTest;

/**
 * Tests class <code>GoNext</code>.
 */
public class GoNextTestCase extends CueStepTestCaseA {

    public static CueStepTests createTests() {
        CueStepTests tests = new CueStepTests();
        tests.add("go next", new GoNext());
        tests.add("go next", new GoNext(0));
        tests.add("go next in layer A", new GoNext(1));
        GoNext step = new GoNext(2);
        step.setWaitOnStep(true);
        tests.add("! go next in layer B", step);
        return tests;
    }

    @Test
    public void test() {
        doTest(createTests());
    }

    @Test
    public void object() {
        ObjectBuilder b = new ObjectBuilder() {

            public Object getObject1() {
                return new GoNext(1);
            }

            public Object getObject2() {
                return new GoNext(2);
            }
        };
        ObjectTest.test(b);
    }
}

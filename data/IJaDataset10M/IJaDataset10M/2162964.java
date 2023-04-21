package gnu.testlet.java2.lang.Math;

import gnu.testlet.Testlet;
import gnu.testlet.TestHarness;

public class rint implements Testlet {

    public void test(TestHarness harness) {
        harness.check(new Double(Math.rint(-3.5)).toString(), "-4.0");
        harness.check(new Double(Math.rint(4.5)).toString(), "4.0");
    }
}

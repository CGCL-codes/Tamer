package gnu.testlet.java2.lang.Float;

import gnu.testlet.Testlet;
import gnu.testlet.TestHarness;

public class new_Float implements Testlet {

    public void test(TestHarness harness) {
        harness.check(new Float(22.0 / 7.0).toString(), "3.142857");
        harness.check(new Float(Math.PI).toString(), "3.1415927");
        harness.check(Float.valueOf(new Float(Math.PI).toString()), new Float(Math.PI));
        harness.check(Float.valueOf(new Float(-Math.PI).toString()), new Float(-Math.PI));
        harness.check(new Float(Float.MAX_VALUE).toString(), "3.4028235E38");
        harness.check(new Float(-Float.MAX_VALUE).toString(), "-3.4028235E38");
        harness.check(new Float(Float.POSITIVE_INFINITY).toString(), "Infinity");
        harness.check(new Float(-Float.POSITIVE_INFINITY).toString(), "-Infinity");
        harness.check(new Float(Float.NaN).toString(), "NaN");
    }
}

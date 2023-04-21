package gnu.testlet.java2.lang.StringBuffer;

import gnu.testlet.Testlet;
import gnu.testlet.TestHarness;

public class plus implements Testlet {

    public String s(int x) {
        if (x == 0) return null; else return "z";
    }

    public void test(TestHarness harness) {
        harness.check(s(0) + "", "null");
        harness.check(s(1) + "", "z");
        harness.check("wxy" + s(0), "wxynull");
        harness.check("wxy" + s(1), "wxyz");
        harness.check(5 + s(1), "5z");
        harness.check(0.2 + "" + s(0) + 7, "0.2null7");
    }
}

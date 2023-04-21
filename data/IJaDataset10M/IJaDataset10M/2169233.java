package gnu.testlet.java2.lang.System;

import gnu.testlet.Testlet;
import gnu.testlet.TestHarness;

public class identityHashCode implements Testlet {

    public int hashCode() {
        return 42;
    }

    private int origHashCode() {
        return super.hashCode();
    }

    public void test(TestHarness harness) {
        Object o = new Object();
        harness.check(System.identityHashCode(o), o.hashCode());
        o = new Exception();
        harness.check(System.identityHashCode(o), o.hashCode());
        identityHashCode ihc = new identityHashCode();
        harness.check(System.identityHashCode(ihc), ihc.origHashCode());
        harness.check(System.identityHashCode(null), 0);
    }
}

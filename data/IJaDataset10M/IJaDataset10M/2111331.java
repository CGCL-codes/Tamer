package gnu.testlet.java2.lang.InheritableThreadLocal;

import gnu.testlet.Testlet;
import gnu.testlet.TestHarness;

public class simple implements Testlet, Runnable {

    private class TestInheritableThreadLocal extends InheritableThreadLocal {

        public Object initialValue() {
            return "Maude";
        }

        protected Object childValue(Object parentValue) {
            myHarness.check(parentValue, "Liver", "Check parent value");
            return "Spice";
        }
    }

    private TestHarness myHarness;

    private TestInheritableThreadLocal loc = new TestInheritableThreadLocal();

    public void run() {
        myHarness.check(loc.get(), "Spice", "Child value in new thread");
        loc.set("Wednesday");
        myHarness.check(loc.get(), "Wednesday", "Changed value in new thread");
    }

    public void test(TestHarness harness) {
        this.myHarness = harness;
        harness.check(loc.get(), "Maude", "Check initial value");
        loc.set("Liver");
        harness.check(loc.get(), "Liver", "Check changed value");
        try {
            Thread t = new Thread(this);
            t.start();
            t.join();
        } catch (InterruptedException _) {
        }
        harness.check(loc.get(), "Liver", "Value didn't change");
    }
}

package gnu.testlet.java2.lang.Character;

import gnu.testlet.Testlet;
import gnu.testlet.TestHarness;

public class classify12 implements Testlet {

    public void test(TestHarness harness) {
        harness.check(Character.isUpperCase('ℂ'));
    }
}

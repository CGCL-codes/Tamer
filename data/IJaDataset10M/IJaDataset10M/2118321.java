package gnu.testlet.java2.lang.String;

import gnu.testlet.Testlet;
import gnu.testlet.TestHarness;

public class to implements Testlet {

    public void test(TestHarness harness) {
        String b = new String(" abc\tABC 123\t");
        harness.check(b.toLowerCase(), " abc	abc 123	");
        harness.check(b.toUpperCase(), " ABC	ABC 123	");
    }
}

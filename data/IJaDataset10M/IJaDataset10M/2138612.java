package gnu.testlet.java2.lang.Number;

import gnu.testlet.Testlet;
import gnu.testlet.TestHarness;

/**
 * 
 * @modifiedby sgurin
 *
 */
public class NumberTest implements Testlet {

    protected static TestHarness harness;

    public void test_Basics() {
        NewNumber _newnum = new NewNumber();
        NewNumber newnum = new NewNumber(300);
        NewNumber newnum1 = new NewNumber(Integer.MAX_VALUE);
        if (newnum.byteValue() != (byte) 300) harness.fail("Error : test_Basics failed -1 ");
        if (newnum1.shortValue() != (short) Integer.MAX_VALUE) harness.fail("Error : test_Basics failed -2 ");
    }

    public void testall() {
        test_Basics();
    }

    public void test(TestHarness the_harness) {
        harness = the_harness;
        testall();
    }
}

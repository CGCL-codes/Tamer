package gov.nasa.jpf.delayed;

import gov.nasa.jpf.delayed.test.util.TestUtils;
import gov.nasa.jpf.jvm.RawTest;
import gov.nasa.jpf.jvm.Verify;

/**
 * Test for the I2 instructions.
 * 
 * @author Milos Gligoric (milos.gligoric@gmail.com)
 * @author Tihomir Gvero (tihomir.gvero@gmail.com)
 * 
 */
public class TestCPI2 extends RawTest {

    public static void main(String[] args) {
        TestCPI2 test = new TestCPI2();
        if (!runSelectedTest(args, test)) {
            runAllTests(args, test);
        }
    }

    public void testSimpleI2B() {
        TestUtils.reset();
        int i = Verify.getInt(1, 3);
        assert (TestUtils.getCounter() == 0);
        byte b = (byte) i;
        TestUtils.incCounter();
        assert (TestUtils.getCounter() == b) : TestUtils.getCounter() + " " + b;
    }

    public void testSimpleI2C() {
        TestUtils.reset();
        int i = Verify.getInt(1, 3);
        assert (TestUtils.getCounter() == 0);
        char c = (char) i;
        TestUtils.incCounter();
        assert (TestUtils.getCounter() == c) : TestUtils.getCounter();
    }

    public void testSimpleI2D() {
        TestUtils.reset();
        int i = Verify.getInt(1, 3);
        assert (TestUtils.getCounter() == 0);
        double d = i;
        TestUtils.incCounter();
        assert (TestUtils.getCounter() == d) : TestUtils.getCounter() + " " + d;
    }

    public void testSimpleI2F() {
        TestUtils.reset();
        int i = Verify.getInt(1, 3);
        assert (TestUtils.getCounter() == 0);
        float f = i;
        TestUtils.incCounter();
        assert (TestUtils.getCounter() == f) : TestUtils.getCounter() + " " + f;
    }

    public void testSimpleI2L() {
        TestUtils.reset();
        int i = Verify.getInt(1, 3);
        assert (TestUtils.getCounter() == 0);
        long l = i;
        TestUtils.incCounter();
        assert (TestUtils.getCounter() == l) : TestUtils.getCounter() + " " + l;
    }

    public void testSimpleI2S() {
        TestUtils.reset();
        int i = Verify.getInt(1, 3);
        assert (TestUtils.getCounter() == 0);
        short s = (short) i;
        TestUtils.incCounter();
        assert (TestUtils.getCounter() == s) : TestUtils.getCounter() + " " + s;
    }
}

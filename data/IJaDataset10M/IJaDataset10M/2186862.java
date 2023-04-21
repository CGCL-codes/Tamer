package gov.nasa.jpf.delayed;

import gov.nasa.jpf.delayed.test.util.TestUtils;
import gov.nasa.jpf.jvm.RawTest;
import gov.nasa.jpf.jvm.Verify;

/**
 * Test for the IADD instruction.
 * 
 * @author Milos Gligoric (milos.gligoric@gmail.com)
 * @author Tihomir Gvero (tihomir.gvero@gmail.com)
 * 
 */
public class TestCPIADD extends RawTest {

    public static void main(String[] args) {
        TestCPIADD test = new TestCPIADD();
        if (!runSelectedTest(args, test)) {
            runAllTests(args, test);
        }
    }

    private int iadd;

    public void testSimpleIADD() {
        TestUtils.reset();
        iadd = Verify.getInt(0, 2);
        iadd++;
        TestUtils.incCounter();
        assert (TestUtils.getCounter() == iadd);
    }

    public void testInOrderIADD() {
        TestUtils.reset();
        iadd = Verify.getInt(1, 3);
        int tmp = iadd;
        iadd += 1;
        tmp += 2;
        TestUtils.incCounter();
        if (TestUtils.getCounter() == 1) {
            assert (iadd == 2 && tmp == 3);
        } else if (TestUtils.getCounter() == 2) {
            assert (iadd == 3 && tmp == 4);
        } else if (TestUtils.getCounter() == 3) {
            assert (iadd == 4 && tmp == 5);
        } else {
            assert (false);
        }
    }

    public void testOutOfOrderIADD() {
        TestUtils.reset();
        iadd = Verify.getInt(1, 3);
        int tmp = iadd;
        tmp += 2;
        iadd += 1;
        TestUtils.incCounter();
        if (TestUtils.getCounter() == 1) {
            assert (iadd == 2 && tmp == 3);
        } else if (TestUtils.getCounter() == 2) {
            assert (iadd == 3 && tmp == 4);
        } else if (TestUtils.getCounter() == 3) {
            assert (iadd == 4 && tmp == 5);
        } else {
            assert (false);
        }
    }

    public void testPlusEqualsIADD() {
        TestUtils.reset();
        iadd = Verify.getInt(0, 2);
        iadd += 5;
        TestUtils.incCounter();
        assert (TestUtils.getCounter() + 4 == iadd);
    }

    public void testAssignmentIADD() {
        TestUtils.reset();
        iadd = Verify.getInt(0, 2);
        TestUtils.incCounter();
        iadd++;
        assert (TestUtils.getCounter() == 1);
    }

    /**
   * Test when first operand has light symbolic value
   */
    public void testPlusFirstIADD() {
        TestUtils.reset();
        iadd = Verify.getInt(0, 2);
        int res = iadd + 3;
        TestUtils.incCounter();
        assert (TestUtils.getCounter() + 2 == res);
    }

    /**
   * Test when second operand has light symbolic value
   */
    public void testPlusSecondIADD() {
        TestUtils.reset();
        iadd = Verify.getInt(0, 2);
        int res = 3 + iadd;
        TestUtils.incCounter();
        assert (TestUtils.getCounter() + 2 == res);
    }

    private int iaddOperand1;

    private int iaddOperand2;

    /**
   * Test when both operands have light symbolic values
   */
    public void testPlusBothIADD() {
        TestUtils.setCounter(2);
        iaddOperand1 = Verify.getInt(1, 3);
        iaddOperand2 = Verify.getInt(2, 4);
        int res = iaddOperand1 + iaddOperand2;
        TestUtils.incCounter();
        assert (TestUtils.getCounter() > 0 && TestUtils.getCounter() < 12);
        if (TestUtils.getCounter() >= 3 && TestUtils.getCounter() <= 5) assert (TestUtils.getCounter() == res); else if (TestUtils.getCounter() >= 6 && TestUtils.getCounter() <= 8) assert (TestUtils.getCounter() == res + 2); else if (TestUtils.getCounter() >= 9 && TestUtils.getCounter() <= 11) assert (TestUtils.getCounter() == res + 4);
    }

    /**
   * Create two delayed assignment, propagate them in locals, use locals
   * variable in iadd instruction
   */
    public void testPlusBothPropagateIADD() {
        TestUtils.setCounter(2);
        iaddOperand1 = Verify.getInt(1, 3);
        iaddOperand2 = Verify.getInt(2, 4);
        int tmp1 = iaddOperand1;
        int tmp2 = iaddOperand2;
        int res = tmp1 + tmp2;
        TestUtils.incCounter();
        assert (TestUtils.getCounter() > 0 && TestUtils.getCounter() < 12);
        if (TestUtils.getCounter() >= 3 && TestUtils.getCounter() <= 5) assert (TestUtils.getCounter() == res); else if (TestUtils.getCounter() >= 6 && TestUtils.getCounter() <= 8) assert (TestUtils.getCounter() == res + 2); else if (TestUtils.getCounter() >= 9 && TestUtils.getCounter() <= 11) assert (TestUtils.getCounter() == res + 4);
    }

    /**
   * Create two delayed assignment, propagate them in locals, start enumerating
   * original variables, use propagated values in iadd instruction
   */
    public void testPlusBothInitIADD() {
        TestUtils.setCounter(2);
        iaddOperand1 = Verify.getInt(1, 3);
        iaddOperand2 = Verify.getInt(2, 4);
        int tmp1 = iaddOperand1;
        int tmp2 = iaddOperand2;
        iaddOperand1 += 1;
        iaddOperand2 += 1;
        int res = tmp1 + tmp2;
        TestUtils.incCounter();
        assert (TestUtils.getCounter() > 0 && TestUtils.getCounter() < 12);
        if (TestUtils.getCounter() >= 3 && TestUtils.getCounter() <= 5) assert (TestUtils.getCounter() == res); else if (TestUtils.getCounter() >= 6 && TestUtils.getCounter() <= 8) assert (TestUtils.getCounter() == res + 2); else if (TestUtils.getCounter() >= 9 && TestUtils.getCounter() <= 11) assert (TestUtils.getCounter() == res + 4);
    }

    /**
   * When getInt is used in expression
   */
    public void testPlusNDAExpressionIADD() {
        TestUtils.reset();
        int res = Verify.getInt(1, 3) + 5;
        TestUtils.incCounter();
        assert (TestUtils.getCounter() + 5 == res);
    }
}
